package com.mi.hundsun.oxchains.base.core.service.user.impl;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.mapper.user.UserInLetterMapper;
import com.mi.hundsun.oxchains.base.core.po.user.UserInLetter;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.user.UserInLetterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户业务相关Service接口实现<br>
 *
 * @author bin
 * @ClassName: UserInLetterServiceImpl
 * @date 2018-04-12 03:56:01
 */
@Slf4j
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class UserInLetterServiceImpl implements UserInLetterService {


    @Resource
    private RedisService redisService;
    @Resource
    private UserInLetterMapper userInLetterMapper;

    @Override
    public boolean clearMyMsg(Integer userId, List<Integer> ids) {
        Map<String, Object> param = new HashMap<>();
        StringBuilder inSqlBuilder = new StringBuilder();
        for (Integer id : ids) {
            inSqlBuilder.append(id).append(",");
        }
        String inSql = inSqlBuilder.toString();
        inSql = inSql.substring(0, inSql.lastIndexOf(","));
        param.put("whereSql", " AND t.user_id = " + userId + " AND t.id in (" + inSql + ")");
        return userInLetterMapper.clearMyMsg(param);
    }

    @Override
    public long userInLetterCount(Integer userId) {
        Map<String, Object> params = new HashMap<>();
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY create_time DESC");
        return userInLetterMapper.userInLetterCount(params);
    }

    @Override
    public List<UserInLetter> userInLetterList(Integer userId, Integer pageSize, Integer pageNumber) {
        Map<String, Object> params = new HashMap<>();
        int startPos = (pageNumber - 1) * pageSize;
        params.put("whereSql", " AND user_id = " + userId + " AND del_flag = 0 ");
        params.put("sortSql", " ORDER BY create_time DESC");
        params.put("nowPage", startPos);
        params.put("pageSize", pageSize);
        return userInLetterMapper.userInLetterList(params);
    }

    @Override
    public GenericMapper<UserInLetter, Integer> _getMapper() {
        return userInLetterMapper;
    }
}
