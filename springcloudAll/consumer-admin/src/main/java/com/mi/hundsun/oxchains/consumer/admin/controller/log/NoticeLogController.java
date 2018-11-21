package com.mi.hundsun.oxchains.consumer.admin.controller.log;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping(BaseController.BASE_URI)
public class NoticeLogController extends BaseController {

    /**
     * 通知记录
     * @return
     */
    @RequestMapping("noticeLog/list")
    public String list() {
        return ok("log/noticeLog/list");
    }


}
