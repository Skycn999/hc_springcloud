package com.mi.hundsun.oxchains.base.core.service.tx.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.service.tx.SubDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.mapper.SubDelegationMapper;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易管理-子委托管理业务相关Service接口实现<br>
 *
 * @author bin
 * @date 2018-04-13 09:54:58
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class SubDelegationServiceImpl implements SubDelegationService {

    @Resource
    private SubDelegationMapper subDelegationMapper;

    @Override
    public List<SubDelegation> getMySubsByMainNo(String delegateNo, Integer userId) {
        SubDelegation sd = new SubDelegation();
        sd.setMainDelegateNo(delegateNo);
        sd.setUserId(userId);
        sd.setDelFlag(SubDelegation.DELFLAG.NO.code);
        return subDelegationMapper.select(sd);
    }

    @Override
    public Map<String, Object> getMySubDelegates(Integer userId, Integer direction, Integer pageNumber, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        if (0 != direction) {
            params.put("whereSql", " AND user_id = " + userId + " AND direction = " + direction + " AND del_flag = 0 ");
        } else {
            params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        }
        params.put("sortSql", " ORDER BY create_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        Map<String, Object> map = new LinkedHashMap<>();
        List<SubDelegation> subDelegations = subDelegationMapper.selectByPage(params);
        map.put("list", subDelegations);
        if (null != subDelegations && subDelegations.size() > 0) {
            long totalSize = subDelegationMapper.countBySqlPage(params);
            map.put("totalSize", totalSize);
            map.put("totalPage", totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1);
        } else {
            map.put("totalSize", 0);
            map.put("totalPage", 0);
        }
        return map;
    }

    @Override
    public SubDelegation getByEntrustNo(String entrustNo) {
        SubDelegation sd = new SubDelegation();
        sd.setEntrustNo(entrustNo);
        sd.setDelFlag(SubDelegation.DELFLAG.NO.code);
        return subDelegationMapper.selectOne(sd);
    }

    @Override
    public SubDelegation getByBillNo(String billNo) {
        SubDelegation sd = new SubDelegation();
        sd.setBillNo(billNo);
        sd.setDelFlag(SubDelegation.DELFLAG.NO.code);
        return subDelegationMapper.selectOne(sd);
    }

    @Override
    public List<SubDelegation> getTradingSubDelegates() {
        return subDelegationMapper.getTradingSubDelegates();
    }

    @Override
    public int updateByStateForFailure(SubDelegation sub) {
        if (null == sub) {
            return 0;
        }
        if (null == sub.getId() || null == sub.getUserId()) {
            return 0;
        }
        return subDelegationMapper.updateByStateForFailure(sub.getId(), sub.getUserId(), sub.getInfo(), sub.getState());
    }

    @Override
    public SubDelegation generateForLimited(MainDelegation delegation, BigDecimal amount, BigDecimal price, String exchangeNo,
                                            String accountNo) {
        SubDelegation sub = new SubDelegation();
        sub.setUuid(RandomUtils.randomCustomUUID());
        sub.setBillNo(OrderNoUtils.getSubDelegateBillNo());
        sub.setUserId(delegation.getUserId());
        sub.setMainDelegateNo(delegation.getDelegateNo());
        sub.setAmount(amount);
        sub.setPrice(price);
        sub.setCoinCurrency(delegation.getCoinCurrency());
        sub.setCoinCode(delegation.getCoinCode());
        sub.setGmv(amount.multiply(price));
        sub.setDirection(delegation.getDirection());
        sub.setStyle(delegation.getStyle());
        sub.setExchange(exchangeNo);
        sub.setMotherAccount(accountNo);
        sub.setIsConfirm(SubDelegation.ISCONFIRM.NO.code);
        sub.setState(SubDelegation.STATE.REPORTED.code);
        sub.setServiceFeeScale(delegation.getServiceFeeScale());
        sub.setInfo("生成子委托成功,已报");
        //判断主委托是否进行了拆单 拆单则设置hasBrother为1 否则为0
        if (delegation.getIsSplit() == MainDelegation.DELFLAG.YES.code) {
            sub.setHasBrother(SubDelegation.DELFLAG.YES.code);
        } else {
            sub.setHasBrother(SubDelegation.DELFLAG.NO.code);
        }
        int i = subDelegationMapper.insert(sub);
        if (i < 1) {
            throw new BussinessException("子委托记录保存失败");
        }

        sub = subDelegationMapper.selectByBillNo(sub.getBillNo());
        return sub;
    }

    @Override
    public SubDelegation generateForMarketBuyIn(MainDelegation delegation, BigDecimal gmv, String exchangeNo, String accountNo) {
        SubDelegation sub = new SubDelegation();
        sub.setBillNo(OrderNoUtils.getSubDelegateBillNo());
        sub.setUuid(RandomUtils.randomCustomUUID());
        sub.setUserId(delegation.getUserId());
        sub.setMainDelegateNo(delegation.getDelegateNo());
        sub.setCoinCurrency(delegation.getCoinCurrency());
        sub.setCoinCode(delegation.getCoinCode());
        sub.setGmv(gmv);
        sub.setDirection(delegation.getDirection());
        sub.setStyle(delegation.getStyle());
        sub.setExchange(exchangeNo);
        sub.setMotherAccount(accountNo);
        sub.setIsConfirm(SubDelegation.ISCONFIRM.NO.code);
        sub.setState(SubDelegation.STATE.REPORTED.code);
        sub.setCreator(delegation.getCreator());
        sub.setServiceFeeScale(delegation.getServiceFeeScale());
        sub.setInfo("生成子委托成功,已报");
        //判断主委托是否进行了拆单 拆单则设置hasBrother为1 否则为0
        if (delegation.getIsSplit() == MainDelegation.DELFLAG.YES.code) {
            sub.setHasBrother(SubDelegation.DELFLAG.YES.code);
        } else {
            sub.setHasBrother(SubDelegation.DELFLAG.NO.code);
        }
        int i = subDelegationMapper.insert(sub);
        if (i < 1) {
            throw new BussinessException("子委托记录保存失败");
        }

        sub = subDelegationMapper.selectByBillNo(sub.getBillNo());
        return sub;
    }

    @Override
    public SubDelegation generateForMarketSellOut(MainDelegation delegation, BigDecimal gmv, String exchangeNo, String accountNo) {
        SubDelegation sub = new SubDelegation();
        sub.setBillNo(OrderNoUtils.getSubDelegateBillNo());
        sub.setUuid(RandomUtils.randomCustomUUID());
        sub.setUserId(delegation.getUserId());
        sub.setMainDelegateNo(delegation.getDelegateNo());
        sub.setCoinCurrency(delegation.getCoinCurrency());
        sub.setCoinCode(delegation.getCoinCode());
        sub.setAmount(gmv);
        sub.setDirection(delegation.getDirection());
        sub.setStyle(delegation.getStyle());
        sub.setExchange(exchangeNo);
        sub.setMotherAccount(accountNo);
        sub.setIsConfirm(SubDelegation.ISCONFIRM.NO.code);
        sub.setState(SubDelegation.STATE.REPORTED.code);
        sub.setCreator(delegation.getCreator());
        sub.setServiceFeeScale(delegation.getServiceFeeScale());
        sub.setInfo("生成子委托成功,已报");
        //判断主委托是否进行了拆单 拆单则设置hasBrother为1 否则为0
        if (delegation.getIsSplit() == MainDelegation.DELFLAG.YES.code) {
            sub.setHasBrother(SubDelegation.DELFLAG.YES.code);
        } else {
            sub.setHasBrother(SubDelegation.DELFLAG.NO.code);
        }
        int i = subDelegationMapper.insert(sub);
        if (i < 1) {
            throw new BussinessException("子委托记录保存失败");
        }

        sub = subDelegationMapper.selectByBillNo(sub.getBillNo());
        return sub;
    }

    @Override
    public List<SubDelegation> getSubDelegatesByMainDelegateNo(String delegateNo, Integer userId) {
        if (StrUtil.isBlank(delegateNo)) {
            throw new BussinessException("[delegateNo]不能为空");
        }
        if (StrUtil.isBlank(delegateNo)) {
            throw new BussinessException("[userId]不能为空");
        }
        SubDelegation delegation = new SubDelegation();
        delegation.setMainDelegateNo(delegateNo);
        delegation.setUserId(userId);
        delegation.setDelFlag(SubDelegation.DELFLAG.NO.code);
        return subDelegationMapper.select(delegation);
    }

    @Override
    public GenericMapper<SubDelegation, Integer> _getMapper() {
        return subDelegationMapper;
    }

}
