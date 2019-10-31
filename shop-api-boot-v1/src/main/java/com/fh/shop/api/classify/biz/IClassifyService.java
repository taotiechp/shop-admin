package com.fh.shop.api.classify.biz;

import com.fh.shop.api.classify.po.Classify;

import java.util.List;

public interface IClassifyService {
    List<Classify> findClassifyList();
}
