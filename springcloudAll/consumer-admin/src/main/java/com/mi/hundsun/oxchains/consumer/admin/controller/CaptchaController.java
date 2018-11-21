package com.mi.hundsun.oxchains.consumer.admin.controller;

import com.mi.hundsun.oxchains.base.common.config.CaptchaHelper;
import com.mi.hundsun.oxchains.consumer.admin.service.RedisService;
import com.mi.hundsun.oxchains.base.core.constant.ConfigNID;
import com.mi.hundsun.oxchains.base.core.constant.SessionId;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

@Api(description = "图片验证码相关")
@RestController
@RequestMapping("/captcha")
public class CaptchaController extends GenericController {

    @Resource
    private RedisService redisService;

    @ApiOperation(value = "获取图片验证码", notes = "获取图片验证码")
    @RequestMapping(value = "getcaptcha", method = RequestMethod.GET)
    public ModelAndView getCaptcha() throws Exception {
        String str = new CaptchaHelper().createCaptcha();
        setSession(SessionId.CAPTCHA, str);
        return null;
    }


    @ApiOperation(value = "校验图片验证码", notes = "校验图片验证码")
    @RequestMapping(value = "check", method = RequestMethod.GET)
    public boolean check(@RequestParam(value = "captcha") String captcha) throws Exception {
        return checkCaptcha(captcha);
    }

    /**
     * 校验图片验证码
     *
     * @param captcha
     * @return
     */
    private boolean checkCaptcha(String captcha) {
        String str = getSession(SessionId.CAPTCHA);
        // 是否开通万能验证码
        Boolean superValidate = redisService.get(ConfigNID.SUPER_VALIDATE_OPEN, Boolean.class);
        // 开通万能验证码，可使用万能验证码
        return superValidate != null && superValidate && "9999".equals(captcha) || StrUtil.isNotBlank(str) &&
                StrUtil.isNotBlank(captcha) && StrUtil.equalsIgnoreCase(captcha, str);

    }
}
