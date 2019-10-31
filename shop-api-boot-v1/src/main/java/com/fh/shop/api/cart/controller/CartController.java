package com.fh.shop.api.cart.controller;

import com.fh.shop.api.cart.biz.ICartService;
import com.fh.shop.api.check.Check;
import com.fh.shop.api.check.Idempotent;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Resource(name = "cartService")
    private ICartService cartService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 购物车增加
     * @param productId
     * @param count
     * @return
     */
    @PostMapping
    @Check
    public ServerResponse saveCart(long productId,long count,MemberVo member){
        //获取用户id
        //MemberVo member = (MemberVo) request.getAttribute("member");
        long memberId = member.getId();
        return cartService.savaCart(productId,count,memberId);
    }

    /**
     * 购物车列表
     */
    @PostMapping("/toList")
    @Check
    public ServerResponse toList(MemberVo member){
        //获取用户id
        //MemberVo member = (MemberVo) request.getAttribute("member");
        long memberId = member.getId();
        return cartService.findCart(memberId);
    }

    /**
     * 购物车删除
     */
    @PostMapping("/delCartProduct")
    @Check
    public ServerResponse delCartProduct(long productId,MemberVo member){
        //MemberVo member = (MemberVo) request.getAttribute("member");
        long memberId = member.getId();
        return cartService.delCartProduct(productId,memberId);
    }

    /**
     * 结算
     */
    @GetMapping("/kont")
    public ServerResponse kont(){
        return cartService.kontCart();
    }


}
