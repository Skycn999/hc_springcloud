package com.mi.hundsun.oxchains.base.core.service.tpl;

import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 手续费模板业务相关Service接口<br>
 *
 * @ClassName: ServiceFeeService
 * @author bin
 * @date   2018-04-12 11:00:23
 */
public interface ServiceFeeService extends GenericService<Integer, ServiceFee> {

    void insertFee(ServiceFee serviceFee)throws Exception;

    void updateServiceFeee(ServiceFee serviceFee)throws Exception;

    /**
     *   获取启用中默认的手续费模板，若无默认，则获取最新启用的模板
     */

    ServiceFee findServiceFeeIsDefault(String coinCode);
}
