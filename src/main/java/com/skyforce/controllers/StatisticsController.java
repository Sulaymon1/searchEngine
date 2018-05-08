package com.skyforce.controllers;

import com.skyforce.models.Statistics;
import com.skyforce.services.interfaces.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * Date 01.05.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/

@Controller
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/statistics")
    public String getStatisticsPage(@ModelAttribute("model")ModelMap modelMap){
        List<Statistics> statistics = statisticsService.getAllSearchedData();
        modelMap.addAttribute("statistics", statistics);
        return "statistics";
    }

}
