package com.fh.shop.api.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SMSUtil {

    private static final String URL = "https://api.netease.im/sms/sendcode.action";
    private static final String APPKEY = "22c0bee758f30c55da1e632d68730065";
    private static final String NONCE = UUID.randomUUID().toString();
    private static final String CURTIME = System.currentTimeMillis()+"";
    private static final String APPSECRET  = "78dc1b3e3155";
    private static final String PHONE  = "17839231072";

    /**
     * 网易云信
     */
    public static String getSMSCode(String phone) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(URL);
        //传递的参数集合
        List<BasicNameValuePair> params = new ArrayList<>();
        //传递header
        httpPost.addHeader("AppKey",APPKEY);
        httpPost.addHeader("Nonce",NONCE);
        httpPost.addHeader("CurTime",CURTIME);
        httpPost.addHeader("CheckSum",CheckSumBuilder.getCheckSum(APPSECRET,NONCE,CURTIME));
        //传递的参数
        BasicNameValuePair mobileParam = new BasicNameValuePair("mobile", phone);
        //将所有需要传递的参数添加到参数聚合
        params.add(mobileParam);
        //对参数集合进行url编码
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params, "utf-8");
        //将参数添加到httppost的请求中
        httpPost.setEntity(urlEncodedFormEntity);
        //发送请求
        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity, "utf-8");
        System.out.println(result);
        //获取发送验证码
        String obj = JSON.parseObject(result).getString("obj");
        return result;
    }

}
