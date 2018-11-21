
//列表开始
var dtGridColumns = [
    {
        id: 'delFlag',
        type: 'int',
        hideQuery:true,
        'export':false,
        hideQueryType:'eq',
        hideQueryValue:0,
        hide:true
    },
    {
        id: 'id',
        title: '编号',
        type: 'string',
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
        title: '模版名称',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'percent',
        title: '百分比%',
        type: 'double',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'createTime',
        title: '添加时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: "range"
    },
    {
        id: 'updateTime',
        title: '最近更新时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: "range"
    },
    {
        id: 'state',
        title: '状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: "eq",
        codeTable: {
            1: $lang.netWorthControlState.T1,
            0: $lang.netWorthControlState.T2
        }
    },
    {
        id: 'operation',
        title: '操作',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            var edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            // 判断是否有查看权限
            if ($("#percentEdit") && $("#percentEdit").val() == 1) {
                html += edit;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/percentControl/json/list',
    // exportURL: ncGlobal.adminRoot + 'userIdentify/json/export',
    exportFileName: '百分比风控表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int:["id","delFlag","state"],Timestamp:["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);
//默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//排序
grid.sortParameter.columnId = ['asc_id'];
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            //清除错误信息
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();

            $("#addForm").find("[name='tplName']").val("");
            $("#addForm").find("[name='tplNo']").val("");
            $("#addForm").find("[name='percent']").val("");
            $(".state").bootstrapSwitch('state', true);
            //$("#typeAdd").find("option[value= '']").prop("selected",true);
            $(".isDefault").bootstrapSwitch('state', true);
        });

        // 编辑对话框
        $('#editModal').on('show.bs.modal', function (event) {
            //清除错误信息
            $(".parsley-type").remove();
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            var editForm = $("#editForm");
            //清除错误提示
            $("#editForm").psly().reset();
            $(".alert-danger").remove();
            modal.find('input[name="id"]').val(gridData.id);
            modal.find('input[name="tplName"]').val(gridData.tplName);
            modal.find('input[name="tplNo"]').val(gridData.tplNo);
            modal.find('input[name="percent"]').val(gridData.percent);
            if(gridData.state == 1){
                $(".state").bootstrapSwitch('state',true);
            }else{
                $(".state").bootstrapSwitch('state',false);
            };
            // if(gridData.isDefault == 1){
            //     $(".isDefault").bootstrapSwitch('state',true);
            // }else{
            //     $(".isDefault").bootstrapSwitch('state',false);
            // }
        });

        //模糊搜索
        $('#customSearch').click(function () {
            //grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_tplName'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        //去除输入框回车键提交
        $("input").on("keydown", function (e) {
            if (e.keyCode == 13) {
                e.preventDefault();
                var a = $("#releasePrice");
                a.length && a.trigger("click");
            }
        });
    }

    //外部可调用
    return {
        bindEvent: function (){
            _bindEvent();
        }
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});