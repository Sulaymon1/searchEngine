package com.skyforce.repositories.jpa;

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
    @Query("select u from Data u where u.keyword =:keyword and u.city=:city and u.email is not null")
    List<Data> findAllByKeywordAndCity(@Param("keyword") String keyword,@Param("city") String city, Pageable pageable);
    List<Data> findByKeywordAndCity(String keyword, String city, Pageable pageable);
    List<Data> findByKeyword(String keyword, Pageable pageable);
}
