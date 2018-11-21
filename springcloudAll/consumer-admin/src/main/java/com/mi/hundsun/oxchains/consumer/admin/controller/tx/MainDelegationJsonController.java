package com.mi.hundsun.oxchains.consumer.admin.controller.tx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.export.ExportUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.base.core.tx.po.MainDelegation;
import com.mi.hundsun.oxchains.consumer.admin.service.exchange.ExchangeInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.MainDelegationInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UsersInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 主委托管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class MainDelegationJsonController extends GenericController<Integer, MainDelegation> {
    @Autowired
    MainDelegationInterface mainDelegationInterface;
    @Autowired
    UsersInterface usersInterface;
    @Autowired
    ExchangeInterface exchangeInterface;

    /**
     * 分页查询
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("mainDelegation/json/list")
    @RequiresPermissions("sys:mainDelegation:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
        String param = null;
        int mark = 0;
        if(!StringUtils.isBlank(dtGrid.getFastQueryParameters().get("lk_mobile"))){
            param =  dtGrid.getFastQueryParameters().get("lk_mobile").toString();
            mark = 1;
        }

        if(param !=null){
            String userIds;
            List<Users> list =  usersInterface.fastFindUserByParam(param);
            if (list != null && list.size() >0){
                userIds = list.stream().map(params -> params.getId().toString()).collect(Collectors.joining(","));
                dtGrid.getFastQueryParameters().put("in_userId",userIds);
            }else{
                dtGrid.setExhibitDatas(new ArrayList<Object>());
                return dtGrid;
            }
        }
        if(mark == 1){
            dtGrid.getFastQueryParameters().remove("lk_mobile");
        }
        dtGrid = mainDelegationInterface.getDtGridLists(dtGrid);
        if(dtGrid.getExhibitDatas().size()>0){
            for(Object obj : dtGrid.getExhibitDatas()) {
                LinkedHashMap map = (LinkedHashMap)obj;
                //查询user信息
                Users users = new Users();
                users.setId(Integer.valueOf(map.get("userId").toString()));
                users = usersInterface.selectOne(users);
                if(users!=null){
                    map.put("mobile",users.getMobile());
                    map.put("realname",users.getRealname());
                    map.put("idType",users.getIdType());
                    map.put("idNo",users.getIdNo());
                }
            }
        }
        return dtGrid;
    }

    /**
     * 撤单?
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("mainDelegation/json/revoke")
    @RequiresPermissions("sys:mainDelegation:revoke")
    public ResultEntity revokeJson(Integer id) throws Exception {
        return mainDelegationInterface.revoke(id);
    }

    /**
     * 列表导出
     *
     * @param dtGridPager
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mainDelegation/json/export")
    @RequiresPermissions("sys:mainDelegation:export")
    public ResultEntity export(String dtGridPager, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 执行导出
        try {
            DtGrid dtGrid  = mainDelegationInterface.getDtGridListExport(dtGridPager);
            ExportUtils.export(request, response, dtGrid);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("导出失败");
        }

    }
}
