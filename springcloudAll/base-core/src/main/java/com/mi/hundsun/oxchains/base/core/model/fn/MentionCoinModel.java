package com.mi.hundsun.oxchains.base.core.model.fn;

import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import lombok.Data;

/**
 * 提币管理model
 */
@Data
public class MentionCoinModel extends MentionCoin {
    private Users u;
}
