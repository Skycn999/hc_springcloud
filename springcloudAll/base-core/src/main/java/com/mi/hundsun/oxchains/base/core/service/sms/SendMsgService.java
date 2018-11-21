package com.mi.hundsun.oxchains.base.core.service.sms;

import com.mi.hundsun.oxchains.base.common.utils.JsonUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.MsgType;
import com.mi.hundsun.oxchains.base.core.constant.State;
import com.mi.hundsun.oxchains.base.core.po.system.MsgTemplate;
import com.mi.hundsun.oxchains.base.core.po.system.NoticeLog;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.function.Consumer;

@Service
@Scope("prototype")
@Slf4j
public class SendMsgService {

    @Resource
    RedisService redisService;

    /**
     * @param nid      消息模板标识（必填）
     * @param title    发送主题（必填）
     * @param userId   接收用户（必填）
     * @param mobile   手机号（需要发送短信时必填）
     * @param email    邮箱（需要发送邮件时必填）
     * @param consumer 模板内容参数
     */
    public void pushMessageQueue(String nid, String title, Integer userId, String mobile, String email, Consumer<HashMap<String, Object>> consumer) {
        HashMap<String, Object> params = new HashMap<>();
        if (consumer != null) consumer.accept(params);

        params.put("title", title);
        params.put("userId", userId);
        params.put("mobile", mobile);
        params.put("email", email);
        pushMessageQueue(nid, params);
    }

    /**
     * 发送消息
     *
     * @param nid    消息模板标识（必填）
     * @param params 模板内容参数（必填）
     * @param title  发送主题（必填）
     * @param userId 接收用户（必填）
     * @param mobile 手机号（需要发送短信时必填）
     * @param email  邮箱（需要发送邮件时必填）
     * @param agentId  代理商Id（必填）
     */
    public void pushMessageQueue(String nid, HashMap<String, Object> params, String title, Integer userId, String mobile, String email,Integer agentId) {
        params.put("title", title);
        params.put("userId", userId);
        params.put("mobile", mobile);
        params.put("email", email);
        params.put("agentId", agentId);
        pushMessageQueue(nid, params);
    }

    /**
     * 加入队列
     *
     * @param nid
     * @param params
     */
    private void pushMessageQueue(String nid, HashMap<String, Object> params) {
        MsgTemplate template = redisService.get(CacheID.TEMPLATE_MESSAGE_PREFIX + nid, MsgTemplate.class);
        if (template == null) {
            log.info("信息模板为空,key:" + CacheID.TEMPLATE_MESSAGE_PREFIX + nid);
            return;
        }

        // 是否需要发送站内信
        if (State.YES == template.getLetterState()) {
            NoticeLog notice = new NoticeLog();
            notice.setNid(nid);
            notice.setType(MsgType.LETTER);
            notice.setTitle((String) params.get("title"));
            notice.setUserId((Integer) params.get("userId"));
            notice.setContent(StringUtils.replace(template.getLetterContent(), params));
            notice.setReadFlag(NoticeLog.READ_FLAG.NOT.code);
            redisService.push(CacheID.QUEUE_MESSAGE, JsonUtils.toJson(notice));
        }
        // 是否需要发送短信
        if (State.YES == template.getSmsState() && StringUtils.isNotBlank(params.get("mobile"))) {
            NoticeLog notice = new NoticeLog();
            notice.setNid(nid);
            notice.setType(MsgType.SMS);
            notice.setTitle((String) params.get("title"));
            notice.setUserId((Integer) params.get("userId"));
//            msgTemplate.setAgentId((Integer) params.get("agentId"));
            notice.setReceiveAddress((String) params.get("mobile"));
            notice.setContent(StringUtils.replace(template.getSmsContent(), params));
            redisService.push(CacheID.QUEUE_MESSAGE, JsonUtils.toJson(notice));
        }
        // 是否需要发送邮件
        if (State.YES == template.getEmailState() && StringUtils.isNotBlank(params.get("email"))) {
            NoticeLog notice = new NoticeLog();
            notice.setNid(nid);
            notice.setType(MsgType.EMAIL);
            notice.setTitle(template.getEmailTitle());
            notice.setUserId((Integer) params.get("userId"));
            notice.setReceiveAddress((String) params.get("email"));
            notice.setContent(StringUtils.replace(template.getEmailContent(), params));
            redisService.push(CacheID.QUEUE_MESSAGE, JsonUtils.toJson(notice));
        }

    }


}
