package com.hcy.smartrobot.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SpeechRecognizerDemoTest {



   @Test
   public void test(){

        SmartAudioTrans demo = new SmartAudioTrans();
        demo.appKey = "WR4xyZhc4HGAlTRG";

        String audioTransData = demo.getAudioTransData("http://www.hcyang.top/demo.wav");
        System.out.println(audioTransData);

    }
}