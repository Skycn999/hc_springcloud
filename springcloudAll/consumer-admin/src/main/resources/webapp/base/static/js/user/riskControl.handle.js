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
        id: 'userId',
        title: '用户ID',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'u.mobile',
        title: '手机号',
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
        id: 'netWorth.tplName',
        title: '净值风控模板',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'percent.tplName',
        title: '百分比风控模板',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'percentInitialBalance',
        title: '百分比风控期初值',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'updateTime',
        title: '最后更新时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center width-200',
        columnClass: 'text-center width-200',
        fastSort: false
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var settingHtml = "<a data-target='#settingModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;设置模板&nbsp;</a>";
            var html = "";
            // 判断是否有设置权限
            if ($("#settingPermi") && $("#settingPermi").val() == 1) {
                html += settingHtml;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'riskControl/json/list',
    exportFileName: '风控设置列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "userId","delFlag","isEnableRiskControl"], Timestamp: ["createTime","updateTime"]}
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
        id: 'type',
        type: 'int',
        hideQuery:true,
        hideQueryType:'eq',
        hideQueryValue:1,
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
        id: 'email',
        title: '邮箱',
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
        $("#mobile").val(record.mobile);
        $("#email").val(record.email);
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
            modal.find('input[name="userId"]').val("");
            $("#mobile").val("");
            $("#email").val("");
            $("#netWorthAdd").find("option[value= '']").prop("selected",true);
            $("#percenAdd").find("option[value= '']").prop("selected",true);
            $(".isEnable").bootstrapSwitch('state', false);
            modal.find('input[name="percentInitialBalance"]').val("");
        });

        //设置对话框初始化
        $("#settingModal").on("show.bs.modal", function (event) {
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
            //选择的净值风控
            var obj = document.getElementById('netWorth');
            for(var i = 0; i < obj.options.length; i++){
                var tmp = obj.options[i].value;
                if(tmp == gridData.netWorthTpl){
                    obj.options[i].selected = 'selected';
                    break;
                }
            }
            //百分比风控状态
            if (gridData.isEnableRiskControl == 1) {
                $(".isEnable").bootstrapSwitch('state', true);
            } else {
                $(".isEnable").bootstrapSwitch('state', false);
            }
            //选择的百分比风控模板
            var obj = document.getElementById('percent');
            for(var i = 0; i < obj.options.length; i++){
                var tmp = obj.options[i].value;
                if(tmp == gridData.percentTpl){
                    obj.options[i].selected = 'selected';
                    break;
                }
            }
            modal.find('input[name="percentInitialBalance"]').val(gridData.percentInitialBalance);
        });

        $("#userModal").on("show.bs.modal", function (event) {
            grid1.fastQueryParameters = new Object();
            grid1.fastQueryParameters['eq_delFlag'] = 0;
            grid1.fastQueryParameters['eq_type'] = 1;
            grid1.pager.startRecord = 0;
            grid1.pager.nowPage = 1;
            grid1.pager.recordCount = -1;
            grid1.pager.pageCount = -1;
            grid1.load();
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_u.realname_or_lk_u.mobile'] = $('#keyword').val();
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