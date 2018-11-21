package com.mi.hundsun.oxchains.consumer.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.export.ExportUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatAssetLog;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.fn.PlatAssetLogInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 资产划拨记录controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class PlatAssetLogJsonController extends GenericController<Integer, PlatAssetLog> {
    @Autowired
    PlatAssetLogInterface platAssetLogInterface;

    /**
     * 分页查询
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("platAssetLog/json/list")
    @RequiresPermissions("sys:platAssetLog:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return platAssetLogInterface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     * @param platAssetLog
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("platAssetLog/json/add")
    @RequiresPermissions("sys:platAssetLog:add")
    public ResultEntity addJson(PlatAssetLog platAssetLog,String turnCoinTimeStr) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        if(StringUtils.isBlank(platAssetLog.getBillNo())){
            throw new BussinessException("流水号不能为空");
        }else if(platAssetLog.getBillNo().length()>50){
            throw new BussinessException("流水号长度不能超过50位");
        }
        if(StringUtils.isBlank(platAssetLog.getPlatAddr())){
            throw new BussinessException("平台地址不能为空");
        }else if(platAssetLog.getPlatAddr().length()>60){
            throw new BussinessException("平台地址长度不能超过60位");
        }
        if (null == platAssetLog.getAmount()) {
            throw new BussinessException("请输入划拨数量");
        }else if (!ValidateUtils.isPrice3(platAssetLog.getAmount().toString())) {
            throw new BussinessException("划拨数量格式不正确,可整数可小数,小数点前后最大可为10位");
        }
        if(StringUtils.isBlank(turnCoinTimeStr)){
            throw new BussinessException("请选择转账时间");
        }
        SimpleDateFormat slf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date turnCoinTime = slf.parse(turnCoinTimeStr);
        platAssetLog.setTurnCoinTime(turnCoinTime);
        platAssetLog.setUuid(RandomUtils.randomCustomUUID().toString());
        platAssetLog.setState(PlatAssetLog.STATE.NORMAL.code);
        Admin admin = AdminSessionHelper.getCurrAdmin();
        platAssetLog.setCreateTime(new Date());
        platAssetLog.setCreator(admin.getName());
        platAssetLogInterface.insert(platAssetLog);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     *  作废
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("platAssetLog/json/del")
    @RequiresPermissions("sys:platAssetLog:del")
    public ResultEntity delJson(int id) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
        platAssetLogInterface.removeById(new PlatAssetLog(a ->{
            a.setId(id);
            a.setState(PlatAssetLog.STATE.DELETE.code);
        }));
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 列表导出
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "platAssetLog/json/export")
    @RequiresPermissions("sys:platAssetLog:export")
    public ResultEntity export(String dtGridPager, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 执行导出
        try {
            DtGrid dtGrid  = platAssetLogInterface.getDtGridListExport(dtGridPager);
            ExportUtils.export(request, response, dtGrid);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("导出失败");
        }

    }
}
