package com.mi.hundsun.oxchains.base.core.service.quote;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;
import java.util.Map;

/**
 * 交易代码对配置管理业务相关Service接口<br>
 *
 * @ClassName: CodePairConfigService
 * @author db
 * @date   2018-06-10 03:23:44
 */
public interface CodePairConfigService extends GenericService<Integer, CodePairConfig> {
    /**
     * 通过交易所查询交易对
     * @param exchange  交易所编号
     * @return  List<CodePairConfig>
     */
    List<CodePairConfig> getByExchange(String exchange);

    /**
     * 获取某个分区下所有的品种
     *
     * @param partition  分区
     * @param pageNumber 页码
     * @return List<Account>
     * @throws BussinessException ex
     */
    List<CodePairConfig> pageByCodeAndBaseCode(String partition, String code, String orderColumn, String orderType, int pageNumber, int pageSize) throws BussinessException;

//    List<Commodity> findListBySql(String partition, String code, String orderColumn, String orderType, int pageNumber, int pageSize) throws BussinessException;

    List<CodePairConfig> findByCodeAndBaseCode(String partition, String code) throws BussinessException;

    List<CodePairConfig> searchForApp(String code);

    List<CodePairConfig> getDisplayOnAppCodes();

    List<CodePairConfig> getAllValidCodes();

    CodePairConfig getCodePairConfig(String currencyPair);

    Map<String ,CodePairConfig> getSymbolsConfigs(String symbols);
}
