package com.fh.shop.api.order.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.order.param.OrderParam;

public interface IOrderService {
    ServerResponse saveOrder(OrderParam orderParam, long memberId);
}
