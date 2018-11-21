package com.mi.hundsun.oxchains.consumer.admin.service.user;

import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-user-${feignSuffix}")
public interface UserInLetterInterface {

    /**
     * 发送站内信
     */
    @PostMapping(value = "/prod/registLogin/sendLetter2")
    void sendLetter2(@RequestParam("userId") Integer userId,@RequestParam("currency") String currency,@RequestParam("num") String num, @RequestParam("nid") String nid) throws BussinessException;
}
