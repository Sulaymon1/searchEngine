package com.skyforce.services.interfaces;

import com.skyforce.models.Data;
import com.skyforce.models.Info;

import java.util.List;

/**
 * Created by Sulaymon on 12.03.2018.
 */
public interface DataService {
    List<Data> getDataListByKeywordAndCity(String keyword, String city, Integer page);

}
