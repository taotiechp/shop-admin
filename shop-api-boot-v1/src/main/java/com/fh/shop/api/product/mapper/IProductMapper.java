package com.fh.shop.api.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fh.shop.api.product.po.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IProductMapper extends BaseMapper<Product> {
    long updateStock(@Param("id") long id, @Param("count") long count);

    List<Product> findStock();
}
