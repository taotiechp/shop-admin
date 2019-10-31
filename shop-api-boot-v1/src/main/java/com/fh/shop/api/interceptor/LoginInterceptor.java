package com.fh.shop.api.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.fh.shop.api.check.Check;
import com.fh.shop.api.common.Enum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.exception.GlobalException;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.Md5Util;
import com.fh.shop.api.util.RedisUtil;
import com.fh.shop.api.util.SystemConst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.util.Base64;

public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //ajax跨域接受处理请求的头信息
        response.addHeader("Access-Control-Allow-Headers","x-requested-with,content-type,x-auth,token");
        response.addHeader("Access-Control-Allow-Origin","*");
        response.addHeader("Access-Control-Allow-Methods","POST,GET,OPTIONS,DELETE");

        //options特殊请求的处理
        String methods = request.getMethod();
        if (methods.equalsIgnoreCase("options")){
            return false;
        }

        //将加入自定义注解的方法设置拦截
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (!method.isAnnotationPresent(Check.class)){
            return true;
        }
        //取出头信息
        String header = request.getHeader("x-auth");
        if (StringUtils.isEmpty(header)){
            throw new GlobalException(Enum.HEADLER_IS_MISS);
        }
        //拆分
        String[] headers = header.split("\\.");
        if (headers.length!=2){
            throw new GlobalException(Enum.HEADLER_IS_LACK);
        }
        //用户信息
        String memberVoBase = headers[0];
        //签名
        String signBase = headers[1];
        //转为json字符串
        //String memberVo = new String(Base64.getDecoder().decode(memberVoBase));
        String memberVo = StringUtils.toEncodedString(Base64.getDecoder().decode(memberVoBase),null);
        String newSign = Md5Util.baseInfo(memberVo, SystemConst.MEMBER_SECRET);
        String newSignBase = Base64.getEncoder().encodeToString(newSign.getBytes());
        if (!signBase.equals(newSignBase)){
            throw new GlobalException(Enum.HEADLER_IS_DG);
        }
        //验证是否过期
        MemberVo memberVos = JSONObject.parseObject(memberVo, MemberVo.class);
        String memberName = memberVos.getMemberName();
        String uuid = memberVos.getUuid();
        //判断是否过期
        boolean exists = RedisUtil.exists(memberName + uuid);
        if (!exists){
            throw new GlobalException(Enum.HEADLER_IS_TIME);
        }
        //续命
        RedisUtil.setExpire(memberName+uuid,60*10);

        //用户信息放入request
        request.setAttribute("member",memberVos);
        return true;
    }
}
