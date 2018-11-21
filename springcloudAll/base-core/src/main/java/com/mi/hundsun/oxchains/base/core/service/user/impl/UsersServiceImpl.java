package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.common.utils.MD5Utils;
import com.mi.hundsun.oxchains.base.common.utils.RSAUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.mapper.tpl.NetWorthControlMapper;
import com.mi.hundsun.oxchains.base.core.mapper.tpl.PercentControlMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserFreezeMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserIdentifyMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserRiskControlMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UsersMapper;
import com.mi.hundsun.oxchains.base.core.model.user.UserCountModel;
import com.mi.hundsun.oxchains.base.core.model.user.UserFormModel;
import com.mi.hundsun.oxchains.base.core.model.user.UsersModel;
import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.user.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户业务相关Service接口实现<br>
 *
 * @author bin
 * @date 2018-04-12 03:56:01
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class UsersServiceImpl implements UsersService {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private UserFreezeMapper userFreezeMapper;
    @Resource
    private UserIdentifyMapper userIdentifyMapper;
    @Resource
    private UserRiskControlMapper userRiskControlMapper;
    @Resource
    private NetWorthControlMapper netWorthControlMapper;
    @Resource
    private PercentControlMapper percentControlMapper;

    @Override
    public GenericMapper<Users, Integer> _getMapper() {
        return usersMapper;
    }

    @Override
    public Users selectByUuid(String uuid) {
        Users users = new Users();
        users.setUuid(uuid);
        return selectOne(users);
    }

    @Override
    public Users selectByEmail(String username) {
        Users users = new Users();
        users.setEmail(username);
        return selectOne(users);
    }

    @Override
    public Users selectByMobile(String username) {
        Users users = new Users();
        users.setMobile(username);
        return selectOne(users);
    }

    @Override
    public Users selectByUserName(String username) {
        if (username.contains("@")) {
            return selectByEmail(username);
        } else {
            return selectByMobile(username);
        }
    }

    @Override
    public void updateUsers(Users users) {
        usersMapper.updateByPrimaryKeySelective(users);
    }


    @Override
    public boolean checkMentionPwd(Integer userId, String encryptMentionPwd) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Users users = this.selectByPrimaryKey(userId);
        //数据库保存的提现密码
        String oldMentionPwd = users.getMentionPwd();
        //解密用户输入的密码的rsa密文
        String decryptMentionPwd = RSAUtils.decrypt(encryptMentionPwd);
        String salt = users.getMentionPwdSalt();
        String md5 = MD5Utils.getMd5(decryptMentionPwd, salt);
        return md5.equals(oldMentionPwd);
    }

    @Override
    public boolean modelRepeatCheck(UsersModel users) {
        long count = 0;
        Map<String, Object> params = new HashMap<>();
        params.put("id", users.getId());
        params.put("delFlag", Users.DELFLAG.NO.code);
        switch (users.getMark()) {
            case "mobile":
                params.put("mobile", users.getMobile());
                count = usersMapper.mobileRepeatCheck(params);
                break;
            case "idNo":
                params.put("idType", users.getIdType());
                params.put("idNo", users.getIdNo());
                count = usersMapper.idNoRepeatCheck(params);
                break;
            case "email":
                params.put("email", users.getEmail());
                count = usersMapper.emailRepeatCheck(params);
                break;
        }
        return count > 0;
    }

    @Override
    public List<Users> fastFindUserByParam(String param) {
        Map<String, Object> params = new HashMap<>();
        params.put("whereSql", " AND (t.email like '%" + param + "%' OR t.realname  like '%" + param + "%' OR t.mobile like '%" + param + "%') AND del_flag = 0 ");
        return usersMapper.getUserListByUserId(params);
    }

    @Override
    public boolean preValidUserInfoToTx(Integer userId, BigDecimal netWorth) {
        //校验实名认证信息
        UserIdentify identify = new UserIdentify();
        identify.setUserId(userId);
        identify = userIdentifyMapper.selectOne(identify);
        if (UserIdentify.STATE.IDENTIFIED.code != identify.getRealnameState()) {
            throw new BussinessException("您还没有实名认证,请先通过实名认证");
        }

        //校验交易冻结信息
        UserFreeze userFreeze = new UserFreeze();
        userFreeze.setUserId(userId);
        userFreeze = userFreezeMapper.selectOne(userFreeze);
        if (UserFreeze.TRADESTATE.FROZEN.code == userFreeze.getTradeState()) {
            throw new BussinessException("您的交易功能已冻结");
        }

        //校验风控信息
        UserRiskControl userRiskControl = new UserRiskControl();
        userRiskControl.setUserId(userId);
        userRiskControl = userRiskControlMapper.selectOne(userRiskControl);
        if (null != userRiskControl) {
            //查找净值风控模板
            NetWorthControl netWorthControl = new NetWorthControl();
            netWorthControl.setState(NetWorthControl.STATE.ENABLE.code);
            netWorthControl.setDelFlag(NetWorthControl.DELFLAG.NO.code);
            String netWorthTplNo = userRiskControl.getNetWorthTpl();
            netWorthControl.setTplNo(netWorthTplNo);
            netWorthControl = netWorthControlMapper.selectOne(netWorthControl);
            if (null != netWorthControl) {
                BigDecimal allowMinAmount = netWorthControl.getAllowMinAmount();
                if (null != allowMinAmount && allowMinAmount.compareTo(netWorth) > 0) {
                    //更新用户状态
                    userFreeze.setTradeState(UserFreeze.TRADESTATE.FROZEN.code);
                    userFreeze.setRemark("触发净值风控锁定交易功能");
                    userFreeze.setUpdateTime(new Date());
                    userFreezeMapper.updateByPrimaryKeySelective(userFreeze);
                    throw new BussinessException("您已触发净值风控,不能交易");
                }
            }

            //启用了百分比风控
            if (userRiskControl.getIsEnableRiskControl() == UserRiskControl.ISENABLERISKCONTROL.YES.code) {
                //查找百分比分控模板
                PercentControl percentControl = new PercentControl();
                percentControl.setState(NetWorthControl.STATE.ENABLE.code);
                percentControl.setTplNo(userRiskControl.getPercentTpl());
                percentControl.setDelFlag(PercentControl.DELFLAG.NO.code);
                percentControl = percentControlMapper.selectOne(percentControl);
                if (null != percentControl) {
                    Double percent = percentControl.getPercent();
                    BigDecimal percentInitialBalance = userRiskControl.getPercentInitialBalance();
                    if (null == percentInitialBalance) {
                        log.error("百分比风控期初值设置错误");
                        throw new BussinessException("委托异常-(system percentage set error)");
                    }
                    BigDecimal divide = netWorth.divide(percentInitialBalance, 2);
                    if (percent > divide.doubleValue()) {
                        //更新用户状态
                        userFreeze.setTradeState(UserFreeze.TRADESTATE.FROZEN.code);
                        userFreeze.setRemark("触发百分比风控锁定交易功能");
                        userFreeze.setUpdateTime(new Date());
                        userFreezeMapper.updateByPrimaryKeySelective(userFreeze);
                        throw new BussinessException("您已触发百分比风控,不能交易");
                    }
                }
            }
        }

        return true;
    }

    @Override
    public UserFormModel userSum(UserFormModel model) {
        Map<String, Object> params1 = new HashMap<>();
        Map<String, Object> params2 = new HashMap<>();
        String startTime = model.getStartTime();
        String endTime = model.getEndTime();
        String sql1 = "", sql2 = "";
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            sql1 = " AND t.create_time <= '" + endTime + "' AND t.create_time >= '" + startTime + "' ";
            sql2 = " AND t.last_login_date <= '" + endTime + "' AND t.last_login_date >= '" + startTime + "' ";
        }
        params1.put("whereSql", sql1 + "AND t.`del_flag` = 0 ");
        params2.put("whereSql", sql2 + "AND t.`del_flag` = 0 ");
        long regSum = usersMapper.findUserSum(params1);
        long loginSum = usersMapper.findUserSum(params2);
        model.setRegNum(regSum);
        model.setLoginNum(loginSum);
        return model;

    }

    @Override
    public void bindMobile(Integer userId, String mobile) {
        usersMapper.updateByPrimaryKeySelective(new Users(u -> {
            u.setId(userId);
            u.setMobile(mobile);
        }));
        userIdentifyMapper.updateBindMobile(userId, UserIdentify.MOBILESTATE.CERTIFIED.code, new Date());
    }

    @Override
    public void bindEmail(Integer userId, String email) {
        usersMapper.updateByPrimaryKeySelective(new Users(u -> {
            u.setId(userId);
            u.setEmail(email);
        }));
        userIdentifyMapper.updateBindEmail(userId, UserIdentify.EMAILSTATE.CERTIFIED.code, new Date());

    }

    @Override
    public void idCardIdentify(Users users, String idCardFrontPicUuid, String idCardReversePicUuid, Integer id) {
        //更新用户表
        usersMapper.updateByPrimaryKeySelective(users);
        //更新用户认证表
        UserIdentify userIdentify = new UserIdentify();
        userIdentify.setId(id);
        userIdentify.setIdPositivePic(idCardFrontPicUuid);
        userIdentify.setIdNegativePic(idCardReversePicUuid);
        userIdentify.setRealnameState(UserIdentify.REALNAMESTATE.CERTIFIED.code);
        userIdentify.setRealnameTime(new Date());
        userIdentifyMapper.updateByPrimaryKeySelective(userIdentify);
    }


    @Override
    public void passportIdentify(Users users, String idCardFrontPicUuid, Integer id) {
        //更新用户表
        usersMapper.updateByPrimaryKeySelective(users);
        //更新用户认证表
        UserIdentify userIdentify = new UserIdentify();
        userIdentify.setId(id);
        userIdentify.setIdPositivePic(idCardFrontPicUuid);
        userIdentify.setRealnameState(UserIdentify.REALNAMESTATE.WAITING.code);
        userIdentifyMapper.updateByPrimaryKeySelective(userIdentify);
    }

    @Override
    public void updateUser(Users users, boolean mobile, boolean email, boolean realname) {
        usersMapper.updateByPrimaryKeySelective(users);
        //更新用户认证记录
        if (mobile) {
            userIdentifyMapper.updateBindMobile(users.getId(), UserIdentify.MOBILESTATE.CERTIFIED.code, new Date());
        }
        if (email) {
            userIdentifyMapper.updateBindEmail(users.getId(), UserIdentify.EMAILSTATE.CERTIFIED.code, new Date());
        }
        if (realname) {
            userIdentifyMapper.updateRealname(users.getId(), UserIdentify.EMAILSTATE.CERTIFIED.code, new Date());
        }
    }

    public UserCountModel countUserInfo() {
        UserCountModel userRegisterModel = usersMapper.countUserRegisterInfo();
        UserCountModel userLoginModel = usersMapper.countUserLoginInfo();
        userRegisterModel.setUserRegisterCount(userLoginModel.getUserLoginCount());
        return userRegisterModel;
    }

    @Override
    public UserFormModel countByMgrIndex() {
        UserFormModel model = new UserFormModel();
        long registerUser = usersMapper.countTodayRegisterUser();
        long loginUser = usersMapper.countTodayLoginUser();
        long historyRegisterUser = usersMapper.countHistoryRegisterUser();
        model.setLoginNum(loginUser);
        model.setRegNum(registerUser);
        model.setTotalNum(historyRegisterUser);
        return model;
    }
}
