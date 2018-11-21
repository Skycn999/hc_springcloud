package com.mi.hundsun.oxchains.base.core.model.user;

import com.mi.hundsun.oxchains.base.core.po.user.UserFreeze;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import lombok.Data;

@Data
public class UserFreezeModel extends UserFreeze {

    private Users u;
}
