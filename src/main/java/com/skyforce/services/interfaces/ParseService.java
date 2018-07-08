package com.skyforce.services.interfaces;

import com.skyforce.models.*;

import java.util.List;

/**
 * Created by Sulaymon on 11.03.2018.
 */
public interface ParseService {
    void parseCategory();
    void parseStatesAndCities();
    List<Data> parseDataByInput(Category keyword, City city, int currentPage);
    void parseByCategoryAndCity(Category category, City city) ;
}
