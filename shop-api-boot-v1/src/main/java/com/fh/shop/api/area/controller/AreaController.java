package com.fh.shop.api.area.controller;

import com.fh.shop.api.area.biz.IAreaService;
import com.fh.shop.api.area.po.Area;
import com.fh.shop.api.area.vo.AreaVo;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/areas")
@CrossOrigin("*")
public class AreaController {
    @Resource(name = "areaService")
    private IAreaService areaService;

    @GetMapping(value = "/{id}")
    public ServerResponse findListArea(@PathVariable Integer id){
        List<Area> areaVoList = areaService.findListArea(id);
        return ServerResponse.success(areaVoList);
    }

}
