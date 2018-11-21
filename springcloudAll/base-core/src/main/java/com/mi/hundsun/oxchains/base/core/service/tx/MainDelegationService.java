package com.mi.hundsun.oxchains.base.core.service.tx;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.base.core.tx.po.SubDelegation;

import java.util.List;
import java.util.Map;

/**
 * 交易管理-主委托管理业务相关Service接口<br>
 *
 * @author bin
 * @ClassName: MainDelegationService
 * @date 2018-04-13 09:54:58
 */
public interface MainDelegationService extends GenericService<Integer, MainDelegation> {

    /**
     * 查询主委托记录
     *
     * @param userId 用户id
     * @return 委托列表
     */
    Map<String, Object> getMyMainDelegates(Integer userId, Integer direction, Integer pageNumber, Integer pageSize);

    List<MainDelegation> getMyCurrMainDelegates(Integer userId, Integer direction);

    /**
     * 更新状态
     *
     * @param state      状态值
     * @param remark     备注
     * @param delegateNo 委托编号
     */
    void updateStateByMainNo(int state, String remark, String delegateNo);

    /**
     * 更新是否拆单标记
     * @param isSplit    标记值
     * @param delegateNo 委托编号
     */
    void updateIsSplitByMainNo(int isSplit, String delegateNo);

    /**
     * 获取所有委托中的主委托
     * @return List<MainDelegation>
     */
    List<MainDelegation> findTradingMainDelegateList();

}
