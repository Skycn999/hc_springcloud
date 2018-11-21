package com.mi.hundsun.oxchains.base.core.api.sendEmail;

import com.mi.hundsun.oxchains.base.common.utils.SpringContextUtils;
import com.mi.hundsun.oxchains.base.core.constant.ConfigNID;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * 发送邮件工具类
 * ......................我佛慈悲......................
 * ......................_oo0oo_.......................
 * .....................o8888888o......................
 * .....................88" . "88......................
 * .....................(| -_- |)......................
 * .....................0\  =  /0......................
 * ...................___/`---'\___....................
 * ..................' \\|     |// '...................
 * ................/ \\|||  :  |||// \.................
 * .............../ _||||| -卍-|||||- \................
 * ..............|   | \\\  -  /// |   |...............
 * ..............| \_|  ''\---/''  |_/ |...............
 * ..............\  .-\__  '-'  ___/-. /...............
 * ............___'. .'  /--.--\  `. .'___.............
 * .........."" '<  `.___\_<|>_/___.' >' ""............
 * ........| | :  `- \`.;`\ _ /`;.`/ - ` : | |.........
 * ........\  \ `_.   \_ __\ /__ _/   .-` /  /.........
 * ....=====`-.____`.___ \_____/___.-`___.-'=====......
 * ......................`=---='.......................
 * ....................................................
 * .................佛祖开光 ,永无BUG...................
 */
@Slf4j
public class SendEmailUtils {

    /**
     * 发送邮件
     *
     * @param title
     * @param content
     * @param email
     * @throws Exception
     */
    public static void sendEmail(String title, String content, String email) throws Exception {

        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        //发件人邮箱
        final String myEmailAccount = redisService.get(ConfigNID.EMAIL_ACCOUNT);
        log.info(myEmailAccount);
        //发件人邮箱密码
        final String myEmailAccountPwd = redisService.get(ConfigNID.EMAIL_ACCOUNT_PWD);
        log.info(myEmailAccountPwd);
        //发件人姓名
        final String myEmailAccountName = redisService.get(ConfigNID.EMAIL_ACCOUNT_NAME);
        log.info(myEmailAccountName);

        // 1. 创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", redisService.get(ConfigNID.EMAIL_SMTP_ADDRESS));   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        props.setProperty("mail.smtp.ssl.enable", "true");
        log.info(props.toString());

        // SMTP 服务器的端口 (非 SSL 连接的端口一般默认为 25, 可以不添加, 如果开启了 SSL 连接,
        //                  需要改为对应邮箱的 SMTP 服务器的端口, 具体可查看对应邮箱服务的帮助,
        //                  QQ邮箱的SMTP(SSL)端口为465或587, 其他邮箱自行去查看)
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 帐号为邮箱帐号，密码为邮箱密码，用邮箱密码时可能会报错，    如果报错，将密码换成授权码即可，授权码是用来登录邮件客
                // 户端的。建议用163邮箱测试，如果是QQ邮箱，测试前要开启    SMTP服务，默认是关闭的
                return new PasswordAuthentication(myEmailAccount, myEmailAccountPwd);
            }
        };

        // 2. 根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getInstance(props);
        session.setDebug(false);// 设置为debug模式, 可以查看详细的发送 log
        log.info("session:" + session.toString());

        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, myEmailAccount, myEmailAccountName, email, title, content);
        log.info("message:" + message.toString());
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        log.info("transport:", transport.toString());

        // 5. 使用 邮箱账号 和 密码 连接邮件服务器, 这里认证的邮箱必须与 message 中的发件人邮箱一致, 否则报错
        transport.connect(myEmailAccount, myEmailAccountPwd);
        log.info("5");
        // 6. 发送邮件, 发到所有的收件地址, message.getAllRecipients() 获取到的是在创建邮件对象时添加的所有收件人, 抄送人, 密送人
        transport.sendMessage(message, message.getAllRecipients());
//        Transport.send(message);
        log.info("6");
        // 7. 关闭连接
        transport.close();
    }

    /**
     * @param session       和服务器交互的会话
     * @param sendMail      发件人邮箱
     * @param sendEmailName 发件人姓名
     * @param receiveMail   收件人邮箱
     * @param title         邮件主题
     * @param content       邮件内容
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, String sendEmailName, String receiveMail, String title, String content) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);

        // 2. From: 发件人（昵称有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改昵称）
        message.setFrom(new InternetAddress(sendMail, sendEmailName, "UTF-8"));

        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, "UTF-8"));

        // 4. Subject: 邮件主题（标题有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改标题）
        message.setSubject(title, "UTF-8");

        // 5. Content: 邮件正文（可以使用html标签）（内容有广告嫌疑，避免被邮件服务器误认为是滥发广告以至返回失败，请修改发送内容）
        message.setContent(content, "text/html;charset=UTF-8");

        // 6. 设置发件时间
        message.setSentDate(new Date());

        // 7. 保存设置
        message.saveChanges();

        return message;
    }

}
