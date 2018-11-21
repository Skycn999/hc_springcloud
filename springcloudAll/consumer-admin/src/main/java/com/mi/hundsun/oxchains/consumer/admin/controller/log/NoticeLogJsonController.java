package com.mi.hundsun.oxchains.consumer.admin.controller.log;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.system.NoticeLogInterface;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.model.log.NoticeLogModel;
import com.mi.hundsun.oxchains.base.core.po.system.NoticeLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class NoticeLogJsonController extends GenericController<Integer,NoticeLog> {
    @Autowired
    private NoticeLogInterface noticeLogInterface;


    /**
     * 通知记录列表json数据
     */
    @ResponseBody
    @RequestMapping("noticeLog/json/list")
    @RequiresPermissions("sys:noticeLog:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        DtGrid dtgrid = dataAuthority(dtGridPager, Constants.AG);
        return noticeLogInterface.getDtGridList(dtgrid,NoticeLogModel.class);
    }

}
