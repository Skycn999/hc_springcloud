/*
 * Copyright (c) 2015-2017, HuiMi Tec co.,LTD. 枫亭子 (646496765@qq.com).
 */
package com.mi.hundsun.oxchains.provider.user.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.api.sendEmail.SendEmailUtils;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.ConfigConsts;
import com.mi.hundsun.oxchains.base.core.constant.Constants;
import com.mi.hundsun.oxchains.base.core.constant.MsgTempNID;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.MsgTemplate;
import com.mi.hundsun.oxchains.base.core.po.user.UserInLetter;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.user.UserInLetterService;
import com.mi.hundsun.oxchains.base.core.webpowerSms.OkHttpClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 枫亭
 * @description 发送信息消费者
 * @date 2018-05-06 14:37.
 */
@Slf4j
@Component
public class SysSmsMessageConsumer {


    @Autowired
    private RedisService redisService;
    @Autowired
    private UserInLetterService userInLetterService;

    //发送短信
    @RabbitListener(queues = "#{waitSendSmsQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void handleSmsMsg(Map<String, Object> map) {
        if (null == map) {
            return;
        }
        log.info("发送短信信息开始，参数={}", JSON.toJSONString(map));
        try {
            String prefix = this.getPrefix(map.get("type").toString());
            this.sendSms(map.get("mobile").toString(), prefix);
        } catch (Exception e) {
            log.error("发送短信信息MQ时出现异常:{}", e);
        }
    }

    private void sendSms(String mobile, String prefix) throws Exception {
        String randomString = RandomUtils.randomNumbers(6);
        Integer codeTime = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.VERIFY_CODE_TIME, Integer.class);
        long time = Integer.valueOf(codeTime * 60).longValue();
        MsgTemplate msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + MsgTempNID.VALIDATE_CODE, MsgTemplate.class);
        if (null == msgTemplate) {
            throw new Exception("类型错误");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", randomString);
        data.put("limit", codeTime);
        String content = StringUtils.replace(msgTemplate.getSmsContent(), data);

        String smsUrl = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.SMS_URL); //短信地址
        String campaignId = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.SMS_CAMPAIGN_ID);//短信活动ID
        String apikey = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.SMS_APIKEY);//短信服务授权码
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", apikey);
        Map<String, Object> body = new HashMap<>();
        body.put("mobile", mobile);
        body.put("content", content);
        body.put("campaignID", campaignId);
        String result = OkHttpClientHelper.post(smsUrl, header, body);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String status = jsonObject.get("status").toString();
        if (!StringUtils.isBlank(status) && status.equals("OK")) {
            redisService.put(prefix + mobile, randomString, time);
        }
    }

    private String getPrefix(String type) throws Exception {
        switch (type) {
            case Constants.BACK_PWD_EMAIL:
                return CacheID.BACK_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.BACK_PWD_MOBILE:
                return CacheID.BACK_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.REGIST_CODE_EMAIL:
                return CacheID.REGIST_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.REGIST_CODE_MOBILE:
                return CacheID.REGIST_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.MENTION_COIN_CODE_EMAIL:
                return CacheID.MENTION_COIN_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.MENTION_COIN_CODE_MOBILE:
                return CacheID.MENTION_COIN_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.BIND_EMAIL:
                return CacheID.BIND_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.BIND_MOBILE:
                return CacheID.BIND_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.MODIFY_PWD_EMAIL:
                return CacheID.MODIFY_PWD_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.MODIFY_PWD_MOBILE:
                return CacheID.MODIFY_PWD_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.BACK_MENTION_PWD_EMAIL:
                return CacheID.BACK_MENTION_PWD_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.BACK_MENTION_PWD_MOBILE:
                return CacheID.BACK_MENTION_PWD_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.BIND_GOOGLE_KEY_EMAIl:
                return CacheID.BIND_GOOGLE_KEY_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.BIND_GOOGLE_KEY_MOBILE:
                return CacheID.BIND_GOOGLE_KEY_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.MODIFY_GOOGLE_KEY_EMAIL:
                return CacheID.MODIFY_GOOGLE_KEY_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.MODIFY_GOOGLE_KEY_MOBILE:
                return CacheID.MODIFY_GOOGLE_KEY_MOBILE_VERIFY_CODE_PREFIX;
            case Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_EMAIL:
                return CacheID.CLOSE_OR_OPEN_GOOGLE_AUTH_EMAIL_VERIFY_CODE_PREFIX;
            case Constants.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE:
                return CacheID.CLOSE_OR_OPEN_GOOGLE_AUTH_MOBILE_VERIFY_CODE_PREFIX;
            default:
                throw new BussinessException("类型错误");

        }
    }

    //发送邮件
    @RabbitListener(queues = "#{waitSendEmailQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void sendEmailMsg(Map<String, Object> map) {
        if (null == map) {
            return;
        }
        log.info("发送邮箱信息开始，参数={}", JSON.toJSONString(map));
        try {
            String prefix = getPrefix(map.get("type").toString());
            sendEmail(map.get("email").toString(), prefix);
        } catch (Exception e) {
            log.error("发送邮箱信息MQ时出现异常:{}", e);
//            throw new RuntimeException(e);
        }
    }

    private void sendEmail(String email, String prefix) throws Exception {
        String randomString = RandomUtils.randomNumbers(6);
        Integer codeTime = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.VERIFY_CODE_TIME, Integer.class);
        long time = Integer.valueOf(codeTime * 60).longValue();
        MsgTemplate msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + MsgTempNID.VALIDATE_CODE, MsgTemplate.class);
        if (null == msgTemplate) {
            throw new Exception("类型错误");
        }
        String title = msgTemplate.getEmailTitle();
        Map<String, Object> data = new HashMap<>();
        data.put("code", randomString);
        data.put("limit", codeTime);
        String content = StringUtils.replace(msgTemplate.getEmailContent(), data);   //内容
        SendEmailUtils.sendEmail(title, content, email);
        redisService.put(prefix + email, randomString, time);
    }

    //发送站内信
    @RabbitListener(queues = "#{waitSendInsideQueue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void sendLetterMsg(Map<String, Object> map) {
        if (null == map) {
            return;
        }
        log.info("发送站内信信息开始，参数={}", JSON.toJSONString(map));
        try {
            Map<String, String> result = getReslut(map.get("type").toString());
            UserInLetter letter = new UserInLetter();
            letter.setUuid(RandomUtils.randomCustomUUID());
            letter.setUserId(Integer.valueOf(map.get("userId").toString()));
            letter.setReadFlag(UserInLetter.READFLAG.UNREAD.code);
            letter.setTitle(result.get("title"));
            letter.setContent(result.get("content"));
            letter.setCreateTime(new Date());
            userInLetterService.insert(letter);
        } catch (Exception e) {
            log.error("发送站内信信息MQ时出现异常:{}", e);
//            throw new RuntimeException(e);
        }
    }


    private Map<String, String> getReslut(String type) throws Exception {
        MsgTemplate msgTemplate;
        Map<String, Object> data = new HashMap<>();
        Map<String, String> result = new HashMap<>();
        String content;
        String sign = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.WEB_SIGN);
        switch (type) {
            case MsgTempNID.REGISTER_SUCCESS:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.BIND_MOBILE:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.BIND_EMAIL:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.RETRIEVE_LOGIN_PWD:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.SET_MENTION_COIN_PWD:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.MODIFY_LOGIN_PWD:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.MODIFY_MENTION_COIN_PWD:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.RETRIEVE_MENTION_COIN_PWD:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.BIND_GOOGLE_KEY:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.MODIFY_GOOGLE_KEY:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.CLOSE_GOOGLE_KEY:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            case MsgTempNID.OPEN_GOOGLE_KEY:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                break;
            default:
                throw new Exception("类型错误");
        }
        content = StringUtils.replace(msgTemplate.getLetterContent(), data);   //内容
        result.put("content", content);
        result.put("title", msgTemplate.getName());
        return result;
    }

    //发送站内信
    @RabbitListener(queues = "#{waitSendInside2Queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void sendLetter2Msg(Map<String, Object> map) {
        if (null == map) {
            return;
        }
        log.info("发送站内信信息开始，参数={}", JSON.toJSONString(map));
        try {
            Map<String, String> result = getReslut2(map.get("type").toString(), map.get("currency").toString(), map.get("num").toString());
            UserInLetter letter = new UserInLetter();
            letter.setUuid(RandomUtils.randomCustomUUID());
            letter.setUserId(Integer.valueOf(map.get("userId").toString()));
            letter.setReadFlag(UserInLetter.READFLAG.UNREAD.code);
            letter.setTitle(result.get("title"));
            letter.setContent(result.get("content"));
            letter.setCreateTime(new Date());
            userInLetterService.insert(letter);
        } catch (Exception e) {
            log.error("发送站内信信息MQ时出现异常:{}", e);
            throw new RuntimeException(e);
        }
    }


    private Map<String, String> getReslut2(String type, String currency, String num) throws Exception {
        MsgTemplate msgTemplate;
        Map<String, Object> data = new HashMap<>();
        Map<String, String> result = new HashMap<>();
        String content;
        String sign = redisService.get(CacheID.CONFIG_PREFIX + ConfigConsts.WEB_SIGN);
        switch (type) {
            case MsgTempNID.MENTION_COIN_SUCCESS:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                data.put("currency", currency);
                data.put("num", num);
                break;
            case MsgTempNID.RECHARGE_COIN_SUCCESS:
                msgTemplate = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + type, MsgTemplate.class);
                if (null == msgTemplate) {
                    throw new BussinessException("模板错误");
                }
                data.put("sign", sign);
                data.put("currency", currency);
                data.put("num", num);
                break;
            default:
                throw new Exception("类型错误");
        }
        content = StringUtils.replace(msgTemplate.getLetterContent(), data);   //内容
        result.put("content", content);
        result.put("title", msgTemplate.getName());
        return result;
    }
}

