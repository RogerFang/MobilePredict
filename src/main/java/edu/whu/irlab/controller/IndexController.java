package edu.whu.irlab.controller;

import edu.whu.irlab.model.SystemProps;
import edu.whu.irlab.model.TrainRecord;
import edu.whu.irlab.process.service.SystemPropsService;
import edu.whu.irlab.service.DataService;
import edu.whu.irlab.service.TrainRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by Roger on 2016/5/11.
 */
@Controller
public class IndexController {
    @Autowired
    private DataService dataService;

    @Autowired
    private SystemPropsService systemPropsService;

    @Autowired
    private TrainRecordService trainRecordService;

    @RequestMapping("/system")
    public String system(Model model){
        List<String> data2TrainPath = dataService.getData2Train();
        String data2PredictPath = dataService.getData2Predict();
        String predictMonth = dataService.getPredictMonth();
        List<TrainRecord> trainRecords = trainRecordService.selectAll();
        List<TrainRecord> trainRecordsFinished = trainRecordService.selectByState(0);
        SystemProps systemProps = systemPropsService.getSystemProps();

        model.addAttribute("data2TrainPath", data2TrainPath);
        model.addAttribute("data2PredictPath", data2PredictPath);
        model.addAttribute("predictMonth", predictMonth);
        model.addAttribute("trainRecords", trainRecords);
        model.addAttribute("trainRecordsFinished", trainRecordsFinished);
        model.addAttribute("systemProps", systemProps);

        return "system";
    }
}
