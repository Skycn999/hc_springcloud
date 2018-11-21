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
        title: '序号',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'anNo',
        title: '公告编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'cnContent',
        title: '公告内容',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'title',
        title: '公告标题',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'state',
        title: '状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        codeTable: {
            0: $lang.announcementState.unpublished,
            1: $lang.announcementState.published,
            2: $lang.announcementState.revoke
        }
    },
    {
        id: 'createTime',
        title: '发布时间',
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
        columnClass: 'text-center width-260',
        fastSort: false,
        extra: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            var back = "";
            var publish = "";
            if ($("#announcePublish") && $("#announcePublish").val() == 1) {
                publish = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='OperateHandle.pubInfo(" + record.id + ",\"" + record.title + "\")'><i class='fa fa-paper-plane-o'></i>&nbsp;发布&nbsp;</a>";
            }
            if ($("#announceBack") && $("#announceBack").val() == 1) {
                back = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='OperateHandle.backInfo(" + record.id + ",\"" + record.title + "\")'><i class='fa fa-reply-all'></i>&nbsp;撤回&nbsp;</a>";
            }
            if(record.state !=1 ){
                html = publish;
            }else if(record.state == 1){
                html = back;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/announcement/json/list',
    //exportURL: ncGlobal.adminRoot + 'users/json/export',
    exportFileName: '维护公告',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    //tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "delFlag","state"], Timestamp: ["createTime", "updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);
//默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//排序
grid.sortParameter.columnId = ['desc_id'];
//列表结束

//操作处理开始
var OperateHandle = function () {

    //撤回url
    var backUrl = ncGlobal.adminRoot + "announcement/json/back";
    //发布url
    var publishUrl = ncGlobal.adminRoot + "announcement/json/publish";

    /**
     * 撤回
     */
    function _backInfo(id, content) {
        var tpl = '您是否确定撤回公告 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: backUrl,
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "撤回操作"
        });
    }

    /**
     * 发布
     */
    function _pubInfo(id, content) {
        var tpl = '您是否确定发布公告 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: publishUrl,
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "发布操作"
        });
    }

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

            $("#addForm").find("[name='title']").val("");
            $("#addForm").find("[name='content']").val("");
            $(".state").bootstrapSwitch('state', true);
            $("#cnContentAdd").val("");
            $("#enContentAdd").val("");
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_title'] = $('#keyword').val();
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
        },
        backInfo: _backInfo,
        pubInfo: _pubInfo
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});