package com.mi.hundsun.oxchains.base.core.service.user;

import com.mi.hundsun.oxchains.base.core.po.user.UserInLetter;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;

import java.util.List;
import java.util.Map;

/**
 * 用户站内信表业务相关Service接口<br>
 *
 * @ClassName: UserInLetterService
 * @author lzj
 * @date   2018-04-14 10:22:36
 */
public interface UserInLetterService extends GenericService<Integer, UserInLetter> {

    boolean clearMyMsg(Integer userId,List<Integer> ids);

    long  userInLetterCount(Integer userId);

    List<UserInLetter> userInLetterList(Integer userId, Integer pageSize,Integer pageNumber);
}
