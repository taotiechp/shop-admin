package com.fh.shop.api.common;

public enum Enum {
    USERNAME_ERROR(1001,"用户名错误"),
    PASSWORD_ERROR(1002,"密码错误"),
    PASSWORD_ERROR3(1003,"密码连续错误3次,请明天登录"),
    PASSWORD_EMPTY(1004,"请输入全部密码"),
    PASSWORD_UNLIKE(1005,"新密码与确认密码不一致"),
    PASSWORD_OLDPASSWORDERROR(1006,"原密码错误"),
    PASSWORD_ERROR_EMAIL(1007,"该邮箱未注册或输入错误"),
    USER_IS_NULL(1008,"该用户不存在"),
    MEMBER_SMS_ERROR(1009,"验证码错误"),
    CODE_SMS_ERROR(1010,"验证码发送失败"),
    PHONE_IS_NULL(1011,"手机号不能为空"),
    PHONE_IS_TRUE(1012,"该手机号已注册"),
    MEMBERNAME_IS_NULL(1013,"用户不能为空"),
    EMAIL_IS_NULL(1014,"邮箱不能为空"),
    MEMBERNAME_IS_NOTNULL(1015,"用户名已存在"),
    EMAIL_IS_NOTNULL(1016,"邮箱已存在"),
    CODE_IS_NULL(1017,"验证码已过期"),
    PASSWORD_IS_NULL(1018,"密码不能为空"),
    PRODUCT_IS_NAME(1023,"该商品不存在"),
    PRODUCT_IS_NUIP(1024,"该商品存在"),
    CODE_IS_NUIP(1025,"验证码为空"),
    SITE_IS_NULL(1026,"该用户没有地址信息"),
    CART_IS_NULL(1027,"购物车为空"),
    TOKEN_IS_NULL(1028,"订单已过期"),
    PRODUCT_IS_STOCK(1029,"所选商品商品库存全部不足"),

    HEADLER_IS_MISS(1019,"头信息为空"),
    HEADLER_IS_LACK(1020,"头信息缺失"),
    HEADLER_IS_DG(1021,"头信息异常"),
    HEADLER_IS_TIME(1022,"请求超时"),

    USERNAME_PASSWOED_ERROR(1000,"用户名或密码错误");

    private int code;

    private String msg;

    private Enum(int code, String msg){
        this.code=code;
        this.msg=msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
