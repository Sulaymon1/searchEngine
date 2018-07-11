package com.skyforce.services.implementations;

import com.skyforce.models.Category;
import com.skyforce.models.City;
import com.skyforce.models.DataParser;
import com.skyforce.models.Info;
//import com.skyforce.repositories.jdbc.DataJdbcRepository;
//import com.skyforce.repositories.jdbc.DataParserJdbcRepository;
import com.skyforce.repositories.jpa.CategoryRepository;
import com.skyforce.repositories.jpa.CityRepository;
import com.skyforce.repositories.jpa.DataParserRepository;
import com.skyforce.repositories.jpa.DataRepository;
import com.skyforce.services.interfaces.DataParserService;
import com.skyforce.services.interfaces.ParseService;
import com.skyforce.util.CopyDBToFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.awt.print.Pageable;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Date 07.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
@Service
@Slf4j
public class DataParserServiceImpl implements DataParserService {


    private Boolean isStop = true;

    @Autowired
    private DataParserRepository dataParserRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ParseService parseService;

    /*@Autowired
    private DataParserJdbcRepository dataParserJdbcRepository;*/

//    @Autowired
//    private DataJdbcRepository dataJdbcRepository;

    @Autowired
    private CopyDBToFile copyDBToFile;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private List<DataParser> getDataParserList(){
        return dataParserRepository.findAllByIsCompletedFalse(); // and isCurrentWorking false
    }

    @Override
    @Async
    public void addNewDataToParse(String categories) {
        if (categories.length()>0)
            //if you got many categories
            for (String categoryTitle: categories.split("\n")) {
                categoryTitle = categoryTitle.trim();
                Optional<Category> categoryOptional = categoryRepository.findByCategoryNameToLower(categoryTitle.toLowerCase());
                if (!categoryOptional.isPresent()){
                    Category category = Category.builder()
                            .title(categoryTitle)
                            .categoryNameToLower(categoryTitle.toLowerCase())
                            .build();
                    categoryRepository.save(category);
                    DataParser dataParser = DataParser.builder()
                            .category(category)
                            .size(0)
                            .isCompleted(false)
                            .onlySelectedCities(false)
                            .build();
                    dataParserRepository.save(dataParser);
                }
            }
        if (isStop)
            getNextDataToParse();
    }

    private volatile Info info;

    @Override
    public Info getInfo(){
        return info;
    }

    @Override
    public void getNextDataToParse() {
        isStop = false;
        List<DataParser> dataParserList = getDataParserList();
        if (dataParserList.size() > 0) {
            dataParserList.forEach(dataParser -> {
               /* if (dataParser.getOnlySelectedCities()) {
                    dataParser.getStates().forEach(state -> {
                        List<City> cities = cityRepository.findAllByStateToLower(state.toLowerCase());
                        parseService.parseByCategoryAndCities(dataParser.getKeyword(), cities);
                        dataParser.setIsCompleted(true);
                        dataParserRepository.save(dataParser);
                    });
                } else {
*/
                List<City> cities = cityRepository.findAll();
                int size = cities.size();
                double doubleSize = size;
                int currentCityNum = 0;
                info = Info.builder()
                        .totalCity(size)
                        .categoryTitle(dataParser.getCategory().getTitle())
                        .isCompleted(false)
                        .build();
                for (City city: cities){
                    try {
                        int percent = (int) ((currentCityNum / doubleSize) * 100.0);
                        info.setCurrentCityNum(currentCityNum++);
                        info.setPercent(percent);
                        parseService.parseByCategoryAndCity(dataParser.getCategory(), city);
                        dataParser.setCurrentCityNumber(currentCityNum);
                        dataParserRepository.save(dataParser);
                        simpMessagingTemplate.convertAndSend("/topic/status", info);
                    }catch (Throwable e){
                        log.error("throwable: "+e.getMessage(), e);
                    }
                }
                info.setIsCompleted(true);
                simpMessagingTemplate.convertAndSend("/topic/status", info);
                dataParser.setIsCompleted(true);
                dataParserRepository.save(dataParser);
                });
            /*});*/
            getNextDataToParse(); // it will check for new categories
        }
        isStop = true;
    }

    @Override
    public void deleteData(String keyword){
//        dataParserJdbcRepository.deleteAllByKeyword(keyword);
//        dataJdbcRepository.deleteAllDataByKeyword(keyword);
    }

    @Override
    public void downloadData(String keyword, HttpServletResponse response) throws SQLException {

        String path = copyDBToFile.copy(keyword);

        try {
            InputStream inputStream = new FileInputStream(path+"/"+keyword+".txt");

            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition","attachment; filename=" + keyword+".txt");
            IOUtils.copy(inputStream, response.getOutputStream());

            response.flushBuffer();
            inputStream.close();
        } catch (IOException e) {
            log.info(e.getMessage(), e);
        }
    }

    @Override
    public List<DataParser> getAllTasks(){
        return dataParserRepository.findAll();
    }


}
