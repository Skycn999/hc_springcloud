package com.mi.hundsun.oxchains.consumer.admin.service.exchange;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("provider-admin-${feignSuffix}")
public interface MotherAccountInterface {

    /**
     * 获取页面RSA公钥
     * @return
     */
    @PostMapping("/exchange/motherAccount/getRsaPublicKey")
    ResultEntity getRsaPublicKey();

    /**
     * 交易所母账号分页查询
     *
     * @param dtGridPager
     * @return
     */
    @PostMapping(value = "/exchange/motherAccount/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager) throws Exception;

    /**
     * 添加交易所母账号
     *
     * @param motherAccount
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/exchange/motherAccount/insert")
    ResultEntity insert(@RequestBody MotherAccount motherAccount) throws Exception;

    /**
     * 更新交易母账号
     *
     * @param motherAccount
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/exchange/motherAccount/update")
    ResultEntity update(@RequestBody MotherAccount motherAccount) throws Exception;

    /**
     * 交易所母账号分页查询
     *
     * @param motherAccount
     * @return
     */
    @PostMapping(value = "/exchange/motherAccount/select")
    List<MotherAccount> select(@RequestBody MotherAccount motherAccount) throws Exception;

}
