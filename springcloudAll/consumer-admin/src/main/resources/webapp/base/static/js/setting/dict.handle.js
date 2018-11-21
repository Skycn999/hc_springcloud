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
        id: 'nid',
        title: '标识',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'sortNo',
        title: '排序',
        type: 'number',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: false,
        fastQueryType: 'eq'
    },
    {
        id: 'name',
        title: '名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'val',
        title: '值',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: false,
        fastQueryType: 'eq'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-300',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html, edit, del, open, close;
            if ($("#edit") && $("#edit").val() == 1) {
                edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            }
            if ($("#del") && $("#del").val() == 1) {
                del = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='capital_template.delJson(" + record.id + ",\"" + record.name + "\")'><i class='fa fa-trash-o'></i>&nbsp;删除&nbsp;</a>";
            }
            if ($("#open") && $("#open").val() == 1) {
                open = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='capital_template.openJson(" + record.id + ",\"" + record.name + "\")'><i class='fa fa-trash-o'></i>&nbsp;启用&nbsp;</a>";
            }
            if ($("#close") && $("#close").val() == 1) {
                close = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='capital_template.closeJson(" + record.id + ",\"" + record.name + "\")'><i class='fa fa-trash-o'></i>&nbsp;停用&nbsp;</a>";
            }
            if (record.status == 1) {
                html = edit + close;
            } else {
                html = edit + open + del;
            }
            return html;
        }
    },
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'dict/json/list',
    exportFileName: '字典条目列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|fastQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "delFlag"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = ['asc_nid'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;

var capital_template = function () {

    /**
     * 删除
     */
    function _delJson(id, content) {
        var tpl = '您是否确认需要删除字典条目 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "dict/json/del",
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "删除操作"
        });
    }

    /**
     * 启用
     */
    function _openJson(id, content) {
        var tpl = '您是否确认需要启用字典条目 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "dict/json/open",
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "启用操作"
        });
    }

    /**
     * 停用
     */
    function _closeJson(id, content) {
        var tpl = '您是否确认需要停用字典条目 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "dict/json/close",
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "停用操作"
        });
    }

    var _bindEven = function () {

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_name_or_lk_nid'] = $('#keyword').val();
            grid.pager.startRecord = 0;
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
            $("#addForm").find("[name='val']").val("");
            $("#addForm").find("[name='sortNo']").val("");
            $("#addForm").find("[name='description']").val("");
            $(".status").bootstrapSwitch('state', false);
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
            $("#editForm").find("[name='val']").val(gridData.val);
            $("#editForm").find("[name='sortNo']").val(gridData.sortNo);
            $("#editForm").find("[name='description']").val(gridData.description);
            if (gridData.status == 1) {
                $(".status").bootstrapSwitch('state', true);
            } else {
                $(".status").bootstrapSwitch('state', false);
            }
        });
    }

    return {
        init: function () {
            _bindEven();
        },
        delJson: _delJson,
        openJson: _openJson,
        closeJson: _closeJson
    }
}();

$(function () {
    grid.load();
    capital_template.init();
});