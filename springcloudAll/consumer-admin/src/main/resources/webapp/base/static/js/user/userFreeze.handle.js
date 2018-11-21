
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
        title: '用户编号',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'u.mobile',
        title: '手机号码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'u.email',
        title: '邮箱',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'u.realname',
        title: '姓名',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'u.idType',
        title: '证件类型',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        codeTable: {
            1: $lang.idType.T1,
            2: $lang.idType.T2
        }
    },
    {
        id: 'u.idNo',
        title: '证件号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'loginState',
        title: '登录状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            0: $lang.userFreezeState.FROZEN,
            1: $lang.userFreezeState.UNBLOCKED
        }
    },
    {
        id: 'tradeState',
        title: '交易状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            0: $lang.userFreezeState.FROZEN,
            1: $lang.userFreezeState.UNBLOCKED
        }
    },
    {
        id: 'mentionCoinState',
        title: '提币状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            0: $lang.userFreezeState.FROZEN,
            1: $lang.userFreezeState.UNBLOCKED
        }
    },
    {
        id: 'rechargeCoinState',
        title: '充币状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            0: $lang.userFreezeState.FROZEN,
            1: $lang.userFreezeState.UNBLOCKED
        }
    },
    {
        id: 'operation',
        title: '操作',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            var edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;冻结设置&nbsp;</a>";
            // 判断是否有查看权限
            if ($("#freezeEdit") && $("#freezeEdit").val() == 1) {
                html += edit;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/userFreeze/json/list',
    exportURL: ncGlobal.adminRoot + '/userFreeze/json/export',
    exportFileName: '用户冻结列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery|export[excel]',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int:["id","delFlag","loginState","tradeState","mentionCoinState","rechargeCoinState"],Timestamp:["createTime","updateTime"]}

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

        // 编辑对话框
        $('#editModal').on('show.bs.modal', function (event) {
            var    //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            //清除错误提示
            //editForm.psly().reset();
            $(".alert-danger").remove();
            modal.psly().reset();
            modal.find('input[name="id"]').val(gridData.id);
            modal.find('input[name="realname"]').val(gridData.u.realname);
            modal.find('input[name="mobile"]').val(gridData.u.mobile);
            modal.find('textarea[name="remark"]').val('');
            // if(gridData.loginState == 0){$("#login1").attr("checked",true);$("#login1").val(0);}else{$("#login2").attr("checked",true);$("#login2").val(1);}
            // if(gridData.mentionCoinState == 0){$("#cash1").attr("checked",true);$("#cash1").val(0);}else{$("#cash2").attr("checked",true);$("#cash2").val(1);}
            // if(gridData.rechargeCoinState == 0){$("#recharge1").attr("checked",true);$("#cash1").val(0);}else{$("#recharge2").attr("checked",true);$("#recharge2").val(1);}
            // if(gridData.tradeState == 0){$("#tender1").attr("checked",true);$("#tender1").val(0);}else{$("#tender2").attr("checked",true);$("#tender2").val(1);}
            if ( gridData.loginState == 1) {
                $(".loginState").bootstrapSwitch('state', true);
            } else {
                $(".loginState").bootstrapSwitch('state', false);
            };
            if(gridData.mentionCoinState == 1){
                $(".mentionCoinState").bootstrapSwitch('state',true);
            }else{
                $(".mentionCoinState").bootstrapSwitch('state',false);
            }
            if ( gridData.rechargeCoinState == 1) {
                $(".rechargeCoinState").bootstrapSwitch('state', true);
            } else {
                $(".rechargeCoinState").bootstrapSwitch('state', false);
            };
            if(gridData.tradeState == 1){
                $(".tradeState").bootstrapSwitch('state',true);
            }else{
                $(".tradeState").bootstrapSwitch('state',false);
            }

            $("#remark").val(gridData.remark);
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_u.realname_or_lk_u.mobile'] = $('#keyword').val();
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