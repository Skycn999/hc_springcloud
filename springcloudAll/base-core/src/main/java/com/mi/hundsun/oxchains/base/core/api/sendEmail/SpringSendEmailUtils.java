package com.mi.hundsun.oxchains.base.core.api.sendEmail;

import com.mi.hundsun.oxchains.base.common.utils.SpringContextUtils;
import com.mi.hundsun.oxchains.base.core.constant.ConfigNID;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * spring发邮件
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
public class SpringSendEmailUtils {
    private static JavaMailSenderImpl mailSender = createMailSender();

    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);

        //邮件服务器
        final String host = redisService.get(ConfigNID.EMAIL_SMTP_ADDRESS);
        //发件人邮箱
        final String myEmailAccount = redisService.get(ConfigNID.EMAIL_ACCOUNT);
        log.info(myEmailAccount);
        //发件人邮箱密码
        final String myEmailAccountPwd = redisService.get(ConfigNID.EMAIL_ACCOUNT_PWD);
        log.info(myEmailAccountPwd);
        //发件人姓名
        final String myEmailAccountName = redisService.get(ConfigNID.EMAIL_ACCOUNT_NAME);

        log.info(myEmailAccountName);
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(25);
        sender.setUsername(myEmailAccount);
        sender.setPassword(myEmailAccountPwd);
        sender.setDefaultEncoding("Utf-8");
        sender.setProtocol("smtp");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", "25000");
        p.setProperty("mail.smtp.auth", "true");
        final String smtpPort = "465";
        p.setProperty("mail.smtp.port", smtpPort);
        p.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        p.setProperty("mail.smtp.socketFactory.fallback", "false");
        p.setProperty("mail.smtp.socketFactory.port", smtpPort);
        sender.setJavaMailProperties(p);
        return sender;
    }

    /**
     * 发送邮件
     *
     * @param toEmail 接受人
     * @param title 主题
     * @param content
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendHtmlMail(String toEmail, String title, String content) throws MessagingException,UnsupportedEncodingException {
        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
        //发件人邮箱
        final String myEmailAccount = redisService.get(ConfigNID.EMAIL_ACCOUNT);
        //发件人姓名
        final String myEmailAccountName = redisService.get(ConfigNID.EMAIL_ACCOUNT_NAME);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(myEmailAccount, myEmailAccountName);
        messageHelper.setTo(toEmail);
        messageHelper.setSubject(title);
        messageHelper.setText(content, true);
        mailSender.send(mimeMessage);
    }
}
