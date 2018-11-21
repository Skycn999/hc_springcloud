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
        columnClass: 'text-center width-50',
        fastSort: false
    },
    {
        id: 'name',
        title: '参数名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-150',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'nid',
        title: '参数标识',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'type',
        title: '参数类型',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left width-100',
        fastSort: false,
        fastQuery: true,
        fastQueryType: "eq",
        codeTable: {
            1: $lang.configType.BOTTOM,
            2: $lang.configType.FEES,
            3: $lang.configType.NOTICE,
            4: $lang.configType.THIRD,
            5: $lang.configType.OTHER
        }
    },
    {
        id: 'value',
        title: '参数值',
        type: 'string',
        headerClass: 'text-left width-250',
        columnClass: 'text-left width-250',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            if(value.length>35){
                return value.substring(0,35);
            }else {
                return value;
            }
        }
    },
    {
        id: 'remark',
        title: '参数描述',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false
    },
    {
        id: 'status',
        title: '状态',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        fastSort: false,
        fastQuery: true,
        fastQueryType: "eq",
        codeTable: {
            1: $lang.state.OPEN,
            0: $lang.state.CLOSE
        }
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var editHtml = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            var delHtml = "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delConfig(" + record.id + ")' ><i class='fa fa fa-lg fa-trash-o'></i>&nbsp;删除&nbsp;</a>";

            var html = "";
            // 判断是否有编辑权限
            if ($("#editPermi") && $("#editPermi").val() == 1) {
                html += editHtml;
            }
            // 判断是否有删除权限
            if ($("#delPermi") && $("#delPermi").val() == 1) {
                html += delHtml;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'config/json/list',
    exportFileName: '管理员列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "delFlag", "type", "status"], Timestamp: ["createTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['asc_type','asc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();

            $(".status").bootstrapSwitch('state', false);
            $("#addForm").find("[name='name']").val("");
            $("#addForm").find("[name='nid']").val("");
            $("#addForm").find("[name='type']").val("");
            $("#addForm").find("[name='status']").val("");
            $("#addForm").find("[name='remark']").val("");
        });

        //编辑对话框初始化
        $("#editModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];

            $("#id").val(gridData.id);
            $("#name").val(gridData.name);
            $("#nid").val(gridData.nid);
            $("#value").val(gridData.value);
            $("#type").val(gridData.type);
            if (gridData.status == 1) {
                $(".status").bootstrapSwitch('state', true);
            } else {
                $(".status").bootstrapSwitch('state', false);
            }
            $("#remark").val(gridData.remark);
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
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