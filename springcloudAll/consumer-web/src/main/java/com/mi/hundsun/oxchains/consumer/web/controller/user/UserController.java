/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.consumer.web.controller.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.consumer.web.config.WebGenericController;
import com.mi.hundsun.oxchains.consumer.web.service.user.UserInterface;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 枫亭
 * @description 我的消息
 * @date 2018-04-13 19:46.
 */
@RestController
@RequestMapping("/api/web/user")
public class UserController extends WebGenericController {

    @Autowired
    UserInterface userInterface;

    @ApiOperation(value = "我的消息", notes = "查询用户的消息-包含系统发送的公告信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "pageSize", value = "每页条数", required = true, dataType = "Integer")
            , @ApiImplicitParam(name = "pageNumber", value = "页码", required = true, dataType = "Integer")
    })
    @RequestMapping("/myMsg")
    public ResultEntity myMsg(@RequestParam Integer pageSize, @RequestParam Integer pageNumber) {
        if (null == pageNumber || null == pageSize) {
            throw new BussinessException("分页参数错误");
        }
        ResultEntity myMsg = userInterface.getMyMsg(getLoginUserId(), pageSize, pageNumber);
        if (myMsg.getCode() == ResultEntity.SUCCESS) {
            return ok(myMsg.getData());
        }
        return fail("获取失败");
    }


    @ApiOperation(value = "清空消息", notes = "清空用户的消息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uuid", value = "用户的uuid", required = true, dataType = "String")
            , @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true, dataType = "String")
            , @ApiImplicitParam(name = "idStr", value = "选择中idStr", required = true, dataType = "String")
    })
    @RequestMapping("/clearMyMsg")
    public ResultEntity clearMyMsg(@RequestParam String idStr) {
        if (StringUtils.isBlank(idStr)) {
            fail("选中参数有误");
        }
        List<Integer> ids = new ArrayList<>();
        String[] arr = idStr.split(",");
        for (String anArr : arr) {
            ids.add(Integer.valueOf(anArr));
        }
        return userInterface.clearMyMsg(getLoginUserId(), ids);
    }


}
