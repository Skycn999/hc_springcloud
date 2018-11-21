package com.mi.hundsun.oxchains.base.core.service.tx.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.service.tx.MainDelegationService;
import com.mi.hundsun.oxchains.base.core.tx.mapper.MainDelegationMapper;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 交易管理-主委托管理业务相关Service接口实现<br>
 *
 * @author bin
 * @date   2018-04-13 09:54:58
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class MainDelegationServiceImpl implements MainDelegationService {

	@Resource
    private MainDelegationMapper mainDelegationMapper;

    @Override
    public GenericMapper<MainDelegation,Integer> _getMapper() {
        return mainDelegationMapper;
    }

    @Override
    public Map<String, Object> getMyMainDelegates(Integer userId, Integer direction, Integer pageNumber, Integer pageSize) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        if(0 != direction) {
            params.put("whereSql", " AND user_id = " + userId + " AND direction = " + direction + " AND del_flag = 0 ");
        } else {
            params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        }
        params.put("sortSql", " ORDER BY create_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        Map<String, Object> map = new LinkedHashMap<>();
        List<MainDelegation> mainDelegations = mainDelegationMapper.selectByPage(params);
        map.put("list", mainDelegations);
        if(null != mainDelegations && mainDelegations.size() > 0) {
            long totalSize = mainDelegationMapper.countBySqlPage(params);
            map.put("totalSize", totalSize);
            map.put("totalPage", totalSize % pageSize == 0 ? totalSize / pageSize : totalSize / pageSize + 1);
        } else {
            map.put("totalSize", 0);
            map.put("totalPage", 0);
        }
        return map;
    }

    @Override
    public List<MainDelegation> getMyCurrMainDelegates(Integer userId, Integer direction) {
        if(direction == 0) {
            return mainDelegationMapper.getMyCurrDelegates(userId);
        } else {
            return mainDelegationMapper.getMyCurrDelegatesByDirection(userId, direction);
        }
    }

    @Override
    public void updateStateByMainNo(int state, String remark, String delegateNo) {
        mainDelegationMapper.updateStateByMainNo(state, remark, delegateNo);
    }

    @Override
    public void updateIsSplitByMainNo(int isSplit, String delegateNo) {
        mainDelegationMapper.updateIsSplitByMainNo(isSplit, delegateNo);
    }

    @Override
    public List<MainDelegation> findTradingMainDelegateList() {
        return mainDelegationMapper.findTradingMainDelegateList();
    }
}
