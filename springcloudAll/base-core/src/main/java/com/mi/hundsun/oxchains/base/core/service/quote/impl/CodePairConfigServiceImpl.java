package com.mi.hundsun.oxchains.base.core.service.quote.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.quote.CodePairConfigMapper;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import com.mi.hundsun.oxchains.base.core.service.quote.CodePairConfigService;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易代码对配置管理业务相关Service接口实现<br>
 *
 * @author db
 * @ClassName: CodePairConfigServiceImpl
 * @date 2018-06-10 03:23:44
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class CodePairConfigServiceImpl implements CodePairConfigService {

    @Resource
    private CodePairConfigMapper codePairConfigMapper;

    @Override
    public List<CodePairConfig> getByExchange(String exchangeNo) {
        if (StrUtil.isBlank(exchangeNo)) {
            throw new BussinessException("[exchangeNo]不能为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("exchangeNo", exchangeNo);
        return codePairConfigMapper.selectByExchange(params);
    }

    @Override
    public List<CodePairConfig> pageByCodeAndBaseCode(String partition, String code, String orderColumn, String orderType, int pageNumber, int pageSize) throws BussinessException {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        params.put("baseCode", partition);
        params.put("code", code);
        params.put("orderColumn", orderColumn);
        params.put("orderType", orderType);
        params.put("startPos", startPos);
        params.put("pageSize", pageSize);
        return codePairConfigMapper.pageByCodeAndBaseCode(params);
    }

    @Override
    public List<CodePairConfig> findByCodeAndBaseCode(String baseCode, String code) throws BussinessException {
        if (StrUtil.isBlank(baseCode)) {
            throw new BussinessException("[baseCode]不能为空");
        }
        if (StrUtil.isBlank(code)) {
            throw new BussinessException("[code]不能为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("baseCode", baseCode);
        return codePairConfigMapper.findByCodeAndBaseCode(params);
    }

    @Override
    public List<CodePairConfig> searchForApp(String code) {
        if (StrUtil.isBlank(code)) {
            throw new BussinessException("[code]不能为空");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        return codePairConfigMapper.searchForApp(params);
    }

    @Override
    public List<CodePairConfig> getDisplayOnAppCodes() {
        return codePairConfigMapper.getDisplayOnAppCodes();
    }

    @Override
    public List<CodePairConfig> getAllValidCodes() {
        return codePairConfigMapper.getAllValidCodes();
    }

    @Override
    public CodePairConfig getCodePairConfig(String currencyPair) {
        if (StrUtil.isBlank(currencyPair)) {
            throw new BussinessException("[currencyPair]不能为空");
        }
        String code = currencyPair.split("_")[0].toUpperCase();
        String baseCode = currencyPair.split("_")[1].toUpperCase();
        return codePairConfigMapper.getCodePairConfig(code, baseCode);
    }

    @Override
    public Map<String ,CodePairConfig> getSymbolsConfigs(String symbols) {
        if (StrUtil.isBlank(symbols)) {
            throw new BussinessException("[symbols]不能为空");
        }
        Map<String ,CodePairConfig> map = new HashMap<>();
        if (!symbols.contains(",")) {
            String code = symbols.split("_")[0].toUpperCase();
            String baseCode = symbols.split("_")[1].toUpperCase();
            CodePairConfig codePairConfig = codePairConfigMapper.getCodePairConfig(code, baseCode);
            map.put(symbols,codePairConfig);
        } else {
            String[] symbolArr = symbols.split(",");
            for (String symbol : symbolArr) {
                String code = symbol.split("_")[0].toUpperCase();
                String baseCode = symbol.split("_")[1].toUpperCase();
                map.put(symbol,codePairConfigMapper.getCodePairConfig(code, baseCode));
            }

        }
        return map;
    }

    @Override
    public GenericMapper<CodePairConfig, Integer> _getMapper() {
        return codePairConfigMapper;
    }

}
