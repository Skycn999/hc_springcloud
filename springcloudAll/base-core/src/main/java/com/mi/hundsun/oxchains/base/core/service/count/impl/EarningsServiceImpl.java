package com.mi.hundsun.oxchains.base.core.service.count.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.count.CountEarningsMapper;
import com.mi.hundsun.oxchains.base.core.po.count.CountEarnings;
import com.mi.hundsun.oxchains.base.core.service.count.CountEarningsService;

/**
 * 收益汇总业务相关Service接口实现<br>
 *
 * @ClassName: EarningsServiceImpl
 * @author bin
 * @date   2018-04-24 10:49:34
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class EarningsServiceImpl implements CountEarningsService {

	@Resource
    private CountEarningsMapper countEarningsMapper;

    @Override
    public GenericMapper<CountEarnings,Integer> _getMapper() {
        return countEarningsMapper;
    }

}
