package com.fh.shop.api.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    /**
     * 写cookie
     * @param name
     * @param value
     * @param domain
     * @param response
     */
    public static void writerCookie(String name, String value, String domain, HttpServletResponse response){
        Cookie cookie = new Cookie(name,value);
        cookie.setDomain(domain);//域名
        cookie.setPath("/");//代表网站根目录
        //写cookie
        response.addCookie(cookie);
    }

    /**
     * 读cookie
     */
    public static String readCookie(String name, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies==null){
            return "";
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)){
                return cookie.getValue();
            }
        }
        return "";
    }

}
