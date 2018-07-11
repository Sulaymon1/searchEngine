package com.skyforce.services.interfaces;

import com.skyforce.models.DataParser;
import com.skyforce.models.Info;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.List;

/**
 * Date 07.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
public interface DataParserService {
    Info getInfo();

    @Async(value = "dataParserProcess")
    void getNextDataToParse();

    //    void getNextDataToParse();
    void deleteData(String keyword);
    void downloadData(String keyword, HttpServletResponse response) throws SQLException;
    List<DataParser> getAllTasks();

    void addNewDataToParse(String categories);
}
