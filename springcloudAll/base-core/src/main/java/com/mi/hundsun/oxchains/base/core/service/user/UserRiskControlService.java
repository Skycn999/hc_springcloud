package com.mi.hundsun.oxchains.base.core.service.user;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import org.springframework.stereotype.Service;

/**
 * 用户风控设置业务相关Service接口<br>
 *
 * @ClassName: UserRiskControlService
 * @author bin
 * @date   2018-04-12 07:55:12
 */
public interface UserRiskControlService extends GenericService<Integer, UserRiskControl> {

    void  updateSettingTpl(UserRiskControl userRiskControl)throws BussinessException;

    boolean checkUserServiceFeeTpl(String code,String serviceFee)throws Exception;

    ServiceFee getServiceFee(String code);

    void insertRiskControl(UserRiskControl userRiskControl) throws Exception;
}
