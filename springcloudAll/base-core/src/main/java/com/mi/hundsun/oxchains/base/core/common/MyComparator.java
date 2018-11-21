/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import com.xiaoleilu.hutool.util.StrUtil;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * @author 枫亭
 * @description 品种列表排序器
 * @date 2018-04-15 14:33.
 */
public class MyComparator implements Comparator<CommodityModel> {

    private String sortType;
    private String sortColumn;

    public MyComparator(String sortType, String sortColumn) {
        this.sortType = sortType;
        this.sortColumn = sortColumn;
    }

    @Override
    public int compare(CommodityModel o1, CommodityModel o2) {
        if(sortColumn.equals("lastPrice")) {
            BigDecimal lastPrice1 = new BigDecimal(StrUtil.isBlank(o1.getLastPrice()) ? "0" : o1.getLastPrice());
            BigDecimal lastPrice2 = new BigDecimal(StrUtil.isBlank(o2.getLastPrice()) ? "0" : o2.getLastPrice());
            if (sortType.equals("ASC")) {
                return lastPrice1.compareTo(lastPrice2);
            }
            return lastPrice2.compareTo(lastPrice1);
        } else {
            BigDecimal priceIncrease1 = new BigDecimal(StrUtil.isBlank(o1.getPriceIncrease()) ? "0" : o1.getPriceIncrease().replace("%",""));
            BigDecimal priceIncrease2 = new BigDecimal(StrUtil.isBlank(o2.getPriceIncrease()) ? "0" : o2.getPriceIncrease().replace("%",""));
            if (sortType.equals("ASC")) {
                return priceIncrease1.compareTo(priceIncrease2);
            }
            return priceIncrease2.compareTo(priceIncrease1);
        }

    }
}
