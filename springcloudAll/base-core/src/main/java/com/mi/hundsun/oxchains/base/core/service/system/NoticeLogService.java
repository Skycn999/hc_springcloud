package com.mi.hundsun.oxchains.base.core.service.system;

import com.mi.hundsun.oxchains.base.core.po.system.NoticeLog;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

/**
 * 通知记录表业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: NoticeLogService
 * @date 2017-08-25 04:45:54
 */
public interface NoticeLogService extends GenericService<Integer, NoticeLog> {
    /**
     * 批量删除站内信
     *
     * @param userId
     * @param delIds
     */
    void deleteNoticeLogs(Integer userId, String delIds);

    /**
     * 批量修改站内信已读未读状态
     *
     * @param userId
     * @param delIds
     * @param readFlag
     */
    void updateNoticeLogsToRead(Integer userId, String delIds, Integer readFlag);
}
