package com.skyforce.controllers;

import com.skyforce.models.Category;
import com.skyforce.models.DataParser;
import com.skyforce.models.Info;
import com.skyforce.repositories.jpa.CategoryRepository;
import com.skyforce.services.implementations.DataParserServiceImpl;
import com.skyforce.services.interfaces.ParseService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by Sulaymon on 21.03.2018.
 */
@RestController
public class ParseRestController {

    @Autowired
    private DataParserServiceImpl dataParserService;

    @Autowired
    private CategoryRepository categoryRepository;


    @PostMapping("/addCategory")
    @SneakyThrows
    public void addNewTask(@RequestParam("categories") String categories, HttpServletResponse response){
        if (categories.length()>0)
            for (String s: categories.split("\n")) {
                DataParser dataParser = DataParser.builder()
                        .keyword(s.trim())
                        .size(0)
                        .states(new ArrayList<>(0))
                        .onlySelectedStates(false)
                        .build();
                Optional<Category> categoryOptional = categoryRepository.findByCategoryNameToLower(dataParser.getKeyword().toLowerCase());
                if (!categoryOptional.isPresent())
                    categoryRepository.saveAndFlush(Category.builder()
                            .categoryName(dataParser.getKeyword())
                            .categoryNameToLower(dataParser.getKeyword().toLowerCase()).build());
                dataParserService.addNewDataToParse(dataParser);
            }
        response.sendRedirect("/dataParser");
    }

    @PostMapping("/addNewTask")
    public ResponseEntity addNewTask(@ModelAttribute("model") @Valid DataParser dataParser,
                                     BindingResult result, HttpServletResponse response){
        if (!result.hasErrors()){
            Optional<Category> categoryOptional = categoryRepository.findByCategoryNameToLower(dataParser.getKeyword().toLowerCase());
            if (!categoryOptional.isPresent())
                categoryRepository.saveAndFlush(Category.builder()
                        .categoryName(dataParser.getKeyword())
                        .categoryNameToLower(dataParser.getKeyword().toLowerCase()).build());
            dataParserService.addNewDataToParse(dataParser);
            return ResponseEntity.ok(dataParser);
        }
        return null;
    }

    @GetMapping("/getDataToParse")
    public ResponseEntity getDataParsers(){
        if (dataParserService.getIsWorking()) {
            dataParserService.getNextDataToParse();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete/{keyword}")
    public ResponseEntity deleteData(@PathVariable("keyword") String keyword){
        dataParserService.deleteData(keyword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download/{keyword}")
    public void downloadData(@PathVariable("keyword") String keyword, HttpServletResponse response){
        dataParserService.downloadData(keyword, response);
    }
}
