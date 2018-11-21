package com.mi.hundsun.oxchains.base.core.constant;

/**
 * 消息模板标识常量
 *
 * @author Vector
 * @date 2017/5/22
 */
public interface MsgTempNID {
    /** 统一短信验证码 */
    String VALIDATE_CODE = "validate_code";
    /**注册成功*/
    String REGISTER_SUCCESS  = "register_success";
    /**绑定手机号码成功*/
    String BIND_MOBILE = "bind_mobile";
    /**绑定绑定邮箱成功*/
    String BIND_EMAIL = "bind_email";
    /**找回登录密码成功 */
    String RETRIEVE_LOGIN_PWD = "retrieve_login_pwd";
    /**设置提币密码成功 */
    String SET_MENTION_COIN_PWD = "set_mention_coin_pwd";
    /**修改登录密码成功 */
    String MODIFY_LOGIN_PWD = "modify_login_pwd";
    /**修改提币密码成功  */
    String MODIFY_MENTION_COIN_PWD = "modify_mention_coin_pwd";
    /**找回提币密码成功  */
    String RETRIEVE_MENTION_COIN_PWD = "retrieve_mention_coin_pwd";
    /**绑定谷歌验证器成功  */
    String BIND_GOOGLE_KEY = "bind_google_key";
    /**修改谷歌验证器成功  */
    String MODIFY_GOOGLE_KEY = "modify_google_key";
    /**关闭谷歌验证器成功 */
    String CLOSE_GOOGLE_KEY = "close_google_key";
    /**开启谷歌验证器成功 */
    String OPEN_GOOGLE_KEY = "open_google_key";
    /**提币成功 */
    String MENTION_COIN_SUCCESS = "mention_coin_success";
    /**充币成功 */
    String RECHARGE_COIN_SUCCESS = "recharge_coin_success";
}
