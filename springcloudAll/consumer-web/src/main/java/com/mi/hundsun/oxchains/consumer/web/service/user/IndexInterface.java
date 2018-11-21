package com.mi.hundsun.oxchains.consumer.web.service.user;


import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider-user-${feignSuffix}")
public interface IndexInterface {

    @PostMapping("/prod/index/bannerList")
    ResultEntity bannerList();

    @PostMapping("/prod/index/getRealPath")
    String getRealPath(@RequestParam("picUuid") String picUuid);

    @PostMapping("/prod/index/getNewAnnouncement")
    ResultEntity getNewAnnouncement();

    @PostMapping("/prod/index/announcementDetail")
    ResultEntity announcementDetail(@RequestParam("ancUuid") String ancUuid);

    @PostMapping("/prod/index/announcementList")
    ResultEntity  announcementList(@RequestParam("pageSize") Integer pageSize,@RequestParam("pageNumber") Integer pageNumber);
}
