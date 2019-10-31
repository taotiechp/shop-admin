package com.fh.shop.api.site.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.common.Enum;
import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.site.mapper.ISiteMapper;
import com.fh.shop.api.site.po.Site;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("siteService")
public class ISiteServiceImpl implements ISiteService {
    @Autowired
    private ISiteMapper siteMapper;

    /**
     * 查询会员地址
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse findOrderSite(long memberId) {
        //非空判断
        QueryWrapper<Site> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("memberId",memberId);
        List<Site> sites = siteMapper.selectList(queryWrapper);
        if (sites.size()<1){
            return ServerResponse.error(Enum.SITE_IS_NULL);
        }
        return ServerResponse.success(sites);
    }

    /**
     * 增加地址
     * @param memberId
     * @return
     */
    @Override
    public ServerResponse addSite(Site site,long memberId) {
        site.setMemberId(memberId);
        siteMapper.insert(site);
        return ServerResponse.success();
    }
}
