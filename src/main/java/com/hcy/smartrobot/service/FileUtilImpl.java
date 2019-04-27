package com.hcy.smartrobot.service;

import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ClassName: FileUtilImpl
 * @Author: hcy
 * @Description:
 * @Date: 2019-04-27 10:20
 * @Version: 1.0
 **/

@Service
public class FileUtilImpl implements FileUtil {

    @Value("${value.AudioFile}")
    String filePath;

    @Override
    public String saveIOStream(MultipartFile file)  {
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        File out = new File(filePath+'/'+file.getOriginalFilename());
        try {
            inputStream = file.getInputStream();

            outputStream = new FileOutputStream(out);
            int temp;
            while ((temp = inputStream.read())!= -1){
                outputStream.write(temp);
                outputStream.flush();
            }

        }catch (Exception e){
            System.out.println("出现错误");
            System.out.println(e.fillInStackTrace());
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return out.getAbsolutePath();
    }
}
