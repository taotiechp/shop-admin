package com.fh.shop.api.area.biz;

import com.fh.shop.api.area.po.Area;
import com.fh.shop.api.area.vo.AreaVo;

import java.util.List;

public interface IAreaService {
    List<Area> findListArea(Integer id);
}
