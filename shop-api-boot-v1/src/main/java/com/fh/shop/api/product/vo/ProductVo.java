package com.fh.shop.api.product.vo;

import java.io.Serializable;

public class ProductVo implements Serializable {

    //主键
    private long id;
    //商品名
    private String productName;
    //商品价格
    private String productPrice;
    //商品图片
    private String productPhoto;
    //生产日期
    private String createTime;
    //库存量
    private int stock;
    //是否热销
    private int isFiery;
    //上下架
    private int isPutaway;
    //品牌名
    private String brandName;
    //品牌id
    private int brandId;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

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

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
