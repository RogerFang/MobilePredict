package edu.whu.irlab.controller;

import edu.whu.irlab.model.SystemProps;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.SystemPropsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roger on 2016/5/18.
 */
@Controller
@RequestMapping("/system")
public class SystemController {
    private static Logger logger = LoggerFactory.getLogger(SystemController.class);

    @Autowired
    private SystemPropsService systemPropsService;

    @Autowired
    private PreProcessProps preProcessProps;

    @RequestMapping("/setProps")
    @ResponseBody
    public Map<String,String> systemSet(SystemProps systemProps){
        System.out.println("system set:");
        systemPropsService.updateSystemProps(systemProps);
        String perFileCountInCache = preProcessProps.getProp("PER_FILE_COUNT_IN_CACHE");
        Map<String, String> map = new HashMap<>();
        map.put("msg", "设置成功");
        logger.info("system set:" +
                "PER_FILE_COUNT_IN_CACHE={}," +
                "INTER_COUNT_IN_CACHE={}," +
                "PER_FILE_COUNT={}," +
                "LINE_COUNT={}," +
                "PY_INTERFACE={}," +
                "trainingEpochs={}," +
                "batchSize={}," +
                "displayStep={}," +
                "saveStep={}," +
                "learningRate={}," +
                "numSteps={}," +
                "featureSize={}," +
                "outputSize={}," +
                "perm={}",
                systemProps.getPerFileCountInCache(),
                systemProps.getInterCountInCache(),
                systemProps.getPerFileCount(),
                systemProps.getLineCount(),
                systemProps.getPyInterface(),
                systemProps.getTrainingEpochs(),
                systemProps.getBatchSize(),
                systemProps.getDisplayStep(),
                systemProps.getSaveStep(),
                systemProps.getLearningRate(),
                systemProps.getNumSteps(),
                systemProps.getFeatureSize(),
                systemProps.getOutputSize(),
                systemProps.getPerm());
        return map;
    }
}
