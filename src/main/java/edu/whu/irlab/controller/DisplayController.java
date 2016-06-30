package edu.whu.irlab.controller;

import edu.whu.irlab.model.PredictRecord;
import edu.whu.irlab.model.TrainRecord;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.service.DisplayService;
import edu.whu.irlab.service.PredictRecordService;
import edu.whu.irlab.service.TrainRecordService;
import edu.whu.irlab.util.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roger on 2016/5/19.
 */
@Controller
public class DisplayController {

    @Autowired
    private PreProcessProps preProcessProps;

    @Autowired
    private TrainRecordService trainRecordService;

    @Autowired
    private PredictRecordService predictRecordService;

    @Autowired
    private DisplayService displayService;

    @RequestMapping("/display")
    public String display(Model model){
        List<TrainRecord> allTrainRecords = trainRecordService.selectAll();
        List<TrainRecord> trainRecords = new ArrayList<>();
        for (TrainRecord trainRecord: allTrainRecords){
            String modelName = trainRecord.getModel();
            System.out.println(modelName);
            if (!modelName.equals(preProcessProps.getProp("MODEL_LINEAR")) && !modelName.equals(preProcessProps.getProp("MODEL_RMLP"))){
                trainRecords.add(trainRecord);
            }
        }
        System.out.println("trainrecords:"+trainRecords.size());

        // 本月是上个月预测的predictMonth
        String curMonth = CalendarUtil.getCurMonth();

        List<PredictRecord> allPredictRecords = predictRecordService.selectByPredictMonth(curMonth);
        List<PredictRecord> predictRecords = new ArrayList<>();
        for (PredictRecord predictRecord: allPredictRecords){
            String modelName = predictRecord.getModel();
            System.out.println(modelName);
            if (!modelName.equals(preProcessProps.getProp("MODEL_LINEAR")) && !modelName.equals(preProcessProps.getProp("MODEL_RMLP"))){
                predictRecords.add(predictRecord);
            }
        }

        for (PredictRecord predictRecord: predictRecords){
            if (predictRecord.getPredictPrecision()==null){
                try {
                    displayService.calculatePredictPrecision(predictRecord);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        model.addAttribute("trainRecords", trainRecords);
        model.addAttribute("predictRecords", predictRecords);
        return "display";
    }
}
