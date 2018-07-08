package com.skyforce.repositories.jpa;

import com.skyforce.models.Category;
import com.skyforce.models.DataParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Date 07.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
public interface DataParserRepository extends JpaRepository<DataParser, Long> {
    Optional<DataParser> findFirstById(Long id);
    List<DataParser> findAllByIsCompletedFalse();
    Optional<DataParser> findFirstByCategory(Category category);
    List<DataParser> findAllByCategory(Category category);
}
