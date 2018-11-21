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
        id: 'businessNo',
        title: '成交编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'entrustNo',
        title: '子委托编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'subDel.mainDelegateNo',
        title: '主委托编号',
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
        fastSort: false
    },
    {
        id: 'mobile',
        title: '手机号码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'coinCurrency',
        title: '分区',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
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
        id: 'subDel.amount',
        title: '委托量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'price',
        title: '成交价',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'amount',
        title: '成交量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'platFee',
        title: '手续费',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'dealTime',
        title: '成交时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center width-180',
        columnClass: 'text-center width-180',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    },
    {
        id: 'mainDel.origin',
        title: '成交来源',
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
        id: 'isConfirm',
        title: '是否已对账',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            0: $lang.dealIsConfirm.NO,
            1: $lang.dealIsConfirm.YES,
        }
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var detail = "<a data-target='#detailModal' class='btn btn-sm btn-success' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-eye'></i>&nbsp;详情&nbsp;</a>";
            var html = '';
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
    loadURL: ncGlobal.adminRoot + 'dealOrder/json/list',
    exportFileName: '成交管理列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id","isConfirm","delFlag"], Timestamp: ["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['desc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//列表结束

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
            modal.find('input[name="entrustNo"]').val(gridData.entrustNo);
            modal.find('input[name="subDel.style"]').val(gridData.subDel.style);
            modal.find('input[name="coinCurrency"]').val(gridData.coinCurrency);
            modal.find('input[name="subDel.createTime"]').val(gridData.subDel.createTime);
            modal.find('input[name="coinCode"]').val(gridData.coinCode);
            modal.find('input[name="subDel.amount"]').val(gridData.subDel.amount);
            modal.find('input[name="subDel.price"]').val(gridData.subDel.price);
            modal.find('input[name="subDel.exchange"]').val(gridData.subDel.exchange);
            modal.find('input[name="subDel.gmv"]').val(gridData.subDel.gmv);
            if(gridData.state == 1){
                modal.find('input[name="state"]').val("下单申请(已报)");
            }else if(gridData.state == 2){
                modal.find('input[name="state"]').val("交易中");
            }else if(gridData.state == 3){
                modal.find('input[name="state"]').val("撤单中");
            }else if(gridData.state == 4){
                modal.find('input[name="state"]').val("已报待撤");
            }else if(gridData.state == 5){
                modal.find('input[name="state"]').val("已撤单");
            }else if(gridData.state == 6){
                modal.find('input[name="state"]').val("部分撤单");
            }else if(gridData.state == 7){
                modal.find('input[name="state"]').val("下单失败");
            }else if(gridData.state == 8){
                modal.find('input[name="state"]').val("已成交");
            }else if(gridData.state == 9){
                modal.find('input[name="state"]').val("申请失败");
            }else if(gridData.state == 10){
                modal.find('input[name="state"]').val("已退款");
            }else if(gridData.state == 11){
                modal.find('input[name="state"]').val("部分成交");
            }
            modal.find('input[name="delegateNo"]').val(gridData.delegateNo);
            if(gridData.style == 1){
                modal.find('input[name="style"]').val("市价委托");
            }else {
                modal.find('input[name="style"]').val("限价委托");
            }
            modal.find('input[name="businessNo"]').val(gridData.businessNo);
            if (gridData.direction == 1){
                modal.find('input[name="subDel.style"]').val("买入");
            }else {
                modal.find('input[name="subDel.style"]').val("卖出");
            }
            modal.find('input[name="amount"]').val(gridData.amount);
            modal.find('input[name="dealTime"]').val(gridData.dealTime);
            modal.find('input[name="price"]').val(gridData.price);
            modal.find('input[name="gmv"]').val(gridData.gmv);
            modal.find('input[name="exchange"]').val(gridData.exchange);
            modal.find('input[name="platFee"]').val(gridData.platFee);
            modal.find('input[name="serviceFee"]').val(gridData.serviceFee);
            modal.find('input[name="motherAccount"]').val(gridData.motherAccount);
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_coin_code_or_lk_business_no'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }


    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});