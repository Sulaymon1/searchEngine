package com.skyforce.services.interfaces;

import com.skyforce.models.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Sulaymon on 11.03.2018.
 */
public interface ParseService {
    void parseCategory();
    void parseStatesAndCities();
    List<Data> parseDataByInput(Category keyword, City city, int currentPage);

    // TODO: 05.07.2018 parse by template and without it
    void parseByCategoryAndCity(Category category, City city);
//    void parseByCategoryAndCity(Category category, City city) ;
}
