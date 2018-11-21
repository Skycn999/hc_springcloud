package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.system.OptLogMapper;
import com.mi.hundsun.oxchains.base.core.po.system.OptLog;
import com.mi.hundsun.oxchains.base.core.service.system.OptLogService;

/**
 * 后台系统操作日志业务相关Service接口实现<br>
 *
 * @ClassName: OptLogServiceImpl
 * @author bin
 * @date   2018-04-24 02:00:12
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class OptLogServiceImpl implements OptLogService {

	@Resource
    private OptLogMapper optLogMapper;

    @Override
    public GenericMapper<OptLog,Integer> _getMapper() {
        return optLogMapper;
    }

}
