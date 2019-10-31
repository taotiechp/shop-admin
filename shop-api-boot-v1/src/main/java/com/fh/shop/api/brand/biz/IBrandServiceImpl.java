package com.fh.shop.api.brand.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.brand.mapper.IBrandMapper;
import com.fh.shop.api.brand.po.Brand;
import com.fh.shop.api.brand.vo.BrandVo;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("brandService")
public class IBrandServiceImpl implements IBrandService {
    @Autowired
    private IBrandMapper brandMapper;

    /**
     * 数据查询
     * @return
     */
    @Override
    public List<BrandVo> toList() {
        String brandListAPI = RedisUtil.get("brandListAPI");
        if (StringUtils.isNotEmpty(brandListAPI)){
            //json格式字符串转为java对象
            List<BrandVo> brandVoList = JSONObject.parseArray(brandListAPI, BrandVo.class);
            return brandVoList;
        }
        QueryWrapper<Brand> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort");
        queryWrapper.eq("ishot",2);
        List<Brand> brandList = brandMapper.selectList(queryWrapper);
        //po转vo
        List<BrandVo> brandVoList = getBrandVos(brandList);
        //java对象转换为json格式字符串
        String jsonString = JSONObject.toJSONString(brandVoList);
        RedisUtil.setEX("brandListAPI",jsonString,20);
        return brandVoList;
    }

    private List<BrandVo> getBrandVos(List<Brand> brandList) {
        List<BrandVo> brandVoList = new ArrayList<>();
        for (Brand brand : brandList) {
            BrandVo brandVo = new BrandVo();
            brandVo.setId(brand.getId());
            brandVo.setBrandName(brand.getBrandName());
            brandVo.setPhoto(brand.getPhoto());
            brandVo.setIshot(brand.getIshot());
            brandVoList.add(brandVo);
        }
        return brandVoList;
    }
}
