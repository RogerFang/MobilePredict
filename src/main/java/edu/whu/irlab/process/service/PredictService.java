package edu.whu.irlab.process.service;

import edu.whu.irlab.model.PredictRecord;
import edu.whu.irlab.process.model.InputParam;

import java.util.List;

/**
 * Created by Roger on 2016/5/17.
 */
public interface PredictService {

    public PredictRecord doPredict(InputParam inputParam);

    public String getPredictDataPath(List<String> months);
}
