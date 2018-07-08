package com.skyforce.repositories.jpa;

import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.models.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Sulaymon on 12.03.2018.
 */
public interface DataRepository extends JpaRepository<Data, Long> {
    List<Data> findAllByCategoryAndCityAndEmailIsNotNull(Category category, City city, Pageable pageable);
}
