package com.skyforce.services.implementations;

import com.skyforce.models.Statistics;
import com.skyforce.repositories.jpa.StatisticsRepository;
import com.skyforce.services.interfaces.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date 01.05.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Override
    public List<Statistics> getAllSearchedData() {
        return  statisticsRepository.findAll();
    }

    @Override
    public void addNewSearchedData(Statistics statistics) {
        Optional<Statistics> statisticsOptional = statisticsRepository.findFirstByCategoryAndCity(statistics.getCategory(), statistics.getCity());
        if (statisticsOptional.isPresent()){
            Statistics statistics1 = statisticsOptional.get();
            Long count = statistics1.getCount();
            statistics1.setCount(++count);
            statisticsRepository.save(statistics1);
        }else{
            statistics.setCount(1L);
            statisticsRepository.save(statistics);
        }
    }
}
