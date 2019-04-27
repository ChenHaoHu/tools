package com.hcy.smartrobot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ChatRobotImplTest {



    @Autowired
    ChatRobot chatRobot;

    @Test
    public void getChatContent() {
        String dasd = chatRobot.getChatContent("你好？");
        System.out.println(dasd);


    }
}