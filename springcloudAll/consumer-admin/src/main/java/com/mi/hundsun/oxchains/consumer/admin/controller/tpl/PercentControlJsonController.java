package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.PercentControlInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class PercentControlJsonController extends GenericController<Integer, PercentControl> {

    @Resource
    PercentControlInterface percentControlInterface;

    @ResponseBody
    @RequestMapping("percentControl/json/list")
    @RequiresPermissions("sys:percent:control:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return percentControlInterface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     *
     * @param percentControl
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("percent/json/add")
    @RequiresPermissions("sys:percent:add")
    public ResultEntity addJson(PercentControl percentControl) throws Exception {
        checkPercentControl(percentControl, "add");
        ResultEntity resultEntity = new ResultEntity();
        Admin admin = AdminSessionHelper.getCurrAdmin();
        percentControl.setTplNo(OrderNoUtils.getSerialNumber());
        percentControl.setCreator(admin.getName());
        percentControl.setCreateTime(new Date());
        percentControl.setUuid(RandomUtils.randomCustomUUID().toString());
        percentControlInterface.insert(percentControl);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑用户信息
     *
     * @param percentControl
     * @return
     */
    @ResponseBody
    @RequestMapping("percent/json/edit")
    @RequiresPermissions("sys:percent:edit")
    public ResultEntity editJson(PercentControl percentControl) throws Exception {
        checkPercentControl(percentControl, "edit");
        percentControlInterface.updateByPrimaryKeySelective(percentControl);
        return ok();
    }

    private void checkPercentControl(PercentControl percentControl, String action) {
        if (null == percentControl) {
            throw new BussinessException("参数有误");
        }
        if ("edit".equals(action)) {
            if (null == percentControl.getId()) {
                throw new BussinessException("参数有误");
            }
        }
        if (StringUtils.isBlank(percentControl.getTplName())) {
            throw new BussinessException("请输入模版名称");
        } else if (percentControl.getTplName().length()> 200) {
            throw new BussinessException("输入字符长度不能大于200位");
        }
        if (null == percentControl.getPercent()) {
            throw new BussinessException("请输入百分比");
        }
        else if (!ValidateUtils.isPrice2(percentControl.getPercent().toString())) {
            throw new BussinessException("百分比格式不正确");
        }
        else if(percentControl.getPercent()>1 || percentControl.getPercent() <=0){
            throw new BussinessException("百分比应该在0到1之间");
        }

        if (null == percentControl.getState()) {
            throw new BussinessException("请选择状态");
        }
        if (percentControl.getState() != PercentControl.STATE.DISABLE.code && percentControl.getState() != PercentControl.STATE.ENABLE.code) {
            throw new BussinessException("状态不正确");
        }
//        if (null == percentControl.getIsDefault()) {
//            throw new BussinessException("请选择是否默认");
//        } else if (percentControl.getIsDefault() != PercentControl.ISDEFAULT.NO.code && percentControl.getIsDefault() != PercentControl.ISDEFAULT.YES.code) {
//            throw new BussinessException("是否默认状态不正确");
//        }
    }
}
