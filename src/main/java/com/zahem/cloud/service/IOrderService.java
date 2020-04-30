package com.zahem.cloud.service;


import com.alipay.api.AlipayApiException;
import com.zahem.cloud.config.AxiosResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IOrderService {
    public String aliPay( Integer amount, int number,String token) throws AlipayApiException;
    public String notify(HttpServletRequest request, HttpServletResponse response);
}
