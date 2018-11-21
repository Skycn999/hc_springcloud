package com.mi.hundsun.oxchains.consumer.admin.service.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.quote.CodePairConfig;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface CodePairConfigInteface {

    /**
     * 代码对配置管查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/exchange/codePairConfig/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws Exception;

    /**
     * 新增
     * @param codePairConfig
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/exchange/codePairConfig/insert")
    ResultEntity insert(@RequestBody CodePairConfig codePairConfig)throws Exception;

    /**
     * 更新
     * @param codePairConfig
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/exchange/codePairConfig/updateCodePacirConfig")
    ResultEntity updateCodePacirConfig(@RequestBody CodePairConfig codePairConfig)throws Exception;
}
