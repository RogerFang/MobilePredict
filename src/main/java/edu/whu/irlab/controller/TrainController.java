package edu.whu.irlab.controller;

import edu.whu.irlab.model.TrainRecord;
import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.TrainService;
import edu.whu.irlab.service.TrainRecordService;
import edu.whu.irlab.thread.TrainThread;
import edu.whu.irlab.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.*;


/**
 * Created by Roger on 2016/5/17.
 */
@Controller
public class TrainController {

    private static Logger logger = LoggerFactory.getLogger(TrainController.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private TrainService trainService;

    @Autowired
    private TrainRecordService trainRecordService;

    @Autowired
    private PreProcessProps preProcessProps;

    @RequestMapping("/train")
    @ResponseBody
    public Map<String, String> train(InputParam inputParam){
        Map<String, String> map = new HashMap<>();
        String model = inputParam.getModel();
        List<String> months = inputParam.getMonths();
        map.put("msg", "");

        if (model.equals(preProcessProps.getProp("MODEL_RNN"))){
            if (months== null || months.size() != (Integer.parseInt(preProcessProps.getProp("numSteps"))+1)){
                map.put("msg", "数据只能输入连续"+(Integer.parseInt(preProcessProps.getProp("numSteps"))+1)+"个月份");
                return map;
            }
        }else {
            if (months== null || months.size() != 2){
                map.put("msg", "数据只能输入连续2个月份");
                return map;
            }
        }
        inputParam.setModelPath(preProcessProps.getProp("MODEL_SAVE_DIR")+ File.separator+inputParam.getModel()+".model");
        TrainRecord trainRecord = new TrainRecord(inputParam);
        trainRecord.setState(1);

        if (trainRecordService.selectByModel(inputParam.getModel()) == null){
            trainRecordService.insertSelective(trainRecord);
        }else {
            trainRecordService.updateByPrimaryKey(trainRecord);
        }

        logger.info("Start Training:model:{}; months:{}",months,trainRecord.getModel());
        threadPoolTaskExecutor.execute(new TrainThread(trainService, trainRecordService, inputParam));
        return  map;
    }

    @RequestMapping("/getTrainRecord")
    @ResponseBody
    public Map<String, String> getTrainRecord(String model){
        System.out.println(model);
        TrainRecord trainRecord = trainRecordService.selectByModel(model);
        Map<String, String> map = new HashMap<>();
        if (trainRecord == null){
            map.put("state", "-1");
        }else {
            map.put("state", String.valueOf(trainRecord.getState()));
        }
        return map;
    }
}
