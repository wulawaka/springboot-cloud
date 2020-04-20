package com.zahem.cloud.config;

import lombok.Data;

@Data
public class AxiosResponse {
    private int code;
    private String msg;
    private Object data;

    public AxiosResponse(){}

    public static AxiosResponse error(CustomExprotion e){
        AxiosResponse response=new AxiosResponse();
        response.setCode(e.getCode());
        response.setMsg(e.getMsg());
        return response;
    }

    public static AxiosResponse success(){
        AxiosResponse response=new AxiosResponse();
        response.setCode(200);
        response.setMsg("success");
        return response;
    }

    public static AxiosResponse success(Object data){
        AxiosResponse response=new AxiosResponse();
        response.setCode(200);
        response.setMsg("success");
        response.setData(data);
        return response;
    }
}
