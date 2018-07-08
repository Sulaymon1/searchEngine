package com.skyforce.controllers.rest;

import com.skyforce.dto.SearchDTO;
import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.models.Data;
import com.skyforce.models.Statistics;
import com.skyforce.services.implementations.DataServiceImpl;
import com.skyforce.services.implementations.SearchServiceImpl;
import com.skyforce.services.interfaces.StatisticsService;
import com.skyforce.validator.SearchFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Sulaymon on 10.03.2018.
 */
@RestController
public class SearchRestController {


    @Autowired
    private SearchFormValidator searchFormValidator;

    @Autowired
    private SearchServiceImpl service;

    @Autowired
    private DataServiceImpl dataService;

    @Autowired
    private StatisticsService statisticsService;

    @InitBinder("searchForm")
    public void initSearchValidator(WebDataBinder dataBinder){
        dataBinder.addValidators(searchFormValidator);
    }

    @GetMapping("/searchCategory")
    public List<Category> getCategories(@RequestParam("input") String input){
        List<Category> categoriesByInput = service.getCategoriesByInput(input);
        return categoriesByInput;
    }

    @GetMapping("/searchCity")
    public List<City> getCities(@RequestParam("input") String input){
        List<City> citiesByInput = service.getCitiesByInput(input);
        return citiesByInput;
    }


    @GetMapping("/doSearch")
    public ResponseEntity<List<Data>> doSearch(@ModelAttribute("searchForm") @Valid SearchDTO searchDTO,
                                              BindingResult result){
        if (result.hasErrors()){
            ObjectError error = result.getAllErrors().get(0);
            return ResponseEntity.badRequest().build();
        }
        Integer currentPage = 0;
        if (searchDTO.getCurrentPage() !=null){
            currentPage = searchDTO.getCurrentPage();
        }else
            statisticsService.addNewSearchedData(Statistics.builder()
                                                                .category(searchDTO.getKeyword())
                                                                .city(searchDTO.getCity())
                                                                .build());
        List<Data> dataList = dataService.getDataListByKeywordAndCity(searchDTO.getKeyword(), searchDTO.getCity(), currentPage);
        return ResponseEntity.ok(dataList);
    }
}
