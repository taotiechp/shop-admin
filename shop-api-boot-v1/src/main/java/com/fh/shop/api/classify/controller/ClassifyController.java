package com.fh.shop.api.classify.controller;

import com.fh.shop.api.classify.biz.IClassifyService;
import com.fh.shop.api.classify.po.Classify;
import com.fh.shop.api.common.ServerResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/classify")
public class ClassifyController {
    @Resource(name = "classifyService")
    private IClassifyService classifyService;

    @RequestMapping("/findClassifyList")
    @CrossOrigin(value = "*")
    public ServerResponse findClassifyList(){
        List<Classify> classifyList = classifyService.findClassifyList();
        return ServerResponse.success(classifyList);
    }

}
