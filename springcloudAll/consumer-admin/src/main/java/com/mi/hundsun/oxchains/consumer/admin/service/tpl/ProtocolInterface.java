package com.mi.hundsun.oxchains.consumer.admin.service.tpl;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface ProtocolInterface {

    /**
     * 列表查询
     *
     * @return
     */
    @PostMapping(value = "/tpl/protocol/select")
    List<Protocol> select(Protocol protocol) throws BussinessException;

    /**
     * 用户列表查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/tpl/protocol/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 查询用户
     *
     * @param protocol
     * @return
     */
    @PostMapping(value = "/tpl/protocol/selectOne")
    Protocol selectOne(@RequestBody Protocol protocol);

    /**
     * 插入
     * @param protocol
     */
    @PostMapping(value = "/tpl/protocol/insert")
    void insert(@RequestBody Protocol protocol);

    /**
     * 编辑用户
     *
     * @param protocol
     * @return
     */
    @PostMapping(value = "/tpl/protocol/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Protocol protocol);
}
