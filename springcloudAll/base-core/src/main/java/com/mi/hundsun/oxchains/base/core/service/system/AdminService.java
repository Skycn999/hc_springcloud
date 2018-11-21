package com.mi.hundsun.oxchains.base.core.service.system;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

/**
 * 管理员账号业务相关Service接口<br>
 *
 * @ClassName: AdminService
 * @author donfy
 * @date   2017-08-16 08:18:00
 */
public interface AdminService extends GenericService<Integer, Admin> {
    /**
     * 保存后台管理员
     *
     * @param admin
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
//    Admin saveAdmin(Admin admin) throws BussinessException;

//    /**
//     * 修改后台管理员
//     *
//     * @param admin
//     * @return void    返回类型
//     * @throws BussinessException 异常信息
//     */
//    void updateAdmin(Admin admin) throws BussinessException;

    /**
     * 删除后台管理员，实际上做逻辑删除操作
     *
     * @param id 主键
     * @return void    返回类型
     * @throws BussinessException 异常信息
     */
    Admin getAdminById(Integer id) throws BussinessException;
}
