package com.fh.shop.api.pay.controller;

import com.fh.shop.api.check.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.pay.biz.IPayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.rmi.server.ServerCloneException;

@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource(name = "payService")
    private IPayService payService;

    /**
     * 请求验证码
     * @param memberVo
     * @return
     */
    @Check
    @PostMapping
    public ServerResponse getPayUrl(MemberVo memberVo){
        long memberId = memberVo.getId();
        return payService.getPayUrl(memberId);
    }

    /**
     * 验证码回调
     */
    @Check
    @GetMapping
    public ServerResponse getStatus(MemberVo memberVo){
        long memberId = memberVo.getId();
        return payService.getStatus(memberId);
    }


}
