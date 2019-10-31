package com.fh.shop.api.product.controller;

import com.fh.shop.api.common.ServerResponse;
import com.fh.shop.api.product.biz.IProductService;
import com.fh.shop.api.product.vo.ProductVo;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource(name = "productService")
    private IProductService productService;

    /**
     * 查询
     */
    @RequestMapping("/toList")
    public Object toList(String callback){
        List<ProductVo> productVoList = productService.findProductList();
        Object list = ServerResponse.success(productVoList);
        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(list);
        mappingJacksonValue.setJsonpFunction(callback);
        return mappingJacksonValue;
    }

}
