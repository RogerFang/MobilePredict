package edu.whu.irlab.process.service;

import java.io.File;
import java.util.List;

/**
 * Created by Roger on 2016/5/17.
 */
public interface PreProcessService {

    public String[] genTrainAndTestData(List<File> files, boolean isClassification, String path);

    public String genPredictData(List<File> files, String path);
}
