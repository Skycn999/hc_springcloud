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
        id: 'ip',
        type: 'string',
        title: '操作IP地址',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    },
    {
        id: 'uri',
        type: 'string',
        title: 'URL',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk',
    },
    {
        id: 'method',
        type: 'string',
        title: '操作方式',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        hideType: 'md|sm',
        fastQueryType: 'lk',
    },
    {
        id: 'code',
        type: 'string',
        title: '操作结果',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        hideType: 'md|sm',
        fastQueryType: 'lk',
    },
    {
        id: 'params',
        type: 'string',
        title: '操作提交的数据',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        hideType: 'md|sm|xs|lg',
        fastQueryType: 'lk',
    },
    {
        id: 'exception',
        type: 'string',
        title: '异常信息',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        hideType: 'md|sm|xs|lg',
        fastQueryType: 'lk',
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            if(record.exception== undefined){
                return "--";
            }else{
                return record.loseAmount;
            }
        }
    },
    {
        id: 'createTime',
        title: '操作时间',
        type: 'date',
        format: 'yyyy-MM-dd HH:mm:ss',
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
    loadURL: ncGlobal.adminRoot + 'log/json/list',
    exportFileName: '操作日志',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id","delFlag"], Timestamp: ["createTime"]}
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
            grid.fastQueryParameters['lk_ip_or_lk_uri'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.sortParameter.columnId=['desc_createTime'];
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

