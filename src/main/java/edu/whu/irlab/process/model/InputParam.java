package edu.whu.irlab.process.model;

import java.util.List;

/**
 * Created by Roger on 2016/5/17.
 */
public class InputParam {

    private String mode;

    private String model;

    private String modelPath;

    private boolean isStdOut = false;

    private List<String> months;

    public String getMode() {
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

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public boolean isStdOut() {
        return isStdOut;
    }

    public void setStdOut(boolean stdOut) {
        isStdOut = stdOut;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }
}
