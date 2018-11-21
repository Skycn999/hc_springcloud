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
        id: 'billNo',
        title: '流水号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'coinCurrency',
        title: '币种',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'direction',
        title: '转换方向',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.platAssetLogDirection.TOACCOUNT,
            2: $lang.platAssetLogDirection.TOPLATFORM
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
            0: $lang.platAssetLogStatus.NORMAL,
            1: $lang.platAssetLogStatus.DELETE
        }
    },
    {
        id: 'platAddr',
        title: '平台地址',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'platAddr',
        title: '平台地址',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'exchange',
        title: '交易所',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'motherAccount',
        title: '母账号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'amount',
        title: '划拨数量',
        type: 'number',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'turnCoinTime',
        title: '转账时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center width-180',
        columnClass: 'text-center width-180',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    },
    {
        id: 'creator',
        title: '录入人员',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: false
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var delHtml = "<a href='javascript:;' class='btn btn-danger btn-sm' onclick='OperateHandle.delConfig(" + record.id + ")' ><i class='fa fa fa-lg fa-trash-o'></i>&nbsp;作废&nbsp;</a>";
            var html = "";
            // 判断是否有作废权限
            if ($("#delPermi") && $("#delPermi").val() == 1) {
                if (record.state == 0) {
                    html += delHtml;
                }
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'platAssetLog/json/list',
    exportURL: ncGlobal.adminRoot + 'platAssetLog/json/export',
    exportFileName: '资产划拨记录列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery|export[excel]',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id","direction", "state","delFlag"], Timestamp: ["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);

//排序
grid.sortParameter.columnId = ['desc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        //添加对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            var modal = $(this);
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();
            modal.find('input[name="billNo"]').val("");
            $("#coinCurrencyAdd").find("option[value= '']").prop("selected",true);
            $("#motherAccountAdd").html("");
            $("#directionAdd").find("option[value= '']").prop("selected",true);
            $("#exchangeAdd").find("option[value= '']").prop("selected",true);
            modal.find('input[name="platAddr"]').val("");
            modal.find('input[name="amount"]').val("");
            modal.find('input[name="turnCoinTimeStr"]').val("");
        })

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_coin_currency_or_lk_bill_no'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 作废入驻申请
     */
    function _delConfig(id) {
        var tpl = '您确定要作废该记录吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "platAssetLog/json/del",
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "作废操作"
        });
    }

    /**
     * 交易所母账号联动下拉
     */
    function _changeData(){
        // 获取交易所下拉框选中的值
        var v =  $("#exchangeAdd").attr("excid");
        if( v == ''){
            // 母账号下拉框清空
            $("#motherAccountAdd").html("");
        }else{
            // 交易所选择后母账号下拉框初始化
            var moAcc = $("#motherAccountAdd");
            moAcc.html("");
            var url = ncGlobal.adminRoot+"/motherAccount/json/listByEx";
            // 向后台请求获取数据
            $.ajax({
                type: 'POST',
                url: url,
                dataType: "json",
                data: {
                    id:v
                },
                success: function (data) {
                    if (data.code != 200) {
                        $.ncAlert({
                            content:"获取联动交易母账号失败",
                            autoCloseTime: 3,
                            callback: function () {
                            }
                        });
                    }else if(data.data.length>0){
                        $.each(data.data, function (i, value) {
                            var tempOption = document.createElement("option");
                            tempOption.value = value.accountNo;
                            tempOption.innerHTML = value.accountNo;
                            moAcc.append(tempOption);
                        });
                    } else {
                        $.ncAlert({
                            content:"该交易所下未维护交易母账号，请先添加交易母账号!",
                            autoCloseTime: 3,
                            callback: function () {
                            }
                        });
                    }
                }
            });
        }
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delConfig: _delConfig,
        changeData: _changeData
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});