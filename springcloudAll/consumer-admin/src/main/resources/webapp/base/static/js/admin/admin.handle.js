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
        headerClass: 'text-center width-80',
        columnClass: 'text-center width-80',
        fastSort: false
    },
    {
        id: 'type',
        title: '类型',
        type: 'string',
        headerClass: 'text-left width-100',
        columnClass: 'text-left width-100',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.adminType.T1,
            2: $lang.adminType.T2,
            3: $lang.adminType.T3
        }
    },
    {
        id: 'name',
        title: '登录名',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'roleName',
        title: '权限组',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var editHtml = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            var delHtml = "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delAdmin(" + record.id + ")' ><i class='fa fa fa-lg fa-trash-o'></i>&nbsp;删除&nbsp;</a>";

            var html = "";
            // 判断是否有编辑权限
            if ($("#editPermi") && $("#editPermi").val() == 1 && record.roleId != 0) {
                html += editHtml;
            }
            // 判断是否有删除权限
            if ($("#delPermi") && $("#delPermi").val() == 1 && record.roleId != 0) {
                html += delHtml;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'admin/json/list',
    exportFileName: '管理员列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int:["id","delFlag","type"],Timestamp:["createTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['asc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//列表结束

var dtGridColumnsInvestor = [
    {
        id: 'delFlag',
        type: 'int',
        hideQuery:true,
        hideQueryType:'eq',
        hideQueryValue:0,
        hide:true
    },
    {
        id: 'u.userName',
        title: '用户名',
        type: 'string',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    },
    {
        id: 'u.mobile',
        title: '手机号',
        type: 'string',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    }
];

var dtGridColumnsAgent = [
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
        type: 'int',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false
    },
    {
        id: 'name',
        title: '代理商名称',
        type: 'string',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    },
    {
        id: 'code',
        title: '代理商ID',
        type: 'string',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    }
];

var dtGridOptionInvestor = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + "investor/josn/bindList",
    exportFileName: '选择列表',
    columns: dtGridColumnsInvestor,
    gridContainer: 'dtGridContainerUser',
    toolbarContainer: 'dtGridToolBarContainerUser',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id"], Timestamp: ["createTime"]},
    onRowDblClick: function (value, record, column, grid, dataNo, columnNo, cell, row, extraCell, e) {
        if (record.u.userName != null) {
            $("#chooseUserAdd").text(record.u.userName);
            $("#addForm").find('input[name="name"]').val(record.u.userName);
        } else {
            $("#chooseUserAdd").text("");
        }
        $('input[name="bussId"]').val(record.userId);
        $("#userModal").hide();//隐藏关联栏目窗口
        return false;
    }
};

var dtGridOptionAgent = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + "agent/josn/bindList",
    exportFileName: '选择列表',
    columns: dtGridColumnsAgent,
    gridContainer: 'dtGridContainerUser',
    toolbarContainer: 'dtGridToolBarContainerUser',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id"], Timestamp: ["createTime"]},
    onRowDblClick: function (value, record, column, grid, dataNo, columnNo, cell, row, extraCell, e) {
        if (record.name != null) {
            $("#chooseUserAdd").text(record.name);
            $("#addForm").find('input[name="name"]').val(record.name);
        } else {
            $("#chooseUserAdd").text("");
        }
        $('input[name="bussId"]').val(record.id);
        $("#userModal").hide();//隐藏关联栏目窗口
        return false;
    }
};

var grid1 = $.fn.DtGrid.init(dtGridOptionAgent);

var grid2 = $.fn.DtGrid.init(dtGridOptionInvestor);


//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();
            $addForm = $("#addForm");
            $addForm.find("[name='name']").val("");
            $addForm.find("[name='password']").val("");
            $addForm.find("[name='password2']").val("");
            $addForm.find("[name='id']").val("");
            $addForm.find('input[name="bussId"]').val("");
            $("#chooseUserAdd").text("");
            $addForm.find('input[name="type"]').removeAttr("checked");
            $("#adminType3").prop("checked",true);
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
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];

            $("#id").val(gridData.id);
            $("#adminName").val(gridData.name);
            modal.find("[name='password']").val("");
            modal.find("[name='password2']").val("");
            modal.find("[name='roleId']").val(gridData.roleId);
        });

        $("#userModal").on("show.bs.modal", function (event) {
            var type = $("#addForm").find("input[name='type']:checked").val();
            if(type == 3){
                $("#dtGridContainerUser").html("");
                grid1.fastQueryParameters = new Object();
                grid1.fastQueryParameters['eq_delFlag'] = 0;
                //排序
                grid1.sortParameter.columnId = ['asc_id'];
                grid1.pager.nowPage = 1;
                grid1.load();
            }else if(type == 2){
                $("#dtGridContainerUser").html("");
                grid2.fastQueryParameters = new Object();
                grid2.fastQueryParameters['eq_delFlag'] = 0;
                //排序
                grid2.sortParameter.columnId = ['asc_id'];
                grid2.pager.nowPage = 1;
                grid2.load();
            }
        });

         //选择类型判断是否隐藏选择绑定人
        $(".radio-inline input").click(function () {
            var type = $("#addForm").find("input[name='type']:checked").val();
            if(type == 3){
                $("#isChoose").show();
            }else if(type == 2){
                $("#isChoose").show();
            }else if(type == 1){
                $("#isChoose").hide();

            }
            $("#chooseUserAdd").text("");
            $("#addForm").find('input[name="name"]').val("");
            $("#addForm").find('input[name="bussId"]').val("");
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
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
    function _delAdmin(id) {
        var tpl = '您确定要删除该管理员吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "admin/json/del",
            data: {
                id: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delAdmin: _delAdmin
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});