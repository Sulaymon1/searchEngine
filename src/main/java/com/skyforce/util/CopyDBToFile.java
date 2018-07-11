package com.skyforce.util;

import com.skyforce.models.Data;
import org.postgresql.copy.CopyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Date 23.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
@Component
public class CopyDBToFile {

    @Value("${storage.path}")
    private String path;

    @Autowired
    private CopyManager copyManager;

    public String copy(String keyword){
        try {
            keyword = keyword.replace("'","").toLowerCase();
            String fileName = path + "/" + keyword + ".txt";
            FileWriter fileWriter = new FileWriter(fileName);
            String sql = "COPY (SELECT data.id,data.address,email,data.name, data.phone,data.website,categories.title,cities.name FROM data" +
                    " JOIN (SELECT id,name FROM city) as cities ON data.city_id = cities.id "+
                    " JOIN (SELECT category.id, category.title FROM category) as categories ON data.category_id=categories.id "+
                    " WHERE categories.title='"+keyword+"') to stdout (DELIMITER '\t')";
            copyManager.copyOut(sql, fileWriter);
            fileWriter.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return path;
    }


}
