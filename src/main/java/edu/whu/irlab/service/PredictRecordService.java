package edu.whu.irlab.service;

import edu.whu.irlab.model.PredictRecord;

import java.util.List;

/**
 * Created by Roger on 2016/5/19.
 */
public interface PredictRecordService {

    public int deleteByPrimaryKey(Integer id);

    public int insert(PredictRecord record);

    public int insertSelective(PredictRecord record);

    public PredictRecord selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(PredictRecord record);

    public int updateByPrimaryKey(PredictRecord record);

    public int updateByModelAndPredictMonth(PredictRecord record);

    public PredictRecord selectByModelAndPredictMonth(String model, String predictMonth);

    public List<PredictRecord> selectByPredictMonth(String predictMonth);
}
