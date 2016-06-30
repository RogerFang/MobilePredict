package edu.whu.irlab.service.impl;

import edu.whu.irlab.dao.TrainRecordMapper;
import edu.whu.irlab.model.TrainRecord;
import edu.whu.irlab.service.TrainRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Roger on 2016/5/19.
 */
@Service
public class TrainRecordServiceImpl implements TrainRecordService{

    @Autowired
    private TrainRecordMapper trainRecordMapper;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return trainRecordMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(TrainRecord record) {
        return trainRecordMapper.insert(record);
    }

    @Override
    public int insertSelective(TrainRecord record) {
        return trainRecordMapper.insertSelective(record);
    }

    @Override
    public TrainRecord selectByPrimaryKey(Integer id) {
        return trainRecordMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(TrainRecord record) {
        return trainRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TrainRecord record) {
        return trainRecordMapper.updateByPrimaryKey(record);
    }

    @Override
    public TrainRecord selectByModel(String model) {
        return trainRecordMapper.selectByModel(model);
    }

    @Override
    public List<TrainRecord> selectByState(Integer state) {
        return trainRecordMapper.selectByState(state);
    }

    @Override
    public List<TrainRecord> selectAll() {
        return trainRecordMapper.selectAll();
    }

    @Override
    public int updateStateByModel(TrainRecord record) {
        return trainRecordMapper.updateStateByModel(record);
    }

    @Override
    public int updateByModelSelective(TrainRecord record) {
        return trainRecordMapper.updateByModelSelective(record);
    }
}
