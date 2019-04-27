package com.hcy.smartrobot.service;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUtil {

    String saveIOStream(MultipartFile file);


}
