package com.mi.hundsun.oxchains.base.core.service.fn;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提币管理业务相关Service接口<br>
 *
 * @ClassName: MentionCoinService
 * @author bin
 * @date   2018-04-15 06:13:26
 */
public interface MentionCoinService extends GenericService<Integer, MentionCoin> {

    void  audit(MentionCoin mentionCoin)throws BussinessException;

    long  myMentionCoinLogCount(Integer userId);

    List<MentionCoin> myMentionCoinLog(Integer userId, Integer pageSize,Integer pageNumber);

    BigDecimal getMentionCoinByToday(Integer userId, String code);
}
