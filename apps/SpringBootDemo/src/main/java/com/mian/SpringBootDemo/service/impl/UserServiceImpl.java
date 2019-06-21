package com.mian.SpringBootDemo.service.impl;

import com.mian.SpringBootDemo.dao.UserRepository;
import com.mian.SpringBootDemo.domain.User;
import com.mian.SpringBootDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findUserByName(String name) {
        return this.userRepository.findByName(name);
    }
}
