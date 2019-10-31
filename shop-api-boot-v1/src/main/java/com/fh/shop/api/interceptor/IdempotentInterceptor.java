package com.fh.shop.api.interceptor;

import com.fh.shop.api.check.Idempotent;
import com.fh.shop.api.common.Enum;
import com.fh.shop.api.exception.GlobalException;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class IdempotentInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //ajax跨域接受处理请求的头信息
        /*response.addHeader("Access-Control-Allow-Headers","x-requested-with,content-type,token");
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");*/



        //对应不是提交订单的方法放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(Idempotent.class)){
            return true;
        }
        //取出头信息token
        String token = request.getHeader("token");
        //非空判断
        if (StringUtils.isEmpty(token)){
            throw new GlobalException(Enum.TOKEN_IS_NULL);
        }
        //取出redis中的头信息
        String redisToken = RedisUtil.get(token);
        //非空判断
        if (StringUtils.isEmpty(redisToken)){
            throw new GlobalException(Enum.HEADLER_IS_DG);
        }
        //判断redis是否还有数据，防止重复提交
        long del = RedisUtil.del(token);
        if (del < 1){
            throw new GlobalException(Enum.HEADLER_IS_DG);
        }
        return true;
}
}
