package com.mi.hundsun.oxchains.base.core.model.user;

import com.mi.hundsun.oxchains.base.core.po.user.UserIdentify;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import lombok.Data;

@Data
public class UserIdentifyModel extends UserIdentify {
    private Users u;
}
