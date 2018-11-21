package com.mi.hundsun.oxchains.base.core.mapper.fn;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import org.apache.ibatis.annotations.Select;

/**
 * 平台分配给客户地址相关Dao接口<br>
 *
 * @author bin
 * @date 2018-04-15 07:49:10
 */
public interface PlatUserAddressMapper extends GenericMapper<PlatUserAddress, Integer> {

    @Select(" SELECT * FROM fn_plat_user_address f where f.user_id = 0 AND f.state = 0 AND f.del_flag = 0 " +
            " AND f.coin_currency = 'ETH' order by f.create_time ASC limit 1")
    PlatUserAddress findOneNoDistributedEthAddress();

    @Select(" SELECT * FROM fn_plat_user_address f where f.user_id = 0 AND f.state = 0 AND f.del_flag = 0 " +
            " AND f.coin_currency = 'BTC' order by f.create_time ASC limit 1")
    PlatUserAddress findOneNoDistributedBtcAddress();

}
