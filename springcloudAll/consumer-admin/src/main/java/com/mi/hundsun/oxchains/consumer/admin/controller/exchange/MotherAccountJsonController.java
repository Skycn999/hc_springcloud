package com.mi.hundsun.oxchains.consumer.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.*;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.MotherAccountInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 交易所母账号controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MotherAccountJsonController extends GenericController<Integer, MotherAccount> {
    @Autowired
    MotherAccountInterface motherAccountInterface;

    /**
     * 交易所母账号分页列表
     */
    @ResponseBody
    @RequestMapping("motherAccount/json/list")
    @RequiresPermissions("sys:motherAccount:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        DtGrid dtGrid = motherAccountInterface.getDtGridList(dtGridPager);
        if (dtGrid.getExhibitDatas().size() > 0) {
            for (Object obj : dtGrid.getExhibitDatas()) {
                LinkedHashMap<String,Object> map = (LinkedHashMap<String,Object>) obj;
                //解密
                String accountPwd = ToolAES.decrypt(map.get("accountPwd").toString());
                String googlePrivateKey = ToolAES.decrypt(map.get("googlePrivateKey").toString());
                map.put("accountPwd", accountPwd);
                map.put("googlePrivateKey", googlePrivateKey);
            }
        }
        return dtGrid;
    }

    /**
     * 添加交易所母账号
     */
    @ResponseBody
    @RequestMapping("/motherAccount/json/add")
    @RequiresPermissions("sys:motherAccount:save")
    public ResultEntity addJson(MotherAccount motherAccount) throws Exception {
        String result = checkModel(motherAccount);
        if (result != null) {
            throw new BussinessException(result);
        }
        motherAccount.setUuid(RandomUtils.randomCustomUUID());
        motherAccount.setAccountNo(OrderNoUtils.getSerialNumber());
        if (motherAccount.getState() == MotherAccount.STATE.ENABLE.code) {
            motherAccount.setEnableTime(new Date());
        } else {
            motherAccount.setStopTime(new Date());
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        motherAccount.setCreateTime(new Date());
        motherAccount.setCreator(admin.getName());
        motherAccount.setDelFlag(GenericPo.DELFLAG.NO.code);
        ResultEntity insert = motherAccountInterface.insert(motherAccount);
        if (insert.getCode() == ResultEntity.SUCCESS) {
            return ok("添加成功");
        }
        return fail(insert.getMessage());
    }

    /**
     * 校验
     */
    private String checkModel(MotherAccount motherAccount) {
        if(StringUtils.isBlank(motherAccount.getAccountId())){
            return  "帐号ID(火币网专有)不能为空";
        } else if(motherAccount.getAccountId().length()>50){
            return "帐号ID长度不能超过50位";
        }
        if (StringUtils.isBlank(motherAccount.getAccountName())) {
            return "母账号名称不能为空";
        } else if(motherAccount.getAccountName().length()>50){
            return "母帐号名称长度不能超过50位";
        }
        if (StringUtils.isBlank(motherAccount.getAccountPwd())) {
            return "母账号密码不能为空";
        }
        if (StringUtils.isBlank(motherAccount.getApiKey())) {
            return "key不能为空";
        } else if(motherAccount.getApiKey().length()>500){
            return "key长度不能超过500位";
        }
        if(StringUtils.isBlank(motherAccount.getApiSecret())){
            return "交易账户secret不能为空";
        } else if(motherAccount.getApiSecret().length()>500){
            return "交易账户secret长度不能大于500位";
        }
        if (StringUtils.isBlank(motherAccount.getGooglePrivateKey())) {
            return "Google私钥不能为空";
        }
        if (StringUtils.isBlank(motherAccount.getAccountEmail())) {
            return "邮箱不能为空";
        } else if(!ValidateUtils.isEmail(motherAccount.getAccountEmail())){
            return "邮箱格式不正确";
        }
        if (StringUtils.isBlank(motherAccount.getAccountMobile())) {
            return "手机号码不能为空";
        }else if(!ValidateUtils.isPhone(motherAccount.getAccountMobile())){
            return "手机号码格式不正确";
        }
        if (StringUtils.isBlank(motherAccount.getState())) {
            return "请选择状态";
        }
        return null;
    }

    /**
     * 交易母账号根据交易所id查询
     */
    @ResponseBody
    @RequestMapping("motherAccount/json/listByEx")
    public ResultEntity listByExJson(Integer id) throws Exception {
        MotherAccount motherAccount = new MotherAccount();
        motherAccount.setExId(id);
        motherAccount.setDelFlag(GenericPo.DELFLAG.NO.code);
        List<MotherAccount> accounts = motherAccountInterface.select(motherAccount);
        if (null != accounts && accounts.size() > 0) {
            return ok(accounts);
        }
        return fail();
    }

    /**
     * 更新交易所母账号
     */
    @ResponseBody
    @RequestMapping("/motherAccount/json/update")
    @RequiresPermissions("sys:motherAccount:edit")
    public ResultEntity editJson(MotherAccount motherAccount) throws Exception {
        String result = checkModel(motherAccount);
//        if (result != null) {
//            throw new BussinessException(result);
//        }
        if (motherAccount.getState() == MotherAccount.STATE.ENABLE.code) {
            motherAccount.setEnableTime(new Date());
        } else {
            motherAccount.setStopTime(new Date());
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        motherAccount.setUpdateTime(new Date());
        motherAccount.setUpdator(admin.getName());
        return motherAccountInterface.update(motherAccount);
    }


}
