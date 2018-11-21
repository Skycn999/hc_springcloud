package com.mi.hundsun.oxchains.consumer.admin.service.fn;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatAssetLog;
import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-admin-${feignSuffix}")
public interface PlatAssetLogInterface {

    /**
     * 资产划拨记录分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/fn/platAssetLog/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 保存资产划拨记录
     * @param platAssetLog
     */
    @PostMapping(value = "/fn/platAssetLog/insert")
    void insert(@RequestBody PlatAssetLog platAssetLog);

    /**
     * 资产划拨记录作废
     *
     * @return
     */
    @PostMapping(value = "/fn/platAssetLog/removeById")
    void removeById(@RequestBody PlatAssetLog platAssetLog)throws Exception;

    /**
     * 导出excel
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/fn/platAssetLog/getDtGridListExport")
    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;
}
