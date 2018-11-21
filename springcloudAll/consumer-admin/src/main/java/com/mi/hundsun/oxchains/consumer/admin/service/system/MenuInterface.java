package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.model.system.MenuModel;
import com.mi.hundsun.oxchains.base.core.model.system.MenuStateModel;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 系统菜单业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: MenuInterface
 * @date 2017-08-14 07:53:50
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface MenuInterface {


    /**
     * 根据角色ID 和 父级菜单ID查询菜单树
     *
     * @param roleId   角色ID
     * @param parentId 父级菜单ID
     * @return
     */
    @PostMapping(value = "/sys/menu/findPermiMenuList")
    List<MenuModel> findPermiMenuList(@RequestParam("roleId") Integer roleId, @RequestParam("parentId") Integer parentId);

    /**
     * 查询后台菜单选中状态
     *
     * @param path
     * @return
     */
    @PostMapping(value = "/sys/menu/getMenuState")
    MenuStateModel getMenuState(@RequestParam("path") String path);

    /**
     * 根据ID 删除菜单及其所有子菜单
     *
     * @param id
     */
    @PostMapping(value = "/sys/menu/delMenuInculdeSuns")
    void delMenuInculdeSuns(@RequestParam("id") Integer id);


    @PostMapping(value = "/sys/menu/selectOne")
    Menu selectOne(@RequestBody Menu menu);

    @PostMapping(value = "/sys/menu/select")
    List<Menu> select(@RequestBody Menu menu);

    @PostMapping(value = "/sys/menu/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/menu/insert")
    void insert(@RequestBody Menu menu);

    @PostMapping(value = "/sys/menu/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody Menu menu);

    @PostMapping(value = "/sys/menu/selectAll")
    List<Menu> selectAll();
}
