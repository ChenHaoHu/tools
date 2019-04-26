package com.hcy.smartrobot.service;

import com.alibaba.nls.client.AccessToken;
import com.aliyuncs.exceptions.ClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

/**
 * @ClassName: TimerConfig
 * @Author: hcy
 * @Description:
 * @Date: 2019-04-26 17:31
 * @Version: 1.0
 **/

@Service
public class TimerConfig {

    @Value("${value.AccessKeyID}")
    private String akid;
    @Value("${value.AccessKeySecret}")
    private String akSecrete;
    public static  String token = "8ae17f0d3be44a29b2e1d760e8efbce8";
    Logger logger = LoggerFactory.getLogger("TimerConfig");


    @Scheduled(fixedRate = 1200000)
    public void testTasks() {

        AccessToken accessToken = null;
        try {
            accessToken = AccessToken.apply(akid, akSecrete);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        token = accessToken.getToken();
        logger.info("GET Token:" + token);
    }
}
