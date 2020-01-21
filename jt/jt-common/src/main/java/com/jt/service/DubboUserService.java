package com.jt.service;

import com.jt.pojo.User;

public interface DubboUserService {

    void save(User user);

    String findUserByUP(User user);

}
