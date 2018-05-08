package com.skyforce.services.interfaces;

import com.skyforce.models.Statistics;

import java.util.List;

/**
 * Date 01.05.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
public interface StatisticsService {
    List<Statistics> getAllSearchedData();
    void addNewSearchedData(Statistics statistics);
}
