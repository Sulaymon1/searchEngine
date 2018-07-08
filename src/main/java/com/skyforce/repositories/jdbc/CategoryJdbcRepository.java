/*
package com.skyforce.repositories.jdbc;

import com.skyforce.models.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

*/
/**
 * Created by Sulaymon on 11.03.2018.
 *//*

@Repository
public class CategoryJdbcRepository {

    private final String SQL_FIND_BY_INPUT = "SELECT 1, * FROM category as t WHERE category_name_to_lower LIKE :input||'%' " +
            "UNION (SELECT 2,* FROM category " +
            "WHERE category_name_to_lower LIKE '%'||:input||'%' " +
            "EXCEPT (SELECT  2,* FROM category as t WHERE category_name_to_lower LIKE :input||'%'))" +
            "ORDER BY 1 LIMIT 10;";
    private Map<Long, Category> categoryMap = new HashMap<>();


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<Category> rowMapper = (resultSet, i) -> {
        Long currentId = resultSet.getLong(2);
        if (categoryMap.get(currentId)==null){
            categoryMap.put(currentId, Category.builder()
                    .id(resultSet.getLong(2))
                    .title(resultSet.getString(3))
                    .categoryNameToLower(resultSet.getString(4))
                    .build());
        }
        return categoryMap.get(currentId);
    };

    public List<Category> findSearchInput(String input){
        Map<String, String> map = new HashMap<>();
        map.put("input", input);
        List<Category> categoryList = namedParameterJdbcTemplate.query(SQL_FIND_BY_INPUT, map, rowMapper);
        categoryMap.clear();
        return categoryList;
    }
}
*/
