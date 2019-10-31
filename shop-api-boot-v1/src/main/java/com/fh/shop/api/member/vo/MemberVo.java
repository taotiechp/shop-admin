package com.fh.shop.api.member.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Data
 public class MemberVo implements Serializable {

    private long id;

    private String realName;

    private String memberName;

    private String uuid;

}
