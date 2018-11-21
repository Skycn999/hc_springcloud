package com.mi.hundsun.oxchains.consumer.admin.controller.setting;

import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.MsgTemplateInterface;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.po.system.MsgTemplate;
import com.mi.hundsun.oxchains.consumer.admin.service.RedisService;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 消息模板Controller
 *
 * @author liweidong
 * @date 2017年03月15日 22:59
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MsgTemplateJsonController extends GenericController<Integer, MsgTemplate> {

    @Autowired
    MsgTemplateInterface msgTemplateInterface;
    @Resource
    RedisService redisService;

    /**
     * 消息模板列表json数据
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping("/msgTemplate/json/list")
    @RequiresPermissions("sys:msgTemplate:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return msgTemplateInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加消息模板
     *
     * @param template
     * @return
     */
    @ResponseBody
    @RequestMapping("/msgTemplate/json/add")
    @RequiresPermissions("sys:msgTemplate:save")
    public ResultEntity addJson(MsgTemplate template) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        Admin admin = AdminSessionHelper.getCurrAdmin();
        template.setUuid(RandomUtils.randomCustomUUID());
        template.setCreateTime(new Date());
        template.setCreator(admin.getName());
        msgTemplateInterface.insert(template);
        redisService.put(CacheID.TEMPLATE_MESSAGE_PREFIX + template.getNid(), template);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑消息模板
     *
     * @param template
     * @return
     */
    @ResponseBody
    @RequestMapping("/msgTemplate/json/edit")
    @RequiresPermissions("sys:msgTemplate:edit")
    public ResultEntity editLetterJson(MsgTemplate template) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        msgTemplateInterface.updateByPrimaryKeySelective(template);
        redisService.put(CacheID.TEMPLATE_MESSAGE_PREFIX + template.getNid(), template);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 删除消息模板
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/msgTemplate/json/del")
    @RequiresPermissions("sys:msgTemplate:del")
    public ResultEntity delJson(int id) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        msgTemplateInterface.removeById(new MsgTemplate(m -> m.setId(id)));
        MsgTemplate template = msgTemplateInterface.selectOne(new MsgTemplate(m -> {
            m.setId(id);
            m.setDelFlag(MsgTemplate.DELFLAG.NO.code);
        }));
        if (template != null) {
            redisService.del(CacheID.TEMPLATE_MESSAGE_PREFIX + template.getNid());
        }
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }
}
