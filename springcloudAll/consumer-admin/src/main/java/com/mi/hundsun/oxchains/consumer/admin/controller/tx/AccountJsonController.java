package com.mi.hundsun.oxchains.consumer.admin.controller.tx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.tx.po.Account;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tx.AccountInterface;
import com.mi.hundsun.oxchains.consumer.admin.service.user.UsersInterface;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户资产持仓controller
 *
 * @author bin
 * @date 2018年03月15日 22:58
 */
@Controller
@RequestMapping(BaseController.BASE_URI)
public class AccountJsonController extends GenericController<Integer, Account> {
    @Autowired
    AccountInterface accountInterface;
    @Autowired
    UsersInterface usersInterface;

    /**
     * 用户资产持仓列表
     *
     * @param dtGridPager
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("account/json/list")
    @RequiresPermissions("sys:account:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
        String param = null;
        int mark = 0;
        if(!StringUtils.isBlank(dtGrid.getFastQueryParameters().get("lk_realname_or_lk_mobile_lk_email"))){
            param =  dtGrid.getFastQueryParameters().get("lk_realname_or_lk_mobile_lk_email").toString();
            mark = 1;
        }else if(!StringUtils.isBlank(dtGrid.getFastQueryParameters().get("lk_realname"))){
            param =  dtGrid.getFastQueryParameters().get("lk_realname").toString();
            mark = 2;
        }else if(!StringUtils.isBlank(dtGrid.getFastQueryParameters().get("lk_mobile"))){
            param =  dtGrid.getFastQueryParameters().get("lk_mobile").toString();
            mark = 3;
        }else if(!StringUtils.isBlank(dtGrid.getFastQueryParameters().get("lk_email"))){
            param =  dtGrid.getFastQueryParameters().get("lk_email").toString();
            mark = 4;
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
            dtGrid.getFastQueryParameters().remove("lk_realname_or_lk_mobile_lk_email");
        }else if(mark == 2){
            dtGrid.getFastQueryParameters().remove("lk_realname");
        }else if(mark == 3){
            dtGrid.getFastQueryParameters().remove("lk_mobile");
        }else if(mark == 4){
            dtGrid.getFastQueryParameters().remove("lk_email");
        }

        dtGrid = accountInterface.getDtGridList(dtGrid);
        if(dtGrid.getExhibitDatas().size()>0){
            for(Object obj : dtGrid.getExhibitDatas()) {
                LinkedHashMap map = (LinkedHashMap)obj;
                //查询user信息
                Users users = new Users();
                users.setId(Integer.valueOf(map.get("userId").toString()));
                users = usersInterface.selectOne(users);
                if(users!=null){
                    map.put("mobile",users.getMobile());
                    map.put("email",users.getEmail());
                    map.put("realname",users.getRealname());
                }
            }
        }
        return dtGrid;
    }

}
