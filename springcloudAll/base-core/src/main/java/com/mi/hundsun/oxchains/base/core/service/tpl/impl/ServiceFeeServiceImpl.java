package com.mi.hundsun.oxchains.base.core.service.tpl.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.tpl.ServiceFeeMapper;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.service.tpl.ServiceFeeService;

import java.util.List;

/**
 * 手续费模板业务相关Service接口实现<br>
 *
 * @ClassName: ServiceFeeServiceImpl
 * @author bin
 * @date   2018-04-12 11:00:23
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ServiceFeeServiceImpl implements ServiceFeeService {

	@Resource
    private ServiceFeeMapper serviceFeeMapper;

    @Override
    public GenericMapper<ServiceFee,Integer> _getMapper() {
        return serviceFeeMapper;
    }

    @Override
    public void insertFee(ServiceFee serviceFee) throws Exception {
        if (serviceFee.getState().intValue()==ServiceFee.STATE.ENABLE.code && serviceFee.getIsDefault().intValue()==ServiceFee.ISDEFAULT.YES.code){
            serviceFeeMapper.updateBySql(ServiceFee.ISDEFAULT.NO.code,ServiceFee.ISDEFAULT.YES.code,serviceFee.getCoinCurrency(),GenericPo.DELFLAG.NO.code);
        }
        serviceFeeMapper.insert(serviceFee);
    }

    @Override
    public void updateServiceFeee(ServiceFee serviceFee) throws Exception {
        if (serviceFee.getState().intValue()==ServiceFee.STATE.ENABLE.code && serviceFee.getIsDefault().intValue()==ServiceFee.ISDEFAULT.YES.code){
            serviceFeeMapper.updateBySqlEdit(ServiceFee.ISDEFAULT.NO.code,ServiceFee.ISDEFAULT.YES.code,serviceFee.getCoinCurrency(),GenericPo.DELFLAG.NO.code,serviceFee.getId());
        }
        serviceFeeMapper.updateByPrimaryKeySelective(serviceFee);

    }

    @Override
    public ServiceFee findServiceFeeIsDefault(String coinCode) {
        ServiceFee fee  = new ServiceFee();
        fee.setState(ServiceFee.STATE.ENABLE.code);
        fee.setIsDefault(ServiceFee.ISDEFAULT.YES.code);
        fee.setCoinCurrency(coinCode);
        fee.setDelFlag(GenericPo.DELFLAG.NO.code);
        List<ServiceFee> serviceFees = serviceFeeMapper.select(fee);
        if (null != serviceFees && serviceFees.size() >0) {
            return serviceFees.get(0);
        }else {
            List<ServiceFee> sfList = serviceFeeMapper.findServiceFeeList(coinCode,ServiceFee.STATE.ENABLE.code,GenericPo.DELFLAG.NO.code,ServiceFee.ISDEFAULT.YES.code);
            if(null!= sfList && sfList.size()>0){
                return sfList.get(0);
            }
        }
        return null;
    }
}
