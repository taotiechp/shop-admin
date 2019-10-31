package com.fh.shop.api.paylog.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_PayLog")
public class PayLog implements Serializable {
    //支付订单号
    @TableId(value = "out_trade_no",type = IdType.INPUT)
    private String outTradeNo;
    //用户Id
    private long memberId;
    //订单id
    private String orderId;
    //交易流水号
    private String transaction;
    //创建时间
    private Date createTime;
    //支付时间
    private Date payTime;
    //交付金额
    private BigDecimal payMoney;
    //支付平台
    private int payType;
    //支付状态
    private int payStatus;
    //二维码
    @TableField(exist = false)
    private String url;

}

