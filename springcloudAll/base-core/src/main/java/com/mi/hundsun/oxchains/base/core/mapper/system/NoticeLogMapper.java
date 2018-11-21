package com.mi.hundsun.oxchains.base.core.mapper.system;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.log.NoticeLogModel;
import com.mi.hundsun.oxchains.base.core.po.system.NoticeLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 通知记录表相关Dao接口<br>
 *
 * @author donfy
 * @date 2017-08-25 04:45:54
 */
public interface NoticeLogMapper extends GenericMapper<NoticeLog, Integer> {

    /**
     * 前台消息列表 （发送成功，站内信，未删除）
     *
     * @param param
     * @return
     */
    @Select("SELECT t.* FROM s_notice_log t WHERE t.user_id = ${userId} AND t.del_flag = 1 AND t.type = 3 AND t.status = 1 ORDER BY t.create_time DESC ")
    List<NoticeLog> findListForNoticeLogList(Map<String, Object> param);

    /**
     * 前台消息列表 （发送成功，站内信，未删除）
     *
     * @param param
     * @return
     */
    @Select("SELECT count(*) FROM s_notice_log t WHERE t.user_id = ${userId} AND t.del_flag = 1 AND t.type = 3 AND t.status = 1 ORDER BY t.create_time DESC ")
    Integer findCountForNoticeLogList(Map<String, Object> param);

    /**
     * 批量删除站内信
     *
     * @param userId
     * @param delIds
     */
    @Delete("DELETE FROM s_notice_log WHERE id IN ${delIds} AND user_id = #{userId} AND type = 3")
    void deleteNoticeLogs(@Param(value = "userId") Integer userId, @Param(value = "delIds") String delIds);

    @Update("UPDATE s_notice_log SET read_flag = #{readFlag} WHERE user_id = #{userId} AND id IN ${ids}")
    void updateNoticeLogsToRead(@Param(value = "userId") Integer userId, @Param(value = "ids") String ids, @Param(value = "readFlag") Integer readFlag);


    /**
     * 后台通知列表
     */
    @Select("SELECT t.*, IFNULL(u.user_name,'') AS 'u.userName', IFNULL(u.mobile,'') AS 'u.mobile' FROM s_notice_log t LEFT JOIN biz_user u ON u.id = t.user_id  WHERE 1=1 ${whereSql} ${sortSql} limit ${nowPage}, ${pageSize} ")
    List<NoticeLogModel> findListBySqlJoin(Map<String, Object> params);

    /**
     * 后台通知列表
     */
    @Select("SELECT count(1)  FROM s_notice_log t LEFT JOIN biz_user u ON u.id = t.user_id  WHERE 1=1 ${whereSql}")
    long countBySqlJoin(Map<String, Object> params);


}
