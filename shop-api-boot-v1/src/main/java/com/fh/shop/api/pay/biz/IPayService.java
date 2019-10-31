package com.fh.shop.api.pay.biz;

import com.fh.shop.api.common.ServerResponse;

import java.rmi.server.ServerCloneException;

public interface IPayService {
    ServerResponse getPayUrl(long memberId);

    ServerResponse getStatus(long memberId);
}
