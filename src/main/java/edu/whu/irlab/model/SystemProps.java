package edu.whu.irlab.model;

/**
 * Created by Roger on 2016/5/18.
 */
public class SystemProps {
    private String perFileCountInCache;
    private String interCountInCache;
    private String perFileCount;
    private String lineCount;
    private String pyInterface;
    private String trainingEpochs;
    private String batchSize;
    private String displayStep;
    private String saveStep;
    private String learningRate;
    private String numSteps;
    private String featureSize;
    private String outputSize;
    private String perm;

    private String dataToTrainDir;
    private String dataToPredictDir;
    private String modelSaveDir;
    private String trainDir;
    private String testDir;
    private String predictDir;
    private String tmpDir;

    public String getPerFileCountInCache() {
        return perFileCountInCache;
    }

    public void setPerFileCountInCache(String perFileCountInCache) {
        this.perFileCountInCache = perFileCountInCache;
    }

    public String getInterCountInCache() {
        return interCountInCache;
    }

    public void setInterCountInCache(String interCountInCache) {
        this.interCountInCache = interCountInCache;
    }

    public String getPerFileCount() {
        return perFileCount;
    }

    public void setPerFileCount(String perFileCount) {
        this.perFileCount = perFileCount;
    }

    public String getLineCount() {
        return lineCount;
    }

    public void setLineCount(String lineCount) {
        this.lineCount = lineCount;
    }

    public String getPyInterface() {
        return pyInterface;
    }

    public void setPyInterface(String pyInterface) {
        this.pyInterface = pyInterface;
    }

    public String getTrainingEpochs() {
        return trainingEpochs;
    }

    public void setTrainingEpochs(String trainingEpochs) {
        this.trainingEpochs = trainingEpochs;
    }

    public String getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(String batchSize) {
        this.batchSize = batchSize;
    }

    public String getDisplayStep() {
        return displayStep;
    }

    public void setDisplayStep(String displayStep) {
        this.displayStep = displayStep;
    }

    public String getSaveStep() {
        return saveStep;
    }

    public void setSaveStep(String saveStep) {
        this.saveStep = saveStep;
    }

    public String getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(String learningRate) {
        this.learningRate = learningRate;
    }

    public String getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(String numSteps) {
        this.numSteps = numSteps;
    }

    public String getFeatureSize() {
        return featureSize;
    }

    public void setFeatureSize(String featureSize) {
        this.featureSize = featureSize;
    }

    public String getOutputSize() {
        return outputSize;
    }

    public void setOutputSize(String outputSize) {
        this.outputSize = outputSize;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public String getDataToTrainDir() {
        return dataToTrainDir;
    }

    public void setDataToTrainDir(String dataToTrainDir) {
        this.dataToTrainDir = dataToTrainDir;
    }

    public String getDataToPredictDir() {
        return dataToPredictDir;
    }

    public void setDataToPredictDir(String dataToPredictDir) {
        this.dataToPredictDir = dataToPredictDir;
    }

    public String getModelSaveDir() {
        return modelSaveDir;
    }

    public void setModelSaveDir(String modelSaveDir) {
        this.modelSaveDir = modelSaveDir;
    }

    public String getTrainDir() {
        return trainDir;
    }

    public void setTrainDir(String trainDir) {
        this.trainDir = trainDir;
    }

    public String getTestDir() {
        return testDir;
    }

    public void setTestDir(String testDir) {
        this.testDir = testDir;
    }

    public String getPredictDir() {
        return predictDir;
    }

    public void setPredictDir(String predictDir) {
        this.predictDir = predictDir;
    }

    public String getTmpDir() {
        return tmpDir;
    }

    public void setTmpDir(String tmpDir) {
        this.tmpDir = tmpDir;
    }
}
