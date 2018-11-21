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
        id: 'name',
        title: '模板名称',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastQuery: true,
        fastQueryType: 'lk',
        fastSort: false,
    },
    {
        id: 'status',
        title: '状态',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.pdfStatus.ENABLE,
            2: $lang.pdfStatus.DISABLE
        }
    },
    {
        id: 'type',
        title: '类型',
        type: 'string',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'eq',
        codeTable: {
            1: $lang.pdfType.INVEST,
            2: $lang.pdfType.REGISTER,
            3: $lang.pdfType.TRANSFER
        }
    },
    {
        id: 'operation',
        title: '管理操作',
        type: 'string',
        headerClass: 'text-center width-200',
        columnClass: 'text-center width-200',
        fastSort: false,
        extra: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var html = '';
            html += "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            html += "<a data-target='#addParamModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-bandcamp'></i>&nbsp;填参&nbsp;</a>";
            return html;
        }
    }
];


var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + 'pdf/json/list',
    exportFileName: '协议管理',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    tools: 'refresh|faseQuery',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int: ["id", "status", "tpye"]},
    onGridComplete: function (grid) {
    }
};

var grid = $.fn.DtGrid.init(dtGridOption);
// 默认查询条件
grid.fastQueryParameters = new Object();
grid.fastQueryParameters['eq_delFlag'] = 0;

grid.sortParameter.columnId = ['asc_id'];

/** 参数选项初始化 */

var datas;
$.ajax({
    dataType:'json',
    type: "POST",
    async: false,
    data: "",
    url: ncGlobal.adminRoot + "pdf/json/pdfParam",
    success: function (data) {
        datas=data.data;
    }
});
function formatPdfParam(param){
    var result = "";
    if(datas!=null && datas!="[]"){
        result +="<select name='param'>";
        for(var i = 0;i<datas.length;i++){
            if(param!=null && datas[i].value==param){
                result+="<option selected value ='"+datas[i].value+"'>"+datas[i].name+"</option>";
            }else{
                result+="<option value ='"+datas[i].value+"'>"+datas[i].name+"</option>";
            }
        }
         result+="</select>";
    }
    return result;

}

var information = function () {

    /**
     * 图片缩放
     */
    function _refreshImage() {
        $(".viewImage").jqthumb({
            width: 160,
            height: 100,
            after: function (imgObj) {
                imgObj.css('opacity', 0).animate({opacity: 1}, 1500);
            }
        });
    }


    //编辑器对象[添加]
    var _ueAdd;
    //编辑器对象[编辑]
    var _ueEdit;
    //编辑器对象[填参]
    // var _ueEdit2;


    var addInfo = {
        initAddModal: function () {
            $addForm = $("#addForm");
            $(".alert-danger").remove();
            $addForm.psly().reset();
            $addForm.find('input[name="name"]').val("");
            $(".addStatus").bootstrapSwitch('state', true);
            $("#type").find("option[value='1']").attr("selected",true);
            $("#type").find("option[value='2']").attr("selected",false);
            $("#type").find("option[value='3']").attr("selected",false);
            $('#addModal').find('div[name="preview"]').html('');
            information.ueAdd.setContent("");
        }
    }

    /**
     * 事件绑定
     * @private
     */
    function _bindEvent() {
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters['lk_name'] = $('#keyword').val();
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        //添加事件时显示时
        $('#addModal').on('show.bs.modal', function (event) {
            addInfo.initAddModal();
            _refreshImage();
        });
        // bycj [ 编辑对话框显示时调用 ]
        $('#editModal').on('show.bs.modal', function (event) {
            var    //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano],
                editForm = $("#editForm");
            modal.find('input[name="id"]').val(gridData.id);
            modal.find('input[name="name"]').val(gridData.name);
            if(gridData.status == 1){
                $(".editStatus").bootstrapSwitch('state', true);
            }else{
                $(".editStatus").bootstrapSwitch('state', false);
            }
            if(gridData.type ==1){
                $("#editType").find("option[value='1']").attr("selected",true);
                $("#editType").find("option[value='2']").attr("selected",false);
                $("#editType").find("option[value='3']").attr("selected",false);
            }else if(gridData.type ==2){
                $("#editType").find("option[value='1']").attr("selected",false);
                $("#editType").find("option[value='2']").attr("selected",true);
                $("#editType").find("option[value='3']").attr("selected",false);
            }else {
                $("#editType").find("option[value='1']").attr("selected",false);
                $("#editType").find("option[value='2']").attr("selected",false);
                $("#editType").find("option[value='3']").attr("selected",true);
            }

            $('#editModal').find('div[name="preview"]').html('');
            if (gridData.content != null) {
                information.ueEdit.setContent(gridData.content);
            }
            _refreshImage();
            //清除错误提示
            editForm.psly().reset();
            $(".alert-danger").remove()
        });

        //填参弹窗
        $('#addParamModal').on('show.bs.modal', function (event) {
            var    //获取接受事件的元素
                button = $(event.relatedTarget),
                //获取data 参数
                datano = button.data('no'),
                modal = $(this),
                //获取列表框中的原始数据
                gridData = grid.sortOriginalDatas[datano];
            modal.find('input[name="id"]').val(gridData.id);
            var result = gridData.content;
            if (gridData.param != null) {
                // result = result.replace(/{}/g, formatPdfParam(gridData.param))

                var ii = 0;
                var p = [];
                if(gridData.param!=null && gridData.param!=""){
                    p = $.parseJSON(gridData.param)
                }
                for(var i = result.indexOf("{}",0);i!=-1;i=result.indexOf("{}",i)){
                    var sel = formatPdfParam(p[ii++]);
                    result = result.substring(0,i)+sel+result.substring(i+2,result.length);
                    i+=sel.length;
                }
            }else{
                for(var i = result.indexOf("{}",0);i!=-1;i=result.indexOf("{}",i)){
                    var sel = formatPdfParam(null);
                    result = result.substring(0,i)+sel+result.substring(i+2,result.length);
                    i+=sel.length;
                }
            }

            $("#constantVal").html(result);
            var paramForm = $("#addParamForm")
            //清除错误提示
            paramForm.psly().reset();
            $(".alert-danger").remove()
        });
        // 图片上传
        $("#addFormFile").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "file/upload",
            send: function (e, data) {
                //进行图片格式验证
                var reg = /^image\/(gif|jpg|jpeg|png|GIF|JPG|PNG|JPEG)$/;
                if (reg.test(data.files[0].type)) {
                    if (data.files[0].size > 2 * 1024 * 1024) {
                        $.ncAlert({
                            closeButtonText: "关闭",
                            autoCloseTime: 3,
                            content: "图片格式超过2M请重新上传！"
                        });
                    }
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: "请上传符合格式要求的图片！"
                    });
                    return;
                }
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    var html = '<span class="col-md-3 col-sm-6 m-b-10"><img class="viewImage" src="' + ncGlobal.fileRoot + data.result.data + '"><a name="insert" href="javascript:;" class="btn btn-success btn-xs m-t-5"><i class="fa fa-arrow-circle-o-up"></i>&nbsp;插入</a><a name="delete" href="javascript:;" class="btn btn-danger btn-xs m-t-5 m-l-5"><i class="fa fa-trash-o"></i>&nbsp;删除</a></span>';
                    $('#addModal').find('div[name="preview"]').append(html);
                    var item = $('#addModal').find('div[name="preview"] > span').last();
                    $(item).find('a[name="insert"]').on('click', function () {
                        information.ueAdd.execCommand('inserthtml', '<img src="' + ncGlobal.fileRoot + data.result.data + '">');
                    });
                    $(item).find('a[name="delete"]').on('click', function () {
                        $(this).parents('span').remove();
                    });
                    //图片同比例缩放-默认
                    $('.information-pic img').jqthumb({
                        width: 100,
                        height: 100,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                    _refreshImage();
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    })
                }
            }
        });
        // 图片上传
        $("#editFormFile").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "file/upload",
            send: function (e, data) {
                //进行图片格式验证
                var reg = /^image\/(gif|jpg|jpeg|png|GIF|JPG|PNG|JPEG)$/;
                if (reg.test(data.files[0].type)) {
                    if (data.files[0].size > 2 * 1024 * 1024) {
                        $.ncAlert({
                            closeButtonText: "关闭",
                            autoCloseTime: 3,
                            content: "图片格式超过2M请重新上传！"
                        });
                    }
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: "请上传符合格式要求的图片！"
                    });
                    return;
                }
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    var html = '<span class="col-md-3 col-sm-6 m-b-10"><img class="viewImage" src="' + ncGlobal.fileRoot + data.result.data + '"><a name="insert" href="javascript:;" class="btn btn-success btn-xs m-t-5"><i class="fa fa-arrow-circle-o-up"></i>&nbsp;插入</a><a name="delete" href="javascript:;" class="btn btn-danger btn-xs m-t-5 m-l-5"><i class="fa fa-trash-o"></i>&nbsp;删除</a></span>';
                    $('#editModal').find('div[name="preview"]').append(html);
                    var item = $('#editModal').find('div[name="preview"] > span').last();
                    $(item).find('a[name="insert"]').on('click', function () {
                        information.ueEdit.execCommand('inserthtml', '<img src="' + ncGlobal.fileRoot + data.result.data + '">');
                    });
                    $(item).find('a[name="delete"]').on('click', function () {
                        $(this).parents('span').remove();
                    });
                    $('.information-pic img').jqthumb({
                        width: 100,
                        height: 100,
                        after: function (imgObj) {
                            imgObj.css('opacity', 0).animate({opacity: 1}, 2000);
                        }
                    });
                    _refreshImage();
                } else {
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: data.result.message
                    })
                }
            }
        });
    }

    //外部可调用
    return {
        init: function () {
            _bindEvent();
        },
        ueAdd: _ueAdd,
        ueEdit: _ueEdit,
        // ueEdit2:_ueEdit2,
    }
}();




$(function () {
    grid.load();
    information.init();
    //实例化编辑器
    information.ueAdd = UE.getEditor('contnetAdd', {textarea: 'content'});
    information.ueEdit = UE.getEditor('contnetEditor', {textarea: 'content'});
    // information.ueEdit2 = UE.getEditor('contnetEditor2', {textarea: 'param'});
})