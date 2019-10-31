package com.fh.shop.api.brand.controller;

import com.fh.shop.api.brand.biz.IBrandService;
import com.fh.shop.api.brand.vo.BrandVo;
import com.fh.shop.api.check.Check;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/brand")
public class BrandController {
    @Resource(name = "brandService")
    private IBrandService brandService;

    @RequestMapping("/toList")
    @Check
    public ServerResponse toList(){
        List<BrandVo> brandVoList = brandService.toList();
        return ServerResponse.success(brandVoList);
    }

}
