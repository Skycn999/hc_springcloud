package com.mi.hundsun.oxchains.consumer.admin.controller.rechargeCoin;


import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.constant.MsgTempNID;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.PlatUserAddressInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.RechargeCoinInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserInLetterInterface;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

@Api("充币对外服务")
@Slf4j
@RestController
@RequestMapping("/rc")
public class RechargeCoinServiceController extends GenericController<Integer, RechargeCoin> {

    @Autowired
    RechargeCoinInterface rechargeCoinInterface;
    @Autowired
    PlatUserAddressInterface platUserAddressInterface;
    @Autowired
    AccountInterface accountInterface;
    @Autowired
    UserInLetterInterface userInLetterInterface;

    @ResponseBody
    @RequestMapping("/addRechargeCoin")
    public void mentionCoinResult(@RequestParam("collectAddr") String collectAddr, @RequestParam("currency") String currency,@RequestParam("amount") String amount,
                                  @RequestParam("rechargeAddr") String rechargeAddr, @RequestParam("txid") String txid) throws Exception {
        if(StringUtils.isBlank(collectAddr)||StringUtils.isBlank(currency)||StringUtils.isBlank(amount)||StringUtils.isBlank(rechargeAddr) ||StringUtils.isBlank(txid)){
            throw new BussinessException("参数错误");
        }
        PlatUserAddress userAddress  =  platUserAddressInterface.selectOne(new PlatUserAddress(a->{
            a.setAddress(rechargeAddr);
            a.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if(null== userAddress){
            throw new BussinessException("用户充币地址不存在");
        }
        if(StringUtils.isBlank(userAddress.getUserId())){
            throw new BussinessException("充币地址未分配给用户");
        }
        //TODO 根据txid请求并返回数据做校验
        boolean b = false;//校验是否通过

        RechargeCoin coin = new RechargeCoin();
        coin.setUserId(userAddress.getUserId());
        coin.setUuid(RandomUtils.randomCustomUUID());
        coin.setOrderNo(OrderNoUtils.getSerialNumber());
        coin.setAmount(new BigDecimal(amount));
        coin.setCoinCurrency(currency);
        coin.setTxId(txid);
        coin.setPlatCollectAddr(collectAddr);
        coin.setUserRechargeAddr(rechargeAddr);
        coin.setState(RechargeCoin.STATE.PENDING.code);
        coin.setCreateTime(new Date());
        ResultEntity resultEntity = rechargeCoinInterface.insert(coin);

        if(resultEntity.getCode() == ResultEntity.SUCCESS && b){
            coin.setState(RechargeCoin.STATE.PASS.code);
            coin.setCheckTime(new Date());
            rechargeCoinInterface.audit(coin);
            Account account = new Account();
            account.setUserId(coin.getUserId());
            account.setCoinCode(coin.getCoinCurrency());
            account.setTotal(coin.getAmount());
            account.setAvailable(coin.getAmount());
            account.setUpdateTime(new Date());
            ResultEntity res2 = accountInterface.updateByRecharg(account);
            //发送站内信
            userInLetterInterface.sendLetter2(coin.getUserId(), coin.getCoinCurrency(), coin.getAmount().toString(), MsgTempNID.RECHARGE_COIN_SUCCESS);
        }else {
            coin.setState(RechargeCoin.STATE.NOPASS.code);
            coin.setCheckTime(new Date());
            rechargeCoinInterface.audit(coin);
        }
    }


}
