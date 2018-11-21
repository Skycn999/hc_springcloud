package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.CommodityInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class CommodityJsonController extends GenericController<Integer, Commodity> {

    @Autowired
    CommodityInterface commodityInterface;

    @ResponseBody
    @RequestMapping("commodity/json/list")
    @RequiresPermissions("sys:commodity:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return commodityInterface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     *
     * @param commodity
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("commodity/json/add")
    @RequiresPermissions("sys:commodity:add")
    public ResultEntity addJson(Commodity commodity) throws Exception {
        checkCommodity(commodity, "add");
        ResultEntity resultEntity = new ResultEntity();
        Admin admin = AdminSessionHelper.getCurrAdmin();
        commodity.setCreator(admin.getName());
        commodity.setCreateTime(new Date());
        commodity.setUuid(RandomUtils.randomCustomUUID().toString());
        commodityInterface.insert(commodity);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑用户信息
     *
     * @param commodity
     * @return
     */
    @ResponseBody
    @RequestMapping("commodity/json/edit")
    @RequiresPermissions("sys:commodity:edit")
    public ResultEntity editJson(Commodity commodity) throws Exception {
        checkCommodity(commodity, "edit");
        commodityInterface.updateByPrimaryKeySelective(commodity);
        return ok();
    }

    private void checkCommodity(Commodity commodity, String action) {
        if (null == commodity) {
            throw new BussinessException("参数有误");
        }
        if ("edit".equals(action)) {
            if (null == commodity.getId()) {
                throw new BussinessException("参数有误");
            }
        }
        if (StringUtils.isBlank(commodity.getCode())) {
            throw new BussinessException("请输入资产代码");
        } else if (commodity.getCode().length() > 50) {
            throw new BussinessException("资产代码不能大于50个字符");
        }
        //资产代码（重复性检验）
        boolean b =  commodityInterface.checkCode(commodity.getCode());
        if(b){
            throw new BussinessException("该资产代码已存在");
        }
        if (commodity.getCnDesc().length() > 2000) {
            throw new BussinessException("中文描述不能超过2000个字符");
        }
        if (commodity.getEnDesc().length() > 2000) {
            throw new BussinessException("英文描述不能超过2000个字符");
        }
    }
}
