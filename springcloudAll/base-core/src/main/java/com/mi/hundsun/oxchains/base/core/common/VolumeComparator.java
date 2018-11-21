/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.base.core.common;

import java.util.Comparator;

/**
 * @author 枫亭
 * @description 交易量排序器
 * @date 2018-04-15 14:33.
 */
public class VolumeComparator implements Comparator<VoDetail> {


    @Override
    public int compare(VoDetail o1, VoDetail o2) {
        return o2.getVolume().compareTo(o1.getVolume());
    }
}
