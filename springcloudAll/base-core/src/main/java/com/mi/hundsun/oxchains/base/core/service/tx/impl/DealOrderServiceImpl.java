package com.mi.hundsun.oxchains.base.core.service.tx.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.quote.model.BusinessDetail;
import com.mi.hundsun.oxchains.base.core.model.quote.model.OrderQryRes;
import com.mi.hundsun.oxchains.base.core.service.tx.DealOrderService;
import com.mi.hundsun.oxchains.base.core.tx.mapper.DealOrderMapper;
import com.mi.hundsun.oxchains.base.core.tx.model.CountDealOrderModel;
import com.mi.hundsun.oxchains.base.core.tx.po.DealOrder;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import com.mi.hundsun.oxchains.base.core.util.TxUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 交易管理-成交订单业务相关Service接口实现<br>
 *
 * @author bin
 * @date 2018-04-13 09:54:58
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class DealOrderServiceImpl implements DealOrderService {

    @Resource
    private DealOrderMapper dealOrderMapper;

    @Override
    public Map<String, Object> getMyDealOrders(Integer userId, Integer direction, Integer pageNumber, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        if (0 != direction) {
            params.put("whereSql", " AND user_id = " + userId + " AND direction = " + direction + " AND del_flag = 0 ");
        } else {
            params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        }
        params.put("sortSql", " ORDER BY deal_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        Map<String, Object> map = new LinkedHashMap<>();
        List<DealOrder> dealOrders = dealOrderMapper.selectByPage(params);
        map.put("list", dealOrders);
        if (null != dealOrders && dealOrders.size() > 0) {
            long totalSize = dealOrderMapper.countBySqlPage(params);
            map.put("totalSize", totalSize);
            map.put("totalPage", totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1);
        } else {
            map.put("totalSize", 0);
            map.put("totalPage", 0);
        }
        return map;
    }

    @Override
    public List<DealOrder> generateForLimited(SubDelegation sub, OrderQryRes result, String entrustNo) {
        List<BusinessDetail> businessDetails = result.getBusiness_detail();
        List<DealOrder> dealOrders = new ArrayList<>();
        for (BusinessDetail detail : businessDetails) {
            DealOrder order = new DealOrder();
            order.setEntrustNo(entrustNo);
            order.setBusinessNo(detail.getBusiness_no());
            List<DealOrder> select = dealOrderMapper.select(order);
            //如果该记录存在,跳出当前循环继续下一个循环
            if(null != select && select.size() > 0) {
                continue;
            }
            order.setUuid(RandomUtils.randomCustomUUID());
            order.setUserId(sub.getUserId());
            order.setCoinCurrency(sub.getCoinCurrency());
            order.setCoinCode(sub.getCoinCode());
            order.setAmount(TxUtils.removeRedundanceZeroString(detail.getBusiness_amount()));
            order.setPrice(TxUtils.removeRedundanceZeroString(detail.getBusiness_price()));
            order.setGmv(TxUtils.removeRedundanceZeroString(order.getPrice().multiply(order.getAmount())).setScale(10,BigDecimal.ROUND_HALF_UP));
            order.setExchange(sub.getExchange());
            order.setMotherAccount(sub.getMotherAccount());
            order.setServiceFee(new BigDecimal(detail.getFee()));
            //计算平台收取的手续费  = 手续费率 * 返回的成交量
            if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                order.setPlatFee(TxUtils.removeRedundanceZeroString(sub.getServiceFeeScale().multiply(order.getAmount())
                        .setScale(10, BigDecimal.ROUND_HALF_UP)));
            } else {
                order.setPlatFee(TxUtils.removeRedundanceZeroString(sub.getServiceFeeScale().multiply(order.getGmv())
                        .setScale(10, BigDecimal.ROUND_HALF_UP)));
            }
            order.setState(DealOrder.STATE.FINISHED.code);
            order.setStyle(sub.getStyle());
            order.setDirection(sub.getDirection());
            order.setDealTime(new Date());
            try {
                dealOrderMapper.insert(order);
            } catch (Exception e) {
                log.error("插入成交记录(for limited)失败,委托编号:" + result.getEntrust_no() + ",失败原因:{}", e.getMessage());
                continue;
            }
            dealOrders.add(order);
        }

        return dealOrders;
    }

    @Override
    public List<DealOrder> generateForMarket(SubDelegation sub, OrderQryRes result, String entrustNo) {
        List<BusinessDetail> businessDetails = result.getBusiness_detail();
        List<DealOrder> dealOrders = new ArrayList<>();
        for (BusinessDetail detail : businessDetails) {
            DealOrder order = new DealOrder();
            order.setUserId(sub.getUserId());
            order.setEntrustNo(entrustNo);
            order.setUuid(RandomUtils.randomCustomUUID());
            order.setBusinessNo(detail.getBusiness_no());
            order.setCoinCurrency(sub.getCoinCurrency());
            order.setCoinCode(sub.getCoinCode());
            order.setAmount(TxUtils.removeRedundanceZeroString(detail.getBusiness_amount()));
            order.setPrice(TxUtils.removeRedundanceZeroString(detail.getBusiness_price()));
            order.setGmv(TxUtils.removeRedundanceZeroString(order.getPrice().multiply(order.getAmount())).setScale(10,BigDecimal.ROUND_HALF_UP));
            order.setExchange(sub.getExchange());
            order.setMotherAccount(sub.getMotherAccount());
            order.setState(DealOrder.STATE.FINISHED.code);
            order.setStyle(sub.getStyle());
            order.setDirection(sub.getDirection());
            order.setServiceFee(new BigDecimal(detail.getFee()));
            //计算平台收取的手续费  = 手续费率 * 返回的成交量
            if (sub.getDirection() == SubDelegation.DIRECTION.BUYIN.code) {
                order.setPlatFee(sub.getServiceFeeScale().multiply(order.getAmount()).setScale(10, BigDecimal.ROUND_HALF_UP));
            } else {
                order.setPlatFee(sub.getServiceFeeScale().multiply(order.getGmv()).setScale(10, BigDecimal.ROUND_HALF_UP));
            }
            order.setDealTime(new Date());
            try {
                dealOrderMapper.insert(order);
            } catch (Exception e) {
                log.error("插入成交记录(for market)失败,委托编号:" + result.getEntrust_no() + ",失败原因:{}", e.getMessage());
                throw new BussinessException("插入成交记录(for market)失败");
            }
            dealOrders.add(order);
        }
        return dealOrders;
    }


    @Override
    public List<CountDealOrderModel> countGroupByExchange() {
        return dealOrderMapper.countGroupByExchange();
    }

    @Override
    public List<CountDealOrderModel> countGroupByCode() {
        return dealOrderMapper.countGroupByCode();
    }


    @Override
    public List<CountDealOrderModel> countDirection() {
        return dealOrderMapper.countDirection();
    }

    @Override
    public List<CountDealOrderModel> countServiceFeeInfo() {
        return dealOrderMapper.countServiceFeeByCreateTime();
    }

    @Override
    public List<CountDealOrderModel> countTodayTxVolume() {
        return null;
    }

    @Override
    public GenericMapper<DealOrder, Integer> _getMapper() {
        return dealOrderMapper;
    }

}
