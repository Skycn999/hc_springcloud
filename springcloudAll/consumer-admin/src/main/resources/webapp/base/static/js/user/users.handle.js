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
        id: 'mobile',
        title: '手机号码',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'email',
        title: '邮箱',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'realname',
        title: '姓名',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'idType',
        title: '证件类型',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        codeTable: {
            0: $lang.idType.T0,
            1: $lang.idType.T1,
            2: $lang.idType.T2
        }
    },
    {
        id: 'idNo',
        title: '证件号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'country',
        title: '地区',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: "eq",
        codeTable: {
            0: $lang.country.T0,
            1: $lang.country.T1,
            2: $lang.country.T2
        }
    },
    {
        id: 'state',
        title: '用户状态',
        type: 'int',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: "eq",
        codeTable: {
            1: $lang.usersState.T1,
            2: $lang.usersState.T2,
            3: $lang.usersState.T3

        }
    }, {
        id: 'createTime',
        title: '注册时间',
        type: 'date',
        format: 'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: true,
        fastQuery: true,
        fastQueryType: "range"

    },
    {
        id: 'lastLoginDate',
        title: '最近登录时间',
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
            var use=record.realname;
            var detail = "<a data-target='#detailModal' class='btn btn-sm btn-success m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-eye'></i>&nbsp;详情&nbsp;</a>";
            var editHtml = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            if ($("#editPermi") && $("#editPermi").val() == 1) {
                html += editHtml;
            }
            if($("#usersStatePermi") && $("#usersStatePermi").val() == 1){
                if(record.state == 1){
                    html += "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='OperateHandle.stateInfo(" + record.id + ",\"" + 3+ "\",\"" + use + "\")'><i class='fa fa-ban'></i>&nbsp;注销&nbsp;</a>";
                }else{
                    html += "<a href='javascript:;' class='btn btn-info btn-sm m-r-10' onclick='OperateHandle.stateInfo(" + record.id + ",\"" + 1+ "\",\"" + use + "\")'><i class='fa fa-plus'></i>&nbsp;启用&nbsp;</a>";
                }
            }
            // 判断是否有查看详情权限
            if ($("#detailPermi") && $("#detailPermi").val() == 1) {
                html += detail;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/users/json/list',
    exportURL: ncGlobal.adminRoot + 'users/json/export',
    exportFileName: '用户认证表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery|export[excel]',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "delFlag", "idType", "country", "state"], Timestamp: ["createTime", "updateTime"]},
    // onGridComplete: function (grid) {
    // }
};

var grid = $.fn.DtGrid.init(dtGridOption);
//默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;
//排序
grid.sortParameter.columnId = ['desc_createTime'];
//列表结束

//操作处理开始
var OperateHandle = function () {

    //状态操作url
    var stateUrl = ncGlobal.adminRoot + "users/json/state";

    //状态操作
    function _stateInfo(id,state, content){
        var option='';
        if(state == "1"){
            option += '启用';
        }else{
            option += '注销';
        }
        var tpl ='你选择对用户 <strong>' + content + '</strong>进行'+ option + '操作。<br/>你确定要进行该操作吗?'
        $.ncConfirm({
            url:stateUrl,
            data:{
                id:id,
                state:state
            },
            content:tpl,
            alertTitle: option +"操作"
        });
    }

    function _bindEvent() {

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
            modal.find('input[name="id"]').val(gridData.id);
            modal.find('input[name="mobile"]').val(gridData.mobile);
            modal.find('input[name="realname"]').val(gridData.realname);

            modal.find('input[name="idNo"]').val(gridData.idNo);
            modal.find('input[name="email"]').val(gridData.email);
           $("#googleKey").val(gridData.googleKey);
            //modal.find('input[name="state"]').val(gridData.state);
            if (gridData.idType == 1) {
                $(".idType").bootstrapSwitch('state', true);
            } else {
                $(".idType").bootstrapSwitch('state', false);
            };
            if (gridData.state == 1) {
                $("#state").val("正常");
            } else if (gridData.state == 2) {
                $("#state").val("登录冻结");
            } else {
                $("#state").val("注销");
            };
             if(gridData.country == 1){
                 $(".editCountry").bootstrapSwitch('state',true);
             }else{
                 $(".editCountry").bootstrapSwitch('state',false);
             }
         });

        //查看对话框初始化
        $("#detailModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            //清除错误信息
            // $(".alert-danger").remove();
            // $("#editForm").psly().reset();
            modal.find('input[name="mobile"]').val(gridData.mobile);
            modal.find('input[name="realname"]').val(gridData.realname);
            modal.find('input[name="idNo"]').val(gridData.idNo);
            modal.find('input[name="email"]').val(gridData.email);
            modal.find('input[name="googleKey"]').val(gridData.googleKey);
            modal.find('input[name="createTime"]').val(gridData.createTime);
            modal.find('input[name="errCount"]').val(gridData.errCount);
            modal.find('input[name="lastLoginDate"]').val(gridData.lastLoginDate);
            $('#lookFormPicImg').attr('src', 'http://bket001.oss-cn-beijing.aliyuncs.com/' + gridData.a.idPositivePic);
            //类型
            if (gridData.idType == 1) {
                modal.find('input[name="idType"]').val("身份证");
            } else if (gridData.idType == 2){
                modal.find('input[name="idType"]').val("护照");
            }else {
                modal.find('input[name="idType"]').val("");
            }
            //国家、地区
            if (gridData.country == 1) {
                modal.find('input[name="country"]').val("境内");
            } else if(gridData.country == 2){
                modal.find('input[name="country"]').val("境外");
            }else {
                modal.find('input[name="country"]').val("");
            }
            //状态
            if (gridData.state == 1) {
                modal.find('input[name="state"]').val("正常");
            } else if (gridData.state == 2) {
                modal.find('input[name="state"]').val("登录冻结");
            } else {
                modal.find('input[name="state"]').val("注销");
            }
        });

        //证件类型触发事件
        $('#idTypeSwitch input').on('switchChange.bootstrapSwitch', function (event,state) {
            if(state){
                $(".editCountry").bootstrapSwitch('state',true);
            }else {
                $(".editCountry").bootstrapSwitch('state',false);
            }
        });
        //境内境外触发事件
        $('#countrySwitch input').on('switchChange.bootstrapSwitch', function (event,state) {
            if(state){
                $(".idType").bootstrapSwitch('state',true);
            }else {
                $(".idType").bootstrapSwitch('state',false);
            }
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['lk_realname_or_lk_mobile'] = $('#keyword').val();
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
        stateInfo: _stateInfo,
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});