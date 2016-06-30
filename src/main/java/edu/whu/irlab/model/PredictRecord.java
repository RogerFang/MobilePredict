package edu.whu.irlab.model;

import edu.whu.irlab.process.model.InputParam;
import edu.whu.irlab.util.CalendarUtil;

import java.io.File;
import java.util.List;

public class PredictRecord {
    private Integer id;

    private String model;

    private String lastMonth;

    private String predictMonth;

    private String indexPath;

    private String resultPath;

    private String predictPrecision;

    private Integer state;

    public PredictRecord(){}

    public PredictRecord(InputParam inputParam){
        String model = inputParam.getModel();
        List<String> months = inputParam.getMonths();
        String lastMonth = months.get(months.size()-1).split("\\.")[0];
        String predictMonth = CalendarUtil.getNextMonth(lastMonth);

        this.setModel(model);
        this.setLastMonth(lastMonth);
        this.setPredictMonth(predictMonth);
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

    public String getPredictMonth() {
        return predictMonth;
    }

    public void setPredictMonth(String predictMonth) {
        this.predictMonth = predictMonth == null ? null : predictMonth.trim();
    }

    public String getIndexPath() {
        return indexPath;
    }

    public void setIndexPath(String indexPath) {
        this.indexPath = indexPath == null ? null : indexPath.trim();
    }

    public String getResultPath() {
        return resultPath;
    }

    public void setResultPath(String resultPath) {
        this.resultPath = resultPath == null ? null : resultPath.trim();
    }

    public String getPredictPrecision() {
        return predictPrecision;
    }

    public void setPredictPrecision(String predictPrecision) {
        this.predictPrecision = predictPrecision == null ? null : predictPrecision.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}