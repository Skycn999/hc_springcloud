package com.mi.hundsun.oxchains.base.core.service.exchange.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.exchange.MotherAccountInfoMapper;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccountInfo;
import com.mi.hundsun.oxchains.base.core.service.exchange.MotherAccountInfoService;

/**
 * 交易所母账号资产信息业务相关Service接口实现<br>
 *
 * @ClassName: MotherAccountInfoServiceImpl
 * @author bin
 * @date   2018-04-24 11:31:55
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class MotherAccountInfoServiceImpl implements MotherAccountInfoService {

	@Resource
    private MotherAccountInfoMapper motherAccountInfoMapper;

    @Override
    public GenericMapper<MotherAccountInfo,Integer> _getMapper() {
        return motherAccountInfoMapper;
    }

}
