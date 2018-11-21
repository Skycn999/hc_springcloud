package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.ServiceLogMapper;
import com.mi.hundsun.oxchains.base.core.po.system.ServiceLog;
import com.mi.hundsun.oxchains.base.core.service.system.ServiceLogService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 后台接口调用日志业务相关Service接口实现<br>
 *
 * @ClassName: LogServiceImpl
 * @author xxw
 * @date   2017-09-18 11:56:52
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ServiceLogServiceImpl implements ServiceLogService {

	@Resource
    private ServiceLogMapper serviceLogMapper;

    @Override
    public GenericMapper<ServiceLog,Integer> _getMapper() {
        return serviceLogMapper;
    }

}
