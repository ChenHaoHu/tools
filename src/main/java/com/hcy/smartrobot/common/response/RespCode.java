package com.hcy.smartrobot.common.response;

public enum RespCode {
    ERROR(1,"请求失败"),
    SUCCESS(200, "请求成功"),
    WARN(-1, "网络异常，请稍后重试");


    private int code;
    private String msg;

    RespCode(int code, String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
}