package com.mi.hundsun.oxchains.base.core.service.fn.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.fn.PlatUserAddressMapper;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.base.core.service.fn.PlatUserAddressService;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台分配给客户地址业务相关Service接口实现<br>
 *
 * @ClassName: PlatUserAddressServiceImpl
 * @author bin
 * @date   2018-04-15 07:49:10
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class PlatUserAddressServiceImpl implements PlatUserAddressService {

	@Resource
    private PlatUserAddressMapper platUserAddressMapper;

    @Override
    public List<PlatUserAddress> findNoDistributedAddress() {
        List<PlatUserAddress> list = new ArrayList<>();
        PlatUserAddress ethAddress = platUserAddressMapper.findOneNoDistributedEthAddress();
        if(null != ethAddress) {
            list.add(ethAddress);
        }
        PlatUserAddress btcAddress = platUserAddressMapper.findOneNoDistributedBtcAddress();
        if(null != btcAddress) {
            list.add(btcAddress);
        }
        return list;
    }

    @Override
    public GenericMapper<PlatUserAddress,Integer> _getMapper() {
        return platUserAddressMapper;
    }

}
