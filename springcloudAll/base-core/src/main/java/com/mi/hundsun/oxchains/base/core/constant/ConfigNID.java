package com.mi.hundsun.oxchains.base.core.constant;

/**
 * 系统参数常量  类型1,系统底层配置:bottom;2,费率信息配置:fees;3,邮件/短信配置:msgTemplate;4,三方接口配置:third;5,附加功能配置:other
 *
 * @author Mr.
 * @date 2017/5/22
 */
public interface ConfigNID {

    /******************************** 1 系统底层配置 ***********************************/
    /** 网站名称 */
    String WEB_NAME = CacheID.CONFIG_PREFIX + "web_name";
    /** 网站备案号 */
    String COPY_RIGHT = CacheID.CONFIG_PREFIX + "copy_right";
    /** 前台服务地址 */
    String WEB_SERVER_URL = CacheID.CONFIG_PREFIX + "web_server_url";
    /** 文件服务器地址 */
    String IMAGE_SEVER_URL = CacheID.CONFIG_PREFIX + "image_server_url";
    /** 后台服务器地址 */
    String ADMIN_SEVER_URL = CacheID.CONFIG_PREFIX + "admin_server_url";
    /** 静态资源地址 */
    String STATIC_SERVER_URL = CacheID.CONFIG_PREFIX + "static_server_url";
    /** 支付同步回调地址 */
    String PAY_RETURN_URL = CacheID.CONFIG_PREFIX + "pay_return_url";
    /** 是否开通万能验证码 9999 */
    String SUPER_VALIDATE_OPEN = CacheID.CONFIG_PREFIX + "super_validate_open";
    /**  第三方实名认证appcode */
    String APPCODE = CacheID.CONFIG_PREFIX + "appcode";
    /** 平台签名 */
    String WEB_SIGN = CacheID.CONFIG_PREFIX + "web_sign";





    /******************************** 2 费率信息配置 ***********************************/


    /******************************** 3 邮件/短信配置 ***********************************/
    /** 是否开通短信功能 */
    String SMS_OPEN = CacheID.CONFIG_PREFIX + "sms_open";
    /** 短信验证码有效期（单位：分钟） */
    String VERIFY_CODE_TIME = CacheID.CONFIG_PREFIX + "verify_code_time";
    /** 邮箱smtp地址 */
    String EMAIL_SMTP_ADDRESS = CacheID.CONFIG_PREFIX + "email_smtp_address";
    /** 邮箱发件人邮箱 */
    String EMAIL_ACCOUNT = CacheID.CONFIG_PREFIX + "email_account";
    /** 邮箱发件人邮箱密码 */
    String EMAIL_ACCOUNT_PWD = CacheID.CONFIG_PREFIX + "email_account_pwd";
    /** 邮箱发件人邮箱名称 */
    String EMAIL_ACCOUNT_NAME = CacheID.CONFIG_PREFIX + "email_account_name";
    /** 短信 创蓝 用户名 */
    String SMS_CL_USERNAME = CacheID.CONFIG_PREFIX + "sms_cl_username";
    /** 短信 创蓝 密码 */
    String SMS_CL_PWD = CacheID.CONFIG_PREFIX + "sms_cl_pwd";
    /** 短信 创蓝 地址 */
    String SMS_CL_URL = CacheID.CONFIG_PREFIX + "sms_cl_url";

    /** 短信 助通 用户名 */
    String SMS_ZT_USERNAME = CacheID.CONFIG_PREFIX + "sms_zt_username";
    /** 短信 助通 密码 */
    String SMS_ZT_PWD = CacheID.CONFIG_PREFIX + "sms_zt_pwd";
    /** 短信 助通 地址 */
    String SMS_ZT_URL = CacheID.CONFIG_PREFIX + "sms_zt_url";



    /******************************** 4 三方接口配置 ***********************************/


    /******************************** 5 附加功能配置 ***********************************/

}
