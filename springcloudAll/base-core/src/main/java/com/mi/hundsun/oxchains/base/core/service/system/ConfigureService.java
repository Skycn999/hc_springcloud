package com.mi.hundsun.oxchains.base.core.service.system;

import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

/**
 * 管理员账号业务相关Service接口<br>
 *
 * @ClassName: ConfigureService
 * @author donfy
 * @date   2017-08-17 04:10:06
 */
public interface ConfigureService extends GenericService<Integer, Configure> {


    /**
     * 通过配置标识获取配置具体值
     * @param nid 配置标识
     * @return 配置值
     */
    String getByNid(String nid);
}
