package edu.whu.irlab.dao;

import edu.whu.irlab.model.TrainRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TrainRecord record);

    int insertSelective(TrainRecord record);

    TrainRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TrainRecord record);

    int updateByPrimaryKey(TrainRecord record);

    TrainRecord selectByModel(String model);

    List<TrainRecord> selectByState(Integer state);

    List<TrainRecord> selectAll();

    int updateStateByModel(TrainRecord record);

    int updateByModelSelective(TrainRecord record);
}