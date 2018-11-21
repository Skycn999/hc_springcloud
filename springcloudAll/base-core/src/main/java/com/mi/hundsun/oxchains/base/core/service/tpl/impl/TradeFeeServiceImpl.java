package com.mi.hundsun.oxchains.base.core.service.tpl.impl;

import com.mi.hundsun.oxchains.base.core.mapper.tpl.TradeFeeMapper;
import com.mi.hundsun.oxchains.base.core.po.tpl.TradeFee;
import com.mi.hundsun.oxchains.base.core.service.tpl.TradeFeeService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;


/**
 * 交易手续费率模板业务相关Service接口实现<br>
 *
 * @ClassName: TradeFeeServiceImpl
 * @author bin
 * @date   2018-05-16 03:26:27
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class TradeFeeServiceImpl implements TradeFeeService {

	@Resource
    private TradeFeeMapper tradeFeeMapper;

    @Override
    public GenericMapper<TradeFee,Integer> _getMapper() {
        return tradeFeeMapper;
    }

}
