package com.mi.hundsun.oxchains.base.core.service.count.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.service.count.CountUserAccountService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.count.CountUserAccountMapper;
import com.mi.hundsun.oxchains.base.core.po.count.CountUserAccount;

/**
 * 用户持仓汇总业务相关Service接口实现<br>
 *
 * @ClassName: CountUserAccountServiceImpl
 * @author bin
 * @date   2018-04-24 10:49:34
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class CountUserAccountServiceImpl implements CountUserAccountService {

	@Resource
    private CountUserAccountMapper countUserAccountMapper;

    @Override
    public GenericMapper<CountUserAccount,Integer> _getMapper() {
        return countUserAccountMapper;
    }

}
