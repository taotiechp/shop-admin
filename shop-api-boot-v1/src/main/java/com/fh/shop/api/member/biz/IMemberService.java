package com.fh.shop.api.member.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.po.Member;

public interface IMemberService {
    ServerResponse addMember(Member member);

    ServerResponse sendCode(String phone);

    ServerResponse queryMemberName(String memberName);

    ServerResponse queryPhone(String phone);

    ServerResponse queryMemberEmail(String email);

    ServerResponse login(Member member);

    ServerResponse loginCode(Member member);
}
