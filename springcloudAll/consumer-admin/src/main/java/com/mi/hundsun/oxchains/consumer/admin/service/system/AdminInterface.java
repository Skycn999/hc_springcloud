package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("provider-admin-${feignSuffix}")
public interface AdminInterface {

    /**
     * 保存后台管理员
     *
     * @param admin
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/admin/saveAdmin")
    Admin saveAdmin(@RequestBody Admin admin) throws BussinessException;

    /**
     * 修改后台管理员
     *
     * @param admin
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    @PostMapping(value = "/sys/admin/updateAdmin")
    void updateAdmin(@RequestBody Admin admin) throws BussinessException;

    /**
     * 列表查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/sys/admin/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 更新管理员信息
     *
     * @param admin
     */
    @PostMapping(value = "/sys/admin/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Admin admin) throws BussinessException;

    /**
     * 物理删除管理员信息
     *
     * @param id
     */
    @PostMapping(value = "/sys/admin/deleteByPrimaryKey")
    void deleteByPrimaryKey(@RequestParam("id") int id) throws BussinessException;


    /**
     * 查询管理员
     *
     * @param admin
     * @return
     */
    @PostMapping(value = "/sys/admin/selectOne")
    Admin selectOne(@RequestBody Admin admin);
}
