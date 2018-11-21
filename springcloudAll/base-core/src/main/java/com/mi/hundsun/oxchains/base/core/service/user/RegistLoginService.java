package com.mi.hundsun.oxchains.base.core.service.user;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 注册登录模块接口
 *
 * @author bin
 * @ClassName: UsersService
 * @date 2018-04-12 03:56:01
 */
public interface RegistLoginService extends GenericService<Integer, Users> {


    /**
     * 校验用户密码是否正确
     *
     * @param users      登录用户
     * @param encryptPwd 用户输入的密码加密后的字符串
     * @return 是否正确
     */
    boolean validLoginInfo(Users users, String encryptPwd) throws InvalidKeySpecException, NoSuchAlgorithmException;

    /**
     * 注册
     *
     * @param username   用户名
     * @param encryptPwd rsa加密密文密码
     * @throws InvalidKeySpecException  ex
     * @throws NoSuchAlgorithmException ex
     */
    Users register(String username, String encryptPwd) throws InvalidKeySpecException, NoSuchAlgorithmException;

    boolean checkGraphicCode(String graphicCodeKey, String graphicCode);

    boolean checkSmsOrEmailCode(String emailOrMobile, String verifyCode, String type);
}
