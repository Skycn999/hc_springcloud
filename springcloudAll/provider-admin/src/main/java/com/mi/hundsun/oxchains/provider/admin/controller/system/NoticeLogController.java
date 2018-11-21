package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.NoticeLog;
import com.mi.hundsun.oxchains.base.core.service.system.NoticeLogService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通知记录表业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: NoticeLogController
 * @date 2017-08-25 04:45:54
 */
@Slf4j
@RestController
@RequestMapping("/sys/noticeLog")
public class NoticeLogController {

    @Autowired
    NoticeLogService noticeLogService;

    /**
     * 批量删除站内信
     *
     * @param userId
     * @param delIds
     */
    @PostMapping(value = "/deleteNoticeLogs")
    public void deleteNoticeLogs(Integer userId, String delIds) {
        noticeLogService.deleteNoticeLogs(userId, delIds);
    }

    /**
     * 批量修改站内信已读未读状态
     *
     * @param userId
     * @param delIds
     * @param readFlag
     */
    @PostMapping(value = "/updateNoticeLogsToRead")
    public void updateNoticeLogsToRead(Integer userId, String delIds, Integer readFlag) {
        noticeLogService.updateNoticeLogsToRead(userId, delIds, readFlag);
    }


    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public void insert(@RequestBody NoticeLog noticeLog) throws BussinessException {
        noticeLogService.insert(noticeLog);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody NoticeLog noticeLog) throws BussinessException {
        noticeLogService.updateByPrimaryKeySelective(noticeLog);
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        noticeLogService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public void removeById(@RequestBody NoticeLog noticeLog) throws BussinessException {
        noticeLogService.removeById(noticeLog);
    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public NoticeLog getNormalModelById(@RequestBody NoticeLog noticeLog) throws BussinessException {
        return noticeLogService.getNormalModelById(noticeLog);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public NoticeLog selectOne(@RequestBody NoticeLog noticeLog) throws BussinessException {
        return noticeLogService.selectOne(noticeLog);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<NoticeLog> select(@RequestBody NoticeLog noticeLog) throws BussinessException {
        return noticeLogService.select(noticeLog);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<NoticeLog> selectAll() throws BussinessException {
        return noticeLogService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return noticeLogService.getDtGridList(dtGridPager);
    }
}
