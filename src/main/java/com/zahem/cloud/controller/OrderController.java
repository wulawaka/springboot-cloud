package com.zahem.cloud.controller;

import com.alipay.api.AlipayApiException;
import com.zahem.cloud.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Slf4j
public class OrderController {
    @Autowired
    private IOrderService iOrderService;

    @RequestMapping(value = "alipay/toPay", method = RequestMethod.GET)
    public String alipay(Integer amount,int number,String token) throws AlipayApiException {
        String s = iOrderService.aliPay(amount, number,token);
        return s;
    }

    @RequestMapping("alipay/notifyurl")
    public String notifyAlipay(HttpServletResponse response, HttpServletRequest request) throws IOException, AlipayApiException {
        String notify = iOrderService.notify(request, response);
        return notify;
    }

    @RequestMapping("alipay/returnurl")
    public String returnAlipay() {
        log.info("----return-----");
        return " a li pay return ";
    }

}
