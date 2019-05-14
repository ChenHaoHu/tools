package com.hcy.smartrobot.service;


import org.springframework.stereotype.Service;

@Service
public interface UserService {

    boolean addUser(String name,String passwd);
    boolean loginUser(String name,String passwd);
}
