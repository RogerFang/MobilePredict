package edu.whu.irlab.service.impl;

import edu.whu.irlab.dao.PredictRecordMapper;
import edu.whu.irlab.model.PredictRecord;
import edu.whu.irlab.service.PredictRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Roger on 2016/5/19.
 */
@Service
public class PredictRecordServiceImpl implements PredictRecordService {

    @Autowired
    private PredictRecordMapper predictRecordMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return predictRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(PredictRecord record) {
        return predictRecordMapper.insert(record);
    }

    @Override
    public int insertSelective(PredictRecord record) {
        return predictRecordMapper.insertSelective(record);
    }

    @Override
    public PredictRecord selectByPrimaryKey(Integer id) {
        return predictRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PredictRecord record) {
        return predictRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(PredictRecord record) {
        return predictRecordMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateByModelAndPredictMonth(PredictRecord record) {
        return predictRecordMapper.updateByModelAndPredictMonth(record);
    }

    @Override
    public PredictRecord selectByModelAndPredictMonth(String model, String predictMonth) {
        return predictRecordMapper.selectByModelAndPredictMonth(model, predictMonth);
    }

    @Override
    public List<PredictRecord> selectByPredictMonth(String predictMonth) {
        return predictRecordMapper.selectByPredictMonth(predictMonth);
    }

}
