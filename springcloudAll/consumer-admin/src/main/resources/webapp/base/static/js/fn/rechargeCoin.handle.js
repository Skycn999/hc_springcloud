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
        title: '账户编号',
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
        title: '充币数量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'txId',
        title: 'TxId',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'userRechargeAddr',
        title: '用户充币地址',
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
            0: $lang.commonAuditStatus.PENDING,
            1: $lang.commonAuditStatus.PASS,
            2: $lang.commonAuditStatus.NOPASS
        }
    },
    {
        id: 'updator',
        title: '审核人',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: false,
        //hideType: 'md|sm|xs|lg'
    },
    {
        id: 'checkTime',
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
        id: 'creator',
        title: '创建人',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: false,
        //hideType: 'md|sm|xs|lg'
    },
    {
        id: 'createTime',
        title: '创建时间',
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
            var detail = "<a data-target='#detailModal' class='btn btn-sm btn-success m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-eye'></i>&nbsp;详情&nbsp;</a>";
            var audit = "<a data-target='#auditModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;审核&nbsp;</a>";
            var html = "";
            // 判断是否有审核权限
            if ($("#auditPermi") && $("#auditPermi").val() == 1) {
                if (record.state == 0) {
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
    loadURL: ncGlobal.adminRoot + 'rechargeCoin/json/list',
    // exportURL: ncGlobal.adminRoot + 'rechargeCoin/json/export',
    exportFileName: '充币管理列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    //tools: 'refresh|faseQuery|export[excel]',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "state","delFlag"], Timestamp: ["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['desc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//列表结束

//用户列表
var dtGridColumnsUser = [
    {
        id: 'delFlag',
        type: 'int',
        hideQuery:true,
        hideQueryType:'eq',
        hideQueryValue:0,
        hide:true
    },
    {
        id: 'realnameState',
        type: 'int',
        hideQuery:true,
        hideQueryType:'eq',
        hideQueryValue:2,
        hide:true
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
        id: 'realname',
        title: '姓名',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'idType',
        title: '证件类型',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        codeTable: {
            1: $lang.idType.T1,
            2: $lang.idType.T2
        }
    },
    {
        id: 'idNo',
        title: '证件号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    }
];

//用户列表
var dtGridOptionUser = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'users/json/list',
    exportFileName: '用户列表',
    columns: dtGridColumnsUser,
    gridContainer: 'dtGridContainerUser',
    toolbarContainer: 'dtGridToolBarContainerUser',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id"], Timestamp: ["createTime"]},
    onRowDblClick: function (value, record, column, grid, dataNo, columnNo, cell, row, extraCell, e) {
        $("#addModal").find('input[name="userId"]').val(record.id);
        $("#realname").val(record.realname);
        if(record.idType==1){
            $("#idType").val("身份证");
        }else if(record.idType==2){
            $("#idType").val("护照");
        }else{
            $("#idType").val("");
        }
        $("#idNo").val(record.idNo);
        $("#userModal").hide();//隐藏关联栏目窗口
        return false;
    }
};

//用户列表
var grid1 = $.fn.DtGrid.init(dtGridOptionUser);

//排序
grid1.sortParameter.columnId = ['desc_id'];
// 默认查询条件
grid1.fastQueryParameters = new Object();
grid1.fastQueryParameters['eq_delFlag'] = 0;

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {


        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                addForm = $("#addForm");
            //清除错误信息
            $("#editForm").psly().reset();
            $(".alert-danger").remove();
            modal.find('input[name="userId"]').val("");
            $("#realname").val("");
            $("#idType").val("");
            $("#idNo").val("");
            modal.find('input[name="coinCurrency"]').val("");
            modal.find('input[name="platCollectAddr"]').val("");
            modal.find('input[name="amount"]').val("");
            modal.find('input[name="txId"]').val("");
            modal.find('input[name="userRechargeAddr"]').val("");
            modal.find('input[name="platCollectAddr"]').val("");
            $("#coinCurrencyAdd").find("option[value= '']").prop("selected",true);
        });

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
            modal.find('input[name="coinCurrency"]').val(gridData.coinCurrency);
            modal.find('input[name="platCollectAddr"]').val(gridData.platCollectAddr);
            modal.find('input[name="realname"]').val(gridData.u.realname);
            if(gridData.u.idType == 1){
                modal.find('input[name="idType"]').val("身份证");
            }else {
                modal.find('input[name="idType"]').val("护照");
            }
            modal.find('input[name="idNo"]').val(gridData.u.idNo);
            modal.find('input[name="amount"]').val(gridData.amount);
            modal.find('input[name="txId"]').val(gridData.txId);
            modal.find('input[name="userRechargeAddr"]').val(gridData.userRechargeAddr);
            //审核状态 默认选中通过
             $(".stateAudit").bootstrapSwitch('state', true);
            modal.find('textarea[name="checkRemark"]').val("");
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
            modal.find('input[name="amount"]').val(gridData.amount);
            modal.find('input[name="coinCurrency"]').val(gridData.coinCurrency);
            modal.find('input[name="userRechargeAddr"]').val(gridData.userRechargeAddr);
            modal.find('input[name="platCollectAddr"]').val(gridData.platCollectAddr);
            modal.find('input[name="txId"]').val(gridData.txId);
            if(gridData.state == 0){
                modal.find('input[name="state"]').val("待审核");
            }else if(gridData.state == 1) {
                modal.find('input[name="state"]').val("通过");
            }else{
                modal.find('input[name="state"]').val("未通过");
            }
            //审核状态 默认选中通过
            modal.find('input[name="createTime"]').val(gridData.createTime);
            modal.find('input[name="updator"]').val(gridData.updator);
            modal.find('input[name="checkTime"]').val(gridData.checkTime);
            modal.find('textarea[name="checkRemark"]').val(gridData.checkRemark);
        });

        $("#userModal").on("show.bs.modal", function (event) {
            grid1.fastQueryParameters = new Object();
            grid1.fastQueryParameters['eq_delFlag'] = 0;
            grid1.fastQueryParameters['eq_a.realnameState'] = 2;
            grid1.pager.startRecord = 0;
            grid1.pager.nowPage = 1;
            grid1.pager.recordCount = -1;
            grid1.pager.pageCount = -1;
            grid1.load();
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