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
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.NetWorthControlInterface;
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
public class NetWorthControlJsonController extends GenericController<Integer, NetWorthControl> {

    @Resource
    NetWorthControlInterface netWorthControlInterface;

    @ResponseBody
    @RequestMapping("netWorthControl/json/list")
    @RequiresPermissions("sys:netWorthControl:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return netWorthControlInterface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     *
     * @param netWorthControl
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("netWorthControl/json/add")
    @RequiresPermissions("sys:netWorthControl:add")
    public ResultEntity addJson(NetWorthControl netWorthControl) throws Exception {
        checkNetWorthControl(netWorthControl, "add");
        ResultEntity resultEntity = new ResultEntity();
        Admin admin = AdminSessionHelper.getCurrAdmin();
        netWorthControl.setTplNo(OrderNoUtils.getSerialNumber());
        netWorthControl.setCreator(admin.getName());
        netWorthControl.setCreateTime(new Date());
        netWorthControl.setUuid(RandomUtils.randomCustomUUID().toString());
        netWorthControlInterface.insertNetWorthControl(netWorthControl);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑用户信息
     *
     * @param netWorthControl
     * @return
     */
    @ResponseBody
    @RequestMapping("netWorthControl/json/edit")
    @RequiresPermissions("sys:netWorthControl:edit")
    public ResultEntity editJson(NetWorthControl netWorthControl) throws Exception {
        checkNetWorthControl(netWorthControl, "edit");
        netWorthControlInterface.updateNetWorthControl(netWorthControl);
        return ok();
    }

    private void checkNetWorthControl(NetWorthControl netWorthControl, String action) {
        if (null == netWorthControl) {
            throw new BussinessException("参数有误");
        }
        if ("edit".equals(action)) {
            if (null == netWorthControl.getId()) {
                throw new BussinessException("参数有误");
            }
        }
        if (null == netWorthControl.getAllowMinAmount()) {
            throw new BussinessException("请输入允许交易最小净值");
        }
//        else if(netWorthControl.getAllowMinAmount().compareTo(BigDecimal.ONE)>0 || netWorthControl.getAllowMinAmount().compareTo(BigDecimal.ZERO) <=0){
//            throw new BussinessException("交易最小净值应该在0到1之间");
//        }
        else if (!ValidateUtils.isPrice3(netWorthControl.getAllowMinAmount().toString())) {
            throw new BussinessException("交易最小净值格式不正确,可整数可小数,小数点前后最大可为10位");
        }

        //        else if (netWorthControl.getAllowMinAmount().toString().length()> 10) {
//            throw new BussinessException("输入小数长度不能大于10位");
//        }
        if (StringUtils.isBlank(netWorthControl.getTplName())) {
            throw new BussinessException("请输入风控模版名称");
        } else if (netWorthControl.getTplName().length()> 50) {
            throw new BussinessException("输入名称长度不能大于50位");
        }
        if (null == netWorthControl.getState()) {
            throw new BussinessException("请选择状态");
        }
        if (netWorthControl.getState() != NetWorthControl.STATE.DISABLE.code && netWorthControl.getState() != NetWorthControl.STATE.ENABLE.code) {
            throw new BussinessException("状态不正确");
        }
        if (null == netWorthControl.getIsDefault()) {
            throw new BussinessException("请选择是否默认");
        } else if (netWorthControl.getIsDefault() != NetWorthControl.ISDEFAULT.NO.code && netWorthControl.getIsDefault() != NetWorthControl.ISDEFAULT.YES.code) {
            throw new BussinessException("是否默认状态不正确");
        }
    }
}
