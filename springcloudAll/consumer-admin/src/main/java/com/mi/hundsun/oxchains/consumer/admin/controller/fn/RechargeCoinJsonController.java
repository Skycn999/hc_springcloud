package com.mi.hundsun.oxchains.consumer.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.constant.MsgTempNID;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.RechargeCoinInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserInLetterInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 充币管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class RechargeCoinJsonController extends GenericController<Integer, RechargeCoin> {
    @Autowired
    RechargeCoinInterface rechargeCoinInterface;
    @Autowired
    AccountInterface accountInterface;
    @Autowired
    UserInLetterInterface userInLetterInterface;

    /**
     * 分页查询
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("rechargeCoin/json/list")
    @RequiresPermissions("sys:rechargeCoin:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return rechargeCoinInterface.getDtGridList(dtGridPager);
    }

    /**
     * 添加
     *
     * @param rechargeCoin
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("rechargeCoin/json/add")
    @RequiresPermissions("sys:rechargeCoin:add")
    public ResultEntity addJson(RechargeCoin rechargeCoin) throws Exception {
        if (StringUtils.isBlank(rechargeCoin.getPlatCollectAddr())) {
            throw new BussinessException("平台收币地址不能为空");
        } else if (rechargeCoin.getPlatCollectAddr().length() > 60) {
            throw new BussinessException("平台收币地址长度不能超过60位");
        }
        if (null == rechargeCoin.getAmount()) {
            throw new BussinessException("充币数量不能为空");
        } else if (!ValidateUtils.isPrice3(rechargeCoin.getAmount().toString())) {
            throw new BussinessException("充币数量输入错误,可整数可小数，小数前后最大10位");
        }
        if (null == rechargeCoin.getCoinCurrency()) {
            throw new BussinessException("请选择币种");
        }
        if (StringUtils.isBlank(rechargeCoin.getTxId())) {
            throw new BussinessException("区块链流水号不能位空");
        } else if (rechargeCoin.getTxId().length() > 50) {
            throw new BussinessException("区块链流水号长度不能超过50位");
        }
        if (StringUtils.isBlank(rechargeCoin.getUserRechargeAddr())) {
            throw new BussinessException("客户充币地址不能为空");
        } else if (rechargeCoin.getUserRechargeAddr().length() > 60) {
            throw new BussinessException("客户充币地址长度不能超过60位");
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        rechargeCoin.setUuid(RandomUtils.randomCustomUUID());
        rechargeCoin.setOrderNo(OrderNoUtils.getSerialNumber());
        rechargeCoin.setState(RechargeCoin.STATE.PENDING.code);
        rechargeCoin.setDelFlag(GenericPo.DELFLAG.NO.code);
        rechargeCoin.setCreateTime(new Date());
        rechargeCoin.setCreator(admin.getName());
        return rechargeCoinInterface.insert(rechargeCoin);
    }


    /**
     * 充币审核
     *
     * @param rechargeCoin
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("rechargeCoin/json/audit")
    @RequiresPermissions("sys:rechargeCoin:audit")
    public ResultEntity auditJson(RechargeCoin rechargeCoin) throws Exception {
        //TODO 充币审核涉及分布式事务
        RechargeCoin coin = rechargeCoinInterface.selectOne(new RechargeCoin(r -> {
            r.setId(rechargeCoin.getId());
            r.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (coin.getState() != RechargeCoin.STATE.PENDING.code) {
            throw new BussinessException("该记录已审核，请刷新重试");
        }
        Admin admin = AdminSessionHelper.getCurrAdmin();
        rechargeCoin.setCheckTime(new Date());
        rechargeCoin.setUpdator(admin.getName());
        try {
            ResultEntity res1 = rechargeCoinInterface.audit(rechargeCoin);
            if (res1.getCode() != ResultEntity.SUCCESS) {
                return ResultEntity.fail();
            }
            if (rechargeCoin.getState() == RechargeCoin.STATE.PASS.code) {
                Account account = new Account();
                account.setUserId(rechargeCoin.getUserId());
                account.setCoinCode(rechargeCoin.getCoinCurrency());
                account.setTotal(rechargeCoin.getAmount());
                account.setAvailable(rechargeCoin.getAmount());
                account.setUpdateTime(new Date());
                account.setUpdator(admin.getName());
                ResultEntity res2 = accountInterface.updateByRecharg(account);
                if (res2.getCode() != ResultEntity.SUCCESS) {
                    return ResultEntity.fail();
                }
                //发送站内信
                userInLetterInterface.sendLetter2(rechargeCoin.getUserId(), rechargeCoin.getCoinCurrency(), rechargeCoin.getAmount().toString(), MsgTempNID.RECHARGE_COIN_SUCCESS);
            }
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

//    /**
//     * 列表导出
//     *
//     * @param dtGridPager
//     * @return
//     */
//    @ResponseBody
//    @RequestMapping(value = "rechargeCoin/json/export")
//    @RequiresPermissions("sys:rechargeCoin:export")
//    public ResultEntity export(String dtGridPager, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        // 执行导出
//        try {
//            DtGrid dtGrid  = rechargeCoinInterface.getDtGridListExport(dtGridPager);
//            ExportUtils.export(request, response, dtGrid);
//            return ok();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return fail("导出失败");
//        }
//
//    }
}
