package edu.whu.irlab.process.service;

import edu.whu.irlab.model.TrainRecord;
import edu.whu.irlab.process.model.InputParam;

/**
 * Created by Roger on 2016/5/17.
 */
public interface TrainService {

    public TrainRecord doTrain(InputParam inputParam);
}
