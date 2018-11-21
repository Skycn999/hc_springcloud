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
        title: '序号',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'tplNo',
        title: '模版编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'tplName',
        title: '模板名称',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'symbol',
        title: '交易币种',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'buyFee',
        title: '买入交易手续费率',
        type: 'decimal',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'sellFee',
        title: '卖出交易手续费率',
        type: 'decimal',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
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
            1: $lang.commonStatus.ENABLE,
            0: $lang.commonStatus.DISABLE
        }
    },
    {
        id: 'createTime',
        title: '创建时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center width-200',
        columnClass: 'text-center width-200',
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
            var edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            var html = "";
            // 判断是否有编辑权限
            if ($("#editPermi") && $("#editPermi").val() == 1) {
                html += edit;
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
    loadURL: ncGlobal.adminRoot + 'tradeFee/json/list',
    exportFileName: '交易手续费模板列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "state"], Timestamp: ["createTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['asc_id'];
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
            modal.find('input[name="tplName"]').val("");
            //$("#symbolAdd").find("option[value= '']").prop("selected",true);
            modal.find('input[name="symbol"]').val("");
            modal.find('input[name="buyFee"]').val("");
            modal.find('input[name="sellFee"]').val("");
            $(".stateAdd").bootstrapSwitch('state', true);
        })

        //编辑对话框初始化
        $("#editModal").on("show.bs.modal", function (event) {
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
            modal.find('input[name="symbol"]').val(gridData.symbol);
            modal.find('input[name="tplName"]').val(gridData.tplName);
            modal.find('input[name="buyFee"]').val(gridData.buyFee);
            modal.find('input[name="sellFee"]').val(gridData.sellFee);
            //币种
            // var obj = document.getElementById('symbolEdit');
            // for(var i = 0; i < obj.options.length; i++){
            //     var tmp = obj.options[i].value;
            //     if(tmp == gridData.symbol){
            //         obj.options[i].selected = 'selected';
            //         break;
            //     }
            // }
            //状态
            if (gridData.state == 1) {
                $(".stateEdit").bootstrapSwitch('state', true);
            } else {
                $(".stateEdit").bootstrapSwitch('state', false);
            }
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
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
            modal.find('input[name="tplName"]').val(gridData.tplName);
            modal.find('input[name="symbol"]').val(gridData.symbol);
            modal.find('input[name="buyFee"]').val(gridData.buyFee);
            modal.find('input[name="sellFee"]').val(gridData.sellFee);
            modal.find('input[name="createTime"]').val(gridData.createTime);
            modal.find('input[name="updateTime"]').val(gridData.updateTime);
            modal.find('input[name="creator"]').val(gridData.creator);
            modal.find('input[name="updator"]').val(gridData.updator);
            //状态
            if (gridData.state == 1) {
                modal.find('input[name="state"]').val("启用");
            } else {
                modal.find('input[name="state"]').val("禁用");
            }
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_symbol_or_lk_tpl_name'] = $('#keyword').val();
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