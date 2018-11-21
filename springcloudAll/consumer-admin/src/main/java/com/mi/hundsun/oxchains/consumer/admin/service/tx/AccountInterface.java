package com.mi.hundsun.oxchains.consumer.admin.service.tx;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-trade-${feignSuffix}")
public interface AccountInterface {

    /**
     * 用户资产持仓分页查询
     *
     * @param dtGrid
     * @return
     */
    @PostMapping(value = "/tx/account/getDtGridList")
    DtGrid getDtGridList(@RequestBody DtGrid dtGrid) throws Exception;

    /**
     * 查询用户资产持仓
     *
     * @param account
     * @return
     */
    @PostMapping(value = "/tx/account/selectOne")
    Account selectOne(@RequestBody Account account);

    /**
     * 充币更新用户资产持仓
     *
     * @param account
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/tx/account/updateByRecharge")
    ResultEntity updateByRecharg(@RequestBody Account account) throws Exception;


    /**
     * 提币不通过解冻资产
     * @param mentionCoin
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/tx/account/mentionCoinNoPass")
    ResultEntity mentionCoinNoPass(@RequestBody MentionCoin mentionCoin) throws Exception;


    /**
     * 提币录入成功扣除资产
     * @param mentionCoin
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/tx/account/mentionCoinSuccess")
    ResultEntity mentionCoinSuccess(@RequestBody MentionCoin mentionCoin) throws Exception;

}
