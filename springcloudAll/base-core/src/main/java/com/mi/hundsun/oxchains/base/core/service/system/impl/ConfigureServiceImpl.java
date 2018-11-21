package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.ConfigureMapper;
import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import com.mi.hundsun.oxchains.base.core.service.system.ConfigureService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 管理员账号业务相关Service接口实现<br>
 *
 * @author donfy
 * @ClassName: ConfigureServiceImpl
 * @date 2017-08-17 04:10:06
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ConfigureServiceImpl implements ConfigureService {

    @Resource
    private ConfigureMapper configureMapper;

    @Override
    public GenericMapper<Configure, Integer> _getMapper() {
        return configureMapper;
    }


    @Override
    public String getByNid(String nid) {
        Configure c = new Configure();
        c.setStatus(Configure.STATUS.ENABLE.code);
        c.setNid(nid);
        c.setDelFlag(Configure.DELFLAG.NO.code);
        c = configureMapper.selectOne(c);
        return null == c ? "" : c.getValue();
    }
}
