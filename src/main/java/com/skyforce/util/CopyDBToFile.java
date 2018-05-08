package com.skyforce.util;

import org.postgresql.copy.CopyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
            FileWriter fileWriter = new FileWriter(path + "/" + keyword + ".txt");
            String sql = "COPY (SELECT address_str, city, email, name, phone_str, website FROM data WHERE keyword='" + keyword.toLowerCase() + "') to stdout (DELIMITER '\t')";
            copyManager.copyOut(sql, fileWriter);
            fileWriter.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return path;
    }


}
