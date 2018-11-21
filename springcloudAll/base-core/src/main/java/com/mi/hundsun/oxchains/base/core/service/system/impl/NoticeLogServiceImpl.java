package com.mi.hundsun.oxchains.base.core.service.system.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.system.NoticeLogMapper;
import com.mi.hundsun.oxchains.base.core.po.system.NoticeLog;
import com.mi.hundsun.oxchains.base.core.service.system.NoticeLogService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 通知记录表业务相关Service接口实现<br>
 *
 * @ClassName: NoticeLogServiceImpl
 * @author donfy
 * @date   2017-08-25 04:45:54
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class NoticeLogServiceImpl implements NoticeLogService {

	@Resource
    private NoticeLogMapper noticeLogMapper;

    @Override
    public GenericMapper<NoticeLog,Integer> _getMapper() {
        return noticeLogMapper;
    }

    @Override
    public void deleteNoticeLogs(Integer userId, String delIds) {
        noticeLogMapper.deleteNoticeLogs(userId, delIds);
    }

    @Override
    public void updateNoticeLogsToRead(Integer userId, String delIds, Integer readFlag) {
        noticeLogMapper.updateNoticeLogsToRead(userId, delIds, readFlag);
    }
}
