package com.fh.shop.api.cart.biz;

import com.fh.shop.api.common.ServerResponse;

public interface ICartService {
    ServerResponse savaCart(long productId, long count, long memberId);

    ServerResponse findCart(long memberId);

    ServerResponse delCartProduct(long productId, long memberId);

    ServerResponse kontCart();

    /*void test();*/

}
