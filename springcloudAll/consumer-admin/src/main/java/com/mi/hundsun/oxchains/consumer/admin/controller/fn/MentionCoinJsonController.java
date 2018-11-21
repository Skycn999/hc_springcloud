package com.mi.hundsun.oxchains.consumer.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.MentionCoinInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 提币管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MentionCoinJsonController extends GenericController<Integer, MentionCoin> {
    @Autowired
    MentionCoinInterface mentionCoinInterface;
    @Autowired
    AccountInterface accountInterface;



    /**
     * 分页查询
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("mentionCoin/json/list")
    @RequiresPermissions("sys:mentionCoin:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return mentionCoinInterface.getDtGridList(dtGridPager);

    }

    /**
     * 提币审核
     *
     * @param mentionCoin
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("mentionCoin/json/audit")
    @RequiresPermissions("sys:mentionCoin:audit")
    public ResultEntity auditJson(MentionCoin mentionCoin) throws Exception {
        try {
            Admin admin = AdminSessionHelper.getCurrAdmin();
            mentionCoin.setConfirmor(admin.getName());
            mentionCoin.setConfirmTime(new Date());
            mentionCoin.setUpdateTime(new Date());
            mentionCoin.setUpdator(admin.getName());
            ResultEntity res = mentionCoinInterface.audit(mentionCoin);
            if (res.getCode() != ResultEntity.SUCCESS) {
                return res;
            }
            //更新用户资产，审核通过，状态为待录入，审核不通过，状态为不通过，解冻资产
            if(mentionCoin.getState() == MentionCoin.STATE.NO_PASS.code){
                accountInterface.mentionCoinSuccess(mentionCoin);
            }
            return ok();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }
}
