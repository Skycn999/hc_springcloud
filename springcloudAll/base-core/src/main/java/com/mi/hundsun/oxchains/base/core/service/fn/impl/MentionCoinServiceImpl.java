package com.mi.hundsun.oxchains.base.core.service.fn.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.utils.DateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.fn.MentionCoinMapper;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.service.fn.MentionCoinService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提币管理业务相关Service接口实现<br>
 *
 * @author bin
 * @ClassName: MentionCoinServiceImpl
 * @date 2018-04-15 06:13:26
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class MentionCoinServiceImpl implements MentionCoinService {

    @Resource
    private MentionCoinMapper mentionCoinMapper;

    @Override
    public GenericMapper<MentionCoin, Integer> _getMapper() {
        return mentionCoinMapper;
    }


    @Override
    public void audit(MentionCoin mentionCoin) throws BussinessException {
        MentionCoin coin = mentionCoinMapper.selectOne(new MentionCoin(c -> {
            c.setId(mentionCoin.getId());
            c.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (coin.getState() != MentionCoin.STATE.PENDING.code) {
            throw new BussinessException("该记录已不在待审核状态！");
        }
        mentionCoinMapper.updateByPrimaryKeySelective(mentionCoin);
    }

    @Override
    public long myMentionCoinLogCount(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY state = 0, create_time DESC");
        return mentionCoinMapper.myMentionCoinLogCount(params);
    }

    @Override
    public List<MentionCoin> myMentionCoinLog(Integer userId, Integer pageSize, Integer pageNumber) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY state = 0, create_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        return mentionCoinMapper.myMentionCoinLog(params);
    }

    @Override
    public BigDecimal getMentionCoinByToday(Integer userId, String code) {
        String todayStr = DateUtils.dateStr2(new Date())+"%";
        BigDecimal total = mentionCoinMapper.getMentionCoinByToday(userId,code,MentionCoin.STATE.NO_PASS.code,todayStr);
        return  null == total ?BigDecimal.ZERO:total;
    }
}
