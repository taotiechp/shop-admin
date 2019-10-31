package com.fh.shop.api.site.controller;

import com.fh.shop.api.check.Check;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.member.vo.MemberVo;
import com.fh.shop.api.site.biz.ISiteService;
import com.fh.shop.api.site.po.Site;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/site")
public class SiteController {
    @Resource(name = "siteService")
    private ISiteService siteService;

    /**
     * 查询地址
     */
    @PostMapping("/findOrderSite")
    @Check
    public ServerResponse findOrderSite(MemberVo memberVo){
        long memberId = memberVo.getId();
        return siteService.findOrderSite(memberId);
    }

    /**
     * 增加地址
     */
    @PostMapping("/addSite")
    @Check
    public ServerResponse addSite(Site site, MemberVo memberVo){
        long memberId = memberVo.getId();
        return siteService.addSite(site,memberId);
    }

}
