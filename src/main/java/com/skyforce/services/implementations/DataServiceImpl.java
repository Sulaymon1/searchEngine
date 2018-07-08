package com.skyforce.services.implementations;

import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.models.Data;
import com.skyforce.repositories.jpa.CategoryRepository;
import com.skyforce.repositories.jpa.CityRepository;
import com.skyforce.repositories.jpa.DataRepository;
import com.skyforce.services.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Sulaymon on 12.03.2018.
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private ParseServiceImpl parseService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Override
    public List<Data> getDataListByKeywordAndCity(String keyword, String cityName, Integer currentPage) {
        PageRequest pageRequest = new PageRequest(currentPage, 30, Sort.Direction.ASC, "category_id","city_id");
        Optional<Category> categoryOptional = categoryRepository.findByCategoryNameToLower(keyword);
        List<Data> dataList = null;
        if (categoryOptional.isPresent()){
            City city = cityRepository.findFirstByStateToLower(cityName);
            if (city != null){
                dataList = dataRepository.findAllByCategoryAndCityAndEmailIsNotNull(categoryOptional.get(), city, pageRequest);
                if (dataList.size()==0){
                    dataList =  parseService.parseDataByInput(categoryOptional.get(), city, currentPage);
                }
            }else {// велосипед
                city = City.builder().name(cityName).nameToLower(cityName.toLowerCase()).build();
                dataList = parseService.parseDataByInput(categoryOptional.get(), city, currentPage);
            }
        }else{
            Category category = Category.builder().title(keyword).categoryNameToLower(keyword.toLowerCase()).build();
            City city = City.builder().name(cityName).nameToLower(cityName.toLowerCase()).build();
            dataList = parseService.parseDataByInput(category, city, currentPage);
        }

        return dataList;
    }
}
