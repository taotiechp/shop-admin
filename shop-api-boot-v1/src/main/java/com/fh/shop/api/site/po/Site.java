package com.fh.shop.api.site.po;

import lombok.Data;

import java.io.Serializable;
@Data
public class Site implements Serializable {
    //主键id
    private long id;
    //会员id；
    private long memberId;
    //收货人
    private String consigneeName;
    //详细地址
    private String minuteSite;
    //联系电话
    private String phone;
    //邮箱
    private String email;
    //地址别名
    private String siteAlias;
    //建议填写常用地址
    private String commonSite;

}
