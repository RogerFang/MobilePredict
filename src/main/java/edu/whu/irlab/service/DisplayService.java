package edu.whu.irlab.service;

import edu.whu.irlab.model.PredictRecord;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Roger on 2016/5/19.
 */
public interface DisplayService {

    public void calculatePredictPrecision(PredictRecord predictRecord) throws IOException;
}
