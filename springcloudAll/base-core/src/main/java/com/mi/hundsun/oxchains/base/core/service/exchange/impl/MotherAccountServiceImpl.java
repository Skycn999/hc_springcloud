package com.mi.hundsun.oxchains.base.core.service.exchange.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.exchange.MotherAccountInfoMapper;
import com.mi.hundsun.oxchains.base.core.mapper.exchange.MotherAccountMapper;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.exchange.MotherAccountService;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 交易所母账号管理业务相关Service接口实现<br>
 *
 * @author 枫亭
 * @date 2018-04-16 05:59:15
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class MotherAccountServiceImpl implements MotherAccountService {

    @Resource
    private MotherAccountMapper motherAccountMapper;
    @Resource
    private MotherAccountInfoMapper motherAccountInfoMapper;
    @Autowired
    RedisService redisService;

    @Override
    public MotherAccountInfoModel findByExchangeNoAndCoinCode(String exchangeNo, String code) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        if (StrUtil.isBlank(code)) {
            throw new BussinessException("[code]不能为空");
        }
        return motherAccountInfoMapper.findByExchangeNoAndCoinCode(exchangeNo, code);
    }

    @Override
    public List<MotherAccountInfoModel> findByCoinCode(String code) {
        if (StrUtil.isBlank(code)) {
            throw new BussinessException("[code]不能为空");
        }
        return motherAccountInfoMapper.groupByCoinCode(code);
    }

    @Override
    public MotherAccountInfoModel findByExNoAndAccountName(String exchangeNo, String accountName) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        if (StrUtil.isBlank(accountName)) {
            throw new BussinessException("[accountName]不能为空");
        }
        List<MotherAccountInfoModel> accountInfoModels = motherAccountInfoMapper.findByAccountNameAndExNo(exchangeNo, accountName);
        return accountInfoModels.size() > 0 ? accountInfoModels.get(0) : null;
    }

    @Override
    public List<MotherAccount> getApiInfoByExchange(String exchangeNo) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        return motherAccountInfoMapper.getApiInfoByExchange(exchangeNo);

    }

    @Override
    public MotherAccountInfoModel findByExchangeNo(String exchangeNo) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        return motherAccountInfoMapper.findByExchangeNo(exchangeNo);
    }

    @Override
    public GenericMapper<MotherAccount, Integer> _getMapper() {
        return motherAccountMapper;
    }


}
