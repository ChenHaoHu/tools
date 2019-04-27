package com.hcy.smartrobot.controller;

import com.hcy.smartrobot.common.response.RespCode;
import com.hcy.smartrobot.common.response.ResponseEntity;
import com.hcy.smartrobot.service.ChatRobot;
import com.hcy.smartrobot.service.FileUtil;
import com.hcy.smartrobot.service.SmartAudioTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

/**
 * @ClassName: RobotController
 * @Author: hcy
 * @Description:
 * @Date: 2019-04-27 12:40
 * @Version: 1.0
 **/
@RestController
@RequestMapping("/v1")
public class RobotController {

    @Autowired
    ChatRobot chatRobot;

    @PostMapping("/chat/")
    public ResponseEntity getAudioContent(@RequestParam("str") String str){
        String data = chatRobot.getChatContent(str);
        HashMap<String,String> map = new HashMap<>();
        map.put("q",str);
        map.put("a",data);
        return  new ResponseEntity(RespCode.SUCCESS,map);
    }





}
