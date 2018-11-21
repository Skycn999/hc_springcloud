package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.core.po.system.AdminRole;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient("provider-admin-${feignSuffix}")
public interface AdminRoleInterface {

    /**
     * 查询管理员与角色关联信息
     */
    @PostMapping(value = "/sys/adminRole/select")
    List<AdminRole> select(@RequestBody AdminRole admin);
}
