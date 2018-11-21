package com.mi.hundsun.oxchains.base.core.mapper.user;


import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.user.UserRiskControlModel;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 用户风控设置相关Dao接口<br>
 *
 * @author bin
 * @date   2018-04-12 07:55:12
 */
public interface UserRiskControlMapper extends GenericMapper< UserRiskControl,Integer> {

    @Select("SELECT t.*,u.email AS 'u.email',u.realname AS 'u.realname',u.mobile AS 'u.mobile',btcFee.tpl_name AS 'btcFee.tplName',ethFee.tpl_name AS 'ethFee.tplName',netWorth.tpl_name AS 'netWorth.tplName',percent.tpl_name AS 'percent.tplName' FROM user_risk_control t LEFT JOIN users u ON u.id = t.user_id LEFT JOIN tpl_service_fee btcFee ON btcFee.tpl_no = t.btc_service_charge_tpl" +
            " LEFT JOIN tpl_service_fee ethFee ON ethFee.tpl_no = t.eth_service_charge_tpl LEFT JOIN tpl_net_worth_control netWorth ON netWorth.tpl_no = t.net_worth_tpl LEFT JOIN tpl_percent_control percent ON percent.tpl_no = t.percent_tpl WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize} ")
    List<UserRiskControlModel> findListBySqlJoin(Map<String, Object> params);

    @Select("SELECT count(1)  FROM user_risk_control t LEFT JOIN users u  ON u.id = t.user_id LEFT JOIN tpl_service_fee btcFee ON btcFee.tpl_no = t.btc_service_charge_tpl LEFT JOIN tpl_service_fee ethFee ON ethFee.tpl_no = t.eth_service_charge_tpl LEFT JOIN tpl_net_worth_control netWorth ON netWorth.tpl_no = t.net_worth_tpl LEFT JOIN tpl_percent_control percent ON percent.tpl_no = t.percent_tpl WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);

}
