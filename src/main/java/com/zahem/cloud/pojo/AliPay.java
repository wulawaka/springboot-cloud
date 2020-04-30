package com.zahem.cloud.pojo;

import lombok.Data;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;
@Data
@Component
public class AliPay {
    @NotEmpty
    private String subject;

    private String out_trade_no;//商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复

    @NotEmpty
    private String timout_express = "1h";//该笔订单允许的最晚付款时间

    @NotEmpty
    private String totle_amout;//订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]

    private static String product_code = "FAST_INSTANT_TRADE_PAY";//销售产品码，商家和支付宝签约的产品码，为固定值 FAST_INSTANT_TRADE_PAY

    private int userId;
}
