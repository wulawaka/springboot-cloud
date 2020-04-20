package com.zahem.cloud.config;

public enum CustomExprotion {
    USER_INPUT_ERROR(400,"用户输入错误");

    private String msg;
    private int code;

    CustomExprotion(int code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public String getMsg(){return msg;}

    public int getCode(){return code;}
}
