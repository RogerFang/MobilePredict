package edu.whu.irlab.dao;

import edu.whu.irlab.model.PredictRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PredictRecord record);

    int insertSelective(PredictRecord record);

    PredictRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PredictRecord record);

    int updateByPrimaryKey(PredictRecord record);

    int updateByModelAndPredictMonth(PredictRecord record);

    PredictRecord selectByModelAndPredictMonth(String model, String predictMonth);

    List<PredictRecord> selectByPredictMonth(String predictMonth);
}