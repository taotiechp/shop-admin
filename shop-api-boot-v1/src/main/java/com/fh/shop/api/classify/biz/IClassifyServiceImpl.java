package com.fh.shop.api.classify.biz;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fh.shop.api.classify.mapper.IClassifyMapper;
import com.fh.shop.api.classify.po.Classify;
import com.fh.shop.api.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("classifyService")
public class IClassifyServiceImpl implements IClassifyService {
    @Autowired
    private IClassifyMapper classifyMapper;


    /**
     * 数据查询
     * @return
     */
    @Override
    public List<Classify> findClassifyList() {
        String classifyJson = RedisUtil.get("classifyList");
        if (StringUtils.isNotEmpty(classifyJson)){
            List<Classify> classifyList = JSONObject.parseArray(classifyJson, Classify.class);
            return classifyList;
        }
        List<Classify> classifyList = classifyMapper.selectList(null);
        String jsonString = JSONObject.toJSONString(classifyList);
        RedisUtil.set("classifyList",jsonString);
        return classifyList;
    }
}
