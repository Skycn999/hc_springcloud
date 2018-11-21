package com.mi.hundsun.oxchains.base.core.service.fn;

import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;

/**
 * 平台分配给客户地址业务相关Service接口<br>
 *
 * @ClassName: PlatUserAddressService
 * @author bin
 * @date   2018-04-15 07:49:10
 */
public interface PlatUserAddressService extends GenericService<Integer, PlatUserAddress> {

    /**
     * 查找没有被分配的地址 btc、eth各一个
     * @return List<PlatUserAddress>
     */
    List<PlatUserAddress> findNoDistributedAddress();

}
