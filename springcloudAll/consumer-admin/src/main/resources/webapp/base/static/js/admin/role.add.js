/**
 * Created by cj on 2016/1/5.
 */
var groupAdd = function ($) {

    //"use strict";

    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "parentId",
                rootPId: -1
            },
            key: {
                url:"nourl"
            }
        },
        check:{
            enable:true,
            nocheckInherit:true
        }
    };

    var ztree;

    /**
     * 获取已经选择的input的权限id
     * @private
     */
    function _getPostData() {
        //获取选择的菜单
        var nodes = ztree.getCheckedNodes(true);
        var menuIdList = new Array();
        for(var i=0; i<nodes.length; i++) {
            menuIdList.push(nodes[i].id);
        }
        return menuIdList;
    }

    function _bindEvent() {
        //表单提交按钮
        $("#formSubmit").on('click', function () {
            var permis = _getPostData();
            if(permis.length == 0){
                $.ncAlert({
                    content: '<div class="alert alert-danger m-b-0"><h4><i class="fa fa-info-circle"></i>&nbsp;请选择权限</h4></div>',
                    autoCloseTime: 2
                });
                return;
            }
            $("#permission").val(JSON.stringify(permis));
            $(this).qSubmit();
        });
    }

    function _initMenuTree() {

        //加载菜单树
        $.get(ncGlobal.adminRoot+"menu/json/allMenu", function(r){
            ztree = $.fn.zTree.init($("#menuTree"), setting, r.data);
            //展开所有节点
            ztree.expandAll(true);

            if($("#roleId") != null){
                _initMenuSelect($("#roleId").val());
            }
        });
    }


    function _initMenuSelect(roleId){
        $.get(ncGlobal.adminRoot + "/menu/json/roleMenuIds?roleId="+roleId, function(r){
            //勾选角色所拥有的菜单
            for(var i=0; i< r.data.length; i++) {
                var node = ztree.getNodeByParam("id", r.data[i]);
                ztree.checkNode(node, true, false);
            }
        });
    }

    return {
        init: function () {
            _bindEvent();
            _initMenuTree();
        }
    }
}(jQuery);
$(function () {
    groupAdd.init();
});
