package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.system.TpiRequestLogMapper;
import com.mi.hundsun.oxchains.base.core.po.system.TpiRequestLog;
import com.mi.hundsun.oxchains.base.core.service.system.TpiRequestLogService;

/**
 * 第三方接口请求记录业务相关Service接口实现<br>
 *
 * @ClassName: TpiRequestLogServiceImpl
 * @author bin
 * @date   2018-04-24 02:00:12
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class TpiRequestLogServiceImpl implements TpiRequestLogService {

	@Resource
    private TpiRequestLogMapper tpiRequestLogMapper;

    @Override
    public GenericMapper<TpiRequestLog,Integer> _getMapper() {
        return tpiRequestLogMapper;
    }

}
