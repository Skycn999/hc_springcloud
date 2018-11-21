package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.RSAUtils;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UsersInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 用户管理
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UsersController extends BaseController {

    @Autowired
    UsersInterface usersInterface;

    /**
     * 用户列表
     * @param modelMap
     * @return
     */
    @RequestMapping("users/list")
    public String list(ModelMap modelMap) throws Exception{
        ResultEntity resultEntity = usersInterface.getRsaPublicKey();
        if(resultEntity.getCode() == ResultEntity.SUCCESS){
            modelMap.put("key",resultEntity.getData().toString());
        }
        return ok("user/users/list");
    }

    /**
     * 平台用户统计
     *
     */
    @RequestMapping("user/userForm")
    public String userForm(ModelMap modelMap) throws Exception{
        return ok("count/userForm");
    }
}
