package com.fh.shop.api.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static final String Y_M_D = "yyyy-MM-dd";

    public static final String FULL_YEAR = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String DateToStr(Date date,String pattern){
        if (date == null){
            return "";
        }
        SimpleDateFormat sim = new SimpleDateFormat(pattern);
        String dateStr = sim.format(date);
        return dateStr;
    }

}
