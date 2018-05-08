package com.skyforce.repositories.jdbc;

import com.skyforce.models.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sulaymon on 12.03.2018.
 */
@Repository
public class CityJdbcRepository {
    private final String SQL_FIND_BY_INPUT = "SELECT 1, * FROM city as t WHERE name_to_lower LIKE :input||'%' " +
            "UNION (SELECT 2,* FROM city " +
            "WHERE name_to_lower LIKE '%'||:input||'%' " +
            "EXCEPT (SELECT  2,* FROM city as t WHERE name_to_lower LIKE :input||'%'))" +
            "ORDER BY 1 LIMIT 10;";
    private Map<Long, City> cityMap = new HashMap<>();


    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<City> rowMapper = (resultSet, i) -> {
        Long currentId = resultSet.getLong(2);
        if (cityMap.get(currentId)==null){
            cityMap.put(currentId, City.builder()
                    .id(resultSet.getLong(2))
                    .name(resultSet.getString(3))
                    .nameToLower(resultSet.getString(4))
                    .build());
        }
        return cityMap.get(currentId);
    };

    public List<City> findSearchCityInput(String input){
        Map<String, String> map = new HashMap<>();
        map.put("input", input);
        List<City> cities = namedParameterJdbcTemplate.query(SQL_FIND_BY_INPUT, map, rowMapper);
        cityMap.clear();
        return cities;
    }
}
