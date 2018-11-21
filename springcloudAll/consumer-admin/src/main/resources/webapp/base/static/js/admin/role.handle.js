/**
 * Created by dqw on 2015/12/30.
 */

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
        columnClass: 'text-center width-100',
        fastSort: false
    },
    {
        id: 'name',
        title: '权限组名称',
        type: 'string',
		headerClass: 'text-left',
        columnClass: 'text-left',
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
            var editHtml = "<a href='" + ncGlobal.adminRoot + "role/edit?id=" + record.id + "' class='btn btn-sm btn-primary m-r-10' data-toggle='modal'><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            var delHtml = "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delRole("+ record.id + ")' ><i class='fa fa fa-lg fa-trash-o'></i>&nbsp;删除&nbsp;</a>";


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
    loadURL: ncGlobal.adminRoot + 'role/json/list',
    exportFileName: '权限组列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int:["id","delFlag"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['asc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //新增
        $("#addModal").on("show.bs.modal", function (event) {
            Nc.go(ncGlobal.adminRoot + "role/add");
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 删除入驻申请
     */
    function _delRole(id) {
        var tpl = '您确定要删除该角色吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "role/json/del",
            data: {
                id: id
            },
            content: tpl
        });
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delRole: _delRole
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});
