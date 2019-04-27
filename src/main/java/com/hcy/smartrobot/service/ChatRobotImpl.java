package com.hcy.smartrobot.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName: ChatRobotImpl
 * @Author: hcy
 * @Description:
 * @Date: 2019-04-27 12:49
 * @Version: 1.0
 **/

@Service
public class ChatRobotImpl implements ChatRobot {


    @Value("${value.RobotUrl}")
    String robotUrl;
    @Value("${value.RobotApiKey}")
    String robotApiKey;
    @Value("${value.RobotUserId}")
    String robotUserId;

    @Override
    public String getChatContent(String str) {
        String data = null;

        JSONObject params = new JSONObject();
        JSONObject perception = new JSONObject();
        JSONObject inputText = new JSONObject();
        inputText.put("text",str);
        perception.put("inputText",inputText);
        params.put("perception",perception);
        JSONObject userInfo = new JSONObject();
        userInfo.put("userId",robotUserId);
        userInfo.put("apiKey",robotApiKey);
        params.put("userInfo",userInfo);

        MediaType mediaType = MediaType.parse("text/json; charset=utf-8");
        String requestBody = params.toJSONString();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(robotUrl)
                .post(RequestBody.create(mediaType,requestBody))//默认就是GET请求，可以不写
                .build();

        Response response = null;
        try {

            response = okHttpClient.newCall(request).execute();
            String res = response.body().string();
            JSONObject json= JSON.parseObject(res);
            JSONArray array = (JSONArray)json.get("results");
            JSONObject o = (JSONObject)array.get(0);
            JSONObject values = (JSONObject)o.get("values");
            data = (String) values.get("text");

        } catch (Exception e) {
            System.out.println("出错");
            data = "错误";
        }
        return data;
    }
}
