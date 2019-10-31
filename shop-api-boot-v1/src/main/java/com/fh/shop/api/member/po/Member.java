package com.fh.shop.api.member.po;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Data
 public class Member implements Serializable {

    //主键id
    private long id;
    //会员名
    private String memberName;
    //真实姓名
    private String realName;
    //密码
    private String passWord;
    //电话
    private String phone;
    //邮箱
    private String email;
    //生日
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    //盐
    private String salt;
    //对应地区
    private Integer area1;
    private Integer area2;
    private Integer area3;
    //接受验证码
    @TableField(exist = false)
    private String code;

}
