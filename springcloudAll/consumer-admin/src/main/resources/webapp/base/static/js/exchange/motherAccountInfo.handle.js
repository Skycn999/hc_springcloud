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
        id: 'accountNo',
        title: '母账号编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'ex.name',
        title: '交易所名称',
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
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'available',
        title: '剩余资产',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'freeze',
        title: '冻结资产',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'total',
        title: '总资产',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'createTime',
        title: '创建时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    },
    {
        id: 'syncTime',
        title: '最近同步时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    }
    // ,
    // {
    //     id: 'operation',
    //     title: '管理操作',
    //     type: 'string',
    //     columnClass: 'text-center width-200',
    //     fastSort: false,
    //     resolution: function (value, record, column, grid, dataNo, columnNo) {
    //         var edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
    //         var html = "";
    //         // 判断是否有编辑权限
    //         if ($("#editPermi") && $("#editPermi").val() == 1) {
    //             html += edit;
    //         }
    //         return html;
    //     }
    // }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'motherAccountInfo/json/list',
    exportURL: ncGlobal.adminRoot + 'motherAccountInfo/json/export',
    exportFileName: '母账号资产管理列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery|export[excel]',
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

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //添加对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();
            modal.find('input[name="name"]').val("");
            modal.find('input[name="quoteUrl"]').val("");
            modal.find('input[name="txUrl"]').val("");
            $(".stateAdd").bootstrapSwitch('state', true);
        })


        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_account_no_or_lk_ex_name'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 同步
     */
    function _synchron() {
        var tpl = '您确定要同步吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "motherAccountInfo/json/synchron",
            data: {
            },
            content: tpl,
            alertTitle:"同步操作"
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        synchron: _synchron
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});