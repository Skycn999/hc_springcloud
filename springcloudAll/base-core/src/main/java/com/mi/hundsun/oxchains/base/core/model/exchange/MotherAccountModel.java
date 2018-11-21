package com.mi.hundsun.oxchains.base.core.model.exchange;

import com.mi.hundsun.oxchains.base.core.model.quote.model.AccountBalance;
import com.mi.hundsun.oxchains.base.core.po.exchange.Exchange;
import com.mi.hundsun.oxchains.base.core.po.exchange.MotherAccount;
import lombok.Data;

import java.util.List;


@Data
public class MotherAccountModel extends MotherAccount{

    private Exchange ex;

    private List<AccountBalance> aBalances;
}
