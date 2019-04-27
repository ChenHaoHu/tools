package com.hcy.smartrobot.service;

import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizer;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerResponse;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerListener;
import com.hcy.smartrobot.config.TimerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SpeechRecognizerDemo class
 *
 * 一句话识别Demo
 */

@Service
public class SmartAudioTransImpl implements SmartAudioTrans {

    @Value("${value.AppKey}")
    public String appKey;
    @Value("${value.AudioFile}")
    String filePath;
    private String accessToken = "";
    NlsClient client;
    Logger logger;
    String res = "";
    ConcurrentHashMap<String,String> data = new ConcurrentHashMap<>();

    /**
     * @param appKey
     * @param token
     */
    {
        // Step0 创建NlsClient实例,应用全局创建一个即可,默认服务地址为阿里云线上服务地址
        client = new NlsClient(accessToken);

        logger = LoggerFactory.getLogger("SpeechRecognizerDemo.class");
    }



    public  String process(InputStream ins) {
        SpeechRecognizer recognizer = null;
        String taskId = "";

        SpeechRecognizerListener listener = new SpeechRecognizerListener() {
            @Override
            public void onRecognitionCompleted(SpeechRecognizerResponse response) {
                // 事件名称 RecognitionCompleted
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示识别成功
                        ", status: " + response.getStatus() +
                        // 一句话识别的完整结果
                        ", result: " + response.getRecognizedText());

                data.put(response.getTaskId(),response.getRecognizedText());
            }
        };

        try {
            // Step1 创建实例,建立连接
            recognizer = new SpeechRecognizer(client, listener);
            recognizer.setAppKey(appKey);
            // 设置音频编码格式
            recognizer.setFormat(InputFormatEnum.PCM);
            // 设置音频采样率
            recognizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            // 设置是否返回中间识别结果
            recognizer.setEnableIntermediateResult(false);
            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            recognizer.start();
            // Step3 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.send(ins)即可
            recognizer.send(ins);
            // Step4 通知服务端语音数据发送完毕,等待服务端处理完成
            recognizer.stop();
            taskId = recognizer.getTaskId();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Step5 关闭连接
            if (null != recognizer) {
                recognizer.close();
            }

            if (null!=ins){
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return taskId;

    }


    @Override
    public String getAudioTransData(InputStream ins) {
        client.setToken(TimerConfig.token);
        String taskid = null;
        taskid = process(ins);
        while (data.get(taskid) == null);
        String res = data.get(taskid);
        data.remove(taskid);
        return res;
    }

    @Override
    public InputStream getFileStream(String filePath) {
        FileInputStream fis = null;
        if (filePath != null){
            try {
                fis = new FileInputStream(new File(filePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
            }
        }
        return fis;
    }

    @Override
    public String transAudioType(String file) {

        String name = filePath+'/'+System.currentTimeMillis()+"";
        try {
            String cmd1 = "lame "+ file + " " + name+".wav --decode";
            Process exec1 = Runtime.getRuntime().exec(cmd1);
            exec1.waitFor();
            String cmd2 = "sox " + name +".wav -r 16000 -c 1 "+name+"_tras.wav";
            Process exec2 = Runtime.getRuntime().exec(cmd2);
            exec2.waitFor();
        }catch(Exception e){
            e.printStackTrace();
        }

        return name+"_tras.wav";
    }

    @Override
    public String getAudioContent(String str) {

        String s = transAudioType(str);
        System.out.println(s);
        InputStream stream = getFileStream(s);
        String audioTransData = getAudioTransData(stream);

        return audioTransData;
    }
}