package com.skyforce.services.implementations;

import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.models.Data;
import com.skyforce.models.Info;
import com.skyforce.repositories.jpa.CategoryRepository;
import com.skyforce.repositories.jpa.CityRepository;
import com.skyforce.repositories.jpa.DataRepository;
import com.skyforce.services.interfaces.ParseService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sulaymon on 11.03.2018.
 */
@Service
public class ParseServiceImpl implements ParseService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private ProductParsingService productParsingService;

    @Override
    public void parseCategory() {  // parsing for categories if database was recreated
        Map<String,Category> categoryName = new TreeMap<>();
        try {
            Document documentSitemap = Jsoup.connect("https://www.yellowpages.com/sitemap").get();
            Elements popular_cities_in_new_york = documentSitemap.getElementsContainingOwnText("Local Yellow Pages");
            Element divElement = popular_cities_in_new_york.first().parent();
            Elements liState = divElement.getElementsByTag("li");
            for (Element state: liState){
                String uriState = state.child(0).attr("href");
                Document documentState = Jsoup.connect("https://www.yellowpages.com/" + uriState).get();
                Element popular_cities = documentState.getElementsContainingOwnText("Popular Cities").first().parent().parent();
                Elements liCities = popular_cities.getElementsByTag("li");
                for (Element city: liCities){
                    String uriCity = city.child(0).attr("href");
                    String urlCity= "https://www.yellowpages.com"+uriCity+"/category/1";
                    try {
                        Document documentCategory = Jsoup.connect(urlCity).get();
                        Element localCategories = documentCategory.getElementsContainingOwnText("Local Categories ").first().parent();
                        Elements liCategories = localCategories.getElementsByTag("li");
                        for (Element categoryLi: liCategories){
                            String uriCategory[] = categoryLi.child(0).attr("href").split("/");
                            String nameCategory = categoryLi.text();
                            Category categoryObject = Category.builder()
                                    .categoryUri(uriCategory[1])
                                    .categoryName(nameCategory)
                                    .categoryNameToLower(nameCategory.toLowerCase())
                                    .build();
                            categoryName.put(nameCategory,categoryObject);
                        }
                    }catch (SocketTimeoutException ignored){
                    }
                }
                System.out.println(state.text());
            }
            List<Category> categoryList1 = new ArrayList<>(categoryName.values());
            categoryRepository.save(categoryList1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseStatesAndCities(){  // parsing for cities search database if database was cleared
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
        }catch (IOException e){
            e.printStackTrace();
        }
        cityRepository.save(cities);
    }


    @Override
    public List<Data> parseDataByInput(String keyword, String city, int currentPage){
        // example https://www.yellowpages.com/search?search_terms=architects&geo_location_terms=New+York%2C+NY&page=1
        keyword = keyword.replace(" ", "+");
        city = city.replace(" ", "+");
        currentPage++;
        productParsingService.getProductList().clear();
        try {
            String url = "https://www.yellowpages.com/search?search_terms=" + keyword +
                    "&geo_location_terms=" + city;
            Document document = Jsoup.connect(url).get();
            Elements pagination = document.getElementsByClass("pagination");
            int productNum = 30;
            try {
                productNum = Integer.parseInt(pagination.first().child(0).ownText());
            }catch (NullPointerException e){};
            int pageNum = 1;
            int itemPerPage = 30;
            if (productNum > itemPerPage){
                pageNum = (productNum/itemPerPage)+(productNum%itemPerPage==1?1:0);
            }
            if (currentPage <= pageNum){
                String url1 = url + "&page=" + currentPage;
                System.out.println(url1);
                Document document1 = Jsoup.connect(url1).get();
                Elements n = document1.getElementsByClass("n");
                ExecutorService executorService = Executors.newFixedThreadPool(15);
                String finalKeyword = keyword;
                String finalCity = city;
                n.forEach(item -> {
                    if (item.childNodes().size() >= 2) {
                        String uri = item.getElementsByTag("a").first().attr("href");
                        executorService.submit(new ProductThread(uri, productParsingService, finalKeyword, finalCity));
                    }
                });
                executorService.shutdown();
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS );
                return productParsingService.getProductList();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }



    private volatile Info info;

    public Info getInfo(){
        return info;
    }
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // TODO: 09.04.2018 analyse which keyword and state is most active
    @Override
    public void parseByKeyword(String keyword, List<City> cities){
        int size = cities.size();
        double doubleSize = size;
        int currentCityNum = 1;
        info =Info.builder()
                .totalCity(size)
                .keyword(keyword)
                .isCompleted(false)
                .build();
        for (City city: cities){
            info.setCurrentCityNum(currentCityNum++);
            int percentDone = (int)((currentCityNum/doubleSize)*100.0);
            info.setPercent(percentDone);
            parseByKeyword(keyword, city.getName());
            simpMessagingTemplate.convertAndSend("/topic/status", info);
            System.out.println("done: "+city.getName());
        }
        info.setIsCompleted(true);
        simpMessagingTemplate.convertAndSend("/topic/status", info);
    }


    @Override
    public void parseByKeyword(String keyword, String city){
        if (city.contains(","))
            city = city.substring(0, city.indexOf(","));
        try {
            try {
                // example https://www.yellowpages.com/search?search_terms=architects&geo_location_terms=New+York%2C+NY
                String url = "https://www.yellowpages.com/search?search_terms=" + keyword
                        + "&geo_location_terms=" + city;
                int pageNum = getPageNum(url);
                ExecutorService executorService = Executors.newFixedThreadPool(15);
                for (int page = 1; page <= pageNum; page++) {
                        Document document = Jsoup.connect(url + "&page=" + page).get();
                        Elements n = document.getElementsByClass("n");
                    String finalCity = city;
                    n.forEach(item -> {
                            if (item.childNodes().size() >= 2) {
                                String uri = item.getElementsByTag("a").first().attr("href");
                                executorService.submit(new ProductThread(uri, productParsingService, keyword, finalCity));
                            }
                        });
                }
                executorService.shutdown();
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS );
                List<Data> productList = productParsingService.getProductList();
                if (productList.size() > 0)
                    dataRepository.save(productList);
                productParsingService.getProductList().clear();
            }catch (SocketException e){
                e.printStackTrace();
                Thread.sleep(10000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static int getPageNum(String url){
        int pageNum = 1;
        int productNum = 0;
        try {
            Document document = Jsoup.connect(url).get();
            Elements pagination = document.getElementsByClass("pagination");
            productNum = Integer.valueOf(pagination.first().child(0).ownText());
            int itemPerPage = 30;
            if (productNum > itemPerPage){
                pageNum = (productNum/itemPerPage)+(productNum%itemPerPage==1?1:0);
            }
        } catch (Exception e) {
            pageNum =1;
        }

        return pageNum;
    }

}

class ProductThread extends Thread{

    private String uri;
    private String keyword;
    private String city;
    private ProductParsingService productParsingService;

    ProductThread(String uri,ProductParsingService productParseService, String keyword, String city) {
        this.uri = uri;
        this.keyword = keyword;
        this.city = city;
        this.productParsingService = productParseService;
    }

    @Override
    public void run() {
        try {

            Document document = Jsoup.connect("https://www.yellowpages.com"+uri).get();
            productParsingService.getProduct(document, keyword, city);

        } catch (IOException e) {
            System.out.println("https://www.yellowpages.com"+uri);
            System.out.println("unknown host");
        }

    }
}
