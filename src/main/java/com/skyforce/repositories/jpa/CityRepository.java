package com.skyforce.repositories.jpa;

import com.skyforce.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Sulaymon on 12.03.2018.
 */

public interface CityRepository extends JpaRepository<City,Long>{
    List<City> findAllByStateToLower(String state);
}
