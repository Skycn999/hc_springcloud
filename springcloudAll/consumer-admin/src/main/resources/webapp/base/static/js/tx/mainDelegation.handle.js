//列表开始
var dtGridColumns = [
    {
        id: 'delFlag',
        type: 'int',
        hideQuery:true,
        hideQueryType:'eq',
        hideQueryValue:0,
        hide:true
    },
    {
        id: 'delegateNo',
        title: '委托编号',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'mobile',
        title: '手机号码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'origin',
        title: '委托来源',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.delegationOrigin.PC,
            2: $lang.delegationOrigin.IOS,
            3: $lang.delegationOrigin.ANDROID
        }
    },
    {
        id: 'style',
        title: '委托方式',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.delegationStyle.MARKET,
            2: $lang.delegationStyle.LIMITED
        }
    },
    {
        id: 'direction',
        title: '委托方向',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.delegationDirection.BUYIN,
            2: $lang.delegationDirection.SELLOUT
        }
    },
    {
        id: 'state',
        title: '委托状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.delegationState.REPORTED,
            2: $lang.delegationState.COMMISSIONEDIN,
            3: $lang.delegationState.REVOKING,
            4: $lang.delegationState.FAILED
        }
    },
    {
        id: 'coinCurrency',
        title: '币种',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'coinCode',
        title: '资产代码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'price',
        title: '委托价',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'amount',
        title: '委托量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'createTime',
        title: '委托时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center width-180',
        columnClass: 'text-center width-180',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var detail = "<a data-target='#detailModal' class='btn btn-sm btn-success' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-eye'></i>&nbsp;详情&nbsp;</a>";
            var revokeHtml = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='OperateHandle.revokeConfig(" + record.id + ")' ><i class='fa fa-reply'></i>&nbsp;撤单&nbsp;</a>";
            var html = '';
            // 判断是否有撤单权限
            if ($("#revokePermi") && $("#revokePermi").val() == 1) {
                if (record.state == 1||record.state == 2) {
                html += revokeHtml
                }
            }
            // 判断是否有查看详情权限
            if ($("#detailPermi") && $("#detailPermi").val() == 1) {
                html += detail;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'mainDelegation/json/list',
    exportURL: ncGlobal.adminRoot + 'mainDelegation/json/export',
    exportFileName: '委托管理列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery|export[excel]',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id","origin","style","direction", "state","delFlag"], Timestamp: ["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['desc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//列表结束

// 子委托列表开始
var dtGridColumnsSub = [
    {
        id: 'delFlag',
        type: 'int',
        hideQuery: true,
        'export': false,
        hideQueryType: 'eq',
        hideQueryValue: 0,
        hide: true
    },
    {
        id: 'entrustNo',
        title: '子委托编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center ',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'price',
        title: '委托价',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'amount',
        title: '委托量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'createTime',
        title: '委托时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'motherAccount',
        title: '交易所母账号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center ',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'state',
        title: '委托状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.subDelegationState.REPORTED,
            2: $lang.subDelegationState.TRADING,
            3: $lang.subDelegationState.REVOKING,
            4: $lang.subDelegationState.REVOKED,
            5: $lang.subDelegationState.PART_OF_REVOKE,
            6: $lang.subDelegationState.PART_OF_DEAL,
            7: $lang.subDelegationState.DEAL,
            8: $lang.subDelegationState.FAILED
        }
    },
    {
        id: 'isConfirm',
        title: '委托状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
        fastQueryType: 'eq',
        codeTable: {
            0: $lang.subDelegationState.NO,
            1: $lang.subDelegationState.YES
        }
    },
    {
        id: 'motherAccount',
        title: '委托交易所',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center ',
        fastSort: false,
        fastQuery: false
    }
];
//子委托列表
var dtGridOptionSub = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'subDelegation/json/list',
    columns: dtGridColumnsSub,
    gridContainer: 'dtGridContainerSub',
    toolbarContainer: 'dtGridToolBarContainerSub',
    pageSize: 100,
    fastQueryWindowIsInit:false,
    pageSizeLimit: [10, 20, 100]
};

//子委托列表
var grid1 = $.fn.DtGrid.init(dtGridOptionSub);

//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {

        //详情对话框初始化
        $("#detailModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            //清除错误信息
            modal.find('input[name="realname"]').val(gridData.realname);
            modal.find('input[name="mobile"]').val(gridData.mobile);
            if(gridData.idType == 1){
                modal.find('input[name="idType"]').val("身份证");
            }else {
                modal.find('input[name="idType"]').val("护照");
            }
            modal.find('input[name="idNo"]').val(gridData.idNo);
            modal.find('input[name="delegateNo"]').val(gridData.delegateNo);
            if(gridData.style == 1){
                modal.find('input[name="style"]').val("市价委托");
            }else {
                modal.find('input[name="style"]').val("限价委托");
            }
            modal.find('input[name="coinCurrency"]').val(gridData.coinCurrency);
            modal.find('input[name="createTime"]').val(gridData.createTime);
            modal.find('input[name="coinCode"]').val(gridData.coinCode);
            modal.find('input[name="amount"]').val(gridData.amount);
            modal.find('input[name="price"]').val(gridData.price);
            modal.find('input[name="gmv"]').val(gridData.gmv);
            if(gridData.state == 1){
                modal.find('input[name="state"]').val("下单申请(已报)");
            }else if(gridData.state == 2){
                modal.find('input[name="state"]').val("委托中");
            }else if(gridData.state == 3){
                modal.find('input[name="state"]').val("已完成");
            }else if(gridData.state == 4){
                modal.find('input[name="state"]').val("失败");
            }
            modal.find('input[name="createTime"]').val(gridData.createTime);
            //加载子委托列表
            grid1.fastQueryParameters = new Object();
            grid1.fastQueryParameters['eq_delFlag'] = 0;
            grid1.fastQueryParameters['eq_mainDelegateNo'] = gridData.delegateNo;
            grid1.sortParameter.columnId = ['desc_id'];
            grid1.load();
            setTimeout(function () {
                var exchanges = "";
                for(var i=0;i<grid1.sortOriginalDatas.length;i++){
                    var gridData1 = grid1.sortOriginalDatas[i];
                    if(i>0){
                        exchanges += "/"+gridData1.exchange;
                    }else {
                        exchanges += gridData1.exchange;
                    }
                }
                modal.find('input[name="exchanges"]').val(exchanges);
            },1500)
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_coin_code_or_lk_delegate_no'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 撤单
     */
    function _revokeConfig(id) {
        var tpl = '您确定要撤回该委托吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "mainDelegation/json/revoke",
            data: {
                id: id
            },
            content: tpl,
            alertTitle:"撤回操作"
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        revokeConfig: _revokeConfig
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});