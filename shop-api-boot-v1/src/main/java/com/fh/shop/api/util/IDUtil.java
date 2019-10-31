package com.fh.shop.api.util;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IDUtil {

    public static String getId(){
        SimpleDateFormat sim = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date();
        String format = sim.format(date);
        String id = format+IdWorker.getId();
        return id;
    }

}
