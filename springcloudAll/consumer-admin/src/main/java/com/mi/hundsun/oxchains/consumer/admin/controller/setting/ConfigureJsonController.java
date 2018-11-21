package com.mi.hundsun.oxchains.consumer.admin.controller.setting;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.ConfigureInterface;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.State;
import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import com.mi.hundsun.oxchains.consumer.admin.service.RedisService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 后台参数配置
 *
 * @author liweidong
 * @date 2017年03月10日 15:28
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class ConfigureJsonController extends GenericController<Integer, Configure> {

    @Resource
    ConfigureInterface configureInterface;
    @Resource
    RedisService redisService;

    /**
     * 参数配置列表json数据
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("/config/json/list")
    @RequiresPermissions("sys:config:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return configureInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加参数配置
     *
     * @param configure
     * @return
     */
    @ResponseBody
    @RequestMapping("/config/json/add")
    @RequiresPermissions("sys:config:save")
    public ResultEntity addJson(Configure configure) throws Exception {
        ResultEntity resultEntity =  configureInterface.insert(configure);
        return resultEntity;
    }

    /**
     * 编辑参数配置
     *
     * @param configure
     * @return
     */
    @ResponseBody
    @RequestMapping("/config/json/edit")
    @RequiresPermissions("sys:config:edit")
    public ResultEntity editJson(Configure configure) throws Exception {
        ResultEntity resultEntity = configureInterface.updateByPrimaryKeySelective(configure);
        return resultEntity;
    }

    /**
     * 删除参数配置
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/config/json/del")
    @RequiresPermissions("sys:config:del")
    public ResultEntity delJson(int id) throws Exception {
        ResultEntity resultEntity =  configureInterface.removeById(new Configure(c -> c.setId(id)));
        return resultEntity;
    }

}