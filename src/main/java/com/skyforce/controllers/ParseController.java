package com.skyforce.controllers;

import com.google.common.collect.Lists;
import com.skyforce.models.DataParser;
import com.skyforce.models.Info;
import com.skyforce.services.implementations.DataParserServiceImpl;
import com.skyforce.services.implementations.ParseServiceImpl;
import com.skyforce.services.interfaces.ParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Sulaymon on 11.03.2018.
 */
@Controller
public class ParseController {

    @Autowired
    private ParseServiceImpl parseService;


    @Autowired
    private DataParserServiceImpl dataParserService;

    @GetMapping("/parseCategory")
    public String parseCategory(){
        parseService.parseCategory();
        return "successPage";
    }

    @GetMapping("/parseCity")
    public String parseCity(){
        parseService.parseStatesAndCities();
        return "successPage";
    }

    @GetMapping("/dataParser")
    public String getDataParser(@ModelAttribute("model") ModelMap modelMap){
        List<DataParser> allTasks = Lists.reverse(dataParserService.getAllTasks());
        modelMap.addAttribute("tasks", allTasks);
        return "dataParserPage";
    }

    @RequestMapping(value = "/status")
    @ResponseBody
    @SubscribeMapping("initial")
    public Info fetchStatus() {
        return parseService.getInfo();
    }

}

