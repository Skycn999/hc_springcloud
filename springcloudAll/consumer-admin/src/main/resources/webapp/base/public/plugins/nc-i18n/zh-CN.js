/**
 * 语言文件
 * Created by cj on 2015/11/20.
 * 使用方法举例：
 * $lang.login.username
 * $lang['login']['username']
 */
(function (window) {
    window.$lang = window.$lang || {
        login: {
            "username": "用戶名",
            "password": "密码"
        },
        brand: {
            "applyState1": "审核通过",
            "applyState0": "等待审核",
            "applyState10": "审核失败",
            "showType0": "文字",
            "showType1": "图片",
            "isRecommend1": "是",
            "isRecommend0": "否"

        },
        member: {
            "memberStateOpen": "开启",
            "memberStateClose": "关闭",
            "allowBuy1": "允许",
            "allowBuy0": "禁止",
            "allowTalk1": "允许",
            "allowTalk0": "禁止",
            "memberSex0": "保密",
            "memberSex1": "男",
            "memberSex2": "女"
        },
        seller: {
            "allowLogin": "允许登录",
            "notAllowLogin": "禁止登录",
        },
        shopCompany: {
            "shipTypeState1": "是",
            "shipTypeState0": "否",
            "shipStateState1": "开启",
            "shipStateState0": "关闭"
        },
        article: {
            "recommendState1": "是",
            "recommendState0": "否",
            "allowDelete1": "是",
            "allowDelete0": "否"
        },
        predeposit: {
            "rechargeStateNotpay": "未支付",
            "rechargeStatePaid": "已支付",
            "cashStateNotDealwith": "未处理",
            "cashStateSuccess": "已支付",
            "cashStateFail": "拒绝提现"
        },
        store: {
            "open": "开启",
            "close": "关闭"
        },
        goods: {
            "own": "自营",
            "other": "三方",
            "goodsState0": "仓库中",
            "goodsState1": "出售中",
            "goodsState10": "违规禁售",
            "goodsVerify0": "等待审核",
            "goodsVerify1": "审核通过",
            "goodsVerify10": "审核失败"
        },
        paymentState: {
            "open": "开启",
            "close": "关闭"
        },
        messageTemplateState: {
            "open": "开启",
            "close": "关闭"
        },
        direction: {
            "payment": "支出",
            "receive": "收入"
        },
        bankStatus: {
            "audit": "待审核",
            "pass": "通过",
            "unpass": "不通过"
        },
        configType: {
            "BOTTOM": "系统底层配置",
            "FEES": "费率信息配置",
            "NOTICE": "邮件/短信配置",
            "THIRD": "三方接口配置",
            "OTHER": "附属信息配置"
        },
        bannerState:{
           "unpublished":"未发布",
           "published":"已发布",
           "revoke":"已撤回"
        },
        templateType: {
            "SYS": "系统消息",
            "BIZ": "业务消息"
        },
        state: {
            "OPEN": "启用",
            "CLOSE": "停用"
        },
        realNameStatus: {
            "NO": "未认证",
            "WAIT": "待审核",
            "PASS": "通过",
            "NOPASS": "未通过"
        },
        mobilePhoneStatus: {
            "NO": "未认证",
            "WAIT": "待审核",
            "PASS": "通过",
            "NOPASS": "未通过"
        },
        emailStatus: {
            "NO": "未认证",
            "WAIT": "待审核",
            "PASS": "通过",
            "NOPASS": "未通过"
        },
        vipStatus: {
            "NO": "未认证",
            "WAIT": "待审核",
            "PASS": "通过",
            "NOPASS": "未通过"
        },
        realNameType: {
            "interface": "接口认证",
            "manual": "人工认证"
        },
        bankType: {
            "interface": "接口认证",
            "manual": "人工认证"
        },
        freezeStatus: {
            "yes": "冻结",
            "no": "未冻结"
        },
        showStatus: {
            "show": "显示",
            "hide": "隐藏"
        },
        replyType: {
            "picture": "图文",
            "content": "文字"
        },
        articleStatus: {
            "WAIT": "待发布",
            "PUBLISHED": "已发布",
            "BACK": "已撤回",
        },
        anoType: {
            "SYSTEM": "系统公告",
            "PRODUCT": "产品公告",
        },
        orderType: {
            "BUY": "购买",
            "SELL": "转让",
            "CANCLE": "撤回",
        },
        sex: {
            "MALE": "男",
            "FEMALE": "女"
        },
        purchaseStatus: {
            "HASREPORT": "申购中",
            "PENDINGDEBIT": "已中签",
            "SUCCESS": "扣款成功",
            "FAILED": "未中签"
        },
        rechargeType: {
            "ONLINE": "线上充值",
            "OFFLINEALIPAY": "线下支付宝",
            "OFFLINEWEIXIN": "线下微信",
            "OFFLINETRANSFER": "现下转账"
        },
        accountLogType: {
            "T1": "充值",
            "T2": "提现冻结",
            "T4": "充值失败退回",
            "T8": "提现解冻",
            "T9": "提现扣款",
            "T10": "提现退回",
            "T11": "推广佣金",
            "T12": "手动入金",
            "T13": "手动出金",
            "T15": "冻结交易综合费",
            "T16": "解冻交易综合费",
            "T17": "扣除交易综合费",
            "T18": "冻结履约保证金",
            "T19": "解冻履约保证金",
            "T21": "冻结止盈保证金",
            "T22": "解冻止盈保证金",
            "T25": "结算收入",
            "T26": "结算扣除",
            "T27": "穿仓扣除"
        },
        userFreezeState:{
            "FROZEN":"冻结",
            "UNBLOCKED":"正常"
        },
        purchaseType: {
            "NEWPURCHASE": "新人申购",
            "ADDPURCHASE": "增发申购"
        },
        reviewStatus: {
            "PASS": "审核通过",
            "NOTPASS": "审核不通过"
        },
        userType: {
            "T1": "点买人",
            "T2": "投资人",
            "T3": "操盘手"
        },
        useStatus: {
            "T1": "可用",
            "T2": "已使用",
            "T3": "已失效",
        },
        idType: {
             "T0":" ",
             "T1":"身份证",
             "T2":"护照",
             "T3":" ",

        },
        realNameAuthentication: {
            "T1":"未认证",
            "T2":"待认证",
            "T3":"已认证",
            "T4":"认证不通过"
        },
        usersState :{
             "T1":"正常",
             "T2":"登录冻结",
             "T3":"注销"
        },
        country: {
            "T0":" ",
            "T1":"境内",
            "T2":"境外",
            "T3":" "
        },
        rechargeStatus: {
            "T1": "待处理",
            "T2": "待审核",
            "T3": "充值成功",
            "T4": "充值失败"
        },
        coinType: {
          "T1":"BTC",
          "T2":"ETH",
          "T3":"USDT"
        },
        rechargeType: {
            "T1": "快捷支付",
            "T2": "支付宝"
        },
        cashStatus: {
            "T1": "待处理",
            "T2": "待审核",
            "T3": "待出账",
            "T4": "提现成功",
            "T5": "提现失败",
            "T6": "提现取消"
        },
        bearParty: {
            "T1": "用户",
            "T2": "平台"
        },
        cardType:{
            "DEBIT":"借记卡",
            "CREDIT":"信用卡",
        },
        noticeStatus:{
            "SUCCESS":"发送成功",
            "FAIL":"发送失败",
        },
        noticeType:{
            "EMAIL":"邮件",
            "SMS":"短信",
            "LETTER":"站内信",
        },
        pdfStatus:{
            "ENABLE":"启用",
            "DISABLE":"禁用",
        },
        pdfType:{
            "INVEST":"投资协议",
            "REGISTER":"注册协议",
            "TRANSFER":"转让协议",
        },
        ruleType:{
            "AMOUNT":"固定金额",
            "PROPORT":"比例发放",
        },
        riskLevel:{
            "CONSERVATIVE":"保守型",
            "STEADY":"稳健型",
            "BALANCE":"平衡型",
            "GROWUP":"成长型",
            "ENTERPRISING":"进取型",
        },
        payType:{
            "T1":"快捷支付",
            "T2":"支付宝",
            "T3":"微信",
            "T4":"余额支付"
        },
        netWorthControlState:{
           "T1":"启用",
           "T2":"停用"
        },
        codePairConfigType:{
            "MAIN":"主流",
            "NON_MAIN":"非主流"
        },
        codePairConfigState:{
            "T1":"停用",
            "T2":"启用"
        },
        isDisplayOnApp:{
            "NO":"否",
            "YES":"是"
        },
        orderStatus:{
            "T1":"待接单",
            "T2":"委托中",
            "T3":"持仓中",
            "T4":"平仓中",
            "T5":"清算中",
            "T6":"已清算",
            "T7":"流单中",
            "T8":"已流单",
            "T9":"撤单中",
            "T10":"已撤单"
        },
        settleWay:{
            "T1":"市价",
            "T2":"限价"
        },
        follow:{
            "T1":"点买单",
            "T2":"跟单"
        },
        investorStatus:{
            "T1":"正常",
            "T2":"冻结"
        },
        invoiceType:{
            "T1":"纸质发票",
            "T2":"电子发票"
        },
        rewardType:{
            "T1":"手机",
            "T2":"MIFI"
        },
        finishFlag:{
            "T1":"未完成",
            "T2":"完成"
        },
        realNameStatus:{
            "T1":"未认证",
            "T2":"审核中",
            "T3":"通过",
            "T4":"不通过"
        },
        bankRefuseStatus:{
            "T1":"审核中",
            "T2":"审核失败",
            "T3":"审核通过",
        },
        freezeStatus:{
            "T1":"未冻结",
            "T2":"冻结"
        },
        commonStatus:{
            "ENABLE":"启用",
            "DISABLE":"禁用"
        },
        protocolState:{
             "enable":"启用",
             "disable":"停用"
        },
        announcementState:{
             "unpublished":"未发布",
             "published":"已发布",
             "revoke":"已撤回"
        },
        commonAuditStatus:{
            "PENDING":"待审核",
            "PASS":"通过",
            "NOPASS":"不通过"
        },
        delegationOrigin:{
            "PC":"PC",
            "IOS":"ios",
            "ANDROID":"安卓"
        },
        dealIsConfirm:{
            "YES":"是",
            "NO":"否",
        },
        delegationStyle:{
            "MARKET":"市价委托",
            "LIMITED":"限价委托"
        },
        delegationDirection:{
            "BUYIN":"买入",
            "SELLOUT":"卖出"
        },
        delegationState:{
            "REPORTED":"下单申请(已报)",
            "COMMISSIONEDIN":"委托中",
            "FINISHED":"已完成",
            "FAILED":"失败"
        },
        subDelegationState:{
            "REPORTED":"下单申请(已报)",
            "TRADING":"交易中",
            "REVOKING":"撤单中",
            "REVOKED":"已完全撤单",
            "PART_OF_REVOKE":"部分撤单",
            "PART_OF_DEAL":"部分成交",
            "DEAL":"已成交",
            "FAILED":"下单失败"
        },
        subDelegationIsConfirm:{
            "NO":"否",
            "YES":"是"
        },
        mentionCoinStatus:{
            "PENDING":"待审核",
            "PEND_ENTER":"待录入",
            "NO_PASS":"不通过",
            "SUCCESS":"已成功"
        },
        platUserAddressStatus:{
            "UNDISTRIBUTED":"未分配",
            "DISTRIBUTED":"已分配",
        },
        platAssetLogDirection:{
            "TOACCOUNT":"平台到母账号",
            "TOPLATFORM":"母账号到平台"
        },
        platAssetLogStatus:{
            "NORMAL":"正常",
            "DELETE":"作废"
        },
        isDefault:{
            "NO":"否",
            "YES":"是"
        },
        refundStatus:{
            "T10":"申请中",
            "T20":"卖家已同意",
            "T30":"买家已退货",
            "T40":"退货退款成功",
            "T50":"卖家不同意",
        },
        refundType:{
            "T1":"仅退款",
            "T2":"退货退款",
            "T3":"全单退款"
        },
        goodsContentType:{
            "T1":"图片",
            "T2":"文字"
        },
        agentStatus:{
            "T1":"正常",
            "T2":"冻结"
        },
        adminType:{
            "T1":"管理员",
            "T2":"投资人",
            "T3":"代理商"
        },
        questionStatus:{
            "T1":"未回复",
            "T2":"已回复"
        },
        OrderDirection:{
            "T1":"买涨",
            "T2":"买跌"
        },
        SettleType:{
            "T1":"自主平仓",
            "T2":"触发止盈平仓",
            "T3":"触发止损平仓",
            "T4":"日内强平",
            "T5":"后台强平"
        }
    };
})(window);
