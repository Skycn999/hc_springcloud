package com.mi.hundsun.oxchains.base.core.service.fn.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.fn.RechargeCoinMapper;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.service.fn.RechargeCoinService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充币管理业务相关Service接口实现<br>
 *
 * @author bin
 * @ClassName: RechargeCoinServiceImpl
 * @date 2018-04-15 03:52:26
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class RechargeCoinServiceImpl implements RechargeCoinService {

    @Resource
    private RechargeCoinMapper rechargeCoinMapper;


    @Override
    public long getRechargeCoinCountByUserId(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY state = 0, create_time DESC");
        return rechargeCoinMapper.getRechargeCoinCountByUserId(params);
    }

    @Override
    public List<RechargeCoin> getRechargeCoinListByUserId(Integer userId, Integer pageSize,Integer pageNumber) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY state = 0, create_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        return rechargeCoinMapper.getRechargeCoinListByUserId(params);
    }

    @Override
    public GenericMapper<RechargeCoin, Integer> _getMapper() {
        return rechargeCoinMapper;
    }

    @Override
    public void audit(RechargeCoin rechargeCoin) throws Exception {
        rechargeCoinMapper.updateByPrimaryKeySelective(rechargeCoin);
    }

}
