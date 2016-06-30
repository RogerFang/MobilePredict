package edu.whu.irlab.service;

import java.util.List;

/**
 * Created by Roger on 2016/5/17.
 */
public interface DataService {

    public List<String> getData2Train();

    public String getData2Predict();

    public String getPredictMonth();
}
