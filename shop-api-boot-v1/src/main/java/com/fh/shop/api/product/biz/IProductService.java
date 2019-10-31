package com.fh.shop.api.product.biz;

import com.fh.shop.api.product.vo.ProductVo;

import java.util.List;

public interface IProductService {
    List<ProductVo> findProductList();

}
