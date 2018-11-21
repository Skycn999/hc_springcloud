package com.mi.hundsun.oxchains.base.util;

import com.xiaoleilu.hutool.util.StrUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 工具类
 *
 * @author xx
 * @version 1.0
 * @since 2014年1月28日
 */
public class GenerateUtils {

    /**
     * 获取当前时间字符串
     *
     * @return
     */
    public static String getCurrTimeStr() {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
    }


    /**
     * 判断字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        return !StrUtil.isEmpty(str == null ? "" : str.toString());
    }

    /**
     * 判断字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(Object str) {
        return StrUtil.isEmpty(str == null ? "" : str.toString());
    }


}
