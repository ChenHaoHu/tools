package com.hcy.smartrobot.common.response;

public class ResponseEntity {
    private int code;
    private String msg;
    private Object data;

    public ResponseEntity(RespCode respCode) {
        this.code = respCode.getCode();
        this.msg = respCode.getMsg();
    }

    public ResponseEntity(RespCode respCode, Object data) {
        this(respCode);
        this.data = data;
    }
    public ResponseEntity(){

    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}