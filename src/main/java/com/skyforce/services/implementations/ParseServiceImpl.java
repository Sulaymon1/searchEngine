package com.skyforce.services.implementations;

import com.skyforce.models.*;
import com.skyforce.repositories.jpa.CategoryRepository;
import com.skyforce.repositories.jpa.CityRepository;
import com.skyforce.repositories.jpa.DataRepository;
import com.skyforce.services.interfaces.ParseService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class ParseServiceImpl implements ParseService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DataRepository dataRepository;

    private ProductParsingService productParsingService;

    @Override
    public void parseCategory() {  // parsing for categories if database was recreated
        Map<String, Category> categoryName = new TreeMap<>();
        try {
            Document documentSitemap = Jsoup.connect("https://www.yellowpages.com/sitemap").get();
            Elements popular_cities_in_new_york = documentSitemap.getElementsContainingOwnText("Local Yellow Pages");
            Element divElement = popular_cities_in_new_york.first().parent();
            Elements liState = divElement.getElementsByTag("li");
            for (Element state : liState) {
                String uriState = state.child(0).attr("href");
                Document documentState = Jsoup.connect("https://www.yellowpages.com/" + uriState).get();
                Element popular_cities = documentState.getElementsContainingOwnText("Popular Cities").first().parent().parent();
                Elements liCities = popular_cities.getElementsByTag("li");
                for (Element city : liCities) {
                    String uriCity = city.child(0).attr("href");
                    String urlCity = "https://www.yellowpages.com" + uriCity + "/category/1";
                    try {
                        Document documentCategory = Jsoup.connect(urlCity).get();
                        Element localCategories = documentCategory.getElementsContainingOwnText("Local Categories ").first().parent();
                        Elements liCategories = localCategories.getElementsByTag("li");
                        for (Element categoryLi : liCategories) {
                            String nameCategory = categoryLi.text();
                            Category categoryObject = Category.builder()
                                    .title(nameCategory)
                                    .categoryNameToLower(nameCategory.toLowerCase())
                                    .build();
                            categoryName.put(nameCategory, categoryObject);
                        }
                    } catch (SocketTimeoutException e) {
                        log.info(e.getMessage(), e);
                    }
                }
            }
            List<Category> categoryList1 = new ArrayList<>(categoryName.values());
            categoryRepository.save(categoryList1);
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    @Override
    public void parseStatesAndCities() {  // parsing for cities search database if database was cleared
        List<City> cities = new ArrayList<>();
        try {
            Document documentSitemap = Jsoup.connect("https://www.yellowpages.com/sitemap").get();
            Elements popular_cities_in_new_york = documentSitemap.getElementsContainingOwnText("Local Yellow Pages");
            Element divElement = popular_cities_in_new_york.first().parent();
            Elements liState = divElement.getElementsByTag("li");
            for (Element state : liState) {
                String stateName = state.text();
                String uriState = state.child(0).attr("href");

                City stateBuild = City.builder()
                        .name(stateName)
                        .nameToLower(stateName.toLowerCase())
                        .build();
                cities.add(stateBuild);
                Document documentState = Jsoup.connect("https://www.yellowpages.com/" + uriState).get();
                Element popular_cities = documentState.getElementsContainingOwnText("Popular Cities").first().parent().parent();
                Elements liCities = popular_cities.getElementsByTag("li");
                for (Element city : liCities) {
                    String cityName = city.text();
                    City cityBuild = City.builder()
                            .name(cityName + ", " + uriState.substring(6).toUpperCase())
                            .nameToLower(cityName.toLowerCase())
                            .state(stateName)
                            .stateToLower(stateName.toLowerCase())
                            .shortenedState(uriState.substring(6).toUpperCase())
                            .build();
                    cities.add(cityBuild);
                }
            }
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
        cityRepository.save(cities);
    }


    @Override
    public List<Data> parseDataByInput(Category category, City city, int currentPage) {
        // example https://www.yellowpages.com/search?search_terms=architects&geo_location_terms=New+York%2C+NY&page=1
        String keyword = category.getTitle();
        if (category.getTitle().contains(" "))
            keyword = category.getTitle().replace(" ", "+");
        String cityName = city.getName();
        if (city.getName().contains(" "))
            cityName = city.getName().replace(" ", "+");
        currentPage++;
        productParsingService = new ProductParsingService();
        productParsingService.getProductList().clear();
        try {
            String url = "https://www.yellowpages.com/search?search_terms=" + keyword +
                    "&geo_location_terms=" + cityName;
            Document document = Jsoup.connect(url).get();
            Elements pagination = document.getElementsByClass("pagination");
            int productNum = 30;
            try {
                productNum = Integer.parseInt(pagination.first().child(0).ownText());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            int pageNum = 1;
            int itemPerPage = 30;
            if (productNum > itemPerPage) {
                pageNum = (productNum / itemPerPage) + (productNum % itemPerPage == 1 ? 1 : 0);
            }
            if (currentPage <= pageNum) {
                String url1 = url + "&page=" + currentPage;
                Document document1 = Jsoup.connect(url1).get();
                Elements n = document1.getElementsByClass("n");
                ExecutorService executorService = Executors.newFixedThreadPool(15);
                n.forEach(item -> {
                    if (item.childNodes().size() >= 2) {
                        String uri = item.getElementsByTag("a").first().attr("href");
                        executorService.submit(parse(category, city, uri));
                    }
                });
                executorService.shutdown();
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                return productParsingService.getProductList();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }


    // TODO: 05.07.2018 parse by template and without it
    @Override
    @Transactional
    public void parseByCategoryAndCity(Category category, City city)  {
        productParsingService = new ProductParsingService();
        String cityName = city.getName();
        if (cityName.contains(","))
            cityName = cityName.replace(",", "%2C");
        if (cityName.contains(" "))
            cityName = cityName.replace(" ", "+");
        // example or template
        // https://www.yellowpages.com/search?search_terms=architects&geo_location_terms=New+York%2C+NY
        String categoryName = category.getTitle().replace(" ", "+");
        StringBuilder url = new StringBuilder();
        url.append("https://www.yellowpages.com/search?search_terms=")
                .append(categoryName)
                .append("&geo_location_terms=")
                .append(cityName);
        try {
            int pageNum = getPageNum(url.toString());
            ExecutorService executorService = Executors.newFixedThreadPool(15);
            for (int page = 1; page <= pageNum; page++) {
                try {
                    url.append("&page=").append(page);
                    Thread.sleep(2000);
                    Document document = Jsoup.connect(url.toString()).userAgent("Mozilla").get();
                    Elements n = document.getElementsByClass("n");
                    n.forEach(item -> {
                        if (item.childNodes().size() >= 2) {
                            String uri = item.getElementsByTag("a").first().attr("href");
                            executorService.submit(parse(category, city, uri));
                        }
                    });
                } catch (IOException e) {
                    log.info("socket exception on url: "+ url+"&page="+page, e);
                    Thread.sleep(10000);
                }
            }
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            List<Data> productList = productParsingService.getProductList();
            if (productList.size() > 0)
                dataRepository.save(productList);
            productParsingService.getProductList().clear();

        } catch (Exception e) {
            log.info("url: "+url);
            log.info("Got exception on city: "+ city.getName()+" and category: "+category.getTitle() , e);
        }
    }


    private int getPageNum(String url) {
        int pageNum = 1;
        int productNum;
        try {
            Document document = Jsoup.connect(url).get();
            Elements pagination = document.getElementsByClass("pagination");
            if (!pagination.isEmpty()){
                productNum = Integer.valueOf(pagination.first().child(0).ownText());
                int itemPerPage = 30;
                if (productNum > itemPerPage) {
                    pageNum = (productNum / itemPerPage) + (productNum % itemPerPage == 1 ? 1 : 0);
                }
            }
        } catch (IOException e) {
            log.info("url: "+url, e);
        }

        return pageNum;
    }

    private Runnable parse(Category category, City city, String uri){
       return  () -> {
           String url = "https://www.yellowpages.com" + uri;
           try {
                Document document1 = Jsoup.connect(url).get();
                productParsingService.getProduct(document1, category, city);
            } catch (IOException e) {
               log.info("line: "+ e.getStackTrace()[1].getLineNumber());
               log.info("url: "+ url, e); // unknown host
           }
        };
    }

}
