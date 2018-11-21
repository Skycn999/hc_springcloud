package com.mi.hundsun.oxchains.provider.admin.controller.user;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.utils.RSAUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.common.utils.ValidateUtils;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.user.UserFormModel;
import com.mi.hundsun.oxchains.base.core.model.user.UsersModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.user.UserIdentifyService;
import com.mi.hundsun.oxchains.base.core.service.user.UsersService;
import com.xiaoleilu.hutool.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Api("系统用户相关服务")
@Slf4j
@RestController
@RequestMapping("/user/users")
public class UsersController extends GenericController {
    @Autowired
    UsersService usersService;
    @Autowired
    UserIdentifyService userIdentifyService;
    @Autowired
    RedisService redisService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return usersService.getDtGridList(dtGridPager, UsersModel.class);
    }

    @ApiOperation(value = "查询单个用户信息")
    @PostMapping(value = "/selectOne")
    public Users selectOne(@RequestBody Users users) throws BussinessException {
        return usersService.selectOne(users);
    }

    @ApiOperation(value = "查询用户信息")
    @PostMapping(value = "/select")
    public List<Users> select(@RequestBody Users users) throws BussinessException {
        return usersService.select(users);
    }

    @ApiOperation(value = "根据参数次查询用户")
    @PostMapping(value = "/fastFindUserByParam")
    public List<Users> fastFindUserByParam(String param) throws BussinessException {
        return usersService.fastFindUserByParam(param);
    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public void updateByPrimaryKeySelective(@RequestBody Users users) throws BussinessException {
        usersService.updateByPrimaryKeySelective(users);
    }

    @ApiOperation(value = "后台编辑更新用户信息")
    @PostMapping(value = "/updateUser")
    public ResultEntity updateUser(@RequestBody Users users) throws Exception {
        UserIdentify userIdentify = userIdentifyService.selectOne(new UserIdentify(u->{
            u.setUserId(users.getId());
            u.setDelFlag(GenericPo.DELFLAG.NO.code);
        }));
        //认证校验、已认证则只能修改，未认证添加后变已认证
        boolean mobile = false;
        boolean email = false;
        boolean realname = false;
        if(userIdentify.getMobileState() == UserIdentify.MOBILESTATE.CERTIFIED.code){
            if (StringUtils.isBlank(users.getMobile())) {
                return fail("请输入手机号码");
            } else if (!ValidateUtils.isPhone(users.getMobile())) {
                return fail("手机号码格式不正确");
            }
        }else {
            if(!StringUtils.isBlank(users.getMobile())){
                if(ValidateUtils.isPhone(users.getMobile())) {
                    mobile = true;
                }else {
                    return fail("手机号码格式不正确");
                }
            }
        }
        if(userIdentify.getEmailState()== UserIdentify.EMAILSTATE.CERTIFIED.code){
            if (StringUtils.isBlank(users.getEmail())) {
                return fail("请输入邮箱");
            } else if (!ValidateUtils.isEmail(users.getEmail())) {
                return fail("邮箱格式不正确");
            }
        }else {
            if(!StringUtils.isBlank(users.getEmail())){
                if(ValidateUtils.isEmail(users.getEmail())) {
                    email = true;
                }else {
                    return fail("邮箱格式不正确");
                }
            }
        }
        if(userIdentify.getRealnameState() != UserIdentify.REALNAMESTATE.UNCERTIFIED.code && userIdentify.getRealnameState() != UserIdentify.REALNAMESTATE.NOTPASS.code) {
            if (StringUtils.isBlank(users.getRealname())) {
                return fail("请输入真实姓名");
            } else if (users.getRealname().length() > 30) {
                return fail("真实姓名不能大于30位");
            }
            if (null == users.getIdType()) {
                return fail("请选择证件类型");
            } else if (users.getIdType() != Users.IDTYPE.IDCARD.code && users.getIdType() != Users.IDTYPE.PASSPORT.code) {
                return fail("证件类型不正确");
            }
            if (StringUtils.isBlank(users.getIdNo())) {
                return fail("请输入证件号码");
            } else if (users.getIdType() == Users.IDTYPE.IDCARD.code && !ValidateUtils.isCard(users.getIdNo())) {
                return fail("证件号格式不正确");
            }
            if (null == users.getCountry()) {
                return fail("请选择境内/境外");
            } else if (users.getCountry() != Users.COUNTRY.DOMESTIC.code && users.getCountry() != Users.COUNTRY.ABROAD.code) {
                return fail("选择不正确");
            }
            if(!((users.getIdType() == Users.IDTYPE.IDCARD.code && users.getCountry() == Users.COUNTRY.DOMESTIC.code)||
                    (users.getIdType() == Users.IDTYPE.PASSPORT.code && users.getCountry() == Users.COUNTRY.ABROAD.code))){
                return fail("证件类型和境内境外相统一");
            }
        }else {
            if (!StringUtils.isBlank(users.getRealname()) && !StringUtils.isBlank(users.getIdNo())) {
                realname = true;
                //真实姓名和证件号码都不填写，则默认为不认证
            }else if(StringUtils.isBlank(users.getRealname()) && StringUtils.isBlank(users.getIdNo()) ){
                users.setIdType(Users.IDTYPE.NO.code);
                users.setCountry(Users.COUNTRY.NO.code);
            }else {
                if(StringUtils.isBlank(users.getRealname())){
                    return fail("请输入真实姓名");
                }else {
                    return fail("请输入证件号码");
                }
            }
        }
        UsersModel model = new UsersModel();
        BeanUtil.copyProperties(users, model);
        //重复校验
        if(!StringUtils.isBlank(users.getMobile())){
            model.setMark("mobile");
            if (modelRepeatCheck(model)) {
                return fail("手机号码已存在");
            }
        }
        if(!StringUtils.isBlank(users.getEmail())){
            model.setMark("email");
            if (modelRepeatCheck(model)) {
                return fail("邮箱已存在");
            }
        }
        if(!StringUtils.isBlank(users.getIdNo())){
            model.setMark("idNo");
            if (modelRepeatCheck(model)) {
                return fail("证件号码已存在");
            }
        }
        try {
            usersService.updateUser(users,mobile,email,realname);
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail("修改失败");
        }
    }


    @ApiOperation(value = "查询单个用户信息")
    @PostMapping(value = "/selectByPrimaryKey")
    public Users selectByPrimaryKey(@RequestBody Integer pk) throws BussinessException {
        return usersService.selectByPrimaryKey(pk);
    }

    @ApiOperation(value = "修改状态")
    @PostMapping(value = "/updateByIdAndVersionSelective")
    public void updateByIdAndVersionSelective(@RequestBody Users users) throws BussinessException {
        usersService.updateByIdAndVersionSelective(users);
    }

    @ApiOperation(value = "重复检验查询")
    @PostMapping(value = "/modelRepeatCheck")
    public boolean modelRepeatCheck(@RequestBody UsersModel users) throws Exception {
        return usersService.modelRepeatCheck(users);
    }

    @ApiOperation(value = "获取页面RSA公钥")
    @PostMapping(value = "/getRsaPublicKey")
    public ResultEntity getRsaPublicKey() {
        ResultEntity resultEntity = ResultEntity.success();
        resultEntity.setData(RSAUtils.getPublicKeyObj());
        return resultEntity;
    }

    @ApiOperation(value = "导出excel")
    @PostMapping(value = "/getDtGridListExport")
    public DtGrid getDtGridListExport(@RequestBody String dtGridPager) throws Exception {
        return usersService.getDtGridListExport(dtGridPager,UsersModel.class);
    }

    @ApiOperation(value = "平台用户统计")
    @PostMapping(value = "/userForm")
    public UserFormModel userSum(@RequestBody UserFormModel model) throws Exception {
        return usersService.userSum(model);
    }

    @ApiOperation(value = "统计用户相关信息  for 管理端首页")
    @PostMapping(value = "/countByMgrIndex")
    public UserFormModel countByMgrIndex() {
        UserFormModel model = usersService.countByMgrIndex();
        Set<String> keys = redisService.getKeys("cache:user:uuid:");
        model.setOnlineNum(null != keys? keys.size() : 0);
        return model;
    }

}
