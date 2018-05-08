package com.skyforce.repositories.jpa;

import com.skyforce.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Date 01.05.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Optional<Statistics> findFirstByCategoryAndCity(String category, String city);
}
