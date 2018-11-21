package com.mi.hundsun.oxchains.base.core.mapper.quote;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;
import com.mi.hundsun.oxchains.base.core.model.exchange.CommodityModel;
import com.mi.hundsun.oxchains.base.core.po.quote.Commodity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 管理员账号相关Dao接口<br>
 *
 * @author donfy
 * @date 2017-08-16 08:18:00
 */
public interface CommodityMapper extends GenericMapper<Commodity, Integer> {

    /**
     * 根据银行编码查询银行信息
     *
     * @param params 参数
     * @return
     */

}
