var dtGridColumns = [
    {
        id: 'delFlag',
        type: 'int',
        hideQuery:true,
        hideQueryType:'eq',
        hideQueryValue:1,
        hide:true

    },
    {
        id: 'id',
        title: '编号',
        type: 'string',
        headerClass: 'text-left width-60',
        columnClass: 'text-left width-60',
        fastQuery: false,
        fastQueryType: 'lk'
    },
    {
        id: 'name',
        title: '模板名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'nid',
        title: '模板标识',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html, edit, del;
            if ($("#edit") && $("#edit").val() == 1) {
                edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            }
            if ($("#del") && $("#del").val() == 1) {
                del = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='capital_template.delJson(" + record.id + ",\"" + record.name + "\")'><i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            }
            return edit + del;
        }
    },
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL : ncGlobal.adminRoot + 'capital/json/list',
    exportFileName: '资金模板列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools : 'refresh|fastQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: { int: ["id", "delFlag"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = 'id';
grid.sortParameter.sortType = 1;
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;

var capital_template = function() {

    /**
     * 删除
     */
    function _delJson(id, content) {
        var tpl = '您是否确认需要删除资金模板 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "capital/json/del",
            data: {
                id: id
            },
            content: tpl
        });
    }

    var _bindEven = function() {

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        /**
         * 新增
         */
        $("#addModal").on("show.bs.modal", function (e) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();

            $("#addForm").find("[name='nid']").val("");
            $("#addForm").find("[name='name']").val("");
            $("#addForm").find("[name='content']").val("");
        });

        /**
         * 编辑
         */
        $("#editModal").on("show.bs.modal", function (e) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
            //获取data 参数
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            $("#editForm").find("[name='id']").val(gridData.id);
            $("#editForm").find("[name='nid']").val(gridData.nid);
            $("#editForm").find("[name='name']").val(gridData.name);
            $("#editForm").find("[name='content']").val(gridData.content);
        });
    }

    return {
        init : function() {
            _bindEven();
        },
        delJson: _delJson
    }
}();

$(function() {
    grid.load();
    capital_template.init();
});