package edu.whu.irlab.model;

import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.util.StrUtil;

import java.util.List;

public class TrainRecord {
    private Integer id;

    private String model;

    private String lastMonth;

    private String dataPath;

    private String modelPath;

    private String trainPrecision;

    private Integer state;

    public TrainRecord(){}

    public TrainRecord(InputParam inputParam){
        List<String> months = inputParam.getMonths();
        this.setModel(inputParam.getModel());
        this.setLastMonth(months.get(months.size()-1).split("\\.")[0]);
        this.setDataPath(StrUtil.strJoin(months, ","));
        this.setModelPath(inputParam.getModelPath());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model == null ? null : model.trim();
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth == null ? null : lastMonth.trim();
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath == null ? null : dataPath.trim();
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath == null ? null : modelPath.trim();
    }

    public String getTrainPrecision() {
        return trainPrecision;
    }

    public void setTrainPrecision(String trainPrecision) {
        this.trainPrecision = trainPrecision == null ? null : trainPrecision.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}