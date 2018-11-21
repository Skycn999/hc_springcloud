package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 管理员账号业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: ConfigureInterface
 * @date 2017-08-17 04:10:06
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface ConfigureInterface {


    /**
     * 通过配置标识获取配置具体值
     *
     * @param webServerUrl 配置标识
     * @return 配置值
     */
    @PostMapping(value = "/sys/configure/getByNid")
    String getByNid(@RequestParam("webServerUrl") String webServerUrl);

    @PostMapping(value = "/sys/configure/selectOne")
    Configure selectOne(@RequestBody Configure configure);

    @PostMapping(value = "/sys/configure/select")
    List<Configure> select(@RequestBody Configure configure);

    @PostMapping(value = "/sys/configure/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/configure/insert")
    ResultEntity insert(@RequestBody Configure configure);

    @PostMapping(value = "/sys/configure/updateByPrimaryKeySelective")
    ResultEntity updateByPrimaryKeySelective(@RequestBody Configure configure);

    @PostMapping(value = "/sys/configure/selectAll")
    List<Configure> selectAll();

    @PostMapping(value = "/sys/configure/removeById")
    ResultEntity removeById(@RequestBody Configure configure);
}
