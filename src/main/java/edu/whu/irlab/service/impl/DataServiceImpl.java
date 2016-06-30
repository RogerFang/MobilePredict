package edu.whu.irlab.service.impl;

import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.service.DataService;
import edu.whu.irlab.util.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Roger on 2016/5/17.
 */
@Service
public class DataServiceImpl implements DataService{

    @Autowired
    private PreProcessProps preProcessProps;


    @Override
    public List<String> getData2Train() {
        return getData(preProcessProps.getProp("DATA_TO_TRAIN_DIR"));
    }

    @Override
    public String getData2Predict() {
        Calendar calendar = Calendar.getInstance();
        Date nowDate = calendar.getTime();
        DateFormat format = new SimpleDateFormat("yyyyMM");
        String nowMonthFileName = format.format(nowDate)+".txt";

        List<String> data2PredictPath = getData(preProcessProps.getProp("DATA_TO_PREDICT_DIR"));
        if (data2PredictPath!=null){
            if (data2PredictPath.contains(nowMonthFileName)) {
                return nowMonthFileName;
            }
        }
        return null;
    }

    private List<String> getData(String dirPath){
        File dir = new File(dirPath);
        if (dir.exists()){
            if (dir.isDirectory()){
                File[] files = dir.listFiles();
                if (files.length > 0) {
                    List<String> dataFiles = new ArrayList<>();
                    for (File file: dir.listFiles()){
                        dataFiles.add(file.getName());
                    }
                    Collections.sort(dataFiles);
                    return dataFiles;
                }
            }
        }else {
            dir.mkdirs();
        }
        return null;
    }

    @Override
    public String getPredictMonth() {
        return CalendarUtil.getNextMonth();
    }
}
