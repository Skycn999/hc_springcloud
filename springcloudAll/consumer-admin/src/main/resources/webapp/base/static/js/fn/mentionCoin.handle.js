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
        id: 'id',
        title: '编号',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'orderNo',
        title: '订单号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'u.mobile',
        title: '手机号码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'u.email',
        title: '邮箱',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'u.realname',
        title: '姓名',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'state',
        title: '状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            0: $lang.mentionCoinStatus.PENDING,
            1: $lang.mentionCoinStatus.PEND_ENTER,
            2: $lang.mentionCoinStatus.NO_PASS,
            3: $lang.mentionCoinStatus.SUCCESS,
        }
    },
    {
        id: 'coinCurrency',
        title: '币种',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'amount',
        title: '提币数量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'serviceFee',
        title: '手续费',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'userMentionAddr',
        title: '提币地址',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'txId',
        title: 'TxId',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'confirmTime',
        title: '审核时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center width-180',
        columnClass: 'text-center width-180',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    },
    {
        id: 'createTime',
        title: '申请时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center width-180',
        columnClass: 'text-center width-180',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range',
        //hideType: 'md|sm|xs'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var detail = "<a data-target='#detailModal' class='btn btn-sm btn-success m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-eye'></i>&nbsp;详情&nbsp;</a>";
            var audit = "<a data-target='#auditModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;审核&nbsp;</a>";
            var input = "<a data-target='#inputModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;录入&nbsp;</a>"
            var html = "";
            // 判断是否有录入权限
            // if ($("#inputPermi") && $("#inputPermi").val() == 1) {
            //     if(record.state == 0){
            //         html += input;
            //     }
            // }
            // 判断是否有审核权限
            if ($("#auditPermi") && $("#auditPermi").val() == 1) {
                if(record.state == 0){
                    html += audit;
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
    loadURL: ncGlobal.adminRoot + 'mentionCoin/json/list',
    exportFileName: '提币管理列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "state","delFlag"], Timestamp: ["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['desc_state=0'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;

//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //录入对话框初始化
        // $("#inputModal").on("show.bs.modal", function (event) {
        //     //获取接受事件的元素
        //     var button = $(event.relatedTarget),
        //         //获取data 参数
        //         datano = button.data('no'),
        //         //获取列表框中的原始数据
        //         gridData = grid.sortOriginalDatas[datano],
        //         modal = $(this),
        //         inputForm = $("#inputForm");
        //     //获取列表框中的原始数据
        //     //清除错误信息
        //     $(".alert-danger").remove();
        //     $("#inputForm").psly().reset();
        //     modal.find('input[name="id"]').val(gridData.id);
        //     modal.find('input[name="txId"]').val("");
        // })

        //审核对话框初始化
        $("#auditModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
            modal.find('input[name="id"]').val(gridData.id);
            modal.find('input[name="userId"]').val(gridData.userId);
            modal.find('input[name="realname"]').val(gridData.u.realname);
            modal.find('input[name="mobile"]').val(gridData.u.mobile);
            if(gridData.u.idType == 1){
                modal.find('input[name="idType"]').val("身份证");
            }else {
                modal.find('input[name="idType"]').val("护照");
            }
            modal.find('input[name="idNo"]').val(gridData.u.idNo);
            modal.find('input[name="userMentionAddr"]').val(gridData.userMentionAddr);
            modal.find('input[name="platPayAddr"]').val(gridData.platPayAddr);
            modal.find('input[name="txId"]').val(gridData.txId);
            modal.find('input[name="coinCurrency"]').val(gridData.coinCurrency);
            modal.find('input[name="amount"]').val(gridData.amount);
            modal.find('input[name="serviceFee"]').val(gridData.serviceFee);
            $(".statecEdit").bootstrapSwitch('state', true);
            modal.find('textarea[name="confirmRemark"]').val("");
        });

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
            modal.find('input[name="realname"]').val(gridData.u.realname);
            modal.find('input[name="mobile"]').val(gridData.u.mobile);
            if(gridData.u.idType == 1){
                modal.find('input[name="idType"]').val("身份证");
            }else {
                modal.find('input[name="idType"]').val("护照");
            }
            modal.find('input[name="idNo"]').val(gridData.u.idNo);
            modal.find('input[name="coinCurrency"]').val(gridData.coinCurrency);
            modal.find('input[name="serviceFee"]').val(gridData.serviceFee);
            modal.find('input[name="amount"]').val(gridData.amount);
            modal.find('input[name="toAmount"]').val(gridData.amount);
            modal.find('input[name="userMentionAddr"]').val(gridData.userMentionAddr);
            modal.find('input[name="platPayAddr"]').val(gridData.platPayAddr);
            modal.find('input[name="txId"]').val(gridData.txId);
            modal.find('input[name="state"]').val(gridData.state);
            if(gridData.state == 0){
                modal.find('input[name="state"]').val("待审核");
            }else if(gridData.state == 1) {
                modal.find('input[name="state"]').val("待录入");
            }else if(gridData.state == 2){
                modal.find('input[name="state"]').val("不通过");
            }else {
                modal.find('input[name="state"]').val("已成功");
            }
            modal.find('input[name="createTime"]').val(gridData.createTime);
            modal.find('input[name="confirmTime"]').val(gridData.confirmTime);
            modal.find('input[name="confirmor"]').val(gridData.confirmor);
            modal.find('textarea[name="confirmRemark"]').val(gridData.confirmRemark);
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_coin_currency_or_lk_u.realname_or_lk_u.mobile'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 删除入驻申请
     */
    function _delConfig(id) {
        var tpl = '您确定要删除该参数吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "config/json/del",
            data: {
                id: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delConfig: _delConfig
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});