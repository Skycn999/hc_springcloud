package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.system.MenuModel;
import com.mi.hundsun.oxchains.base.core.model.system.MenuStateModel;
import com.mi.hundsun.oxchains.base.core.po.system.Menu;
import com.mi.hundsun.oxchains.base.core.service.system.MenuService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统菜单业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: MenuController
 * @date 2017-08-14 07:53:50
 */
@Slf4j
@RestController
@RequestMapping("/sys/menu")
public class MenuController {

    @Autowired
    MenuService menuService;

    /**
     * 根据角色ID 和 父级菜单ID查询菜单树
     *
     * @param roleId   角色ID
     * @param parentId 父级菜单ID
     * @return
     */
    @PostMapping(value = "/findPermiMenuList")
    public List<MenuModel> findPermiMenuList(Integer roleId, Integer parentId) {
        return menuService.findPermiMenuList(roleId, parentId);
    }

    /**
     * 查询后台菜单选中状态
     *
     * @param path
     * @return
     */
    @PostMapping(value = "/getMenuState")
    public MenuStateModel getMenuState(String path) {
        return menuService.getMenuState(path);
    }

    /**
     * 根据ID 删除菜单及其所有子菜单
     *
     * @param id
     */
    @PostMapping(value = "/delMenuInculdeSuns")
    public void delMenuInculdeSuns(Integer id) {
        menuService.delMenuInculdeSuns(id);
    }


    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody Menu menu) throws BussinessException {
        menuService.insert(menu);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Menu menu) throws BussinessException {
        menuService.updateByPrimaryKeySelective(menu);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        menuService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody Menu menu) throws BussinessException {
        menuService.removeById(menu);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public Menu getNormalModelById(@RequestBody Menu menu) throws BussinessException {
        return menuService.getNormalModelById(menu);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public Menu selectOne(@RequestBody Menu menu) throws BussinessException {
        return menuService.selectOne(menu);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Menu> select(@RequestBody Menu menu) throws BussinessException {
        return menuService.select(menu);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<Menu> selectAll() throws BussinessException {
        return menuService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return menuService.getDtGridList(dtGridPager);
    }
}
