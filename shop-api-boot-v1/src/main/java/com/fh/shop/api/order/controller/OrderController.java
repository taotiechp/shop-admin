package com.fh.shop.api.order.controller;

import com.fh.shop.api.check.Check;
import com.fh.shop.api.check.Idempotent;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.order.biz.IOrderService;
import com.fh.shop.api.order.param.OrderParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.rmi.server.ServerCloneException;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Resource(name = "orderService")
    private IOrderService orderService;

    @PostMapping("/saveOrder")
    @Check
    @Idempotent
    public ServerResponse saveOrder(OrderParam orderParam, MemberVo memberVo){
        long memberId = memberVo.getId();
        return orderService.saveOrder(orderParam,memberId);
    }


}
