package com.fh.shop.api.member.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.Enum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.mapper.IMemberMapper;
import com.fh.shop.api.member.po.Member;
import com.fh.shop.api.member.smsCode.SmsCode;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service("memberService")
public class IMemberServiceImpl implements IMemberService {
    @Autowired
    private IMemberMapper memberMapper;

    /**
     * 会员注册
     * @param member
     */
    @Override
    public ServerResponse addMember(Member member) {
        String memberName = member.getMemberName();
        String phone = member.getPhone();
        String email = member.getEmail();
        //取出用户输入验证码
        String code = member.getCode();
        //非空判断
        if (StringUtils.isEmpty(memberName)){
            return ServerResponse.error(Enum.MEMBERNAME_IS_NULL);
        }
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(Enum.PHONE_IS_NULL);
        }
        if (StringUtils.isEmpty(email)){
            return ServerResponse.error(Enum.EMAIL_IS_NULL);
        }

        //取出redis中验证码
        String smsCode = RedisUtil.get(KeyUtil.buildSMSKey(member.getPhone()));
        //判断redis验证码是否过期
        if (StringUtils.isEmpty(smsCode)){
            return ServerResponse.error(Enum.CODE_IS_NULL);
        }
        //判断用户输入验证码与redis验证码是否一致
        if (!smsCode.equals(code)){
            return ServerResponse.error(Enum.MEMBER_SMS_ERROR);
        }

        //唯一性判断
        //判断用户名
        QueryWrapper<Member> queryWrapperName = new QueryWrapper<>();
        queryWrapperName.eq("memberName",memberName);
        Member memberDBName = memberMapper.selectOne(queryWrapperName);
        if (memberDBName!=null){
            return ServerResponse.error(Enum.MEMBERNAME_IS_NOTNULL);
        }
        //判断手机号是否注册，注册过则不能重复注册
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Member memberDB = memberMapper.selectOne(queryWrapper);
        if (memberDB!=null){
            return ServerResponse.error(Enum.PHONE_IS_TRUE);
        }
        //判断邮箱是否注册
        QueryWrapper<Member> queryWrapperEmail = new QueryWrapper<>();
        queryWrapperEmail.eq("email",email);
        Member memberDBEmail = memberMapper.selectOne(queryWrapperEmail);
        if (memberDBEmail != null){
            return ServerResponse.error(Enum.EMAIL_IS_NOTNULL);
        }

        //盐
        String salt = UUID.randomUUID().toString();
        //取出密码
        String passWord = member.getPassWord();
        member.setSalt(salt);
        //双重MD5加盐
        member.setPassWord(Md5Util.encodePassWord(salt,passWord));
        memberMapper.insert(member);
        return ServerResponse.success();
    }

    /**
     * 发送验证码
     */
    @Override
    public ServerResponse sendCode(String phone) {
        //手机号非空判断
        if (phone == null){
            return ServerResponse.error(Enum.PHONE_IS_NULL);
        }
        //java正则表达式验证手机号

        //取出短信验证码
        String smsCode = null;
        try {
            smsCode = SMSUtil.getSMSCode(phone);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        //将验证码转为JavaBean
        SmsCode smsCodes = JSONObject.parseObject(smsCode, SmsCode.class);
        //判断是否发送成功
        if (!(smsCodes.getCode()==200)){
            return ServerResponse.error(Enum.CODE_SMS_ERROR);
        }
        String obj = smsCodes.getObj();
        // 将生成的验证码的值，放到redis中
        RedisUtil.setEX(KeyUtil.buildSMSKey(phone),obj,60);
        return ServerResponse.success();
    }

    /**
     * 查询用户名是否存在
     * @param memberName
     * @return
     */
    @Override
    public ServerResponse queryMemberName(String memberName) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberName",memberName);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member!=null){
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }

    /**
     * 查询手机号是否存在
     * @param phone
     * @return
     */
    @Override
    public ServerResponse queryPhone(String phone) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member!=null){
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }

    /**
     * 查询邮箱是否存在
     * @param email
     * @return
     */
    @Override
    public ServerResponse queryMemberEmail(String email) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        Member member = memberMapper.selectOne(queryWrapper);
        if (member!=null){
            return ServerResponse.success();
        }
        return ServerResponse.error();
    }

    /**
     * 会员登录
     * @param member
     * @return
     */
    @Override
    public ServerResponse login(Member member) {
        String memberName = member.getMemberName();
        String passWord = member.getPassWord();
        //非空判断
        if (StringUtils.isEmpty(memberName)){
            return ServerResponse.error(Enum.MEMBERNAME_IS_NULL);
        }
        if (StringUtils.isEmpty(passWord)){
            return ServerResponse.error(Enum.PASSWORD_IS_NULL);
        }
        //判断用户密码
        QueryWrapper<Member> queryWrapperName = new QueryWrapper<>();
        queryWrapperName.eq("memberName",memberName);
        Member memberDB = memberMapper.selectOne(queryWrapperName);
        if (memberDB==null){
            return ServerResponse.error(Enum.USERNAME_ERROR);
        }
        //密码验证
        String salt = memberDB.getSalt();
        String passWordQD = Md5Util.encodePassWord(salt, passWord);
        if (!passWordQD.equals(memberDB.getPassWord())){
            return ServerResponse.error(Enum.PASSWORD_ERROR);
        }
        //转码
        String uuid = UUID.randomUUID().toString();
        MemberVo memberVo = new MemberVo();
        memberVo.setId(memberDB.getId());
        memberVo.setMemberName(memberName);
        memberVo.setRealName(memberDB.getRealName());
        memberVo.setUuid(uuid);
        //转换为json格式字符串
        String memberVoJson = JSONObject.toJSONString(memberVo);
        String memberBase64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes());
        //签名
        String sign = Md5Util.baseInfo(memberVoJson, SystemConst.MEMBER_SECRET);
        //转为Base64
        String signBase = Base64.getEncoder().encodeToString(sign.getBytes());
        //设置过期时间
        RedisUtil.setEX(memberName+uuid,"1",60*10);
        System.out.println(memberBase64+"."+signBase);
        //System.out.println(org.apache.commons.lang3.StringUtils.toEncodedString(Base64.getDecoder().decode(memberBase64),null));
        return ServerResponse.success(memberBase64+"."+signBase);
    }

    /**
     * 验证码登录
     * @param member
     * @return
     */
    @Override
    public ServerResponse loginCode(Member member) {
        String phone = member.getPhone();
        String code = member.getCode();
        //非空判断
        if (StringUtils.isEmpty(phone)){
            return ServerResponse.error(Enum.PHONE_IS_NULL);
        }
        if (StringUtils.isEmpty(code)){
            return ServerResponse.error(Enum.CODE_IS_NUIP);
        }
        //判断手机用户
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);
        Member memberInfo = memberMapper.selectOne(queryWrapper);
        if (memberInfo==null){
            return ServerResponse.error(Enum.USER_IS_NULL);
        }
        //判断验证码
        //取出redis中验证码
        String smsCode = RedisUtil.get(KeyUtil.buildSMSKey(member.getPhone()));
        //判断redis验证码是否过期
        if (StringUtils.isEmpty(smsCode)){
            return ServerResponse.error(Enum.CODE_IS_NULL);
        }
        //判断用户输入验证码与redis验证码是否一致
        if (!smsCode.equals(code)){
            return ServerResponse.error(Enum.MEMBER_SMS_ERROR);
        }
        //验证全部正确
        //转码
        String uuid = UUID.randomUUID().toString();
        MemberVo memberVo = new MemberVo();
        memberVo.setId(memberInfo.getId());
        memberVo.setMemberName(memberInfo.getMemberName());
        memberVo.setRealName(memberInfo.getRealName());
        memberVo.setUuid(uuid);
        //转换为json格式字符串
        String memberVoJson = JSONObject.toJSONString(memberVo);
        String memberBase64 = Base64.getEncoder().encodeToString(memberVoJson.getBytes());
        //签名
        String sign = Md5Util.baseInfo(memberVoJson, SystemConst.MEMBER_SECRET);
        //转为Base64
        String signBase = Base64.getEncoder().encodeToString(sign.getBytes());
        //设置过期时间
        RedisUtil.setEX(memberInfo.getMemberName()+uuid,"1",60*10);
        System.out.println(memberBase64+"."+signBase);
        //System.out.println(org.apache.commons.lang3.StringUtils.toEncodedString(Base64.getDecoder().decode(memberBase64),null));
        return ServerResponse.success(memberBase64+"."+signBase);
    }
}
