package com.mi.hundsun.oxchains.base.core.api.sendEmail;

public class MyEmailSendUtil {
    public static void sendEmail(String title, String content, String email){
//        RedisService redisService = SpringContextUtils.getBean(RedisService.class);
//        //发件人邮箱
//        final String myEmailAccount = redisService.get(ConfigNID.EMAIL_ACCOUNT);
//        //发件人邮箱密码
//        final String myEmailAccountPwd = redisService.get(ConfigNID.EMAIL_ACCOUNT_PWD);
//        //发件人姓名
//        final String myEmailAccountName = redisService.get(ConfigNID.EMAIL_ACCOUNT_NAME);
//        //邮件服务器
//        final String serverHost = redisService.get(ConfigNID.EMAIL_SMTP_ADDRESS);
//        //这个类主要是设置邮件
//        MailSenderInfo mailInfo = new MailSenderInfo();
//        mailInfo.setMailServerHost(serverHost);
//        mailInfo.setMailServerPort("465");
//        mailInfo.setValidate(true);
//        mailInfo.setUserName(myEmailAccountName);
//        mailInfo.setPassword(myEmailAccountPwd);//您的邮箱密码
//        mailInfo.setFromAddress(myEmailAccount);
//        mailInfo.setToAddress(email);
//        mailInfo.setSubject(title);
//        mailInfo.setContent(content);
//        //这个类主要来发送邮件
//        SimpleMailSender sms = new SimpleMailSender();
//        sms.sendTextMail(mailInfo);//发送文体格式
//        sms.sendHtmlMail(mailInfo);//发送html格式
    }
}
