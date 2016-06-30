package edu.whu.irlab.process.service.impl;

import edu.whu.irlab.model.SystemProps;
import edu.whu.irlab.process.model.PreProcessProps;
import edu.whu.irlab.process.service.SystemPropsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Roger on 2016/5/18.
 */
@Service
public class SystemPropsServiceImpl implements SystemPropsService {

    @Autowired
    private PreProcessProps preProcessProps;

    @Override
    public void updateSystemProps(SystemProps systemProps) {
        String perFileCountInCache = systemProps.getPerFileCountInCache();
        String interCountInCache = systemProps.getInterCountInCache();
        String perFileCount = systemProps.getPerFileCount();
        String lineCount= systemProps.getLineCount();
        String pyInterface = systemProps.getPyInterface();
        String trainingEpochs = systemProps.getTrainingEpochs();
        String batchSize = systemProps.getBatchSize();
        String displayStep = systemProps.getDisplayStep();
        String saveStep = systemProps.getSaveStep();
        String learningRate = systemProps.getLearningRate();
        String numSteps = systemProps.getNumSteps();
        String featureSize = systemProps.getFeatureSize();
        String outputSize = systemProps.getOutputSize();
        String perm = systemProps.getPerm();

        String dataToTrainDir =systemProps.getDataToTrainDir();
        String dataToPredictDir = systemProps.getDataToPredictDir();
        String modelSaveDir = systemProps.getModelSaveDir();
        String trainDir = systemProps.getTrainDir();
        String testDir = systemProps.getTestDir();
        String predictDir = systemProps.getPredictDir();
        String tmpDir = systemProps.getTmpDir();
        if (perFileCountInCache!=null){
            preProcessProps.getProps().setProperty("PER_FILE_COUNT_IN_CACHE", perFileCountInCache);
        }
        if (interCountInCache!=null){
            preProcessProps.getProps().setProperty("INTER_COUNT_IN_CACHE", interCountInCache);
        }
        if (perFileCount!=null){
            preProcessProps.getProps().setProperty("PER_FILE_COUNT", perFileCount);
        }
        if (lineCount!=null){
            preProcessProps.getProps().setProperty("LINE_COUNT", lineCount);
        }
        if (pyInterface!=null){
            preProcessProps.getProps().setProperty("PY_INTERFACE", pyInterface);
        }
        if (trainingEpochs!=null){
            preProcessProps.getProps().setProperty("trainingEpochs", trainingEpochs);
        }
        if (batchSize!=null){
            preProcessProps.getProps().setProperty("batchSize", batchSize);
        }
        if (displayStep!=null){
            preProcessProps.getProps().setProperty("displayStep", displayStep);
        }
        if (saveStep!=null){
            preProcessProps.getProps().setProperty("saveStep", saveStep);
        }
        if (learningRate!=null){
            preProcessProps.getProps().setProperty("learningRate", learningRate);
        }
        if (numSteps!=null){
            preProcessProps.getProps().setProperty("numSteps", numSteps);
        }
        if (featureSize!=null){
            preProcessProps.getProps().setProperty("featureSize", featureSize);
        }
        if (outputSize!=null){
            preProcessProps.getProps().setProperty("outputSize", outputSize);
        }
        if (perm!=null){
            preProcessProps.getProps().setProperty("perm", perm);
        }

        if (dataToTrainDir!=null){
            preProcessProps.getProps().setProperty("DATA_TO_TRAIN_DIR", dataToTrainDir);
        }
        if (dataToPredictDir!=null){
            preProcessProps.getProps().setProperty("DATA_TO_PREDICT_DIR", dataToPredictDir);
        }
        if (modelSaveDir!=null){
            preProcessProps.getProps().setProperty("MODEL_SAVE_DIR", modelSaveDir);
        }
        if (trainDir!=null){
            preProcessProps.getProps().setProperty("TRAIN_DIR", trainDir);
        }
        if (testDir!=null){
            preProcessProps.getProps().setProperty("TEST_DIR", testDir);
        }
        if (predictDir!=null){
            preProcessProps.getProps().setProperty("PREDICT_DIR", predictDir);
        }
        if (tmpDir!=null){
            preProcessProps.getProps().setProperty("TMP_DIR", tmpDir);
        }
    }

    @Override
    public SystemProps getSystemProps() {
        SystemProps systemProps = new SystemProps();
        systemProps.setPerFileCountInCache(preProcessProps.getProp("PER_FILE_COUNT_IN_CACHE"));
        systemProps.setInterCountInCache(preProcessProps.getProp("INTER_COUNT_IN_CACHE"));
        systemProps.setPerFileCount(preProcessProps.getProp("PER_FILE_COUNT"));
        systemProps.setLineCount(preProcessProps.getProp("LINE_COUNT"));
        systemProps.setPyInterface(preProcessProps.getProp("PY_INTERFACE"));
        systemProps.setTrainingEpochs(preProcessProps.getProp("trainingEpochs"));
        systemProps.setBatchSize(preProcessProps.getProp("batchSize"));
        systemProps.setDisplayStep(preProcessProps.getProp("displayStep"));
        systemProps.setSaveStep(preProcessProps.getProp("saveStep"));
        systemProps.setLearningRate(preProcessProps.getProp("learningRate"));
        systemProps.setNumSteps(preProcessProps.getProp("numSteps"));
        systemProps.setFeatureSize(preProcessProps.getProp("featureSize"));
        systemProps.setOutputSize(preProcessProps.getProp("outputSize"));
        systemProps.setPerm(preProcessProps.getProp("perm"));

        systemProps.setDataToTrainDir(preProcessProps.getProp("DATA_TO_TRAIN_DIR"));
        systemProps.setDataToPredictDir(preProcessProps.getProp("DATA_TO_PREDICT_DIR"));
        systemProps.setModelSaveDir(preProcessProps.getProp("MODEL_SAVE_DIR"));
        systemProps.setTrainDir(preProcessProps.getProp("TRAIN_DIR"));
        systemProps.setTestDir(preProcessProps.getProp("TEST_DIR"));
        systemProps.setPredictDir(preProcessProps.getProp("PREDICT_DIR"));
        systemProps.setTmpDir(preProcessProps.getProp("TMP_DIR"));
        return systemProps;
    }
}
