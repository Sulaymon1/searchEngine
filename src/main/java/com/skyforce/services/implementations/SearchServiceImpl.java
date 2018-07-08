package com.skyforce.services.implementations;

import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.repositories.jpa.CategoryRepository;
import com.skyforce.repositories.jpa.CityRepository;
import com.skyforce.services.interfaces.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Sulaymon on 10.03.2018.
 */
@Service
public class SearchServiceImpl implements SearchService {

    private CategoryRepository categoryRepository;
    private CityRepository cityRepository;

    @Autowired
    public SearchServiceImpl(CategoryRepository categoryRepository,
                             CityRepository cityRepository){
        this.categoryRepository = categoryRepository;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<Category> getCategoriesByInput(String input) {
        PageRequest pageRequest = new PageRequest(1, 4);
        return categoryRepository.findAllByCategoryNameToLowerStartsWith(input.toLowerCase(), pageRequest);
    }

    @Override
    public List<City> getCitiesByInput(String input){
        PageRequest pageRequest = new PageRequest(1,4);
        return cityRepository.findAllByStateToLowerStartsWith(input.toLowerCase(), pageRequest);
    }
}
