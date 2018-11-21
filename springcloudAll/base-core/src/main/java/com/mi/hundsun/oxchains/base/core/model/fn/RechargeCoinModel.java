package com.mi.hundsun.oxchains.base.core.model.fn;

import com.mi.hundsun.oxchains.base.core.po.fn.RechargeCoin;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import lombok.Data;

/**
 * 充币管理model
 */
@Data
public class RechargeCoinModel extends RechargeCoin {
    private Users u;
}
