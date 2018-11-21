/**
 * Created by cj on 2015/11/21.
 */

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
        columnClass: 'text-center',
        fastQuery: false,
        fastQueryType: 'eq',
        hideType: 'md|sm|xs'
    },
    {
        id: 'name',
        title: '菜单名称',
        type: 'string',
        columnClass: 'text-center',
        fastQuery: true,
        fastQueryType: 'lk',
    },
    {
        id: 'sortNo',
        title: '排序',
        type: 'number',
        columnClass: 'text-center',
        fastQuery: true,
        fastQueryType: 'eq',
        fastSort: false,
        hideType: 'md|sm|xs'
    },
    {
        id: 'level',
        title: '菜单层级',
        type: 'number',
        columnClass: 'text-center'
    },
    {
        id: 'parentName',
        title: '上级菜单',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        hideType: 'md|sm|xs',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return value != '' ? value : "--";
        },
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-300',
        fastSort: false,
        extra: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var content = "";
            content += '<div class="btn-group">' +
                '<a href="javascript:;" data-toggle="dropdown" aria-expanded="false" class="btn btn-primary btn-sm dropdown-toggle m-r-10">' +
                '<i class="fa fa-gears"></i>&nbsp;编辑&nbsp;' +
                '<span class="caret"></span></a><ul class="dropdown-menu dropdown-menu-left">';
            content += "<li><a href='#editModal' data-no='" + dataNo + "' data-toggle='modal'><i class='fa fa-lg fa-pencil-square m-r-10' ></i>&nbsp;编辑菜单信息</a></li>";
            if (record.type != 2) {
                content += '<li><a href="#addChildModal" data-toggle="modal" title="新增下级菜单" data-no="' + dataNo + '"><i class="fa fa-lg fa-plus m-r-10"></i>新增下级菜单</a></li>';
            }
            content += '</ul></div>';
            content += '<a href="javascript:;" class="btn btn-danger btn-sm m-r-10" name="delete" data-dataNo="' + dataNo + '"><i class="fa fa-lg fa-trash-o m-r-10"></i>删除</a>';
            if (record.type != 2) {
                content += '<a href="javascript:;" name="showChildren" data-dataNo="' + dataNo + '" class="btn btn-white btn-sm"><i class="fa fa-level-down"></i>下级菜单</a>';
            }
            return content;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'menu/json/list',
    exportFileName: '菜单列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 20,
    pageSizeLimit: [20, 50],
    ncColumnsType: {int: ["id", "delFlag","parentId"]},
    onGridComplete: function (grid) {
        var returnBtn = $("#returnParent"),
            parentInfo = menu.current.getParentInfo();
        //是否显示上一级
        if (parentInfo !== '') {
            //重新绑定事件
            returnBtn.removeClass("hidden").off("click")
                .on("click", function () {
                    menu.current.gridGoFormParentId(parentInfo.parentId);
                    //bycj[ 设置菜单信息 ]
                    menu.current.delList(parentInfo.level);
                });
        } else {
            returnBtn.addClass("hidden");
        }

        $("a[name='edit']").on('click', function () {
            var dataNo = $(this).attr("data-dataNo");
            $('a[data-toggle="modal"]').click();
        });

        /**
         * 查看下一级菜单按钮事件
         */
        $("a[name='showChildren']").on('click', function () {
            var dataNo = $(this).attr("data-dataNo");
            menuInfo = grid.exhibitDatas[dataNo];
            //bycj[ 设置菜单信息 ]
            menu.current.setList(menuInfo.level, menuInfo);
            menu.current.gridGoFormParentId(menuInfo.id);
        });
        /**
         * 列表中的删除事件
         */
        $("[name='delete']").on('click', function () {
            var dataNo = $(this).attr("data-dataNo"),
                ed = grid.exhibitDatas[dataNo],
                tpl = '您选择对菜单 <strong>' + ed.name + '</strong> 进行删除操作，系统将会把选中菜单及其所有子菜单删除。<br/>您确定要进行删除操作吗?';
            $.ncConfirm({
                url: ncGlobal.adminRoot + 'menu/json/del',
                data: {
                    id: ed.id
                },
                content: tpl
            });
        });
    }
};

var grid = $.fn.DtGrid.init(dtGridOption);


//自定义查询
function customSearch() {
    grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['eq_delFlag'] = 0;
    grid.fastQueryParameters['lk_name'] = $('#keyword').val();
    grid.pager.startRecord = 0;
    grid.pager.nowPage = 1;
    grid.pager.recordCount = -1;
    grid.pager.pageCount = -1;
    grid.refresh(true);
}

/**
 * 自定义相关
 */
var menu = function ($) {
    /**
     *
     * @type {{}}
     */
    var current = {
        /**
         * 储存菜单列表
         */
        $$list: {},
        /**
         * 修改选择的菜单列表
         * @param level
         * @param obj
         */
        setList: function (level, obj) {
            level && obj && (this.$$list [level] = obj)
        },
        /**
         * 返回列表中指定层级的菜单
         * @param level
         * @returns {*}
         */
        getList: function (level) {
            return this.$$list [level];
        },
        /**
         * 删除
         * @param level
         */
        delList: function (level) {
            delete this.$$list[level];
        },
        /**
         * 获取当前所有 的菜单名字
         */
        getMenuInfo: function () {
            var r = '', n;
            for (n in this.$$list) {
                r += this.$$list[n].name + ' ';
            }
            return r;
        },
        /**
         * 获取上一级信息
         */
        getParentInfo: function () {
            var i = 5;
            while (i) {
                if (this.$$list.hasOwnProperty(i)) {
                    return this.$$list[i];
                }
                i--;
            }
            return '';
        },
        /**
         * 根据父级菜单id设置grid
         * @param parentId
         */
        gridGoFormParentId: function (parentId) {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.fastQueryParameters['eq_parentId'] = parentId;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        }


    };

    /**
     * 绑定事件
     */
    function bindEvent() {
        /**
         * 表单提交成功后的事件
         */
        $("#addForm").on("nc.formSubmit.success", function () {
            //重新
            var $addForm = $("#addForm"),
                $menuName = $addForm.find("input[name='name']"),
                $permission = $addForm.find("input[name='permission']");

            $(".alert-danger").remove();
            formPsly = $addForm.psly();
            formPsly.reset();
            $menuName.val('');
            $permission.val('');
        });


        /**
         * 编辑菜单modal 显示事件
         */
        $("#editModal").on("show.bs.modal", function (e) {

            var $editForm = $("#editForm"),
                $menuId = $editForm.find("input[name='id']"),
                $level = $editForm.find("input[name='level']"),
                $levelHide = $editForm.find("input[name='levelHide']"),
                $menuName = $editForm.find("input[name='name']"),
                $sortNo = $editForm.find("input[name='sortNo']"),
                $type = $editForm.find("input[name='type']"),
                $typeName = $editForm.find("input[name='typeName']"),
                $menuUrl = $editForm.find("input[name='url']"),
                $parentId = $editForm.find("input[name='parentId']"),
                $parentName = $editForm.find("input[name='parentName']"),
                $permission = $editForm.find("input[name='permission']"),
                datano = $(e.relatedTarget).data('no'),
                //获取列表框中的原始数据
                gridData = grid.exhibitDatas[datano];
            //删除错误信息
            $(".alert-danger").remove();
            formPsly = $editForm.psly().reset();
            //修改数值
            $menuId.val(gridData.id);
            $menuName.val(gridData.name);
            $level.val(gridData.level);
            $levelHide.val(gridData.level);
            $sortNo.val(gridData.sortNo);
            $type.val(gridData.type);
            $menuUrl.val(gridData.url);
            $parentId.val(gridData.parentId);
            $permission.val(gridData.permission);
            if(gridData.parentId == 0){
                $parentName.val("--");
            }else{
                $parentName.val(gridData.parentName);
            }
            if (gridData.type == 0){
                $typeName.val("目录");
                $(".menuUrl").hide();
                $(".permission").hide();
            }else if(gridData.type == 1){
                $typeName.val("菜单");
                $(".menuUrl").show();
                $(".permission").show();
            }else {
                $typeName.val("按钮");
                $(".menuUrl").hide();
                $(".permission").show();
            }

        });

        /**
         * 新增下级菜单对话框显示事件
         */
        $("#addChildModal").on("show.bs.modal", function (e) {
            var datano = $(e.relatedTarget).data('no'),
                $addChildForm =$("#addChildForm"),
                $level = $addChildForm.find("input[name='level']"),
                $levelHide = $addChildForm.find("input[name='levelHide']"),
                $parentId = $addChildForm.find("input[name='parentId']"),
                $parentName = $addChildForm.find("input[name='parentName']"),
                gridData = grid.exhibitDatas[datano];


            //清空数据
            $addChildForm.psly().reset();
            $level.val(gridData.level+1);
            $levelHide.val(gridData.level+1);
            $parentId.val(gridData.id);
            $parentName.val(gridData.name);

        });
    }

    /**
     * 返回
     */
    return {
        init: function () {
            bindEvent();
        },
        current: current
    };
}(jQuery);


$(function () {
    grid.fastQueryParameters = new Object();
    grid.fastQueryParameters['eq_parentId'] = 0;
    grid.fastQueryParameters['eq_delFlag'] = 0;
    grid.sortParameter.columnId = ['asc_sortNo'];
    grid.load();
    //绑定方法
    $('#customSearch').click(customSearch);
    menu.init();

    //绑定类型选择事件
    $("input[name=type]").click(function(){
        switch($(this).val()){
            case "0":
                $(".menuUrl").hide();
                $(".permission").hide();
                break;
            case "1":
                $(".menuUrl").show();
                $(".permission").show();
                break;
            default:
                $(".menuUrl").hide();
                $(".permission").show();
                break;
        }
    });
    $(".menuUrl").hide();
    $(".permission").hide();
});