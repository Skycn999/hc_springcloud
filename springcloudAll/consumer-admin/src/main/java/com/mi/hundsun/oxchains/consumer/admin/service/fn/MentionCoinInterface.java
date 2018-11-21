package com.mi.hundsun.oxchains.consumer.admin.service.fn;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface MentionCoinInterface {

    /**
     * 提币管理分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/fn/mentionCoin/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws BussinessException;


    /**
     *  查询提币信息
     * @param mentionCoin
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/fn/mentionCoin/selectOne")
    MentionCoin  selectOne(@RequestBody MentionCoin mentionCoin)throws Exception;

    /**
     *  获取待录入提币
     * @param
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/fn/mentionCoin/getPendEnterList")
    List<MentionCoin>  getPendEnterList()throws Exception;


    /**
     *  提币审核
     * @param mentionCoin
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/fn/mentionCoin/audit")
    ResultEntity audit(@RequestBody MentionCoin mentionCoin)throws Exception;



    /**
     * 录入txid
     * @param mentionCoin
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/fn/mentionCoin/input")
    ResultEntity input(@RequestBody MentionCoin mentionCoin)throws Exception;

}
