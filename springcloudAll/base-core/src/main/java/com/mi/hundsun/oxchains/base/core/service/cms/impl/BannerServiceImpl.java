package com.mi.hundsun.oxchains.base.core.service.cms.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.cms.BannerMapper;
import com.mi.hundsun.oxchains.base.core.po.cms.Banner;
import com.mi.hundsun.oxchains.base.core.service.cms.BannerService;

import java.util.List;

/**
 * banner管理业务相关Service接口实现<br>
 *
 * @ClassName: BannerServiceImpl
 * @author bin
 * @date   2018-04-23 09:43:28
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class BannerServiceImpl implements BannerService {

	@Resource
    private BannerMapper bannerMapper;

    @Override
    public GenericMapper<Banner,Integer> _getMapper() {
        return bannerMapper;
    }

    @Override
    public List<Banner> getIndexList(Integer size) {
        return bannerMapper.selectIndexList(Banner.STATE.PUBLISHED.code,size);
    }
}
