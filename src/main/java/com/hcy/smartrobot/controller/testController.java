package com.hcy.smartrobot.controller;

import com.hcy.smartrobot.service.SmartAudioTrans;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: testController
 * @Author: hcy
 * @Description:
 * @Date: 2019-04-26 19:17
 * @Version: 1.0
 **/


@RestController
public class testController {


    @RequestMapping("/test")
    public String test(@RequestParam("url")String url){
        SmartAudioTrans demo = new SmartAudioTrans();
        demo.appKey = "WR4xyZhc4HGAlTRG";
        String audioTransData = demo.getAudioTransData(url);
        return  audioTransData;
    }





}
