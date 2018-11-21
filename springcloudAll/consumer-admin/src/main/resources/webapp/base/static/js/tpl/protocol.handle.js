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
        id: 'tplCnName',
        title: '协议名称(中文)',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'tplEnName',
        title: '协议名称(英文)',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'tplNo',
        title: '协议编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'createTime',
        title: '添加时间',
        type: 'date',
        format: 'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
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
            1: $lang.protocolState.enable,
            0: $lang.protocolState.disable
        }
    },
    {
        id: 'updateTime',
        title: '更新时间',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'operation',
        title: '操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        extra: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            var detail = "<a data-target='#detailModal' class='btn btn-sm btn-success m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-eye'></i>&nbsp;详情&nbsp;</a>";
            var editHtml = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            if ($("#editPermi") && $("#editPermi").val() == 1) {
                html += editHtml;
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
    loadURL: ncGlobal.adminRoot + '/protocol/json/list',
    exportFileName: '协议',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "delFlag","state"], Timestamp: ["createTime", "updateTime"]},
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

            $("#addForm").find("[name='tplCnName']").val("");
            $("#addForm").find("[name='tplEnName']").val("");
            //$("#addForm").find("[name='tplNo']").val("");
            $("#addForm").find("[name='cnContent']").val("");
            $("#addForm").find("[name='enContent']").val("");

            $(".state").bootstrapSwitch('state', true);
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
            modal.find('input[name="tplCnName"]').val(gridData.tplCnName);
            modal.find('input[name="tplEnName"]').val(gridData.tplEnName);
            modal.find('input[name="tplNo"]').val(gridData.tplNo);
            $("#contentEdita").html(gridData.cnContent);
            $("#contentEditb").html(gridData.enContent);

            if (gridData.state == 1) {
                $(".state").bootstrapSwitch('state', true);
            } else {
                $(".state").bootstrapSwitch('state', false);
            }
        });

        //查看对话框初始化
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
            // $(".alert-danger").remove();
            // $("#editForm").psly().reset();
            modal.find('input[name="tplCnName"]').val(gridData.tplCnName);
            modal.find('input[name="tplEnName"]').val(gridData.tplEnName);
            modal.find('input[name="tplNo"]').val(gridData.tplNo);
            modal.find('input[name="createTime"]').val(gridData.createTime);
            modal.find('input[name="updateTime"]').val(gridData.updateTime);
            //状态
            if (gridData.state == 1) {
                modal.find('input[name="state"]').val("启用");
            } else {
                modal.find('input[name="state"]').val("停用");
            }
        });

        //模糊搜索
        $('#customSearch').click(function () {
            //grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_tpl_cn_name'] = $('#keyword').val();
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
        bindEvent: function () {
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