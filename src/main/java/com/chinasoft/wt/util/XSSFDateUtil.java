package com.chinasoft.wt.util;

import org.apache.poi.ss.usermodel.DateUtil;

import java.util.Calendar;

/**
 * Created by VerRan.Liu on 2017/9/22.
 */
public class XSSFDateUtil extends DateUtil {
    protected static int absoluteDay(Calendar cal, boolean use1904windowing) {
        return DateUtil.absoluteDay(cal, use1904windowing);
    }
}