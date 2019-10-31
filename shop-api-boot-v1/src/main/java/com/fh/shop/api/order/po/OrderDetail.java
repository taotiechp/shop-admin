package com.fh.shop.api.order.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName(value = "t_orderdetail")
public class OrderDetail implements Serializable {
    //订单外键id
    private String orderId;
    //商品外键id
    private long productId;
    //商品名
    private String productName;
    //商品价格
    private BigDecimal productPrice;
    //商品个数
    private long productCount;
    //商品小计
    private String subTotalPrice;
    //商品图片
    private String productImg;

}
