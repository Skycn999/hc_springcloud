package com.mi.hundsun.oxchains.base.core.service.cms;

import com.mi.hundsun.oxchains.base.core.po.cms.Banner;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;

/**
 * banner管理业务相关Service接口<br>
 *
 * @ClassName: BannerService
 * @author bin
 * @date   2018-04-23 09:43:28
 */
public interface BannerService extends GenericService<Integer, Banner> {

    /**
     * 获取首页bannerList,
     * @param size 数量
     * @return
     */
    List<Banner> getIndexList(Integer size);
}
