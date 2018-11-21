package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.export.ExportUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ToolAES;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.user.UserFormModel;
import com.mi.hundsun.oxchains.base.core.model.user.UsersModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserIdentifyInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UsersInterface;
import com.xiaoleilu.hutool.util.BeanUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;


/**
 * 用户管理
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UsersJsonController extends GenericController<Integer, Users> {
    @Autowired
    UsersInterface usersInterface;
    @Autowired
    UserIdentifyInterface userIdentifyInterface;

    @ResponseBody
    @RequestMapping("users/json/list")
    @RequiresPermissions("sys:users:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        DtGrid dtGrid = usersInterface.getDtGridList(dtGridPager);
        if (dtGrid.getExhibitDatas().size() > 0) {
            for (Object obj : dtGrid.getExhibitDatas()) {
                LinkedHashMap map = (LinkedHashMap) obj;
                //解密
                String googleKey = ToolAES.decrypt(map.get("googleKey").toString());
                map.put("googleKey", googleKey);
            }
        }
        return dtGrid;
    }

    /**
     * 编辑用户信息
     *
     * @param users
     * @return
     */
    @ResponseBody
    @RequestMapping("users/json/edit")
    @RequiresPermissions("sys:users:edit")
    public ResultEntity editJson(Users users) throws Exception {


        //参数校验
        if (null == users.getId()) {
            throw new BussinessException("参数有误");
        }
        ResultEntity resultEntity = usersInterface.updateUser(users);
        return resultEntity;
    }

    /**
     * 修改状态
     */
    @ResponseBody
    @RequestMapping(value = "/users/json/state")
    @RequiresPermissions("sys:users:state")
    public ResultEntity state(Integer id, Integer state) throws Exception {
        //参数校验
        if (null == id) {
            throw new BussinessException("参数错误");
        }
        if (null == state || (state != Users.STATE.NORMAL.code && state != Users.STATE.LOGOUT.code)) {
            throw new BussinessException("状态参数错误");
        }
        Users users = usersInterface.selectByPrimaryKey(id);
        if (null == users) {
            throw new BussinessException("该用户不存在");
        }
        users.setState(state);
        usersInterface.updateByIdAndVersionSelective(users);
        return ok();
    }

    /**
     * 列表导出
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "users/json/export")
    @RequiresPermissions("sys:users:export")
    public ResultEntity export(String dtGridPager, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 执行导出
        try {
            DtGrid dtGrid = usersInterface.getDtGridListExport(dtGridPager);
            ExportUtils.export(request, response, dtGrid);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("导出失败");
        }
    }

    /**
     * 平台用户统计
     */
    @ResponseBody
    @RequestMapping("users/json/form")
    @RequiresPermissions("sys:user:form")
    public ResultEntity statisticsJson(@RequestParam(value = "startTimeStr") String startTimeStr,
                                       @RequestParam(value = "endTimeStr") String endTimeStr) throws Exception {
        UserFormModel model = new UserFormModel();
        model.setStartTime(startTimeStr);
        model.setEndTime(endTimeStr);
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setData(usersInterface.userSum(model));
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
      }
}
