package com.fh.shop.api.cart.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartVo implements Serializable {
    //总个数
    private long totalCount;
    //商品集合
    private List<CartItem> cartItem = new ArrayList<>();
    //总价格
    private String totalPrice;

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<CartItem> getCartItem() {
        return cartItem;
    }

    public void setCartItem(List<CartItem> cartItem) {
        this.cartItem = cartItem;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
