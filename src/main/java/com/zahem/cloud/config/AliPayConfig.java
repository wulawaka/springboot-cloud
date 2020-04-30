package com.zahem.cloud.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
@Slf4j
public class AliPayConfig {

    private static String neturl = "http://vc8vir.natappfree.cc";

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号 按照我文章图上的信息填写
    public static String app_id = "2016102400748797";

    // 商户私钥，您的PKCS8格式RSA2私钥  刚刚生成的私钥直接复制填写
    public static String merchant_private_key ="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCBD2kIoqELLgu3sq7AYOWhlj/kwjh+1VussaYConZy3DHE/rwgBbIvXZOffI5g9L79qBd6oWddHPMTGN2zBOw+YqdngzPrH3+XvaFIeglsKYo6RISu1meC5n6g6X901eUUTC9TPKHwDaP6nQj3/zoqxVyp29PN3qNFjfCH5dw0u1oyX+sSSH9ajgGu3XSFnHL7j3/vfcfb+pxhb6E1VwUvOy4Y1rfQQ/f9uIJuEdCWxEM1tF084PLsgG0+VizFjMoTd1QgS8CZVIHWC0YgfzbAmUfa7r7UIgRjKU4EMlKRPOXm5m55eScLRFsI+1W/FfgjjQCfmGyQAr/eTGNEQdClAgMBAAECggEAWirFDBZrAx+Rai75LkjERgdt7knPJLW1jcqznCWbMZYrY2FBV/IC0lrDV2qzIoP7p8Y3WSLIz2H5ZT87LOZkkxVjtcsRi9R+1SnUM4mnaZ0ACMN0G56JxQd6MrvPo3YIzQyJB9NVpBwzk1UBPhpfriAjJD0EFlHUqjSHdrqeBWxcGZTQncjOQNoDJ2VlJdy7/tkS7lxVzBqG6GDIYti9mzwLfP8Wmqj8SfC7OtKzmqI56XOCd3aMn+r3F+OQ+AoFrqO7dFUf8C+YH3zfCcFqoYgCVr7lHpw+XqSZ4KYTXImaurDkSW0srC60qujLCjGfg2zkEymuYQIciNWO8JaOIQKBgQDKe3xDXy+zTOxN/3ghIXwhjrABPH+UnDTageig4x4rDm3hjKsC8wqR9IBl3M2UVOgUR9B3QgX6t26rJnPLpFCdBuh9+Pi2Oc/S47t5nKyToBNzSVfSpcpoTeXkClJz5I3QGSR/ZV7rT1+xj3yKBm7Q/APxPuNrzdH1PB5C8HGYHQKBgQCjK/qMr4QlaBmQYt4Elq/xdjzcrupiacmwE8Ld1lmz73fNJfqV1E8Gd7dTORbvEIfcXJHFY7X74Me3EBeaGY2M7V/A2HVld5/Jj0sSS0WVJkoKLuZM3uyS274AbP7iNdV+nOveCPk9WndP3jpJyiY++8QVJFMPfiwKqcW6+3AEKQKBgG9dPXochzK5cxpvENZAAXK/dMQpTw4gX91yJ80dPUF3HS45XPExVTXzYQ8lQxh7Hm3RFbIR6RH9A2w0qOlvBTtoSkTGw5wN7eTShhEb/7ruBuSYiSn9L3bwqSkkGDdPlEPJIE7FJVCMhlg1T8zmPtrIUS7FZPWs7PZRS1+LuR5tAoGBAKKKFYv87rLgbs6wXZ7OiDcMY/Yk21/rJziZhry6YCLogTs/AM4tbgbwTBK/xMOhgOSW5bJt8AMW13rYsdoUaDeg4YhVVyQbNHVwowR3LLo1KP3Z2Bls2tYzxKHh8Uhe/eug3ygPYk+mhUFvCjsk/lIxMxMoWwEM20xH76qh+uR5AoGBAKgP06pdNI8c6rkZ3nsp7yuI3W4OXgkMiwB6eR4wtwBG1P99wv3xWmoJMnlXzb/l1vttOUmt1aG1piPpWu7xNa9U0WxmaghZ/Sd8kJxDA9fCvk1VLkuJRDR7s+DguHmmxCUNcMcuFV4b12nzDQ15p9EiVIOyHoL3GNcayDIUMqxB";

    // 支付宝公钥,对应APPID下的支付宝公钥。 按照我文章图上的信息填写支付宝公钥，别填成商户公钥
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2snixaBnnLWdMzDjFK05WPVKnH0hkCheQRC7XICh8cm8CKsyaXcM2CGTUhJtiMr5r2y8CMfisW3Bhxe4f4Bpa0ITuBUvNY4ZzHFZUuPKmNbqOjJPowRRXOyvwsOr7aBZsa8u+L1CgGBOL6s5w8G3UJK6ve4W0idIqwKak90V8CH6vMzonGIOTwyL3TVGPwEOg6pGEGicpJ1nxtXa0nIyWKizkPScGmIAJcMq6s2BP63XzwFnsQllnYHnSGHmoSIUUpaB1YobjgbgDwiNrt/JWRMOLx+f79VaMwgIVWSSFrQVv/iteqKGfLXh7YJOwgCjJ/Mni9E67VUvNoGCN9AutwIDAQAB";
    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，其实就是你的一个支付完成后返回的页面URL
    public static String notify_url = "http://frdf6y.natappfree.cc/alipay/notifyurl";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，其实就是你的一个支付完成后返回的页面URL
    public static String return_url ="http://frdf6y.natappfree.cc/alipay/returnurl";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";

    /*** 校验签名*/
    public static boolean rsaCheckV1(HttpServletRequest request) {
        // https://docs.open.alipay.com/54/106370
        // 获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        try {
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipay_public_key, charset, sign_type);
            return signVerified;
        } catch (AlipayApiException e) {
            log.debug("verify sigin error, exception is:{}", e);
            return false;
        }
    }

    /**
     * 转码
     * @param param
     * @return
     */
    public static String getByte(String param) {
        try {
            return new String(param.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }




}
