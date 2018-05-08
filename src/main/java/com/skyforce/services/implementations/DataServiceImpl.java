package com.skyforce.services.implementations;

import com.skyforce.models.Data;
import com.skyforce.repositories.jpa.DataRepository;
import com.skyforce.services.interfaces.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sulaymon on 12.03.2018.
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private ParseServiceImpl parseService;

    @Override
    public List<Data> getDataListByKeywordAndCity(String keyword, String city, Integer currentPage) {
        PageRequest pageRequest = new PageRequest(currentPage, 30, Sort.Direction.ASC, "keyword","city");
        List<Data> dataList = dataRepository.findByKeywordAndCity(keyword.toLowerCase(), city.toLowerCase(), pageRequest);
            if (dataList.size()==0){
                dataList =  parseService.parseDataByInput(keyword, city, currentPage);
            }

        return dataList;
    }
}
