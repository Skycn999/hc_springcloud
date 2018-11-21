package com.mi.hundsun.oxchains.base.core.service.user;

import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;

/**
 * 用户提币地址表业务相关Service接口<br>
 *
 * @ClassName: UserAddressService
 * @author lzj
 * @date   2018-04-14 10:22:36
 */
public interface UserAddressService extends GenericService<Integer, UserAddress> {

    List<UserAddress>  findUserAddressOrderBy(Integer userId,String code);

    long getUserAddressCount(Integer userId);

    long getUserAddressCount(Integer userId,String code);

    List<UserAddress> getUserAddressList(Integer userId, Integer pageSize, Integer pageNumber);

    List<UserAddress> getUserAddressList(Integer userId,String code, Integer pageSize, Integer pageNumber);

    void insertAddress( UserAddress userAddress);

    int delUserAddress(Integer userId,Integer id);

    long getUserAddressSize(Integer userId,String coinCode);

    int delUserAddressForApp(Integer userId,String idStr);

    List<UserAddress> findMyMcAddressByCode(Integer userId, String code);
}
