package com.mi.hundsun.oxchains.provider.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountModel;
import com.mi.hundsun.oxchains.base.core.model.quote.model.AccountBalance;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccountInfo;
import com.mi.hundsun.oxchains.base.core.service.exchange.MotherAccountInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Api("交易所母账号资产信息服务")
@Slf4j
@RestController
public class MotherAccountInfoController  extends GenericController {
    @Autowired
    MotherAccountInfoService motherAccountInfoServicel;


    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/exchange/motherAccountInfo/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return motherAccountInfoServicel.getDtGridList(dtGridPager, MotherAccountInfoModel.class);
    }

    @ApiOperation(value = "导出excel")
    @PostMapping(value = "/exchange/motherAccountInfo/getDtGridListExport")
    public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
        return motherAccountInfoServicel.getDtGridListExport(dtGridPager,MotherAccountInfoModel.class);
    }


    @ApiOperation(value = "交易所母账号资产同步")
    @PostMapping(value = "/exchange/motherAccountInfo/synchronMotherAccount")
    public ResultEntity synchronMotherAccount(@RequestBody MotherAccountModel motherAccountModel) throws Exception {
       List<AccountBalance> accountBalances = motherAccountModel.getABalances();
        for (AccountBalance accountBalance:accountBalances){
           MotherAccountInfo info =  motherAccountInfoServicel.selectOne(new MotherAccountInfo(m->{
                m.setMotherAccountId(motherAccountModel.getId());
                m.setCoinCurrency(accountBalance.getCurrency_code());
                m.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            BigDecimal eBalance =  new BigDecimal(accountBalance.getEnable_balance());
            BigDecimal fBalance =  new BigDecimal(accountBalance.getFrozen_balance());
            //如果冻结加可用资产小于等于0 那么不做操作
            if((eBalance.add(fBalance)).compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
           if(null == info){
               MotherAccountInfo mInfo = new MotherAccountInfo();
               mInfo.setUuid(RandomUtils.randomCustomUUID());
               mInfo.setAccountNo(motherAccountModel.getAccountNo());
               mInfo.setMotherAccountId(motherAccountModel.getId());
               mInfo.setCoinCurrency(accountBalance.getCurrency_code().toUpperCase());
               mInfo.setExNo(motherAccountModel.getExNo());
               mInfo.setAvailable(eBalance);
               mInfo.setFreeze(fBalance);
               mInfo.setTotal(eBalance.add(fBalance));
               mInfo.setSyncTime(new Date());
               mInfo.setCreateTime(new Date());
               mInfo.setDelFlag(GenericPo.DELFLAG.NO.code);
               motherAccountInfoServicel.insert(mInfo);
           }else {
               info.setAvailable(eBalance);
               info.setFreeze(fBalance);
               info.setTotal(eBalance.add(fBalance));
               info.setSyncTime(new Date());
               motherAccountInfoServicel.updateByPrimaryKeySelective(info);
           }
        }
        return ok();
    }
}
