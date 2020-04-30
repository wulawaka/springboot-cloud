package com.zahem.cloud.service.impl;

import com.alibaba.fastjson.JSON;

import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.zahem.cloud.config.AliPayConfig;
import com.zahem.cloud.dao.OrderMapper;
import com.zahem.cloud.dao.PayInfoMapper;
import com.zahem.cloud.dao.ProductMapper;
import com.zahem.cloud.pojo.AliPay;
import com.zahem.cloud.pojo.Order;
import com.zahem.cloud.pojo.Product;
import com.zahem.cloud.service.IOrderService;
import com.zahem.cloud.utils.RedisClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Resource
    private RedisClient redisClient;
    @Autowired
    private AliPayConfig aliPayConfig;
    @Autowired
    private AliPay aliPay;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private PayInfoMapper payInfoMapper;


    /**
     * 支付
     * @param amount    支付金额
     * @param productId    从前端获取1、vip1,2、vip2
     * @param token     交验用户是否登陆
     * @return
     * @throws AlipayApiException
     */
    @Override
    public String aliPay(Integer amount,int productId,String token) throws AlipayApiException {
        //登录校验部分
        Boolean hasToken = redisClient.hasKey(token);
        if(hasToken == false){
            return "FAILE";
        }
        Object userId = redisClient.get("userId");

        String out_trade_no=UUID.randomUUID().toString().replace("-","").substring(0,13);
        Product product = productMapper.selectByPrimaryKey(productId);

        aliPay.setSubject(product.getName());
        aliPay.setOut_trade_no(out_trade_no);
        aliPay.setUserId((Integer) userId);
        // 构建支付数据信息
        Map<String, String> data = new HashMap<>();
        data.put("subject", product.getName()); //订单标题
        data.put("out_trade_no", out_trade_no); //商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复      //此处模拟订单号为时间
        data.put("timeout_express", aliPay.getTimout_express()); //该笔订单允许的最晚付款时间
        data.put("total_amount", amount.toString()); //订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
        data.put("product_code", "FAST_INSTANT_TRADE_PAY"); //销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY

        //构建客户端
        DefaultAlipayClient alipayRsa2Client = new DefaultAlipayClient(
                AliPayConfig.gatewayUrl,
                AliPayConfig.app_id,
                AliPayConfig.merchant_private_key,
                "json",
                AliPayConfig.charset,
                AliPayConfig.alipay_public_key,
                AliPayConfig.sign_type);
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();// APP支付
        AlipayTradePagePayRequest requests = new AlipayTradePagePayRequest();  // 网页支付
//        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();  //移动h5
        requests.setNotifyUrl(AliPayConfig.notify_url);
        requests.setReturnUrl(AliPayConfig.return_url);
        requests.setBizContent(JSON.toJSONString(data));
        log.info(JSON.toJSONString(data));
        String responses = alipayRsa2Client.pageExecute(requests).getBody();
        return responses;
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    public String notify(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        log.info(">>>>>>>>支付成功, 进入异步通知接口...");
        // 一定要验签，防止黑客篡改参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder notifyBuild = new StringBuilder(">>>>>>>>>> alipay notify >>>>>>>>>>>>>>\n");
        parameterMap.forEach((key, value) -> notifyBuild.append(key + "=" + value[0] + "\n"));
        notifyBuild.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info(notifyBuild.toString());
        boolean flag = AliPayConfig.rsaCheckV1(request);
        if (flag) {
            //交易状态
            String tradeStatus = AliPayConfig.getByte(request.getParameter("trade_status"));
            // 商户订单号
            String out_trade_no = AliPayConfig.getByte(request.getParameter("out_trade_no"));
            //支付宝交易号
            String trade_no = AliPayConfig.getByte(request.getParameter("trade_no"));
            //付款金额
            String total_amount = AliPayConfig.getByte(request.getParameter("total_amount"));
            log.info("交易状态:{},商户订单号:{},支付宝交易号:{},付款金额:{}", tradeStatus, out_trade_no, trade_no, total_amount);
            // TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
            // TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
            if (tradeStatus.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，
                // 并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序
                SimpleDateFormat sdf=new SimpleDateFormat();
                String format = new SimpleDateFormat().format(new Date());

                Order order=new Order();
                order.setOrderNo(Long.parseLong(out_trade_no));
                order.setUserId(aliPay.getUserId());
                order.setShippingId(30);
                order.setPayment(BigDecimal.valueOf(Long.parseLong(total_amount)));
                order.setPaymentTime(sdf.parse(format));

                orderMapper.insert(order);

                //注意：
                //如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                //如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
            }
            return "success";
        }
        return "faile";
    }








}
