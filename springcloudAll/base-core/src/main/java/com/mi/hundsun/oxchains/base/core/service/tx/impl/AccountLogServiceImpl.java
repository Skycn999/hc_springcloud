package com.mi.hundsun.oxchains.base.core.service.tx.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.constant.AccountLogType;
import com.mi.hundsun.oxchains.base.core.service.tx.AccountLogService;
import com.mi.hundsun.oxchains.base.core.tx.mapper.AccountLogMapper;
import com.mi.hundsun.oxchains.base.core.tx.po.AccountLog;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 资产变更记录表业务相关Service接口实现<br>
 *
 * @author bin
 * @date 2018-04-13 09:54:58
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class AccountLogServiceImpl implements AccountLogService {

    @Resource
    private AccountLogMapper accountLogMapper;


    @Override
    public int createLog(Integer userId, String coinCode, BigDecimal optAmount, BigDecimal totalAmount, BigDecimal freezeAmount, int approach, String desc, int type) {
        AccountLog log = new AccountLog();
        log.setUuid(RandomUtils.randomCustomUUID());
        log.setUserId(userId);
        log.setOptCoinCode(coinCode);
        log.setOptAmount(optAmount);
        log.setBeforeOptAmount(totalAmount);
        if (type == AccountLogType.ADD){
            log.setAfterOptAmount(totalAmount.add(optAmount));
        }else if(type == AccountLogType.REDUCE){
            log.setAfterOptAmount(totalAmount.subtract(optAmount));
        }else if(type == AccountLogType.UNCHANGE){
            log.setAfterOptAmount(totalAmount);
        }
        log.setFreezeAmount(freezeAmount);
        log.setApproach(approach);
        log.setInfo(desc);
        log.setCreateTime(new Date());
        log.setDelFlag(AccountLog.DELFLAG.NO.code);
        return accountLogMapper.insert(log);
    }



    @Override
    public GenericMapper<AccountLog, Integer> _getMapper() {
        return accountLogMapper;
    }

}
