package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.po.system.Dict;
import com.mi.hundsun.oxchains.base.core.mapper.system.DictMapper;
import com.mi.hundsun.oxchains.base.core.service.system.DictService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 字典表业务相关Service接口实现<br>
 *
 * @ClassName: DictServiceImpl
 * @author donfy
 * @date   2017-08-16 01:59:00
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class DictServiceImpl implements DictService {

	@Resource
    private DictMapper dictMapper;

    @Override
    public GenericMapper< Dict,Integer> _getMapper() {
        return dictMapper;
    }

}
