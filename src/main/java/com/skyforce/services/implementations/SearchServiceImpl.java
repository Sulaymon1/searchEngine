package com.skyforce.services.implementations;

import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.repositories.jdbc.CategoryJdbcRepository;
import com.skyforce.repositories.jpa.CategoryRepository;
import com.skyforce.repositories.jdbc.CityJdbcRepository;
import com.skyforce.services.interfaces.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Sulaymon on 10.03.2018.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private CategoryJdbcRepository jdbcRepository;
    private CityJdbcRepository cityJdbcRepository;

    @Autowired
    public SearchServiceImpl(CategoryJdbcRepository jdbcRepository,
                             CityJdbcRepository cityJdbcRepository){
        this.jdbcRepository = jdbcRepository;
        this.cityJdbcRepository = cityJdbcRepository;
    }

    @Override
    public List<Category> getCategoriesByInput(String input) {
        return jdbcRepository.findSearchInput(input.toLowerCase());
    }

    @Override
    public List<City> getCitiesByInput(String input){
        return cityJdbcRepository.findSearchCityInput(input.toLowerCase());
    }
}
