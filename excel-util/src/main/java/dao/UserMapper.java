package dao;

import model.User;

/**
 * Created by zc on 2017/4/19.
 */
public interface UserMapper {

    public void insertUser(User user);

    public User getUser(String name);
}
