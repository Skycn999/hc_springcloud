/**
 * 依赖全局变量 InfoGlobal
 * Created by shopnc on 2015/11/26.
 */
//定义表格
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
        id: 'u.userName',
        type: 'string',
        title: '用户名',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    },
    {
        id: 'u.mobile',
        type: 'string',
        title: '手机号',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    },
    {
        id: 'title',
        type: 'string',
        title: '标题',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        hideType: 'md|sm',
        fastQueryType: 'lk',
    },
    {
        id: 'type',
        type: 'string',
        title: '通知类型',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',

        codeTable: {
            1: $lang.noticeType.EMAIL,
            2: $lang.noticeType.SMS,
            3: $lang.noticeType.LETTER
        }
    },
    {
        id: 'status',
        type: 'string',
        title: '发送状态',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        hideType: 'md|sm',
        codeTable: {
            1: $lang.noticeStatus.SUCCESS,
            2: $lang.noticeStatus.FAIL
        }
    },
    {
        id: 'receiveAddress',
        type: 'string',
        title: '接收地址',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
        hideType: 'md|sm',
    },
    {
        id: 'content',
        type: 'string',
        title: '发送内容',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
        hideType: 'md|sm|xs|lg'
    },
    {
        id: 'result',
        type: 'string',
        title: '发送结果信息',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
        hideType: 'md|sm|xs|lg'
    },
    {
        id: 'createTime',
        title: '发送时间',
        type: 'date',
        format: 'yyyy-MM-dd',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    }
];
var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'noticeLog/json/list',
    exportFileName: '通知记录',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id","status","type","delFlag"], Timestamp: ["createTime"]}
};
var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId=['desc_createTime'];
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;


var information = function () {

    /**
     * 事件绑定
     * @private
     */
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_u.user_name_or_lk_u.mobile_or_lk_title'] = $('#keyword').val();
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

        $("#addForm").on("nc.formSubmit.success", function (e) {
        });

    }

    //外部可调用
    return {
        init: function () {
            _bindEvent();
        }
    }
}()
$(function () {
    grid.load();
    information.init();
})

