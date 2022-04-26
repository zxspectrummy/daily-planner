package com.example.dailyplanner.service;

import com.example.dailyplanner.model.User;

import java.util.List;

public interface UserService {
    List<User> listAll();

    User getById(long id);

    User saveOrUpdate(User user) throws Exception;

    void delete(long id) throws Exception;

    User getCurrentUser();
}
