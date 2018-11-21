package com.mi.hundsun.oxchains.base.core.service.fn.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.fn.PlatAssetLogMapper;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatAssetLog;
import com.mi.hundsun.oxchains.base.core.service.fn.PlatAssetLogService;

/**
 * 资产划拨记录业务相关Service接口实现<br>
 *
 * @ClassName: PlatAssetLogServiceImpl
 * @author bin
 * @date   2018-04-15 08:22:48
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class PlatAssetLogServiceImpl implements PlatAssetLogService {

	@Resource
    private PlatAssetLogMapper platAssetLogMapper;

    @Override
    public GenericMapper<PlatAssetLog,Integer> _getMapper() {
        return platAssetLogMapper;
    }

}
