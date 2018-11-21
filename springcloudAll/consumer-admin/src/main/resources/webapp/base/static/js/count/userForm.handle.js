/**
 * Created by dqw on 2015/12/30.
 */

var LoadFormStat = function () {
    function _loadForm(startTimeStr, endTimeStr) {
        $.ajax({
            type: 'POST',
            url: ncGlobal.adminRoot + 'users/json/form',
            data: {
                "startTimeStr": startTimeStr,
                "endTimeStr": endTimeStr
            },
            success: function (data) {
                console.log(data);
                if (data.code == '200') {
                    $("#timeStr").html(startTimeStr + ' ~ ' + endTimeStr);
                    $("#regNum").html(data.data.regNum);
                    $("#loginNum").html(data.data.loginNum);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $.ncAlert({
                    content: '<div class="alert alert-danger m-b-0"><h4><i class="fa fa-info-circle"></i>&nbsp;连接超时</h4></div>',
                    autoCloseTime: 3,
                    callback: function () {
                    }
                });
            }
        });
    }

    //外部可调用
    return {
        init: function (startTimeStr, endTimeStr) {
            loadForm:_loadForm(startTimeStr, endTimeStr);
        }
    }
}();


function GetQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}


//操作处理开始
var OperateHandle = function () {

    function _bindEvent() {

    }

    function _doStat() {
        startTime = $("#startTimeStr").val();
        endTime = $("#endTimeStr").val();
        LoadFormStat.init(startTime, endTime);
    }

    //外部可调用
    return {
        init: function () {
            _bindEvent();
        },
        doStat: _doStat
    }
}();
//操作处理结束

$(function () {
    //页面绑定事件
    startTimeStr = $("#startTimeStr").val();
    endTimeStr = $("#endTimeStr").val();
    OperateHandle.init();
    LoadFormStat.init(startTimeStr, endTimeStr);
});