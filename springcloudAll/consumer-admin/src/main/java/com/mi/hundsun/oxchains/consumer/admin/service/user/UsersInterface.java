package com.mi.hundsun.oxchains.consumer.admin.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.user.UserFormModel;
import com.mi.hundsun.oxchains.base.core.model.user.UsersModel;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface UsersInterface {

    /**
     * 用户列表分页查询
     */
    @PostMapping(value = "/user/users/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;

    /**
     * 查询用户
     */
    @PostMapping(value = "/user/users/selectOne")
    Users selectOne(@RequestBody Users users);

    /**
     * 查询用户
     */
    @PostMapping(value = "/user/users/select")
    List<Users> select(@RequestBody Users users);

    /**
     * 根据参数快速查询用户
     */
    @PostMapping(value = "/user/users/fastFindUserByParam")
    List<Users> fastFindUserByParam(@RequestParam("param")String param);

    /**
     * 查询用户
     */
    @PostMapping(value = "/user/users/selectByPrimaryKey")
    Users selectByPrimaryKey(@RequestBody Integer userId);

    /**
     * 编辑用户
     */
    @PostMapping(value = "/user/users/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Users users);

    /**
     * 后台编辑用户
     */
    @PostMapping(value = "/user/users/updateUser")
    ResultEntity updateUser(@RequestBody Users users);

    /**
     * 表中数据量小重复性校验
     */
    @PostMapping(value ="/user/users/updateByIdAndVersionSelective")
    void updateByIdAndVersionSelective(@RequestBody Users users);

    /**
     * 表数据量大重复性校验
     */
    @PostMapping(value ="/user/users/modelRepeatCheck")
    boolean modelRepeatCheck(@RequestBody UsersModel users);

    /**
     * 获取页面RSA公钥
     */
    @PostMapping("/user/users/getRsaPublicKey")
    ResultEntity getRsaPublicKey();

    /**
     * 导出excel
     */
    @PostMapping(value = "/user/users/getDtGridListExport")
    DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws BussinessException;

    /**
     * 平台用户统计
     */
    @PostMapping(value = "/user/users/userForm")
    UserFormModel userSum(@RequestBody UserFormModel model);

    @PostMapping(value = "/user/users/countByMgrIndex")
    UserFormModel countByMgrIndex();

}

