package com.mi.hundsun.oxchains.base.core.model.user;

import com.mi.hundsun.oxchains.base.core.po.tpl.NetWorthControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.PercentControl;
import com.mi.hundsun.oxchains.base.core.po.tpl.ServiceFee;
import com.mi.hundsun.oxchains.base.core.po.user.UserRiskControl;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import lombok.Data;

/**
 * 风控设置model
 */
@Data
public class UserRiskControlModel extends UserRiskControl {

    private Users u;

    private ServiceFee btcFee;

    private ServiceFee ethFee;

    private NetWorthControl netWorth;

    private PercentControl percent;


}
