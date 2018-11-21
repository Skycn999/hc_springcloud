package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.enums.LoginEnum;
import com.mi.hundsun.oxchains.base.common.utils.MD5Utils;
import com.mi.hundsun.oxchains.base.common.utils.RSAUtils;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.ConfigConsts;
import com.mi.hundsun.oxchains.base.core.constant.ConfigNID;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserFreezeMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserIdentifyMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UsersMapper;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.user.RegistLoginService;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

/**
 * 注册登录业务相关Service接口实现<br>
 *
 * @author fengting
 * @date 2018-04-12 03:56:01
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class RegistLoginServiceImpl implements RegistLoginService {

    @Resource
    private UsersMapper usersMapper;
    @Resource
    private UserFreezeMapper userFreezeMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private UserIdentifyMapper userIdentifyMapper;

    @Override
    public GenericMapper<Users, Integer> _getMapper() {
        return usersMapper;
    }

    @Override
    public boolean validLoginInfo(Users users, String encryptPwd) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (null == users) {
            // 用户不存在
            throw new BussinessException(LoginEnum.USERNAME_OR_PASSWD_ERROR.getStateInfo());
        }
        UserFreeze userFreeze = new UserFreeze();
        userFreeze.setUserId(users.getId());
        userFreeze = userFreezeMapper.selectOne(userFreeze);
        if (userFreeze.getLoginState() == UserFreeze.LOGINSTATE.FROZEN.code) {
            throw new BussinessException(LoginEnum.DISABLED.getStateInfo());
        }
        // 密码错误次数超限
        if (!this.validPwdErrorCount(users)) {
            throw new BussinessException(LoginEnum.PASSWD_OUT_TIMES.getStateInfo());
        }
        //rsa加密的密文解密出明文
        String decryptPwd = RSAUtils.decrypt(encryptPwd);
        String salt = users.getPwdSalt();
        String md5 = MD5Utils.getMd5(decryptPwd, salt);
        boolean verify = md5.equals(users.getPwd());
        if (!verify) {
            log.error("username or password incorrect...");
            // 密码验证失败 更新密码错误次数
            this.updatePassErrorCount(users, "+");
            throw new BussinessException(LoginEnum.USERNAME_OR_PASSWD_ERROR.getStateInfo());
        } else {
            //更新密码错误次数
            if (users.getErrCount() > 0) {
                this.updatePassErrorCount(users, "-");
            }
            return true;
        }
    }

    /**
     * 更新密码错误次数
     *
     * @param user 用户
     */
    private void updatePassErrorCount(Users user, String optStyle) throws BussinessException {
        Integer errorCount;
        //当前错误次数
        switch (optStyle) {
            case "+":
                errorCount = user.getErrCount() + 1;
                break;
            case "-":
                errorCount = 0;
                break;
            default:
                errorCount = user.getErrCount();
                break;
        }
        user.setErrCount(errorCount);
        usersMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * 校验密码连续登录错误次数，并判断是否允许继续登录
     *
     * @param user 用户信息
     * @return 允许或不允许
     */
    private boolean validPwdErrorCount(Users user) {
        // 密码错误次数超限检查
        Integer errorCount = user.getErrCount();
        Integer maxErrorCount = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.MAX_PWD_ERROR_COUNT, Integer.class);
        maxErrorCount = maxErrorCount == null ? 6 : maxErrorCount;
        return errorCount < maxErrorCount;
    }



    @Override
    public Users register(String username, String encryptPwd) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //使用RSA解密密文得到明文
        String decryptData = RSAUtils.decrypt(encryptPwd);
        //生成密码盐
        String salt = MD5Utils.generateSalt();
        //获得密码 + 密码盐 md5后的密码串
        String md5Pwd = MD5Utils.getMd5(decryptData, salt);
        Users users = new Users();
        users.setPwd(md5Pwd);
        users.setPwdSalt(salt);
        if (username.contains("@")) {
            users.setEmail(username);
        } else {
            users.setMobile(username);
        }
        users.setDelFlag(GenericPo.DELFLAG.NO.code);
        users.setUuid(RandomUtils.randomCustomUUID());
        int insert = usersMapper.insert(users);
        if (insert < 1) {
            throw new BussinessException("注册失败");
        }
        users = usersMapper.selectOne(users);

        //初始化冻结
        UserFreeze userFreeze = new UserFreeze();
        userFreeze.setUuid(RandomUtils.randomCustomUUID());
        userFreeze.setUserId(users.getId());
        userFreeze.setLoginState(UserFreeze.LOGINSTATE.UNBLOCKED.code);
        userFreeze.setTradeState(UserFreeze.TRADESTATE.UNBLOCKED.code);
        userFreeze.setMentionCoinState(UserFreeze.MENTIONCOINSTATE.UNBLOCKED.code);
        userFreeze.setRechargeCoinState(UserFreeze.RECHARGECOINSTATE.UNBLOCKED.code);
        userFreeze.setCreateTime(new Date());
        userFreeze.setDelFlag(GenericPo.DELFLAG.NO.code);
        userFreezeMapper.insert(userFreeze);
        //  初始化认证设置
        UserIdentify userIdentify = new UserIdentify();
        userIdentify.setUuid(RandomUtils.randomCustomUUID());
        userIdentify.setUserId(users.getId());
        userIdentify.setRealnameState(UserIdentify.REALNAMESTATE.UNCERTIFIED.code);
        if (username.contains("@")) {
            userIdentify.setEmailState(UserIdentify.EMAILSTATE.CERTIFIED.code);
            userIdentify.setEmailTime(new Date());
            userIdentify.setMobileState(UserIdentify.MOBILESTATE.UNCERTIFIED.code);
        } else {
            userIdentify.setMobileState(UserIdentify.MOBILESTATE.CERTIFIED.code);
            userIdentify.setMobileTime(new Date());
            userIdentify.setEmailState(UserIdentify.EMAILSTATE.UNCERTIFIED.code);
        }
        userIdentify.setCreateTime(new Date());
        userIdentify.setDelFlag(GenericPo.DELFLAG.NO.code);
        userIdentifyMapper.insert(userIdentify);
        return users;
    }

    @Override
    public boolean checkGraphicCode(String graphicCodeKey, String graphicCode) {
        String cacheGraphicCode = redisService.get(CacheID.GRAPHIC_CODE_PREFIX + graphicCodeKey);
        if (StrUtil.isBlank(cacheGraphicCode)) {
            throw new BussinessException("图文验证码错误");
        }
        // 是否开通万能验证码
        Boolean superValidate = redisService.get(ConfigNID.SUPER_VALIDATE_OPEN, Boolean.class);
        if (null == superValidate) {
            superValidate = false;
        }
        redisService.del(CacheID.GRAPHIC_CODE_PREFIX);
        // 开通万能验证码，可使用万能验证码
        if (!superValidate || !graphicCode.equals(ConfigConsts.SUPER_VALIDATE_CODE)) {
            return cacheGraphicCode.equals(graphicCode);
        } else {
            return ConfigConsts.SUPER_VALIDATE_CODE.equals(graphicCode);
        }
    }

    @Override
    public boolean checkSmsOrEmailCode(String emailOrMobile, String verifyCode, String type) {
        String cacheVerifyCode;
        switch (type) {
            case Constants.BACK_PWD_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.BACK_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.BACK_PWD_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.BACK_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.MENTION_COIN_CODE_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.MENTION_COIN_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.MENTION_COIN_CODE_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.MENTION_COIN_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.REGIST_CODE_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.REGIST_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.REGIST_CODE_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.REGIST_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.BIND_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.BIND_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.BIND_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.BIND_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.MODIFY_PWD_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.MODIFY_PWD_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.MODIFY_PWD_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.MODIFY_PWD_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.BACK_MENTION_PWD_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.BACK_MENTION_PWD_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.BACK_MENTION_PWD_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.BACK_MENTION_PWD_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.BIND_GOOGLE_KEY_EMAIl:
                cacheVerifyCode = redisService.get(CacheID.BIND_GOOGLE_KEY_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.BIND_GOOGLE_KEY_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.BIND_GOOGLE_KEY_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.MODIFY_GOOGLE_KEY_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.MODIFY_GOOGLE_KEY_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.MODIFY_GOOGLE_KEY_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.MODIFY_GOOGLE_KEY_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_EMAIL:
                cacheVerifyCode = redisService.get(CacheID.CLOSE_OR_OPEN_GOOGLE_AUTH_EMAIL_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            case Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE:
                cacheVerifyCode = redisService.get(CacheID.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE_VERIFY_CODE_PREFIX + emailOrMobile);
                break;
            default:
                throw new BussinessException("类型错误");
        }
        if (StrUtil.isBlank(cacheVerifyCode)) {
            throw new BussinessException("请获取验证码");
        }
        // 是否开通万能验证码
        Boolean superValidate = true;
        Integer superValidateValue = redisService.get(ConfigNID.SUPER_VALIDATE_OPEN, Integer.class);
        if (StringUtils.isBlank(superValidateValue) || superValidateValue.equals(Constants.CONS_ZERO)) {
            superValidate = false;
        }
        // 开通万能验证码，可使用万能验证码
        if (!superValidate || !verifyCode.equals(ConfigConsts.SUPER_VALIDATE_CODE)) {
            return cacheVerifyCode.equals(verifyCode);
        } else {
            return ConfigConsts.SUPER_VALIDATE_CODE.equals(verifyCode);
        }
    }
}
