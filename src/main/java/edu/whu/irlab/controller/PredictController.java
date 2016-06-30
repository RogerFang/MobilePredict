package edu.whu.irlab.controller;

import edu.whu.irlab.model.PredictRecord;
import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.PredictService;
import edu.whu.irlab.service.PredictRecordService;
import edu.whu.irlab.thread.PredictThread;
import edu.whu.irlab.util.CalendarUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Roger on 2016/5/18.
 */
@Controller
public class PredictController {

    private static Logger logger = LoggerFactory.getLogger(PredictController.class);

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private PreProcessProps preProcessProps;

    @Autowired
    private PredictService predictService;

    @Autowired
    private PredictRecordService predictRecordService;

    @RequestMapping("/predict")
    @ResponseBody
    public Map<String, String> doPredict(InputParam inputParam){
        Map<String, String> map = new HashMap<>();
        inputParam.setModelPath(preProcessProps.getProp("MODEL_SAVE_DIR")+ File.separator+inputParam.getModel()+".model");
        PredictRecord predictRecord = new PredictRecord(inputParam);
        predictRecord.setState(1);
        predictRecordService.insertSelective(predictRecord);

        logger.info("Start Predicting:model:{}; months:{}", inputParam.getModel(), inputParam.getMonths());
        threadPoolTaskExecutor.execute(new PredictThread(predictService, predictRecordService, inputParam));
        return  map;
    }

    @RequestMapping("/getPredictRecord")
    @ResponseBody
    public Map<String, String> getPredictRecord(String model, String predictMonth){
        Map<String, String> map = new HashMap<>();
        System.out.println("model:"+model);
        System.out.println("predictMonth:"+predictMonth);
        PredictRecord predictRecord = predictRecordService.selectByModelAndPredictMonth(model, predictMonth);
        if (predictRecord == null){
            map.put("state", "-1");
        }else {
            map.put("state", String.valueOf(predictRecord.getState()));
        }
        return map;
    }
}
