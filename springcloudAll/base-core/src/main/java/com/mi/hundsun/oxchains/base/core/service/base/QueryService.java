package com.mi.hundsun.oxchains.base.core.service.base;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.baseMapper.QueryMapper;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.QueryUtils;
import com.mi.hundsun.oxchains.base.common.page.BasePageParams;
import com.mi.hundsun.oxchains.base.common.page.Pages;
import com.mi.hundsun.oxchains.base.common.utils.JsonUtils;
import com.mi.hundsun.oxchains.base.core.exception.DtGridException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface QueryService<PK, PO extends GenericPo<PK>> {

    QueryMapper<PO, PK> _getQueryMapper();

    /**
     * 分页查询
     *
     * @param dtGridPager
     * @return
     * @throws DtGridException
     */
    default DtGrid getDtGridList(String dtGridPager) throws DtGridException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> hashMap = new HashMap<>();
            DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
            if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
                for (String key : dtGrid.getNcColumnsType().keySet()) {
                    for (int i = 0; i < dtGrid.getNcColumnsType().get(key).size(); i++) {
                        hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                    }
                    dtGrid.setNcColumnsTypeList(hashMap);
                }
            }
            // 表格查询参数处理
            QueryUtils.parseDtGridSql(dtGrid);
            // 获取查询条件Sql
            String whereSql = dtGrid.getWhereSql();
            // 获取排序Sql
            String sortSql = dtGrid.getSortSql();

            Map<String, Object> params = new HashMap<>();
            params.put("whereSql", whereSql);
            params.put("sortSql", sortSql);
            long recordCount = _getQueryMapper().countBySql(params);
            int pageSize = dtGrid.getPageSize();
            int pageNum = (int) recordCount / dtGrid.getPageSize() + (recordCount % dtGrid.getPageSize() > 0 ? 1 : 0);

            dtGrid.setPageCount(pageNum);
            dtGrid.setRecordCount(recordCount);

            params.put("nowPage", (dtGrid.getNowPage() - 1) * pageSize);
            params.put("pageSize", pageSize);

            List<PO> list = _getQueryMapper().findListBySql(params);
            dtGrid.setExhibitDatas(JsonUtils.toGenericObject(JSON.toJSONString(list), new TypeReference<List<Object>>() {
            }));
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }

    }

    /**
     * 分页查询 (用于Export查询)
     *
     * @param dtGrid
     * @return
     * @throws DtGridException
     */
    default DtGrid getDtGridList(DtGrid dtGrid) throws DtGridException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> hashMap = new HashMap<>();
            if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
                for (String key : dtGrid.getNcColumnsType().keySet()) {
                    for (int i = 0; i < dtGrid.getNcColumnsType().get(key).size(); i++) {
                        hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                    }
                    dtGrid.setNcColumnsTypeList(hashMap);
                }
            }
            // 表格查询参数处理
            QueryUtils.parseDtGridSql(dtGrid);
            // 获取查询条件Sql
            String whereSql = dtGrid.getWhereSql();
            // 获取排序Sql
            String sortSql = dtGrid.getSortSql();

            Map<String, Object> params = new HashMap<>();
            params.put("whereSql", whereSql);
            params.put("sortSql", sortSql);
            long recordCount = _getQueryMapper().countBySql(params);
            int pageSize = dtGrid.getPageSize();
            int pageNum = (int) recordCount / dtGrid.getPageSize() + (recordCount % dtGrid.getPageSize() > 0 ? 1 : 0);

            dtGrid.setPageCount(pageNum);
            dtGrid.setRecordCount(recordCount);

            params.put("nowPage", (dtGrid.getNowPage() - 1) * pageSize);
            params.put("pageSize", pageSize);

            List<PO> list = _getQueryMapper().findListBySql(params);
            dtGrid.setExhibitDatas(JsonUtils.toGenericObject(JSON.toJSONString(list), new TypeReference<List<Object>>() {
            }));
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }
    }

    /**
     * 分页查询普通对象, 使用该方法必须再mapper内重写findListBySqlJoin方法 否则报异常(用于Export查询)
     *
     * @param dtGrid
     * @return
     * @throws DtGridException
     */
    default <G> DtGrid getDtGridList(DtGrid dtGrid, Class<G> g) throws DtGridException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> hashMap = new HashMap<>();
            if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
                for (String key : dtGrid.getNcColumnsType().keySet()) {
                    for (int i = 0; i < dtGrid.getNcColumnsType().get(key).size(); i++) {
                        System.out.println();
                        hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                    }
                    dtGrid.setNcColumnsTypeList(hashMap);
                }
            }
            // 表格查询参数处理
            QueryUtils.parseDtGridSql(dtGrid);
            // 获取查询条件Sql
            String whereSql = dtGrid.getWhereSql();
            // 获取排序Sql
            String sortSql = dtGrid.getSortSql();

            Map<String, Object> params = new HashMap<>();
            params.put("whereSql", whereSql);
            params.put("sortSql", sortSql);
            long recordCount = _getQueryMapper().countBySqlJoin(params);
            int pageSize = dtGrid.getPageSize();
            int pageNum = (int) recordCount / dtGrid.getPageSize() + (recordCount % dtGrid.getPageSize() > 0 ? 1 : 0);

            dtGrid.setPageCount(pageNum);
            dtGrid.setRecordCount(recordCount);

            params.put("nowPage", (dtGrid.getNowPage() - 1) * pageSize);
            params.put("pageSize", pageSize);

            List<G> list = (List<G>) _getQueryMapper().findListBySqlJoin(params);
            dtGrid.setExhibitDatas(JsonUtils.toGenericObject(JSON.toJSONString(list), new TypeReference<List<Object>>() {
            }));
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }
    }

    /**
     * 分页查询普通对象, 使用该方法必须再mapper内重写findListBySqlJoin方法 否则报异常
     *
     * @param dtGridPager
     * @return
     * @throws DtGridException
     */
    default <G> DtGrid getDtGridList(String dtGridPager, Class<G> g) throws DtGridException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> hashMap = new HashMap<>();
            DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
            if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
                for (String key : dtGrid.getNcColumnsType().keySet()) {
                    for (int i = 0; i < dtGrid.getNcColumnsType().get(key).size(); i++) {
                        System.out.println();
                        hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                    }
                    dtGrid.setNcColumnsTypeList(hashMap);
                }
            }
            // 表格查询参数处理
            QueryUtils.parseDtGridSql(dtGrid);
            // 获取查询条件Sql
            String whereSql = dtGrid.getWhereSql();
            // 获取排序Sql
            String sortSql = dtGrid.getSortSql();

            Map<String, Object> params = new HashMap<>();
            params.put("whereSql", whereSql);
            params.put("sortSql", sortSql);
            long recordCount = _getQueryMapper().countBySqlJoin(params);
            int pageSize = dtGrid.getPageSize();
            int pageNum = (int) recordCount / dtGrid.getPageSize() + (recordCount % dtGrid.getPageSize() > 0 ? 1 : 0);

            dtGrid.setPageCount(pageNum);
            dtGrid.setRecordCount(recordCount);

            params.put("nowPage", (dtGrid.getNowPage() - 1) * pageSize);
            params.put("pageSize", pageSize);

            List<G> list = (List<G>) _getQueryMapper().findListBySqlJoin(params);
            dtGrid.setExhibitDatas(JsonUtils.toGenericObject(JSON.toJSONString(list), new TypeReference<List<Object>>() {
            }));
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }
    }

    /**
     * 分页查询普通对象, 使用该方法必须再mapper内重写findListBySqlJoin方法 否则报异常
     *
     * @param dtGridPager
     * @return
     * @throws DtGridException
     */
    default <G> DtGrid getDtGridListExport(String dtGridPager, Class<G> g) throws DtGridException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
            // 默认导出当前页数据（不需要数据库查询），ExportAllData为TRUE导出所有数据（取数据库进行查询）
            if (dtGrid.getExportAllData()) {
                dtGrid.setNowPage(1);
                dtGrid.setPageSize(1000000);
                dtGrid = getDtGridList(dtGrid, g);
                String dataJsonStr = JSON.toJSONString(dtGrid.getExhibitDatas());
                List<Map<String, Object>> exportDatas = mapper.readValue(dataJsonStr, new TypeReference<List<HashMap<String, Object>>>() {
                });
                dtGrid.setExportDatas(exportDatas);
            }
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }
    }

    /**
     * 分页查询普通对象, 使用该方法必须再mapper内重写findListBySqlJoin方法 否则报异常
     *
     * @param dtGrid
     * @return
     * @throws DtGridException
     */
    default <G> DtGrid getDtGridListExport(DtGrid dtGrid, Class<G> g) throws DtGridException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            // 默认导出当前页数据（不需要数据库查询），ExportAllData为TRUE导出所有数据（取数据库进行查询）
            if (dtGrid.getExportAllData()) {
                dtGrid.setNowPage(1);
                dtGrid.setPageSize(1000000);
                dtGrid = getDtGridList(dtGrid, g);
                String dataJsonStr = JSON.toJSONString(dtGrid.getExhibitDatas());
                List<Map<String, Object>> exportDatas = mapper.readValue(dataJsonStr, new TypeReference<List<HashMap<String, Object>>>() {
                });
                dtGrid.setExportDatas(exportDatas);
            }
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }
    }

    /**
     * 分页查询普通对象, 使用该方法必须再mapper内重写findListBySqlJoin方法 否则报异常
     *
     * @param dtGridPager
     * @return
     * @throws DtGridException
     */
    default <G> DtGrid getDtGridListExport(String dtGridPager) throws DtGridException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            DtGrid dtGrid = mapper.readValue(dtGridPager, DtGrid.class);
            // 默认导出当前页数据（不需要数据库查询），ExportAllData为TRUE导出所有数据（取数据库进行查询）
            if (dtGrid.getExportAllData()) {
                dtGrid.setNowPage(1);
                dtGrid.setPageSize(1000000);
                dtGrid = getDtGridList(dtGrid);
                String dataJsonStr = JSON.toJSONString(dtGrid.getExhibitDatas());
                List<Map<String, Object>> exportDatas = mapper.readValue(dataJsonStr, new TypeReference<List<HashMap<String, Object>>>() {
                });
                dtGrid.setExportDatas(exportDatas);
            }
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }
    }

    /**
     * 通用前台分页
     *
     * @param po     查询对象
     * @param params 分页参数
     * @return
     */
    default Pages<PO> selectByPage(PO po, BasePageParams params) {
        if (po.getDelFlag() == null) po.setDelFlag(GenericPo.DELFLAG.NO.code);
        return selectByPage(params, q -> q.selectCount(po), q -> q.select(po));
    }


    /**
     * 通用前台分页(参考同名方法selectByPage)<br/>
     * 在建立mapper方法时 传入参数和返回对象相同才能使用该分页方法
     *
     * @param params 分页参数
     * @param count  查询总数 mapper方法
     * @param select 查询记录 mapper方法
     * @param <T>    建议model对象
     * @return
     */
    default <T> Pages<T> selectByPage(BasePageParams params, Function<QueryMapper<PO, PK>, Integer> count, Function<QueryMapper<PO, PK>, List<T>> select) {
        int c = count.apply(_getQueryMapper());
        PageHelper.startPage(params.getStar(), params.getRows(), false);
        List<T> list = select.apply(_getQueryMapper());
        return new Pages<>(list, c, params.getStar(), params.getRows());
    }

    /**
     * 接口端分页 + 排序实现
     * @param dtGrid
     * @return
     * @throws DtGridException
     */
    default DtGrid selectByPage(DtGrid dtGrid) throws DtGridException {

        try {
            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, String> hashMap = new HashMap<>();
            if (dtGrid.getNcColumnsType() != null && dtGrid.getNcColumnsType().size() > 0) {
                for (String key : dtGrid.getNcColumnsType().keySet()) {
                    for (int i = 0; i < dtGrid.getNcColumnsType().get(key).size(); i++) {
                        hashMap.put((String) dtGrid.getNcColumnsType().get(key).get(i), key);
                    }
                    dtGrid.setNcColumnsTypeList(hashMap);
                }
            }
            // 表格查询参数处理
            QueryUtils.parseDtGridSql(dtGrid);
            // 获取查询条件Sql
            String whereSql = dtGrid.getWhereSql();
            // 获取排序Sql
            String sortSql = dtGrid.getSortSql();

            Map<String, Object> params = new HashMap<>();
            params.put("whereSql", whereSql);
            params.put("sortSql", sortSql);
            long recordCount = _getQueryMapper().countBySql(params);
            int pageSize = dtGrid.getPageSize();
            int pageNum = (int) recordCount / dtGrid.getPageSize() + (recordCount % dtGrid.getPageSize() > 0 ? 1 : 0);

            dtGrid.setPageCount(pageNum);
            dtGrid.setRecordCount(recordCount);

            params.put("nowPage", (dtGrid.getNowPage() - 1) * pageSize);
            params.put("pageSize", pageSize);

            List<PO> list = _getQueryMapper().findListBySql(params);
            dtGrid.setExhibitDatas(JsonUtils.toGenericObject(JSON.toJSONString(list), new TypeReference<List<Object>>() {
            }));
            return dtGrid;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DtGridException();
        }
    }

    /**
     * 查询对应参数记录
     *
     * @param po
     * @return
     */
    default List<PO> select(PO po) {
        if (po.getDelFlag() == null) po.setDelFlag(GenericPo.DELFLAG.NO.code);
        return _getQueryMapper().select(po);
    }

    /**
     * 查询所有
     *
     * @return
     */
    default List<PO> selectAll() {
        return _getQueryMapper().selectAll();
    }

    /**
     * 根据对应参数查询单条记录
     *
     * @param po
     * @return
     */
    default PO selectOne(PO po) {
        if (po.getDelFlag() == null) po.setDelFlag(GenericPo.DELFLAG.NO.code);
        return _getQueryMapper().selectOne(po);
    }

    /**
     * 根据对应参数查询数量
     *
     * @param po
     * @return
     */
    default int selectCount(PO po) {
        return _getQueryMapper().selectCount(po);
    }

    /**
     * 根据主键查询
     *
     * @param pk
     * @return
     */
    default PO selectByPrimaryKey(PK pk) {
        return _getQueryMapper().selectByPrimaryKey(pk);
    }

    /**
     * 正常状态记录
     *
     * @param po
     * @return
     */
    default PO getNormalModelById(PO po) {
        po.setDelFlag(GenericPo.DELFLAG.NO.code);
        return _getQueryMapper().selectOne(po);
    }

}
