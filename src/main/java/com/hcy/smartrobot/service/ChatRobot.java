package com.hcy.smartrobot.service;


import org.springframework.stereotype.Service;

@Service
public interface ChatRobot {

    String getChatContent(String str);

}
