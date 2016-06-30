package edu.whu.irlab.service;

import edu.whu.irlab.model.User;

/**
 * Created by Roger on 2016/5/23.
 */
public interface UserService {
    public int deleteByPrimaryKey(Integer id);

    public int insert(User record);

    public int insertSelective(User record);

    public User selectByPrimaryKey(Integer id);

    public int updateByPrimaryKeySelective(User record);

    public int updateByPrimaryKey(User record);

    public User selectByUser(User record);

    public User selectByUsername(String username);
}
