package com.chpok.logiweb.service.impl;

import com.chpok.logiweb.dao.UserDao;
import com.chpok.logiweb.model.User;
import com.chpok.logiweb.service.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserById(Long id) {
        return userDao.findById(id).get();
    }
}
