package com.mi.hundsun.oxchains.consumer.admin.controller.mentionCoin;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.constant.MsgTempNID;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.MentionCoinInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserInLetterInterface;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("提币对外服务")
@Slf4j
@RestController
@RequestMapping("/mc")
public class MentionCoinServiceController extends GenericController<Integer, MentionCoin> {
    @Autowired
    MentionCoinInterface mentionCoinInterface;
    @Autowired
    UserInLetterInterface userInLetterInterface;
    @Autowired
    AccountInterface accountInterface;

    /**
     * 获取待录入提币
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/getPendEnterList")
    public String getPendEnterList() throws Exception {
        List<MentionCoin> list =  mentionCoinInterface.getPendEnterList();
        if(list!=null){
            return  JSON.toJSONString(list);
        }
        return "";
    }

    /**
     * 转账成功 录入txid
     *
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/mentionCoinResult")
    public void mentionCoinResult(@RequestParam("payAddr") String payAddr,@RequestParam("uuid") String uuid,@RequestParam("txid") String txid) throws Exception {
        MentionCoin coin = mentionCoinInterface.selectOne(new MentionCoin(c->{
            c.setUuid(uuid);
            c.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (null == coin){
            throw  new BussinessException("提币记录不存在");
        }
        if (coin.getState() != MentionCoin.STATE.PEND_ENTER.code) {
            throw new BussinessException("该记录已不在待录入状态！");
        }
        coin.setPlatPayAddr(payAddr);
        coin.setTxId(txid);
        ResultEntity resultEntity = mentionCoinInterface.input(coin);
        if(resultEntity.getCode() == ResultEntity.SUCCESS ){
            accountInterface.mentionCoinSuccess(coin);
            //发送站内信
            userInLetterInterface.sendLetter2(coin.getUserId(),coin.getCoinCurrency(),coin.getAmount().toString(), MsgTempNID.MENTION_COIN_SUCCESS);
        }
    }
}
