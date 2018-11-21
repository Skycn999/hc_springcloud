package com.mi.hundsun.oxchains.consumer.admin.controller.tpl;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.OrderNoUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Admin;
import com.mi.hundsun.oxchains.base.core.po.tpl.Protocol;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.mi.hundsun.oxchains.consumer.admin.service.tpl.ProtocolInterface;
import com.mi.hundsun.oxchains.consumer.admin.utils.AdminSessionHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping(BaseController.BASE_URI)
public class ProtocolJsonController extends GenericController<Integer, Protocol> {

    @Resource
    ProtocolInterface protocolInterface;

    @RequestMapping("protocol/json/list")
    @RequiresPermissions("sys:protocol:list")
    public DtGrid listJson(String dtGridPager) throws Exception {
        return protocolInterface.getDtGridList(dtGridPager);
    }

    /**
     * 新增保存
     *
     * @param protocol
     * @return
     * @throws Exception
     */

    @RequestMapping("protocol/json/add")
    @RequiresPermissions("sys:protocol:add")
    public ResultEntity addJson(Protocol protocol) throws Exception {
        checkProtocol(protocol, "add");
        ResultEntity resultEntity = new ResultEntity();
        Admin admin = AdminSessionHelper.getCurrAdmin();
        protocol.setTplNo(OrderNoUtils.getSerialNumber());
        protocol.setCreator(admin.getName());
        protocol.setCreateTime(new Date());
        protocol.setUuid(RandomUtils.randomCustomUUID().toString());
        protocolInterface.insert(protocol);
        resultEntity.setCode(ResultEntity.SUCCESS);
        return resultEntity;
    }

    /**
     * 编辑用户信息
     *
     * @param protocol
     * @return
     */

    @RequestMapping("protocol/json/edit")
    @RequiresPermissions("sys:protocol:edit")
    public ResultEntity editJson(Protocol protocol) throws Exception {
        checkProtocol(protocol, "edit");
        protocolInterface.updateByPrimaryKeySelective(protocol);
        return ok();
    }

    private void checkProtocol(Protocol protocol, String action) {
        if (null == protocol) {
            throw new BussinessException("参数有误");
        }
        if ("edit".equals(action)) {
            if (null == protocol.getId()) {
                throw new BussinessException("参数有误");
            }
        }
        if(StringUtils.isBlank(protocol.getTplCnName())){
            throw new BussinessException("中文协议名称不能为空");
        }
//        else if(!ValidateUtils.isChinese(protocol.getTplCnName())){
//            throw new BussinessException("请输入中文");
//        }
        else if(protocol.getTplCnName().length()>50){
            throw new BussinessException("输入长度不能超过50位");
        }
        if(StringUtils.isBlank(protocol.getTplEnName())){
            throw new BussinessException("英文协议名称不能为空");
        } else if(protocol.getTplEnName().length()>50){
            throw new BussinessException("输入长度不能超过50位");
        }
        if(StringUtils.isBlank(protocol.getCnContent())){
            throw new BussinessException("内容不能为空");
        }
//        else if(!ValidateUtils.isChinese(protocol.getCnContent())){
//            throw new BussinessException("请输入中文");
//        }
        if(StringUtils.isBlank(protocol.getEnContent())){
            throw new BussinessException("内容不能为空");
        } else if(ValidateUtils.isChinese(protocol.getEnContent())){
            throw new BussinessException("请输入英文");
        }
        if(null == protocol.getState()){
            throw new BussinessException("请选择状态");
        } else if(protocol.getState() != Protocol.STATE.ENABLE.code && protocol.getState() != Protocol.STATE.DISABLE.code){
            throw new BussinessException("状态不正确");
        }
//        if (null == protocol.getAllowMinAmount()) {
//            throw new BussinessException("请输入允许交易最小净值");
//        } else if (netWorthControl.getAllowMinAmount().toString().length()> 10) {
//            throw new BussinessException("输入小数长度不能大于10位");
//        }

    }
}
