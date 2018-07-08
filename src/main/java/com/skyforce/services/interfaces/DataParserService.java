package com.skyforce.services.interfaces;

import com.skyforce.models.DataParser;
import com.skyforce.models.Info;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Date 07.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
public interface DataParserService {
    Info getInfo();

    void getNextDataToParse();
    void deleteData(String keyword);
    void downloadData(String keyword, HttpServletResponse response);
    List<DataParser> getAllTasks();

    void addNewDataToParse(String categories);
}
