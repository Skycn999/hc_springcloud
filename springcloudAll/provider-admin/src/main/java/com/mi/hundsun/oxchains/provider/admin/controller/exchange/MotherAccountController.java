package com.mi.hundsun.oxchains.provider.admin.controller.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.RSAUtils;
import com.mi.hundsun.oxchains.base.common.utils.ToolAES;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountInfoModel;
import com.mi.hundsun.oxchains.base.core.model.exchange.MotherAccountModel;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import com.mi.hundsun.oxchains.base.core.service.exchange.ExchangeService;
import com.mi.hundsun.oxchains.base.core.service.exchange.MotherAccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("交易所母账号服务")
@Slf4j
@RestController
public class MotherAccountController extends GenericController{
    @Autowired
    MotherAccountService motherAccountService;
    @Autowired
    ExchangeService exchangeService;

    @ApiOperation(value = "获取页面RSA公钥")
    @PostMapping(value = "/exchange/motherAccount/getRsaPublicKey")
    public ResultEntity getRsaPublicKey() {
        return ok(RSAUtils.getPublicKeyObj());
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/exchange/motherAccount/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return motherAccountService.getDtGridList(dtGridPager, MotherAccountModel.class);
    }

    @ApiOperation(value = "新增母账号")
    @PostMapping(value = "/exchange/motherAccount/insert")
    public ResultEntity insert(@RequestBody MotherAccount motherAccount) {
        try {
            //使用RSA解密密文得到明文
            String accountPwd = RSAUtils.decrypt(motherAccount.getAccountPwd());

            String googlePrivateKey = RSAUtils.decrypt(motherAccount.getGooglePrivateKey());
            //aes加密
            String accountPwdAes = ToolAES.encrypt(accountPwd);
            String googlePrivateKeyAes = ToolAES.encrypt(googlePrivateKey);
            motherAccount.setAccountPwd(accountPwdAes);
            motherAccount.setGooglePrivateKey(googlePrivateKeyAes);
            Exchange exchange = exchangeService.selectOne(new Exchange(e -> e.setId(motherAccount.getExId())));
            motherAccount.setExNo(exchange.getExNo());
            motherAccountService.insert(motherAccount);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation(value = "更新母账号")
    @PostMapping(value = "/exchange/motherAccount/update")
    public ResultEntity update(@RequestBody MotherAccount motherAccount) {
        try {
            //使用RSA解密密文得到明文
            String accountPwd = RSAUtils.decrypt(motherAccount.getAccountPwd());

            String googlePrivateKey = RSAUtils.decrypt(motherAccount.getGooglePrivateKey());
            //aes加密
            String accountPwdAes = ToolAES.encrypt(accountPwd);
            String googlePrivateKeyAes = ToolAES.encrypt(googlePrivateKey);

            motherAccount.setAccountPwd(accountPwdAes);
            motherAccount.setGooglePrivateKey(googlePrivateKeyAes);
            Exchange exchange = exchangeService.selectOne(new Exchange(e -> e.setId(motherAccount.getExId())));
            motherAccount.setExNo(exchange.getExNo());
            motherAccountService.updateByPrimaryKeySelective(motherAccount);
            return ResultEntity.success();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation(value = "查询列表")
    @PostMapping(value = "/exchange/motherAccount/select")
    public List<MotherAccount> select(@RequestBody MotherAccount motherAccount) {
        return motherAccountService.select(motherAccount);
    }
}
