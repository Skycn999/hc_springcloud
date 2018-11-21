package com.mi.hundsun.oxchains.base.core.service.user;

import com.mi.hundsun.oxchains.base.core.exception.DtGridException;
import com.mi.hundsun.oxchains.base.core.model.user.UserCountModel;
import com.mi.hundsun.oxchains.base.core.model.user.UserFormModel;
import com.mi.hundsun.oxchains.base.core.model.user.UsersModel;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

/**
 * 用户业务相关Service接口<br>
 *
 * @author bin
 * @ClassName: UsersService
 * @date 2018-04-12 03:56:01
 */
public interface UsersService extends GenericService<Integer, Users> {

    /**
     * 根据UUID标识查询
     */
    Users selectByUuid(String uuid);

    /**
     * 根据邮箱查询
     */
    Users selectByEmail(String username);

    /**
     * 根据手机号查询
     */
    Users selectByMobile(String username);

    /**
     * 根据用户名查询
     */
    Users selectByUserName(String username);

    /**
     * 用户信息更新
     */
    void updateUsers(Users users) throws Exception;

    /**
     * 密码校验
     */
    boolean checkMentionPwd(Integer userId, String encryptMentionPwd) throws InvalidKeySpecException, NoSuchAlgorithmException;

    /**
     * 用户编辑数据重复性校验
     */
    boolean modelRepeatCheck(UsersModel users) throws Exception;

    List<Users> fastFindUserByParam(String param);

    boolean preValidUserInfoToTx(Integer userId, BigDecimal netWorth);

    /**
     * 平台用户统计
     */
    UserFormModel userSum(UserFormModel userFormModel) throws DtGridException;

    void bindMobile(Integer userId, String mobile) throws Exception;

    void bindEmail(Integer userId, String email) throws Exception;

    void idCardIdentify(Users users, String idCardFrontPicUuid, String idCardReversePicUuid, Integer id) throws Exception;

    void passportIdentify(Users users, String idCardFrontPicUuid, Integer id) throws Exception;

    /**
     * 后台修改更新用户
     */
    void updateUser(Users users, boolean mobile, boolean email, boolean realname) throws Exception;

    UserCountModel countUserInfo();

    /**
     * 给管理端首页统计用户数量
     * @return UserFormModel
     */
    UserFormModel countByMgrIndex();

}
