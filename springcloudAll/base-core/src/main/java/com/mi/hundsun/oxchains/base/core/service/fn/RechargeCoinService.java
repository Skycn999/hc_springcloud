package com.mi.hundsun.oxchains.base.core.service.fn;

import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;

/**
 * 充币管理业务相关Service接口<br>
 *
 * @author bin
 * @ClassName: RechargeCoinService
 * @date 2018-04-15 03:52:26
 */
public interface RechargeCoinService extends GenericService<Integer, RechargeCoin> {

    /**
     * 记录审核修改
     * @param rechargeCoin
     * @throws Exception
     */
    void audit(RechargeCoin rechargeCoin)throws Exception;

    long getRechargeCoinCountByUserId(Integer userId);

    List<RechargeCoin> getRechargeCoinListByUserId(Integer userId, Integer pageSize, Integer pageNumber);

}
