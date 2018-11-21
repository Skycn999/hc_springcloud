package com.mi.hundsun.oxchains.base.core.service.tpl;

import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

/**
 * 净值风控模板业务相关Service接口<br>
 *
 * @ClassName: NetWorthControlService
 * @author bin
 * @date   2018-04-12 11:36:33
 */
public interface NetWorthControlService extends GenericService<Integer, NetWorthControl> {

    void insertNetWorthControl(NetWorthControl netWorthControl)throws Exception;

    void updateNetWorthControl(NetWorthControl netWorthControl)throws Exception;

    /**
     * 获取启用中默认的模板，若无默认，则获取最新启用的模板
     * @return
     */
    NetWorthControl findNetWorthControlIsDefault();
}
