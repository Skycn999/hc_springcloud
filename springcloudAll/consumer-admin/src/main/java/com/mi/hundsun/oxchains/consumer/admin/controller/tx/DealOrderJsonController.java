package com.mi.hundsun.oxchains.consumer.admin.controller.tx;

import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.tx.po.DealOrder;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.DealOrderInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UsersInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;

/**
 * 成交管理controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class DealOrderJsonController extends GenericController<Integer, DealOrder> {
    @Autowired
    DealOrderInterface dealOrderInterface;
    @Autowired
    UsersInterface usersInterface;

    @ResponseBody
    @RequestMapping("dealOrder/json/list")
    @RequiresPermissions("sys:dealOrder:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        DtGrid dtGrid = dealOrderInterface.getDtGridList(dtGridPager);
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
}
