package com.mi.hundsun.oxchains.base.core.model.user;

import com.mi.hundsun.oxchains.base.core.po.user.UserAddress;
import com.mi.hundsun.oxchains.base.core.po.user.Users;
import lombok.Data;

@Data
public class UserAddressModel extends UserAddress {
    private Users u;
}
