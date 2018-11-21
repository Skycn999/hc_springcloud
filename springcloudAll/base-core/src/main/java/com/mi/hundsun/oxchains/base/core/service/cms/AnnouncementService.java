package com.mi.hundsun.oxchains.base.core.service.cms;

import com.mi.hundsun.oxchains.base.core.po.cms.Announcement;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;

/**
 * 系统公告业务相关Service接口<br>
 *
 * @ClassName: AnnouncementService
 * @author bin
 * @date   2018-04-23 09:43:28
 */
public interface AnnouncementService extends GenericService<Integer, Announcement> {

    Announcement getNewAnnouncement();

    long announcementListCount(Integer state);

    List<Announcement> getAnnouncementList(Integer state,Integer pageSize,Integer pageNumber);
}
