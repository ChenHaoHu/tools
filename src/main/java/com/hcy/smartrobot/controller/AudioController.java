package com.hcy.smartrobot.controller;

import com.hcy.smartrobot.common.response.RespCode;
import com.hcy.smartrobot.common.response.ResponseEntity;
import com.hcy.smartrobot.service.FileUtil;
import com.hcy.smartrobot.service.SmartAudioTrans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: testController
 * @Author: hcy
 * @Description:
 * @Date: 2019-04-26 19:17
 * @Version: 1.0
 **/


@RestController
@RequestMapping("/v1")
public class AudioController {


    @Autowired
    FileUtil fileUtil;
    @Autowired
    SmartAudioTrans smartAudioTrans;

    @PostMapping("/audio/")
    public ResponseEntity test(@RequestParam("file") MultipartFile file){
        String s = fileUtil.saveIOStream(file);
        String audioContent = smartAudioTrans.getAudioContent(s);
        return  new ResponseEntity(RespCode.SUCCESS,audioContent);
    }
}
