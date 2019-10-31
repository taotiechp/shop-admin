package com.fh.shop.api.order.param;

import lombok.Data;

@Data
public class OrderParam {

    //支付方式 1 微信 2 支付宝
    private int payType;
    //收获人
    private String consigneeName;
    //收货地址
    private String shippingSite;
    //收货人电话
    private String consigneePhone;

}
