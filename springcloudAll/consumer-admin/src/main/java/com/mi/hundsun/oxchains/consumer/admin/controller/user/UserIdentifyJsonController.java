package com.mi.hundsun.oxchains.consumer.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.export.ExportUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UserIdentifyInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 *用户认证
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserIdentifyJsonController extends GenericController<Integer, UserIdentify> {

    @Autowired
    UserIdentifyInterface userIdentifyInterface;

    @ResponseBody
    @RequestMapping("userIdentify/json/list")
    @RequiresPermissions("sys:user:identify:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return userIdentifyInterface.getDtGridList(dtGridPager);
    }

    /**
     * 审核用户信息
     *
     * @param userIdentify
     * @return
     */
    @ResponseBody
    @RequestMapping("user/identify/json/check")
    @RequiresPermissions("sys:user:identify:check")
    public ResultEntity checkJson(UserIdentify userIdentify) throws Exception {
        if(null == userIdentify.getId()){
            throw new BussinessException("参数有误");
        }
        UserIdentify identify = userIdentifyInterface.selectOne(new UserIdentify(r->{
              r.setId(userIdentify.getId());
              r.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if(identify.getRealnameState() != UserIdentify.REALNAMESTATE.WAITING.code && identify.getRealnameState() != UserIdentify.REALNAMESTATE.UNCERTIFIED.code){
            throw new BussinessException("该记录已审核，请刷新重试");
        }
        identify.setRealnameState(userIdentify.getRealnameState());
        identify.setRemark(userIdentify.getRemark());
        Admin admin = AdminSessionHelper.getCurrAdmin();
        identify.setRealnameTime(new Date());
        identify.setUpdator(admin.getName());
        userIdentifyInterface.updateByPrimaryKeySelective(identify);
        return ok();
    }

    /**
     * 列表导出
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "userIdentify/json/export")
    @RequiresPermissions("sys:userIdentify:export")
    public ResultEntity export(String dtGridPager, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 执行导出
        try {
            DtGrid dtGrid  = userIdentifyInterface.getDtGridListExport(dtGridPager);
            ExportUtils.export(request, response, dtGrid);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("导出失败");
        }

    }
}
