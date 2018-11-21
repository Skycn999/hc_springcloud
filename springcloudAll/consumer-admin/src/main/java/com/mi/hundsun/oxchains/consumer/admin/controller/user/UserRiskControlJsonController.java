package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserRiskControlInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户风控设置
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserRiskControlJsonController extends GenericController<Integer, UserRiskControl> {
    @Autowired
    UserRiskControlInterface userRiskControlInterface;

    /**
     * 风控设置分页列表
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("riskControl/json/list")
    @RequiresPermissions("sys:riskControl:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return userRiskControlInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加用户风控配置
     *
     * @param riskControl
     * @return
     */
    @ResponseBody
    @RequestMapping("/riskControl/json/add")
    @RequiresPermissions("sys:riskControl:add")
    public ResultEntity addJson(UserRiskControl riskControl) throws Exception {
        String result = checkModel(riskControl,"add");
        if(null != result){
            throw new BussinessException(result);
        }
        return userRiskControlInterface.insert(riskControl);
    }

    /**
     * 设置模板
     *
     * @param riskControl
     * @return
     */
    @ResponseBody
    @RequestMapping("/riskControl/json/setting")
    @RequiresPermissions("sys:riskControl:setting")
    public ResultEntity editJson(UserRiskControl riskControl) throws Exception {
        String result = checkModel(riskControl,"set");
        if(null != result){
            throw new BussinessException(result);
        }
        return userRiskControlInterface.updateSettingTpl(riskControl);
    }

    /**
     * 校验
     * @param riskControl
     * @return
     */
    private String  checkModel(UserRiskControl riskControl,String type){
        String result = null;
        if(type.equals("add")&&StringUtils.isBlank(riskControl.getUserId())){
            result = "请选择用户";
        }else if(StringUtils.isBlank(riskControl.getIsEnableRiskControl())){
            result = "百分比风控状态不能为空";
        }else if(riskControl.getIsEnableRiskControl().intValue()==UserRiskControl.ISENABLERISKCONTROL.YES.code &&
                StringUtils.isBlank(riskControl.getPercentTpl())){
            result = "百分比风控模板不能为空";
        }else if(riskControl.getIsEnableRiskControl().intValue()==UserRiskControl.ISENABLERISKCONTROL.YES.code &&
                StringUtils.isBlank(riskControl.getPercentInitialBalance())){
            result = "百分比风控期初值不能为空";
        }
        return  result;
    }

}
