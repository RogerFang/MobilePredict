package edu.whu.irlab.thread;

import edu.whu.irlab.model.PredictRecord;
import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.process.service.PredictService;
import edu.whu.irlab.service.PredictRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Roger on 2016/5/19.
 */
public class PredictThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(TrainThread.class);
    private PredictService predictService;
    private PredictRecordService predictRecordService;
    private InputParam inputParam;

    public PredictThread(PredictService predictService, PredictRecordService predictRecordService, InputParam inputParam){
        this.predictService = predictService;
        this.predictRecordService = predictRecordService;
        this.inputParam = inputParam;
    }

    @Override
    public void run() {
        System.out.println("This is a predict thread");

        PredictRecord predictRecord = predictService.doPredict(inputParam);
        predictRecordService.updateByModelAndPredictMonth(predictRecord);
        logger.info("Start Predicting:model:{}; months:{}", inputParam.getModel(), inputParam.getMonths());

    }
}
