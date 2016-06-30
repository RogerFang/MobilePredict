package edu.whu.irlab.process.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roger on 2016/5/17.
 */
public class Command {

    // private String PY_INTERFACE =  CommandProps.createInstance().getProp("PY_INTERFACE");
    //
    // public String MODE_TRAIN = CommandProps.createInstance().getProp("MODE_TRAIN");
    // public String MODE_PREDICT = CommandProps.createInstance().getProp("MODE_PREDICT");
    //
    // public String MODEL_LINEAR = CommandProps.createInstance().getProp("MODEL_LINEAR");
    // public String MODEL_RMLP = CommandProps.createInstance().getProp("MODEL_RMLP");
    // public  final String MODEL_CMLP = CommandProps.createInstance().getProp("MODEL_CMLP");
    // public static final String MODEL_CNN = CommandProps.createInstance().getProp("MODEL_CNN");
    // public static final String MODEL_RNN = CommandProps.createInstance().getProp("MODEL_RNN");

    private PreProcessProps preProcessProps;

    // 模式:train or predict(必须)
    private String mode = null;
    // 模型:linear,rmlp,cmlp,cnn,rnn; default:linear
    private String model = null;

    //=>> 数据地址
    // 训练数据位置(train模式下必须)
    private String trainDataPath = null;
    // 测试数据位置(train模式下必须)
    private String testDataPath = null;
    // 预测数据位置(predict模式下必须)
    private String predictDataPath = null;

    //=>> 系统运行参数
    // 训练轮数, defalut:200
    private String trainingEpochs = null;
    // batch size, default:200
    private String batchSize = null;
    // 展示间隔display step, default:10
    private String displayStep = null;
    // 保存模型间隔save step, default:50
    private String saveStep = null;
    // 学习率learningRate, default:0.01
    private String learningRate = null;
    // rnn中的月数, default:3
    private String numSteps = null;
    // 特征数量, default:71
    private String featureSize = null;
    // 类别数量, default:2
    private String outputSize = null;
    // CNN model必须
    private String perm = null;

    // 模型存放地址(train模式必须)
    private String modelPath = null;

    public Command(PreProcessProps preProcessProps){
        this.preProcessProps = preProcessProps;
        this.model = preProcessProps.getProp("model");
        this.trainingEpochs = preProcessProps.getProp("trainingEpochs");
        this.batchSize = preProcessProps.getProp("batchSize");
        this.displayStep = preProcessProps.getProp("displayStep");
        this.saveStep = preProcessProps.getProp("saveStep");
        this.learningRate = preProcessProps.getProp("learningRate");
        this.numSteps = preProcessProps.getProp("numSteps");
        this.featureSize = preProcessProps.getProp("featureSize");
        this.outputSize = preProcessProps.getProp("outputSize");
        this.perm = preProcessProps.getProp("perm");
    }

    public String getMode() {
        if (this.mode==null){
            throw new NullPointerException("mode is null, please set mode first");
        }
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTrainDataPath() {
        if (getMode() == preProcessProps.getProp("MODE_TRAIN")){
            if (trainDataPath == null){
                throw new NullPointerException("trainDataPath is null, please set trainDataPath first in mode train");
            }
        }
        return trainDataPath;
    }

    public void setTrainDataPath(String trainDataPath) {
        this.trainDataPath = trainDataPath;
    }

    public String getTestDataPath() {
        if (getMode() == preProcessProps.getProp("MODE_TRAIN")){
            if (testDataPath == null){
                throw new NullPointerException("testDataPath is null, please set testDataPath first in mode train");
            }
        }
        return testDataPath;
    }

    public void setTestDataPath(String testDataPath) {
        this.testDataPath = testDataPath;
    }

    public String getPredictDataPath() {
        if (getMode() == preProcessProps.getProp("MODE_PREDICT")){
            if (predictDataPath == null){
                throw new NullPointerException("predictDataPath is null, please set predictDataPath first in mode predict");
            }
        }
        return predictDataPath;
    }

    public void setPredictDataPath(String predictDataPath) {
        this.predictDataPath = predictDataPath;
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
        if (getModel() == preProcessProps.getProp("MODEL_CNN")){
            if (perm == null){
                throw new NullPointerException("perm is null, please set perm first in mode train with model CNN");
            }
        }
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
    }

    public String getModelPath() {
        if (modelPath == null){
            throw new NullPointerException("modelPath is null, please set modelPath first");
        }
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    private String constructOptions(){
        Map<String, String> options = new HashMap<String, String>();
        options.put("--mode", getMode());
        options.put("--model", getModel());
        options.put("--train_data", getTrainDataPath());
        options.put("--test_data", getTestDataPath());
        options.put("--predict_data", getPredictDataPath());
        options.put("--training_epochs", getTrainingEpochs());
        options.put("--batch_size", getBatchSize());
        options.put("--display_step", getDisplayStep());
        options.put("--save_step", getSaveStep());
        options.put("--learning_rate", getLearningRate());
        options.put("--num_steps", getNumSteps());
        options.put("--feature_size", getFeatureSize());
        options.put("--output_size", getOutputSize());
        options.put("--perm", getPerm());
        options.put("--model_path", getModelPath());

        String rtnOptions = "";
        for (Map.Entry<String, String> entry: options.entrySet()){
            rtnOptions += " " + entry.getKey() + " " + entry.getValue();
        }

        return rtnOptions;
    }

    public String getCommand(){
        return "python -u "+ preProcessProps.getProp("PY_INTERFACE") +constructOptions();
    }

    @Override
    public String toString() {
        return getCommand();
    }
}
