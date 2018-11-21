package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.system.MsgTemplate;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 消息模板表业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: MsgTemplateInterface
 * @date 2017-08-16 01:52:00
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface MsgTemplateInterface {
    @PostMapping(value = "/sys/messageTemplate/selectOne")
    MsgTemplate selectOne(@RequestBody MsgTemplate messageTemplate);

    @PostMapping(value = "/sys/messageTemplate/select")
    List<MsgTemplate> select(@RequestBody MsgTemplate messageTemplate);

    @PostMapping(value = "/sys/messageTemplate/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/messageTemplate/insert")
    void insert(@RequestBody MsgTemplate messageTemplate);

    @PostMapping(value = "/sys/messageTemplate/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody MsgTemplate messageTemplate);

    @PostMapping(value = "/sys/messageTemplate/selectAll")
    List<MsgTemplate> selectAll();

    @PostMapping(value = "/sys/messageTemplate/removeById")
    void removeById(@RequestBody MsgTemplate messageTemplate);
}
