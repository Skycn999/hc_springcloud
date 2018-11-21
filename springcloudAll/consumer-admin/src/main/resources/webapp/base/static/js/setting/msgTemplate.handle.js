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
        id: 'id',
        title: '编号',
        type: 'string',
        headerClass: 'text-left width-60',
        columnClass: 'text-left width-60',
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'nid',
        title: '模板标识',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'type',
        title: '模板类型',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: false,
        fastQueryType: 'eq',
        codeTable:{
            1:$lang.templateType.SYS,
            2:$lang.templateType.BIZ
        }
    },
    {
        id: 'name',
        title: '模板名称',
        type: 'string',
        headerClass: 'text-left',
        columnClass: 'text-left',
        hideType: 'sm|xs',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq'
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        columnClass: 'text-center width-350',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var _content = '<a href="#editModalLetter" class="btn btn-sm btn-primary m-r-10" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-volume-down"></i>&nbsp;站内信&nbsp;</a>' +
                '<a href="#editModalSms" class="btn btn-sm btn-success m-r-10" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-mobile"></i>&nbsp;短信&nbsp;</a>' +
                '<a href="#editModalEmail" class="btn btn-warning btn-sm m-r-10" data-toggle="modal" data-no="' + dataNo + '"><i class="fa fa-envelope-o"></i>&nbsp;邮件&nbsp;</a>' +
                '<a href="javascript:;" class="btn btn-danger btn-sm" name="delete" data-no="' + dataNo + '"><i class="fa fa-lg fa-trash-o m-r-10"></i>删除</a>';;
            return _content;
        }
    },
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL : ncGlobal.adminRoot + 'msgTemplate/json/list',
    exportFileName: '消息模板列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools : 'refresh|fastQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: { int: ["type", "smsState", "emailState", "delFlag"]},
    onGridComplete: function (grid) {
        /**
         * 列表中的删除事件
         */
        $("[name='delete']").on('click', function () {
            var dataNo = $(this).attr("data-no"),
                ed = grid.exhibitDatas[dataNo],
                tpl = '您选择对模板 <strong>' + ed.name + '</strong> 进行删除操作，系统将会删除选中模板的站内信、短信、邮件模板。<br/>您确定要进行删除操作吗?';
            $.ncConfirm({
                url: ncGlobal.adminRoot + 'msgTemplate/json/del',
                data: {
                    id: ed.id
                },
                content: tpl
            });
        });
    }
};

var grid = $.fn.DtGrid.init(dtGridOption);
grid.sortParameter.columnId = ['asc_id'];
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;

var message_template = function() {
    //编辑器对象
    var _ueEdit;
    var _bindEven = function() {

        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
            grid.pager.startRecord= 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        /**
         * 新增消息模板
         */
        $("#addModal").on("show.bs.modal", function (e) {
            //清除错误信息
            var modal = $(this);
            $(".alert-danger").remove();
            $("#addFormMsg").psly().reset();
            modal.find("option[value= '1']").prop("selected",true);
            modal.find("input[name='nid']").val("");
            modal.find("input[name='name']").val("");
        });

        /**
         * 编辑站内信
         */
        $("#editModalLetter").on("show.bs.modal", function (e) {
            //获取接受事件的元素
            //获取data 参数
            var modal = $(this);
            $(".alert-danger").remove();
            $("#editFormLetter").psly().reset();
            var datano = $(e.relatedTarget).data('no'),
            //获取列表框中的原始数据
            gridData = grid.sortOriginalDatas[datano];
            modal.find("input[name='id']").val(gridData.id);
            $('#nidLetter').val(gridData.nid);
            $('#nameLetter').val(gridData.name);
            if (gridData.letterState == 1) {
                $("#letterState").bootstrapSwitch('state', true);
            } else {
                $("#letterState").bootstrapSwitch('state', false);
            }
            $('#letterContent').val(gridData.letterContent);
        });

        /**
         * 编辑短信
         */
        $("#editModalSms").on("show.bs.modal", function(e){
            var modal = $(this);
            $(".alert-danger").remove();
            $("#editFormSms").psly().reset();
            var datano = $(e.relatedTarget).data('no'),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            modal.find("input[name='id']").val(gridData.id);
            $('#nidSms').val(gridData.nid);
            $('#nameSms').val(gridData.name);
            if (gridData.smsState == 1) {
                $("#smsState").bootstrapSwitch('state', true);
            } else {
                $("#smsState").bootstrapSwitch('state', false);
            }
            $('#smsContent').val(gridData.smsContent);
        });

        /**
         * 编辑邮件
         */
        $("#editModalEmail").on("show.bs.modal", function(e){
            var modal = $(this);
            $(".alert-danger").remove();
            $("#editFormEmail").psly().reset();
            var datano = $(e.relatedTarget).data('no'),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            modal.find("input[name='id']").val(gridData.id);
            $('#nidEmail').val(gridData.nid);
            $('#nameEmail').val(gridData.name);
            if (gridData.emailState == 1) {
                $("#emailState").bootstrapSwitch('state', true);
            } else {
                $("#emailState").bootstrapSwitch('state', false);
            }
            modal.find("input[name='emailTitle']").val(gridData.emailTitle);
            if (gridData.emailContent != null) {
                message_template.ueEdit.setContent(gridData.emailContent);
            }
        });
    }
    return {
        init : function() {
            _bindEven();
        },
        ueEdit : _ueEdit
    }
}();

$(function() {
    grid.load();
    message_template.init();
    //实例化编辑器
    message_template.ueEdit = UE.getEditor('contentEdit',{textarea:'emailContent'});
});