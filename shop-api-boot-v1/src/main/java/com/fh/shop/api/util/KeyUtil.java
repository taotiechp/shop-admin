package com.fh.shop.api.util;

public class KeyUtil {

    public static String buildCodeKey(String data){
        return "code:"+data;
    }

    public static String buildUserKey(String data){
        return "user:"+data;
    }

    public static String buildResourceAllKey(String data){
        return "ResourceAll:"+data;
    }

    public static String buildResourceListKey(String data){
        return "ResourceList:"+data;
    }

    public static String buildUserResourceListKey(String data){
        return "UserResourceList:"+data;
    }

    public static String buildSMSKey(String data){
        return "SMS:"+data;
    }

    public static String buildMemberKey(String data){
        return "Member:"+data;
    }

}
