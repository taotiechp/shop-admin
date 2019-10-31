package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Order implements Serializable {
    //订单Id
    @TableId(value = "orderId",type = IdType.INPUT)
    private String orderId;
    //用户Id
    private long memberId;
    //支付方式 1 微信 2 支付宝
    private int payType;
    //订单总金额
    private String totalPrice;
    //订单中商品总数
    private long totalCount;
    //订单创建时间
    private Date createDate;
    //订单支付时间
    private Date payDate;
    //订单发货时间
    private Date shipmentsDate;
    //订单收获时间
    private Date consigneeDate;
    //交易成功时间
    private Date successDate;
    //完成评价时间
    private Date evaluateDate;
    //订单状态
    private int status;
    //收获人
    private String consigneeName;
    //收货地址
    private String shippingSite;
    //收货人电话
    private String consigneePhone;

}
