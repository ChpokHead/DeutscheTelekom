package com.chpok.logiweb.dao;

import com.chpok.logiweb.model.User;

import java.util.Optional;

public interface UserDao extends CrudDao<User> {
    Optional<User> findUserByUsername(String username);
}
