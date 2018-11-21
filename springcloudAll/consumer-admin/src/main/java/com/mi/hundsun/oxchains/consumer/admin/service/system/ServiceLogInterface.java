package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.system.ServiceLog;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 后台接口调用日志业务相关Service接口<br>
 *
 * @ClassName: ServiceLogInterface
 * @author xxw
 * @date   2017-09-18 11:56:52
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface ServiceLogInterface {

    @PostMapping(value = "/sys/log/selectOne")
    ServiceLog selectOne(@RequestBody ServiceLog log);

    @PostMapping(value = "/sys/log/select")
    List<ServiceLog> select(@RequestBody ServiceLog log);

    @PostMapping(value = "/sys/log/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/log/insert")
    void insert(@RequestBody ServiceLog log);

    @PostMapping(value = "/sys/log/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody ServiceLog log);

    @PostMapping(value = "/sys/log/selectAll")
    List<ServiceLog> selectAll();
}
