package com.mi.hundsun.oxchains.base.core.service.exchange.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.exchange.ExchangeMapper;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.service.exchange.ExchangeService;

/**
 * 交易所管理业务相关Service接口实现<br>
 *
 * @ClassName: ExchangeServiceImpl
 * @author bin
 * @date   2018-04-16 05:39:41
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ExchangeServiceImpl implements ExchangeService {

	@Resource
    private ExchangeMapper exchangeMapper;

    @Override
    public GenericMapper<Exchange,Integer> _getMapper() {
        return exchangeMapper;
    }

}
