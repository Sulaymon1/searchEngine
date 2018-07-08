package com.skyforce.repositories.jpa;

import com.skyforce.models.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Sulaymon on 12.03.2018.
 */

public interface CityRepository extends JpaRepository<City,Long>{
    List<City> findAllByStateToLowerStartsWith(String stateToLower, Pageable pageable);
    City findFirstByStateToLower(String stateToLower);
}
