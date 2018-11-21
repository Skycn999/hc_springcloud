package com.mi.hundsun.oxchains.base.core.service.tpl.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.mapper.tpl.NetWorthControlMapper;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.service.tpl.NetWorthControlService;

import java.util.List;

/**
 * 净值风控模板业务相关Service接口实现<br>
 *
 * @ClassName: NetWorthControlServiceImpl
 * @author bin
 * @date   2018-04-12 11:36:33
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class NetWorthControlServiceImpl implements NetWorthControlService {

	@Resource
    private NetWorthControlMapper netWorthControlMapper;

    @Override
    public GenericMapper<NetWorthControl,Integer> _getMapper() {
        return netWorthControlMapper;
    }

    @Override
    public void insertNetWorthControl(NetWorthControl netWorthControl) throws Exception {
        if (netWorthControl.getState().intValue()==NetWorthControl.STATE.ENABLE.code && netWorthControl.getIsDefault().intValue()==NetWorthControl.ISDEFAULT.YES.code){
            netWorthControlMapper.updateBySql(NetWorthControl.ISDEFAULT.NO.code,NetWorthControl.ISDEFAULT.YES.code, GenericPo.DELFLAG.NO.code);
        }
        netWorthControlMapper.insert(netWorthControl);
    }

    @Override
    public void updateNetWorthControl(NetWorthControl netWorthControl) throws Exception {
        if (netWorthControl.getState().intValue()==NetWorthControl.STATE.ENABLE.code && netWorthControl.getIsDefault().intValue()==NetWorthControl.ISDEFAULT.YES.code){
            netWorthControlMapper.updateBySqlEdit(NetWorthControl.ISDEFAULT.NO.code,NetWorthControl.ISDEFAULT.YES.code,GenericPo.DELFLAG.NO.code,netWorthControl.getId());
        }
        netWorthControlMapper.updateByPrimaryKeySelective(netWorthControl);

    }

    @Override
    public NetWorthControl findNetWorthControlIsDefault() {
      List<NetWorthControl>  netWorthControls = netWorthControlMapper.select(new NetWorthControl(n->{
            n.setState(NetWorthControl.STATE.ENABLE.code);
            n.setIsDefault(NetWorthControl.ISDEFAULT.YES.code);
            n.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        if (null != netWorthControls && netWorthControls.size() >0) {
            return netWorthControls.get(0);
        }else {
            List<NetWorthControl> nwcList = netWorthControlMapper.findNetWorthControlList(NetWorthControl.STATE.ENABLE.code,GenericPo.DELFLAG.NO.code,NetWorthControl.ISDEFAULT.YES.code);
            if(null!= nwcList && nwcList.size()>0){
                return nwcList.get(0);
            }
        }
        return null;
    }
}
