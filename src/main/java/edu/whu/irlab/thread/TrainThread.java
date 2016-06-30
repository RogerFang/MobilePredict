package edu.whu.irlab.thread;

import edu.whu.irlab.model.TrainRecord;
import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.process.service.TrainService;
import edu.whu.irlab.service.TrainRecordService;
import edu.whu.irlab.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 训练线程
 * Created by Roger on 2016/5/17.
 */
public class TrainThread implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(TrainThread.class);
    private TrainService trainService;
    private TrainRecordService trainRecordService;
    private InputParam inputParam;

    public TrainThread(){}

    public TrainThread(TrainService trainService, TrainRecordService trainRecordService, InputParam inputParam){
        this.trainService = trainService;
        this.trainRecordService = trainRecordService;
        this.inputParam = inputParam;
    }

    @Override
    public void run() {
        System.out.println("This is a train thread");
        List<String> months = inputParam.getMonths();

        // 训练准确度
        TrainRecord trainRecord = trainService.doTrain(inputParam);
        trainRecordService.updateByModelSelective(trainRecord);

        logger.info("End Training:model:{}; months:{}",inputParam.getModel(), months);
    }
}
