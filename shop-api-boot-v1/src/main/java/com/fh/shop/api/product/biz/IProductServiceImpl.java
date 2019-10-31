package com.fh.shop.api.product.biz;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.product.mapper.IProductMapper;
import com.fh.shop.api.product.po.Product;
import com.fh.shop.api.product.vo.ProductVo;
import com.fh.shop.api.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class IProductServiceImpl implements IProductService {
    @Autowired
    private IProductMapper productMapper;

    /**
     * 查询
     * @return
     */
    @Override
    public List<ProductVo> findProductList() {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.eq("isPutaway","1");
        List<Product> productList = productMapper.selectList(queryWrapper);
        List<ProductVo> productVoList = new ArrayList<>();
        for (Product product : productList) {
            ProductVo productVo = new ProductVo();
            productVo.setId(product.getId());
            productVo.setProductName(product.getProductName());
            productVo.setProductPrice(String.valueOf(product.getProductPrice()));
            productVo.setProductPhoto(product.getProductPhoto());
            productVo.setStock(product.getStock());
            productVo.setIsFiery(product.getIsFiery());
            productVo.setIsPutaway(product.getIsPutaway());
            productVo.setCreateTime(DateUtil.DateToStr(product.getCreateTime(),DateUtil.Y_M_D));
            productVoList.add(productVo);
        }
        return productVoList;
    }



}
