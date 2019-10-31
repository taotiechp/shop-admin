package com.fh.shop.api.product.po;

import com.baomidou.mybatisplus.annotation.TableField;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Product implements Serializable {

    //主键
    private long id;
    //商品名
    private String productName;
    //商品价格
    private BigDecimal productPrice;
    //商品图片
    private String productPhoto;
    //生产日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    //库存量
    private int stock;
    //是否热销
    private int isFiery;
    //上下架
    private int isPutaway;
    //对应品牌外键
    private int brandId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIsFiery() {
        return isFiery;
    }

    public void setIsFiery(int isFiery) {
        this.isFiery = isFiery;
    }

    public int getIsPutaway() {
        return isPutaway;
    }

    public void setIsPutaway(int isPutaway) {
        this.isPutaway = isPutaway;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
