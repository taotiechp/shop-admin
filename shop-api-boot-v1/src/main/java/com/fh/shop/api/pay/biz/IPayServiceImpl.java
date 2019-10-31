package com.fh.shop.api.pay.biz;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.order.mapper.IOrderMapper;
import com.fh.shop.api.order.po.Order;
import com.fh.shop.api.paylog.mapper.IPayLogMapper;
import com.fh.shop.api.paylog.po.PayLog;
import com.fh.shop.api.util.BigDecimalUtil;
import com.fh.shop.api.util.DateUtil;
import com.fh.shop.api.util.MyConfig;
import com.fh.shop.api.util.RedisUtil;
import com.github.wxpay.sdk.WXPay;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.server.ServerCloneException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("payService")
public class IPayServiceImpl implements IPayService {

    @Autowired
    private IPayLogMapper payLogMapper;
    @Autowired
    private IOrderMapper orderMapper;


    /**
     * 获取支付吗路径
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse getPayUrl(long memberId) {
        String payJson = RedisUtil.get("pay" + memberId);
        PayLog payLog = JSONObject.parseObject(payJson, PayLog.class);
        Date date = DateUtils.addMinutes(new Date(), 2);
        String dateToStr = DateUtil.DateToStr(date, DateUtil.YYYYMMDDHHMMSS);
        if (payLog==null){
            ServerResponse.error(-2,"订单不存在");
        }
        MyConfig config = new MyConfig();
        try {
            while (true){
                WXPay wxpay = new WXPay(config);
                Map<String, String> data = new HashMap<String, String>();
                data.put("body", "订单支付");
                data.put("out_trade_no", payLog.getOutTradeNo());
                data.put("fee_type", "CNY");
                data.put("total_fee", BigDecimalUtil.getMoney((BigDecimalUtil.mul(payLog.getPayMoney()+"","1"))+""));
                //data.put("spbill_create_ip", "123.12.12.123");
                data.put("notify_url", "http://www.example.com/wxpay/notify");
                data.put("trade_type", "NATIVE");  // 此处指定为扫码支付
                data.put("product_id", "23434535");  // 此处指定为扫码支付
                data.put("time_expire",dateToStr);//交易结束时间
                Map<String, String> resp = wxpay.unifiedOrder(data);
                String renCode = resp.get("return_code");
                if (!renCode.equalsIgnoreCase("SUCCESS")){
                    String renMsg = resp.get("return_msg");
                    System.out.println(renMsg);
                    return ServerResponse.error(9999,"微信支付平台异常："+renMsg);
                }
                String resCode = resp.get("result_code");
                if (!resCode.equalsIgnoreCase("SUCCESS")){
                    String resMsg = resp.get("err_code_des");
                    System.out.println(resMsg);
                    return ServerResponse.error(9999,"微信支付平台异常："+resMsg);
                }
                String codeUrl = resp.get("code_url");
                payLog.setUrl(codeUrl);
                return ServerResponse.success(payLog);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }
    }

    /**
     * 查询支付状态
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse getStatus(long memberId) {
        String payJson = RedisUtil.get("pay" + memberId);
        PayLog payLog = JSONObject.parseObject(payJson, PayLog.class);
        MyConfig config = new MyConfig();
        try {
            WXPay wxpay = new WXPay(config);
            Map<String, String> data = new HashMap<String, String>();
            data.put("out_trade_no", payLog.getOutTradeNo());
            int count = 0;
            while (true){
                Map<String, String> resp = wxpay.orderQuery(data);
                String renCode = resp.get("return_code");
                if (!renCode.equalsIgnoreCase("SUCCESS")){
                    String renMsg = resp.get("return_msg");
                    System.out.println(renMsg);
                    return ServerResponse.error(9999,"微信支付平台异常："+renMsg);
                }
                String resCode = resp.get("result_code");
                if (!resCode.equalsIgnoreCase("SUCCESS")){
                    String resMsg = resp.get("err_code_des");
                    System.out.println(resMsg);
                    return ServerResponse.error(9999,"微信支付平台异常："+resMsg);
                }
                String state = resp.get("trade_state");
                if (state.equalsIgnoreCase("SUCCESS")){
                    String transactionId = resp.get("transaction_id");
                    Date date = new Date();
                    String orderId = payLog.getOrderId();
                    //更新支付日志表
                    PayLog payLogInfo = payLogMapper.selectById(payLog.getOutTradeNo());
                    payLogInfo.setPayTime(date);
                    payLogInfo.setTransaction(transactionId);
                    payLogInfo.setPayStatus(20);
                    payLogMapper.updateById(payLogInfo);
                    //更新订单表
                    Order orderInfo = orderMapper.selectById(orderId);
                    orderInfo.setPayDate(date);
                    orderInfo.setStatus(20);
                    orderMapper.updateById(orderInfo);
                    //删除缓存
                    RedisUtil.del("pay" + memberId);
                    System.out.println("支付成功");
                    return ServerResponse.success();
                }
                Thread.sleep(3000);
                count++;
                if (count > 40){
                    System.out.println("支付超时");
                    return ServerResponse.error(9998,"支付超时");
                }
                System.out.println("用户未支付");

            }
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.error();
        }
    }
}
