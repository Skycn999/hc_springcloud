
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
        id: 'code',
        title: '资产代码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'cnDesc',
        title: '中文资料',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
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
        id: 'creator',
        title: '添加人',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
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
        id: 'operation',
        title: '操作',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            var edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            // 判断是否有查看权限
            if ($("#commodityEdit") && $("#commodityEdit").val() == 1) {
                html += edit;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/commodity/json/list',
    // exportURL: ncGlobal.adminRoot + 'userIdentify/json/export',
    exportFileName: '代码资料管理表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
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
            modal.find("input[name='code']").val("");
            modal.find("[name='enDesc']").val("");
            modal.find("[name='cnDesc']").val("");
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
            modal.find('input[name="code"]').val(gridData.code);
            modal.find("[name='cnDesc']").val(gridData.cnDesc);
            modal.find("[name='enDesc']").val(gridData.enDesc);
        });


        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_code'] = $('#keyword').val();
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