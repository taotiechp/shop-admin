package com.fh.shop.api.site.biz;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.site.po.Site;

public interface ISiteService {
    ServerResponse findOrderSite(long memberId);

    ServerResponse addSite(Site site, long memberId);
}
