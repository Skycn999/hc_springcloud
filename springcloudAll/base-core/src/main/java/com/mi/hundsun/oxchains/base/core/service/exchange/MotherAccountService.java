package com.mi.hundsun.oxchains.base.core.service.exchange;

import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccountInfo;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;
import java.util.Map;

/**
 * 交易所母账号管理业务相关Service接口<br>
 *
 * @ClassName: MotherAccountService
 * @author bin
 * @date   2018-04-16 05:59:15
 */
public interface MotherAccountService extends GenericService<Integer, MotherAccount> {

    /**
     * 根据交易所和币种查询最优母账号
     * @param exchangeNo  交易所
     * @param code        账号币种
     * @return 母账号信息
     */
    MotherAccountInfoModel findByExchangeNoAndCoinCode(String exchangeNo, String code);

    /**
     * 根据交易所查询最优母账号
     * @param exchangeNo  交易所编码
     * @return 母账号
     */
    MotherAccountInfoModel findByExchangeNo(String exchangeNo);

    /**
     * 根据币种查询最优母账号
     * @param code  币种
     * @return 母账号列表
     */
    List<MotherAccountInfoModel> findByCoinCode(String code);

    /**
     * 获取交易所母账号
     * @param exchangeNo  交易所编号
     * @param accountName 母账号
     * @return MotherAccountInfoModel
     */
    MotherAccountInfoModel findByExNoAndAccountName(String exchangeNo, String accountName);

    List<MotherAccount> getApiInfoByExchange(String exchangeNo);

}
