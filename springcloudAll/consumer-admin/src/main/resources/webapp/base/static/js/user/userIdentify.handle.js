
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
            2: $lang.idType.T2,
            0: $lang.idType.T3
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
        id: 'u.country',
        title: '地区',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            1: $lang.country.T1,
            2: $lang.country.T2,
            0: $lang.country.T3
        }
    },
    {
        id: 'realnameState',
        title: '实名状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            0: $lang.realNameAuthentication.T1,
            1: $lang.realNameAuthentication.T2,
            2: $lang.realNameAuthentication.T3,
            3: $lang.realNameAuthentication.T4

        }
    },
    {
        id: 'realnameTime',
        title: '实名认证时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'emailState',
        title: '邮箱状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            0: $lang.realNameAuthentication.T1,
            1: $lang.realNameAuthentication.T2,
            2: $lang.realNameAuthentication.T3,
            3: $lang.realNameAuthentication.T4

        }
    },
    {
        id: 'emailTime',
        title: '邮箱认证时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'mobileState',
        title: '手机状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType:"eq",
        codeTable: {
            0: $lang.realNameAuthentication.T1,
            1: $lang.realNameAuthentication.T2,
            2: $lang.realNameAuthentication.T3,
            3: $lang.realNameAuthentication.T4

        }
    },
    {
        id: 'mobileTime',
        title: '手机认证时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    // {
    //     id: 'remark',
    //     title: '备注',
    //     type: 'string',
    //     headerClass: 'text-center',
    //     columnClass: 'text-left',
    //     fastSort: false,
    //     hideType: 'md|sm|xs|lg',
    //     resolution: function (value, record, column, grid, dataNo, columnNo) {
    //         if(record.remark== undefined){
    //             return "--";
    //         }else{
    //             return record.remark;
    //         }
    //     }
    // },
    {
        id: 'operation',
        title: '操作',
        type: 'string',
        columnClass: 'text-center',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = "";
            var check = "<a data-target='#checkModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;实名审核&nbsp;</a>";
            // 判断是否有查看权限
            if ($("#checkPermi") && $("#checkPermi").val() == 1) {
                if(record.realnameState == 1){
                    html += check;
                }
            }
            return html;
            // if(record.realnameState ==1){
            //     if($("#checkPermi") && $("#checkPermi").val == 1){
            //         return "<a data-target='#checkModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;实名审核&nbsp;</a>"
            //     }
            // }else {
            //     return "";
            // }
            // return "";
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/userIdentify/json/list',
    exportURL: ncGlobal.adminRoot + 'userIdentify/json/export',
    exportFileName: '用户认证',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery|export[excel]',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int:["id","delFlag","realnameState","emailState","mobileState","u.idType","u.country"],Timestamp:["createTime","updateTime"]}
};

var grid = $.fn.DtGrid.init(dtGridOption);
//默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//排序
grid.sortParameter.columnId = ['asc_realnameState'];
//列表结束

//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

        // 审核对话框
        $('#checkModal').on('show.bs.modal', function (event) {
            var    //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            //清除错误提示
            $(".alert-danger").remove();
            modal.psly().reset();
            modal.find('input[name="id"]').val(gridData.id);
            modal.find('input[name="realname"]').val(gridData.u.realname);
            modal.find('input[name="idNo"]').val(gridData.u.idNo);
            modal.find('textarea[name="remark"]').val("");
            $('#lookFormPicImg1').attr('src', 'http://bket001.oss-cn-beijing.aliyuncs.com/' + gridData.idPositivePic);
            $('#lookFormPicImg2').attr('src', 'http://bket001.oss-cn-beijing.aliyuncs.com/' + gridData.idNegativePic);
            //证件类型
            // var obj = document.getElementById('idTypeEdit');
            // for(var i = 0; i < obj.options.length; i++){
            //     var tmp = obj.options[i].value;
            //     if(tmp == gridData.isType){
            //         obj.options[i].selected = 'selected';
            //         break;
            //     }
            // }
            if (gridData.u.idType == 1) {
                $("#idType").val("身份证");
            } else{
                $("#idType").val("护照");
            }
            //审核状态
            // var obj = document.getElementById('realnameStateEdit');
            // for(var i = 0; i < obj.options.length; i++){
            //     var tmp = obj.options[i].value;
            //     if(tmp == gridData.realnameState){
            //         obj.options[i].selected = 'selected';
            //         break;
            //     }
            // }
            $(".realnameState").bootstrapSwitch('state', true);
        });

        //模糊搜索
        $('#customSearch').click(function () {
            //grid.fastQueryParameters = new Object();
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