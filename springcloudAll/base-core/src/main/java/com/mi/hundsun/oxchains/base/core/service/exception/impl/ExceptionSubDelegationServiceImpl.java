package com.mi.hundsun.oxchains.base.core.service.exception.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.exception.ExceptionSubDelegationMapper;
import com.mi.hundsun.oxchains.base.core.po.exception.ExceptionSubDelegation;
import com.mi.hundsun.oxchains.base.core.service.exception.ExceptionSubDelegationService;

/**
 * 异常子委托信息业务相关Service接口实现<br>
 *
 * @ClassName: ExceptionSubDelegationServiceImpl
 * @author bin
 * @date   2018-04-24 02:46:39
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ExceptionSubDelegationServiceImpl implements ExceptionSubDelegationService {

	@Resource
    private ExceptionSubDelegationMapper exceptionSubDelegationMapper;

    @Override
    public GenericMapper<ExceptionSubDelegation,Integer> _getMapper() {
        return exceptionSubDelegationMapper;
    }

}
