package com.mi.hundsun.oxchains.base.core.service.count.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.count.CountDelegationMapper;
import com.mi.hundsun.oxchains.base.core.po.count.CountDelegation;
import com.mi.hundsun.oxchains.base.core.service.count.CountDelegationService;

/**
 * 委托统计业务相关Service接口实现<br>
 *
 * @ClassName: CountDelegationServiceImpl
 * @author bin
 * @date   2018-04-24 10:49:34
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class CountDelegationServiceImpl implements CountDelegationService {

	@Resource
    private CountDelegationMapper countDelegationMapper;

    @Override
    public GenericMapper<CountDelegation,Integer> _getMapper() {
        return countDelegationMapper;
    }

}
