package com.skyforce.controllers.rest;

import com.skyforce.services.interfaces.DataParserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ParseRestController {

    @Autowired
    private DataParserService dataParserService;


    // TODO: 05.07.2018 add new param city, download category for concreate city
    @PostMapping("/addCategory")
    @SneakyThrows
    public void addNewTask(@RequestParam("categories") String categories, HttpServletResponse response){
        dataParserService.addNewDataToParse(categories);
        response.sendRedirect("/dataParser");
    }

    /*@PostMapping("/addNewTask")
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
    }*/



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
