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
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'accountNo',
        title: '母账号编号',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'ex.name',
        title: '交易所名称',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'accountName',
        title: '母账号名称',
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
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.commonStatus.ENABLE,
            0: $lang.commonStatus.DISABLE
        }
    },
    {
        id: 'createTime',
        title: '创建时间',
        type: 'date',
        format:'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'range'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var detail = "<a data-target='#detailModal' class='btn btn-sm btn-success m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-eye'></i>&nbsp;详情&nbsp;</a>";
            var edit = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            var html = "";
            // 判断是否有编辑权限
            if ($("#editPermi") && $("#editPermi").val() == 1) {
                html += edit;
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
    loadURL: ncGlobal.adminRoot + 'motherAccount/json/list',
    exportFileName: '母账号管理列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "state","delFlag"], Timestamp: ["createTime","updateTime"]}
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
            $("#exchangeAdd").find("option[value= '']").prop("selected",true);
            modal.find('input[name="accountId"]').val("");
            modal.find('input[name="accountName"]').val("");
            modal.find('input[name="accountPwd"]').val("");
            modal.find('input[name="apiKey"]').val("");
            modal.find('input[name="apiSecret"]').val("");
            modal.find('input[name="googlePrivateKey"]').val("");
            $("#accountPwdAdd").val("")
            $("#authKeyAdd").val("")
            $("#googlePrivateKeyAdd").val("")
            modal.find('input[name="accountEmail"]').val("");
            modal.find('input[name="accountMobile"]').val("");
            $(".stateAdd").bootstrapSwitch('state', true);
        })

        //编辑对话框初始化
        $("#editModal").on("show.bs.modal", function (event) {
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
            $(".alert-danger").remove();
            $("#editForm").psly().reset();
            modal.find('input[name="id"]').val(gridData.id);
            modal.find('input[name="accountNo"]').val(gridData.accountNo);
            modal.find('input[name="accountId"]').val(gridData.accountId);
            modal.find('input[name="accountName"]').val(gridData.accountName);
            modal.find('input[name="accountPwd"]').val(gridData.accountPwd);
            modal.find('input[name="apiKey"]').val(gridData.apiKey);
            modal.find('input[name="apiSecret"]').val(gridData.apiSecret);
            $("#accountPwdEdit").val(gridData.accountPwd);
            $("#googlePrivateKeyEdit").val(gridData.googlePrivateKey);
            //RSA加密
            var encrypt = new JSEncrypt();
            var key = $('#key').val();
            encrypt.setPublicKey(key);
            var accountPwd = encrypt.encrypt(gridData.accountPwd);
            var googlePrivateKey = encrypt.encrypt(gridData.googlePrivateKey);
            modal.find('input[name="accountPwd"]').val(accountPwd);
            modal.find('input[name="googlePrivateKey"]').val(googlePrivateKey);
            modal.find('input[name="accountEmail"]').val(gridData.accountEmail);
            modal.find('input[name="accountMobile"]').val(gridData.accountMobile);
            //选择的交易所
            var obj = document.getElementById('exchangeEdit');
            for(var i = 0; i < obj.options.length; i++){
                var tmp = obj.options[i].value;
                if(tmp == gridData.exId){
                    obj.options[i].selected = 'selected';
                    break;
                }
            }
            //状态
            if (gridData.state == 1) {
                $(".statecEdit").bootstrapSwitch('state', true);
            } else {
                $(".statecEdit").bootstrapSwitch('state', false);
            }
        });

        //详情对话框初始化
        $("#detailModal").on("show.bs.modal", function (event) {
            //获取接受事件的元素
            var //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            //清除错误信息
            $(".alert-danger").remove();
            modal.find('input[name="accountNo"]').val(gridData.accountNo);
            modal.find('input[name="accountId"]').val(gridData.accountId);
            modal.find('input[name="exName"]').val(gridData.ex.name);
            modal.find('input[name="accountName"]').val(gridData.accountName);
            modal.find('input[name="accountPwd"]').val(gridData.accountPwd);
            modal.find('input[name="apiKey"]').val(gridData.apiKey);
            modal.find('input[name="apiSecret"]').val(gridData.apiSecret);
            modal.find('input[name="googlePrivateKey"]').val(gridData.googlePrivateKey);
            modal.find('input[name="accountEmail"]').val(gridData.accountEmail);
            modal.find('input[name="accountMobile"]').val(gridData.accountMobile);
            modal.find('input[name="creator"]').val(gridData.creator);
            modal.find('input[name="updator"]').val(gridData.updator);
            modal.find('input[name="createTime"]').val(gridData.createTime);
            modal.find('input[name="updateTime"]').val(gridData.updateTime);
            //状态
            if (gridData.state == 1) {
                modal.find('input[name="state"]').val("启用");
            } else {
                modal.find('input[name="state"]').val("禁用");
            }
        });

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_account_no_or_lk_account_name'] = $('#keyword').val();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });
    }

    /**
     * 删除入驻申请
     */
    function _delConfig(id) {
        var tpl = '您确定要删除该参数吗?'
        $.ncConfirm({
            url: ncGlobal.adminRoot + "config/json/del",
            data: {
                id: id
            },
            content: tpl
        });
    }

    /**
     * 密码加密
     */
    function _changePwd(obj){
        var encrypt = new JSEncrypt();
        var key = $('#key').val();
        encrypt.setPublicKey(key);
        var encrypted = encrypt.encrypt($(obj).val());
        $("#addModal").find('input[name="accountPwd"]').val(encrypted);
    }


    /**
     * Googlekey加密
     * @param obj
     * @private
     */
    function _changeGoogle(obj){
        var encrypt = new JSEncrypt();
        var key = $('#key').val();
        encrypt.setPublicKey(key);
        var encrypted = encrypt.encrypt($(obj).val());
        $("#addModal").find('input[name="googlePrivateKey"]').val(encrypted);
    }


    /**
     * 密码加密
     */
    function _changePwdEdit(obj){
        var encrypt = new JSEncrypt();
        var key = $('#key').val();
        encrypt.setPublicKey(key);
        var encrypted = encrypt.encrypt($(obj).val());
        $("#editModal").find('input[name="accountPwd"]').val(encrypted);
    }


    /**
     * Googlekey加密
     * @param obj
     * @private
     */
    function _changeGoogleEdit(obj){
        var encrypt = new JSEncrypt();
        var key = $('#key').val();
        encrypt.setPublicKey(key);
        var encrypted = encrypt.encrypt($(obj).val());
        $("#editModal").find('input[name="googlePrivateKey"]').val(encrypted);
    }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        delConfig: _delConfig,
        changePwd:_changePwd,
        changeGoogle:_changeGoogle,
        changePwdEdit:_changePwdEdit,
        changeGoogleEdit:_changeGoogleEdit
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});