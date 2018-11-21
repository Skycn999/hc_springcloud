package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.system.Dict;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 字典表业务相关Service接口<br>
 *
 * @ClassName: DictInterface
 * @author donfy
 * @date   2017-08-16 01:59:00
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface DictInterface{


    @PostMapping(value = "/sys/dict/selectOne")
    Dict selectOne(@RequestBody Dict dict);

    @PostMapping(value = "/sys/dict/select")
    List<Dict> select(@RequestBody Dict dict);

    @PostMapping(value = "/sys/dict/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/dict/insert")
    void insert(@RequestBody Dict dict);

    @PostMapping(value = "/sys/dict/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Dict dict);

    @PostMapping(value = "/sys/dict/selectAll")
    List<Dict> selectAll();

    @PostMapping(value = "/sys/dict/removeById")
    void removeById(@RequestBody Dict dict);
}
