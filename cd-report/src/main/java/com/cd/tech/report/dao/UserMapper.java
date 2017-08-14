package com.cd.tech.report.dao;

import com.cd.tech.report.model.User;

import java.util.List;

/**
 * Created by zc on 2017/4/19.
 */
public interface UserMapper {

    void insertUser(User user);

    User getUser(String mobile);

    void batchInsert(List<User> userList);
}
