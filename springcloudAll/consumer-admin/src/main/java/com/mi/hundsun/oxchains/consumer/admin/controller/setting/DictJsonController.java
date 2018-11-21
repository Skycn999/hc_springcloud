package com.mi.hundsun.oxchains.consumer.admin.controller.setting;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.DictInterface;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.constant.State;
import com.mi.hundsun.oxchains.base.core.po.system.Dict;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 数据字典Controller
 *
 * @author liweidong
 * @date 2017年03月15日 22:59
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class DictJsonController extends GenericController<Integer, Dict> {

    @Autowired
    DictInterface dictInterface;

    /**
     * 数据字典列表json数据
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("dict/json/list")
    @RequiresPermissions("sys:dict:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return dictInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加数据字典
     *
     * @param dict
     * @return
     */
    @ResponseBody
    @RequestMapping("dict/json/add")
    @RequiresPermissions("sys:dict:save")
    public ResultEntity addJson(Dict dict) throws Exception {
        dictInterface.insert(dict);
        return ok();
    }

    /**
     * 编辑数据字典
     *
     * @param dict
     * @return
     */
    @ResponseBody
    @RequestMapping("dict/json/edit")
    @RequiresPermissions("sys:dict:edit")
    public ResultEntity editJson(Dict dict) throws Exception {
        dictInterface.updateByPrimaryKeySelective(dict);
        return ok();
    }

    /**
     * 启用数据字典
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("dict/json/open")
    @RequiresPermissions("sys:dict:open")
    public ResultEntity openJson(int id) throws Exception {
        Dict dict = new Dict();
        dict.setId(id);
        dict.setState(Dict.STATE.ENABLE.code);
        dictInterface.updateByPrimaryKeySelective(dict);
        return ok();
    }

    /**
     * 停用数据字典
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("dict/json/close")
    @RequiresPermissions("sys:dict:close")
    public ResultEntity closeJson(int id) throws Exception {
        Dict dict = new Dict();
        dict.setId(id);
        dict.setState(Dict.STATE.DISABLE.code);
        dictInterface.updateByPrimaryKeySelective(dict);
        return ok();
    }

    /**
     * 删除数据字典
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("dict/json/del")
    @RequiresPermissions("sys:dict:del")
    public ResultEntity delJson(int id) throws Exception {
        dictInterface.removeById(new Dict(d -> d.setId(id)));
        return ok();
    }
}
