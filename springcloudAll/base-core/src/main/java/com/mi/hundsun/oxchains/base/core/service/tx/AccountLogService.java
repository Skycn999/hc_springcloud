package com.mi.hundsun.oxchains.base.core.service.tx;

import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import com.mi.hundsun.oxchains.base.core.tx.po.AccountLog;

import java.math.BigDecimal;

/**
 * 资产变更记录表业务相关Service接口<br>
 *
 * @ClassName: AccountLogService
 * @author bin
 * @date   2018-04-13 09:54:58
 */
public interface AccountLogService extends GenericService<Integer, AccountLog> {


    /**
     * 创建资产流水日志
     * @param userId        用户id
     * @param coinCode      操作币种
     * @param optAmount     操作数量
     * @param totalAmount   操作前总数量
     * @param freezeAmount  冻结数量(操作后)
     * @param approach      途径
     * @param desc          描述
     * @return  影响条数
     */
    int createLog(Integer userId, String coinCode, BigDecimal optAmount, BigDecimal totalAmount, BigDecimal freezeAmount, int approach, String desc,int type);

}
