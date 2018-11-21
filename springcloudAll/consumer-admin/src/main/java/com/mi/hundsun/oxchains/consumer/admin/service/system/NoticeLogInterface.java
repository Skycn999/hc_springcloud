package com.mi.hundsun.oxchains.consumer.admin.service.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.model.log.NoticeLogModel;
import com.mi.hundsun.oxchains.base.core.po.system.NoticeLog;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 通知记录表业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: NoticeLogInterface
 * @date 2017-08-25 04:45:54
 */
@FeignClient("provider-admin-${feignSuffix}")
public interface NoticeLogInterface{
    /**
     * 批量删除站内信
     *
     * @param userId
     * @param delIds
     */
    @PostMapping(value = "/sys/noticelog/deleteNoticeLogs")
    void deleteNoticeLogs(@RequestParam("userId") Integer userId, @RequestParam("delIds") String delIds);

    /**
     * 批量修改站内信已读未读状态
     *
     * @param userId
     * @param delIds
     * @param readFlag
     */
    @PostMapping(value = "/sys/noticelog/updateNoticeLogsToRead")
    void updateNoticeLogsToRead(@RequestParam("userId") Integer userId, @RequestParam("delIds") String delIds, @RequestParam("readFlag") Integer readFlag);


    @PostMapping(value = "/sys/noticeLog/selectOne")
    NoticeLog selectOne(@RequestBody NoticeLog noticeLog);

    @PostMapping(value = "/sys/noticeLog/select")
    List<NoticeLog> select(@RequestBody NoticeLog noticeLog);

//    @PostMapping(value = "/sys/noticeLog/getDtGridList")
//    DtGrid getDtGridList(@RequestParam("dtGridPager") String dtGridPager);

    @PostMapping(value = "/sys/noticeLog/insert")
    void insert(@RequestBody NoticeLog noticeLog);

    @PostMapping(value = "/sys/noticeLog/updateByPrimaryKeySelective")
    void updateByPrimaryKeySelective(@RequestBody NoticeLog noticeLog);

    @PostMapping(value = "/sys/noticeLog/selectAll")
    List<NoticeLog> selectAll();

    @PostMapping(value = "/sys/noticeLog/getDtGridList")
    DtGrid getDtGridList(@RequestParam("dtgrid") DtGrid dtgrid, @RequestParam("clazz") Class<NoticeLogModel> clazz);
}
