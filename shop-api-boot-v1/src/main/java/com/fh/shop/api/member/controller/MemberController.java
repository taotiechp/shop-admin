package com.fh.shop.api.member.controller;

import com.fh.shop.api.check.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.biz.IMemberService;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/members")
@CrossOrigin("*")
public class MemberController {
    @Resource(name = "memberService")
    private IMemberService memberServer;

    @Autowired
    private HttpServletRequest request;

    /**
     * 会员注册
     * @param member
     * @return
     */
    @PostMapping
    public ServerResponse addMember(Member member){
        return memberServer.addMember(member);
    }

    /**
     * 会员登录
     */
    @PostMapping("/login")
    public ServerResponse login(Member member){
        return memberServer.login(member);
    }

    /**
     * 发送验证码
     */
    @PostMapping("/sendCode")
    public ServerResponse sendCode(String phone){
        return memberServer.sendCode(phone);
    }

    /**
     * 查询用户是否存在
     */
    @GetMapping("/queryMemberName")
    public ServerResponse queryMemberName(String memberName){
        return memberServer.queryMemberName(memberName);
    }

    /**
     * 查询手机号是否存在
     */
    @GetMapping("/queryPhone")
    public ServerResponse queryPhone(String phone){
        return memberServer.queryPhone(phone);
    }

    /**
     * 查询邮箱是否存在
     */
    @GetMapping("/queryMemberEmail")
    public ServerResponse queryMemberEmail(String email){
        return memberServer.queryMemberEmail(email);
    }

    /**
     * 导航条显示用户信息
     */
    @GetMapping("/loginInfo")
    @Check
    public ServerResponse loginInfo(MemberVo member){
        //取出request中用户信息
        //MemberVo member = (MemberVo) request.getAttribute("member");
        String memberName = member.getMemberName();
        String realName = member.getRealName();
        return ServerResponse.success(realName);
    }

    /**
     * 退出
     */
    @PostMapping("/loginOut")
    @Check
    public ServerResponse loginOut(MemberVo member){
        //取出request中用户数据
        //MemberVo member = (MemberVo) request.getAttribute("member");
        String memberName = member.getMemberName();
        String uuid = member.getUuid();
        RedisUtil.del(memberName+uuid);
        return ServerResponse.success();
    }

    /**
     * 验证码登录
     */
    @PostMapping("/loginCode")
    public ServerResponse loginCode(Member member){
        return memberServer.loginCode(member);
    }

}
