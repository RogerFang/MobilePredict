package edu.whu.irlab.service;

import edu.whu.irlab.model.TrainRecord;

import java.util.List;

/**
 * Created by Roger on 2016/5/19.
 */
public interface TrainRecordService {

    public int deleteByPrimaryKey(Integer id);

    public int insert(TrainRecord record);

    public int insertSelective(TrainRecord record);

    public TrainRecord selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(TrainRecord record);

    public int updateByPrimaryKey(TrainRecord record);

    public TrainRecord selectByModel(String model);

    public List<TrainRecord> selectByState(Integer state);

    public List<TrainRecord> selectAll();

    public int updateStateByModel(TrainRecord record);

    public int updateByModelSelective(TrainRecord record);
}
