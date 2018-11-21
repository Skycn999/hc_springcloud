package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserAddressMapper;
import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.base.core.service.user.UserAddressService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户地址业务相关Service接口实现<br>
 *
 * @ClassName: UserAddressServiceImpl
 * @author lzj
 * @date   2018-04-17 10:40:50
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class UserAddressServiceImpl implements UserAddressService {

    @Resource
    private UserAddressMapper userAddressMapper;

    @Override
    public GenericMapper<UserAddress,Integer> _getMapper() {
        return userAddressMapper;
    }

    @Override
    public List<UserAddress> findUserAddressOrderBy(Integer userId, String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("whereSql", " AND t.user_id = " + userId + " AND t.coin_currency = '"+code+"' AND t.del_flag = 0 ");
        params.put("sortSql", " ORDER BY t.is_default = "+UserAddress.ISDEFAULT.YES.code+" DESC");
        return userAddressMapper.findUserAddressOrderBy(params);
    }

    @Override
    public long getUserAddressCount(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY create_time DESC");
        return userAddressMapper.getUserAddressCount(params);
    }

    @Override
    public long getUserAddressCount(Integer userId,String code) {
        Map<String, Object> params = new HashMap<>();
        params.put("whereSql", " AND user_id = " + userId + " AND coin_currency = '"+code+"' AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY create_time DESC");
        return userAddressMapper.getUserAddressCount(params);
    }

    @Override
    public List<UserAddress> getUserAddressList(Integer userId, Integer pageSize, Integer pageNumber) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY create_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        return userAddressMapper.getUserAddressList(params);
    }

    @Override
    public List<UserAddress> getUserAddressList(Integer userId,String code, Integer pageSize, Integer pageNumber) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        params.put("whereSql", " AND user_id = " + userId + " AND coin_currency = '"+code+"' AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY create_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        return userAddressMapper.getUserAddressList(params);
    }


    @Override
    public void insertAddress(UserAddress userAddress) {
        if(userAddress.getIsDefault() == UserAddress.ISDEFAULT.YES.code){
            userAddressMapper.updateBySql(UserAddress.ISDEFAULT.NO.code,UserAddress.ISDEFAULT.YES.code,userAddress.getUserId(),userAddress.getCoinCurrency(),GenericPo.DELFLAG.NO.code);
        }
        userAddressMapper.insert(userAddress);
    }

    @Override
    public int delUserAddress(Integer userId, Integer id) {
      int i = userAddressMapper.delUserAddress(UserAddress.DELFLAG.YES.code,userId,id);
      return i;
    }

    @Override
    public long getUserAddressSize(Integer userId, String coinCode) {
        return  userAddressMapper.getUserAddressSize(userId,coinCode);
    }

    @Override
    public int delUserAddressForApp(Integer userId, String idStr) {
        Map<String,Object> param = new HashMap<>();
        param.put("whereSql"," AND t.user_id = "+userId+" AND t.id in ("+idStr+")");
        return userAddressMapper.delUserAddressForApp(param);
    }

    @Override
    public List<UserAddress> findMyMcAddressByCode(Integer userId, String code) {
        return userAddressMapper.findMyMcAddressByCode(userId, code);
    }
}
