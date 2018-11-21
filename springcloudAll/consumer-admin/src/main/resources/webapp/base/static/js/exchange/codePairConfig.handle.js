
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
        title: '编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'code',
        title: '资产代码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'exNumbers',
        title: '交易所',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'baseCode',
        title: '分区',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'type',
        title: '类型',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.codePairConfigType.MAIN,
            2: $lang.codePairConfigType.NON_MAIN
        }
    },
    {
        id: 'state',
        title: '状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            0: $lang.codePairConfigState.T1,
            1: $lang.codePairConfigState.T2
        }
    },
    {
        id: 'isDisplayOnApp',
        title: '是否在app主页显示',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            0: $lang.isDisplayOnApp.NO,
            1: $lang.isDisplayOnApp.YES
        }
    },
    {
        id: 'limitMinAmount',
        title: '限价最小下单数量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
    },
    {
        id: 'limitMaxAmount',
        title: '限价最大下单数量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
    },
    {
        id: 'marketMinBuyAmount',
        title: '市价最小买入量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
    },
    {
        id: 'marketMaxBuyAmount',
        title: '市价最大买入量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
    },
    {
        id: 'marketMinSellAmount',
        title: '市价最小卖出量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
    },
    {
        id: 'marketMaxSellAmount',
        title: '市价最大卖出量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
    },
    {
        id: 'minQuotePoint',
        title: '最小行情点位',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false,
    },
    {
        id: 'operation',
        title: '操作',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            var edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            // 判断是否有查看权限
            if ($("#commodityEdit") && $("#commodityEdit").val() == 1) {
                html += edit;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/codePairConfig/json/list',
    // exportURL: ncGlobal.adminRoot + 'userIdentify/json/export',
    exportFileName: '代码对管理表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int:["id","delFlag","state"],Timestamp:["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);
//默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//排序
grid.sortParameter.columnId = ['asc_id'];
//列表结束

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
            $("#codeAdd").find("option[value= '']").prop("selected",true);
            $(".exchange").prop("checked",false);
            modal.find("input[name='baseCode']").prop("checked",false);
            $("#addState").bootstrapSwitch('state', true);
            $("#typeAdd").find("option[value= '']").prop("selected",true);
            $("#addIsDisplayOnApp").bootstrapSwitch('state', true);
            modal.find("input[name='limitMinAmount']").val("");
            modal.find("input[name='limitMaxAmount']").val("")
            modal.find("input[name='marketMinBuyAmount']").val("");
            modal.find("input[name='marketMaxBuyAmount']").val("");
            modal.find("input[name='marketMinSellAmount']").val("");
            modal.find("input[name='marketMaxSellAmount']").val("");
            modal.find("input[name='minQuotePoint']").val("");
            // $("#partitions1").prop("checked",false);
            // $("#partitions2").prop("checked",false);
            // $("#partitions3").prop("checked",false);
            // $("#partitions4").prop("checked",false);
            // $("#partitions5").prop("checked",false);
        });


        // 编辑对话框
        $('#editModal').on('show.bs.modal', function (event) {
            //清除错误信息
            $(".parsley-type").remove();
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            var editForm = $("#editForm");
            //清除错误提示
            $("#editForm").psly().reset();
            $(".alert-danger").remove();

            //选择资产代码
            var objc = document.getElementById('editCode');
            for(var i = 0; i < objc.options.length; i++){
                var tmp = objc.options[i].value;
                if(tmp == gridData.code){
                    objc.options[i].selected = 'selected';
                    break;
                }
            }
            //选择类型
            var obj = document.getElementById('typeEdit');
            for(var i = 0; i < obj.options.length; i++){
                var tmp = obj.options[i].value;
                if(tmp == gridData.type){
                    obj.options[i].selected = 'selected';
                    break;
                }
            }
            modal.find('input[name="id"]').val(gridData.id);
            modal.find("input[name='limitMinAmount']").val(gridData.limitMinAmount);
            modal.find("input[name='limitMaxAmount']").val(gridData.limitMaxAmount)
            modal.find("input[name='marketMinBuyAmount']").val(gridData.marketMinBuyAmount);
            modal.find("input[name='marketMaxBuyAmount']").val(gridData.marketMaxBuyAmount);
            modal.find("input[name='marketMinSellAmount']").val(gridData.marketMinSellAmount);
            modal.find("input[name='marketMaxSellAmount']").val(gridData.marketMaxSellAmount);
            modal.find("input[name='minQuotePoint']").val(gridData.minQuotePoint);

            //状态
            if (gridData.state == 1) {
                $(".state").bootstrapSwitch('state', true);
            } else {
                $(".state").bootstrapSwitch('state', false);
            }
            //是否在App显示
            if (gridData.isDisplayOnApp == 1) {
                $(".isDisplayOnApp").bootstrapSwitch('state', true);
            } else {
                $(".isDisplayOnApp").bootstrapSwitch('state', false);
            }

            var exnumStrs = $("#exnumStrs").val();
            var exNums = gridData.exNumbers;
            var strsArray = exNums.split(",");
            //初始化交易所选中
            modal.find("input[name='exNumbers']").prop("checked",false);
            for(var i=0;i<strsArray.length;i++) {
                var ex = strsArray[i]
                modal.find("input[value='"+ex+"']").prop("checked",true);
                // var number = $("#ex"+i).val();
                // if(exnumStrs.indexOf(number)!= -1){
                //     $("#ex"+i).attr("checked",true);
                // }
            }

            //分区
            var pars = gridData.baseCode;
            modal.find("input[name='baseCode']").prop("checked",false);
            modal.find("input[value='"+pars+"']").prop("checked",true);
        });


        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_code'] = $('#keyword').val();
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
        bindEvent: function (){
            _bindEvent();
        }
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});