package com.mi.hundsun.oxchains.consumer.admin.controller.log;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class ServiceLogController extends BaseController {

    /**
     * 操作日志
     * @return
     */
    @RequestMapping("log/list")
    public String list() {
        return ok("log/log/list");
    }
}
