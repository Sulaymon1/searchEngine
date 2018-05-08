package com.skyforce.services.interfaces;

import com.skyforce.models.Category;
import com.skyforce.models.City;

import java.util.List;

/**
 * Created by Sulaymon on 10.03.2018.
 */
public interface SearchService {
    List<Category> getCategoriesByInput(String input);
    List<City> getCitiesByInput(String input);
}
