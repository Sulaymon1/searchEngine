package com.skyforce.services.interfaces;

import com.skyforce.models.City;
import com.skyforce.models.Data;
import com.skyforce.models.Info;

import java.util.List;
import java.util.Map;

/**
 * Created by Sulaymon on 11.03.2018.
 */
public interface ParseService {
    void parseCategory();
    void parseStatesAndCities();
    List<Data> parseDataByInput(String keyword, String city, int currentPage);
    void parseByKeyword(String keyword, List<City> cities);
    void parseByKeyword(String keyword, String city);
}
