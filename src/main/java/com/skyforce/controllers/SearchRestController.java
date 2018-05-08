package com.skyforce.controllers;

import com.skyforce.dto.SearchDTO;
import com.skyforce.dto.SearchResultDTO;
import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.models.Data;
import com.skyforce.models.Statistics;
import com.skyforce.services.implementations.DataServiceImpl;
import com.skyforce.services.implementations.SearchServiceImpl;
import com.skyforce.services.interfaces.StatisticsService;
import com.skyforce.validator.SearchFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
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
    public List<Data> doSearch( @ModelAttribute("searchForm") @Valid SearchDTO searchDTO,
                                    BindingResult result){

        if (result.hasErrors()){
            Data dataError = Data.builder().errors(result.getAllErrors().get(0).getDefaultMessage()).build();
            List<Data> dataList = new ArrayList<>();
            dataList.add(dataError);
            return dataList;
        }
        Integer currentPage = 0;
        if (searchDTO.getCurrentPage() !=null){
            currentPage = searchDTO.getCurrentPage();
        }else
            statisticsService.addNewSearchedData(Statistics.builder()
                                                                .category(searchDTO.getKeyword())
                                                                .city(searchDTO.getCity())
                                                                .build());
        return dataService.getDataListByKeywordAndCity(searchDTO.getKeyword(), searchDTO.getCity(),currentPage);
    }
}
