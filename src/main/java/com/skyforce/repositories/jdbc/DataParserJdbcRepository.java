package com.skyforce.repositories.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;

/**
 * Date 11.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
@Repository
public class DataParserJdbcRepository {

    private final String DELETEDATA = "delete from public.datatoparse WHERE keyword =?";


    @Autowired
    private JdbcTemplate template;


    public void deleteAllByKeyword(String keyword){
        template.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(DELETEDATA);
            statement.setString(1, keyword);
            return statement;
        });
    }
}
