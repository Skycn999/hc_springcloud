package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.tpl.ServiceFeeMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserRiskControlMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UsersMapper;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.user.UserRiskControlService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 用户风控设置业务相关Service接口实现<br>
 *
 * @author bin
 * @date 2018-04-12 07:55:12
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class UserRiskControlServiceImpl implements UserRiskControlService {

    @Resource
    private UserRiskControlMapper userRiskControlMapper;
    @Resource
    private UsersMapper usersMapper;
    @Resource
    private ServiceFeeMapper serviceFeeMapper;


    @Override
    public GenericMapper<UserRiskControl, Integer> _getMapper() {
        return userRiskControlMapper;
    }

    @Override
    public void updateSettingTpl(UserRiskControl riskControl) throws BussinessException {
        riskControl.setUpdateTime(new Date());
        userRiskControlMapper.updateByPrimaryKeySelective(riskControl);
    }

    @Override
    public boolean checkUserServiceFeeTpl(String code, String serviceFee) {
        List<ServiceFee> tplFees = serviceFeeMapper.select(new ServiceFee(f -> {
            f.setCoinCurrency(code);
            f.setDelFlag(GenericPo.DELFLAG.NO.code);
            f.setState(ServiceFee.STATE.ENABLE.code);
        }));
        ServiceFee tplFee = null;
        if (null != tplFees && tplFees.size() > 0) {
            tplFee = tplFees.get(0);
        }
        return null != tplFee && tplFee.getServiceFee().compareTo(new BigDecimal(serviceFee)) == 0;
    }

    @Override
    public ServiceFee getServiceFee(String code) {
        List<ServiceFee> sFees =  serviceFeeMapper.findServiceFeeByCoin(code, GenericPo.DELFLAG.NO.code);
        ServiceFee sFee = null;
        if(null!= sFees&& sFees.size()>0) {
            sFee = sFees.get(0);
        }       
        return sFee;
    }

    @Override
    public void insertRiskControl(UserRiskControl userRiskControl) throws Exception {
        userRiskControl.setUuid(RandomUtils.randomCustomUUID());
        userRiskControl.setCreateTime(new Date());
        userRiskControlMapper.insert(userRiskControl);
        //更新用户类型
        usersMapper.updateByPrimaryKeySelective(new Users(u->{
            u.setId(userRiskControl.getUserId());
            u.setType(Users.TYPE.AMALDAR.code);
        }));
    }
}
