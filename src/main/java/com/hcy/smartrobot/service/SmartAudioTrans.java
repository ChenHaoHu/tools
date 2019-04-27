package com.hcy.smartrobot.service;

import org.springframework.stereotype.Service;

import java.io.InputStream;


@Service
public interface SmartAudioTrans {
    String getAudioTransData(InputStream is);
    InputStream getFileStream(String filePath);
    String transAudioType(String file);
    String getAudioContent(String str);
}
