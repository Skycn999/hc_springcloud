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
        title: '序号',
        type: 'number',
        columnClass: 'text-center width-100',
        fastSort: true,
        fastQuery: false
    },
    {
        id: 'bannerNo',
        title: 'banner编号',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: true,
        fastQuery: false
    },
    {
        id: 'title',
        title: 'banner标题',
        type: 'string',
        columnClass: 'text-center width-150',
        fastSort: false,
        fastQuery: true,
        fastQueryType: 'lk'
    },
    {
        id: 'appPath',
        title: 'APP端banner路径',
        type: 'string',
        columnClass: 'text-center width-270',
        fastSort: false
    },
    {
        id: 'state',
        title: '状态',
        type: 'int',
        columnClass: 'text-center width-150',
        fastSort: false,
        codeTable:{
            0:$lang.bannerState.unpublished,
            1:$lang.bannerState.published,
            2:$lang.bannerState.revoke
        }
    },
    {
        id: 'createTime',
        title: '发布时间',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss',
        headerClass: 'text-center',
        columnClass: 'text-center',
        fastSort: false
    },
    {
        id: 'verify',
        title: '操作',
        type: 'string',
        'export':false,
        columnClass: 'text-center width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            var editHtml = "<a data-target='#editModal' class='btn btn-sm btn-primary m-r-10' data-toggle='modal' data-no='" + dataNo + "' ><i class='fa fa-edit'></i>&nbsp;编辑&nbsp;</a>";
            var html = "";
            var back = "";
            var publish = "";
            if ($("#bannerPublish") && $("#bannerPublish").val() == 1) {
                publish = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='OperateHandle.pubInfo(" + record.id + ",\"" + record.title + "\")'><i class='fa fa-paper-plane-o'></i>&nbsp;发布&nbsp;</a>";
            }
            if ($("#bannerBack") && $("#bannerBack").val() == 1) {
                back = "<a href='javascript:;' class='btn btn-danger btn-sm m-r-10' onclick='OperateHandle.backInfo(" + record.id + ",\"" + record.title + "\")'><i class='fa fa-reply-all'></i>&nbsp;撤回&nbsp;</a>";
            }
            if(record.state !=1 ){
                html = publish;
            }else if(record.state == 1){
                html = back;
            }
            // 判断是否有编辑权限
            if ($("#editPermi") && $("#editPermi").val() == 1) {
                html += editHtml;
            }
            return html;
        }
    }
];

var dtGridOption = {
    lang: 'zh-cn',
    ajaxLoad: true,
    loadURL: ncGlobal.adminRoot + '/banner/json/list',
    //exportURL: ncGlobal.adminRoot + 'banner/json/export',
    exportFileName: 'banner列表',
    columns: dtGridColumns,
    gridContainer: 'dtGridContainer',
    toolbarContainer: 'dtGridToolBarContainer',
    //tools: 'refresh|faseQuery|export[excel]',
    tools:'refresh',
    pageSize: 10,
    pageSizeLimit: [10, 20, 50],
    ncColumnsType: {int:["id","delFlag","state"],Timestamp:["createTime"]}
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

    //撤回url
    var backUrl = ncGlobal.adminRoot + "banner/json/back";
    //发布url
    var publishUrl = ncGlobal.adminRoot + "banner/json/publish";

    /**
     * 撤回
     */
    function _backInfo(id, content) {
        var tpl = '您是否确定撤回公告 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: backUrl,
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "撤回操作"
        });
    }

    /**
     * 发布
     */
    function _pubInfo(id, content) {
        var tpl = '您是否确定发布公告 <strong>' + content + '</strong> ?'
        $.ncConfirm({
            url: publishUrl,
            data: {
                id: id
            },
            content: tpl,
            alertTitle: "发布操作"
        });
    }

    function _bindEvent() {

        //新增对话框初始化
        $("#addModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".alert-danger").remove();
            $("#addForm").psly().reset();

            $("#addForm").find("[name='title']").val("");
            $("#addForm").find("[name='appPath']").val("");
            $("#addForm").find("[name='pcPath']").val("");
            $('#addFormAppPath').val("");
            $('#addFormPicImg').attr('src', ncGlobal.imgRoot+"default_image.gif");

            $(".state").bootstrapSwitch('state', true);
            _refreshImage();

        });

        //编辑对话框初始化
        $("#editModal").on("show.bs.modal", function (event) {
            //清除错误信息
            $(".parsley-type").remove();
            //获取接受事件的元素
            var button = $(event.relatedTarget);
            //获取data 参数
            var datano = button.data('no');
            //获取列表框中的原始数据
            var gridData = grid.sortOriginalDatas[datano];
            var editForm = $("#editForm");
            //清除错误提示
            $("#editForm").psly().reset();
            $(".alert-danger").remove();

            $("#id").val(gridData.id);
            $("#title").val(gridData.title);
            //$("#appPath").val(gridData.appPath);
            $("#pcPath").val(gridData.pcPath);

            if(gridData.state==1){
                $(".state").bootstrapSwitch('state', true);
            }else{
                $(".state").bootstrapSwitch('state', false);
            }

            $('#editFormAppPath').val(gridData.appPath);
            $('#editFormPicImg').attr('src', ncGlobal.adminRoot + gridData.appPath);
            _refreshImage();
        });
        //模糊搜索
        $('#customSearch').click(function () {
            grid.fastQueryParameters = new Object();
            grid.fastQueryParameters['eq_delFlag'] = 0;
            grid.fastQueryParameters['lk_title'] = $('#keyword').val();
            grid.pager.startRecord = 0;
            grid.pager.nowPage = 1;
            grid.pager.recordCount = -1;
            grid.pager.pageCount = -1;
            grid.refresh(true);
        });

        /**
         * 刷新addmodal 上的图片
         */
        function _refreshImage() {
            $(".viewImage").jqthumb({
                width: 180,
                height: 100,
                after: function (imgObj) {
                    imgObj.css('opacity', 0).animate({opacity: 1}, 1500);
                }
            });
        }

        //图片上传
        $("#addFormPic").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "admin/aws/uploadPic",
            send: function (e, data) {
                //进行图片格式验证
                var reg=/^image\/(gif|jpg|jpeg|png|GIF|JPG|PNG|JPEG)$/;
                if(reg.test(data.files[0].type)){
                    if(data.files[0].size>2*1024*1024){
                        $.ncAlert({
                            closeButtonText: "关闭",
                            autoCloseTime: 3,
                            content: "图片格式超过2M请重新上传！"
                        });
                       // return false;
                    }
                }else{
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: "请上传符合格式要求的图片！"
                    });
                    //return false;
                    return;
                }
            },
            done: function (e, data) {
                if (data.result.code == 200) {
                    $('#addFormAppPath').val(data.result.data.filePath);
                    $('#addFormPicImg').attr('src', ncGlobal.adminRoot + data.result.data.filePath);
                    //图片同比例缩放-新增
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
        /**
         * 上传插件绑定
         */
        $("#editFormPic").fileupload({
            dataType: 'json',
            url: ncGlobal.adminRoot + "admin/aws/uploadPic",
            send: function (e, data) {
                //进行图片格式验证
                var reg=/^image\/(gif|jpg|jpeg|png|GIF|JPG|PNG|JPEG)$/;
                if(reg.test(data.files[0].type)){
                    if(data.files[0].size>2*1024*1024){
                        $.ncAlert({
                            closeButtonText: "关闭",
                            autoCloseTime: 3,
                            content: "图片格式超过2M请重新上传！"
                        });
                        //return false;
                    }
                }else{
                    $.ncAlert({
                        closeButtonText: "关闭",
                        autoCloseTime: 3,
                        content: "请上传符合格式要求的图片！"
                    });
                    //return false;
                    return;
                }
            },
            send: function (e, data) {
                //进行图片格式验证
                var reg=/^image\/(gif|jpg|jpeg|png|GIF|JPG|PNG|JPEG)$/;
                if(reg.test(data.files[0].type)){
                    if(data.files[0].size>2*1024*1024){
                        $.ncAlert({
                            closeButtonText: "关闭",
                            autoCloseTime: 3,
                            content: "图片格式超过2M请重新上传！"
                        });
                    }
                }else{
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
                    $('#editFormAppPath').val(data.result.data);
                    $('#editFormPicImg').attr('src', ncGlobal.adminRoot + data.result.data);
                    //图片同比例缩放-编辑
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

    // /**
    //  * 删除入驻申请
    //  */
    // function delBanner(id) {
    //     var tpl = '您确定要删除该banner吗?'
    //     $.ncConfirm({
    //         url: ncGlobal.adminRoot + "banner/json/del",
    //         data: {
    //             id: id
    //         },
    //         content: tpl
    //     });
    // }

    //外部可调用
    return {
        bindEvent: _bindEvent,
        backInfo: _backInfo,
        pubInfo: _pubInfo
    }
}();
//操作处理结束

$(function () {
    //加载列表
    grid.load();
    //页面绑定事件
    OperateHandle.bindEvent();
});