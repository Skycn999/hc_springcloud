/**
 * Created by zxy on 2016-02-04
 */
//列表开始
var dtGridColumnsStore = [
    {
        id: 'sortNum',
        title: '序号',
        type: 'number',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return dataNo+1;
        }
    },
    {
        id: 'storeName',
        title: '店铺名称',
        type: 'String',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastSort: false
    },
    {
        id: 'ordersAmount',
        title: '下单金额（元）',
        type: 'number',
        format: '###.00',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false
    }
];
var dtGridColumnsGoods = [
    {
        id: 'sortNum',
        title: '序号',
        type: 'number',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false
    },
    {
        id: 'goodsName',
        title: '商品名称',
        type: 'String',
        headerClass: 'text-left width-200',
        columnClass: 'text-left width-200',
        fastSort: false,
        resolution: function (value, record, column, grid, dataNo, columnNo) {
            return "<a href='" + ncGlobal.webRoot + "goods/" + record.commonId + "' target='_blank'>" + value + "</a>";
        }
    },
    {
        id: 'goodsBuyNumSum',
        title: '销量',
        type: 'number',
        headerClass: 'text-center width-100',
        columnClass: 'text-center width-100',
        fastSort: false
    }
];
//操作处理开始
var OperateHandle = function () {
    function _bindEvent() {
        //$.ajax({
        //    url: ncGlobal.adminRoot + 'index/stat/hourtrend',
        //    success: function(xhr) {
        //        var statChartHourtrend = echarts.init(document.getElementById('container_hourtrend'));
        //        statChartHourtrend.setOption(xhr);
        //        return false;
        //    }
        //});
        //$.ajax({
        //    url: ncGlobal.adminRoot + 'index/stat/store/rank',
        //    type: "post",
        //    success: function(xhr) {
        //        var dtGridOptionStore = {
        //            lang : 'zh-cn',
        //            ajaxLoad : false,
        //            exportFileName : '',
        //            datas : xhr,
        //            columns : dtGridColumnsStore,
        //            gridContainer : 'dtGridContainerStore',
        //            pageSize : 50,
        //            pageSizeLimit : [10, 20, 50]
        //        };
        //        var gridStore = $.fn.DtGrid.init(dtGridOptionStore);
        //        gridStore.load();
        //        return false;
        //    }
        //});
        //$.ajax({
        //    url: ncGlobal.adminRoot + 'index/stat/goods/rank',
        //    type: "post",
        //    success: function(xhr) {
        //        var dtGridOptionGoods = {
        //            lang : 'zh-cn',
        //            ajaxLoad : false,
        //            exportFileName : '',
        //            datas : xhr,
        //            columns : dtGridColumnsGoods,
        //            gridContainer : 'dtGridContainerGoods',
        //            pageSize : 50,
        //            pageSizeLimit : [10, 20, 50]
        //        };
        //        var gridGoods = $.fn.DtGrid.init(dtGridOptionGoods);
        //        gridGoods.load();
        //        return false;
        //    }
        //});
    }
    //外部可调用
    return {
        bindEvent: _bindEvent
    }
}();
//操作处理结束

$(function () {
    //页面绑定事件
    OperateHandle.bindEvent();
});