package com.skyforce.services.implementations;

import com.skyforce.models.City;
import com.skyforce.models.Data;
import com.skyforce.models.DataParser;
import com.skyforce.repositories.jdbc.DataJdbcRepository;
import com.skyforce.repositories.jdbc.DataParserJdbcRepository;
import com.skyforce.repositories.jpa.CityRepository;
import com.skyforce.repositories.jpa.DataParserRepository;
import com.skyforce.repositories.jpa.DataRepository;
import com.skyforce.services.interfaces.DataParserService;
import com.skyforce.util.CopyDBToFile;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * Date 07.04.2018
 *
 * @author Hursanov Sulaymon
 * @version v1.0
 **/
@Service
public class DataParserServiceImpl implements DataParserService {


    private Boolean isWorking = true;

    public Boolean getIsWorking(){
        return isWorking;
    }

    @Autowired
    private DataParserRepository dataParserRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private ParseServiceImpl parseService;

    @Autowired
    private DataParserJdbcRepository dataParserJdbcRepository;

    @Autowired
    private DataJdbcRepository dataJdbcRepository;


    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private CopyDBToFile copyDBToFile;

    @Override
    public void addNewDataToParse(DataParser dataParser) {
        dataParser.setIsCompleted(false);
        if (dataParser.getStates().size()>0){
            dataParser.setOnlySelectedStates(true);
            dataParser.setSize(dataParser.getStates().size());
        }else dataParser.setSize(0);
        Optional<DataParser> parserOptional = dataParserRepository.findFirstByKeyword(dataParser.getKeyword());
        if (!parserOptional.isPresent()){
            dataParserRepository.save(dataParser);
        }
    }

    private List<DataParser> getDataParserList(){
       return dataParserRepository.findAllByIsCompletedFalse(); // and isCurrentWorking false
    }

    @Override
    @Async(value = "dataParserProcess")
    public void getNextDataToParse() {
        isWorking = false;
        List<DataParser> dataParserList = getDataParserList();
        if (dataParserList.size() > 0) {
            dataParserList.forEach(dataParser -> {
                if (dataParser.getOnlySelectedStates()) {
                    dataParser.getStates().forEach(state -> {
                        List<City> cities = cityRepository.findAllByStateToLower(state.toLowerCase());
                        parseService.parseByKeyword(dataParser.getKeyword(), cities);
                        dataParser.setIsCompleted(true);
                        dataParserRepository.save(dataParser);
                    });
                } else {
                    List<City> cityList = cityRepository.findAll();
                    parseService.parseByKeyword(dataParser.getKeyword(), cityList);
                    System.out.println("after parsing");
                    dataParser.setIsCompleted(true);
                    dataParserRepository.save(dataParser);
                }
            });
            getNextDataToParse(); // it will check for new keywords
        }
    }

    @Override
    public void deleteData(String keyword){
        dataParserJdbcRepository.deleteAllByKeyword(keyword);
        dataJdbcRepository.deleteAllDataByKeyword(keyword);
    }

    @Override
    public void downloadData(String keyword, HttpServletResponse response){

        String path = copyDBToFile.copy(keyword);
        try {
            InputStream inputStream = new FileInputStream(path+"/"+keyword+".txt");

            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition","attachment; filename=" + keyword+".txt");
            IOUtils.copy(inputStream, response.getOutputStream());

            response.flushBuffer();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<DataParser> getAllTasks(){
        return dataParserRepository.findAll();
    }
}
