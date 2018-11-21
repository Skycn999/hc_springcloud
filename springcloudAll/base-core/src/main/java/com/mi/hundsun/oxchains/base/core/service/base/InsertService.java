package com.mi.hundsun.oxchains.base.core.service.base;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.baseMapper.InsertMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional(rollbackFor = Exception.class)
public interface InsertService<PK, PO extends GenericPo<PK>> {

    InsertMapper<PO, PK> _getInsertMapper();

    /**
     * 新增对象
     *
     * @param po 需要保存的对象
     * @return result != 1 则保存失败
     */
    default int insert(PO po) {
        po.setCreateTime(new Date());
        po.setDelFlag(GenericPo.DELFLAG.NO.code);
        return _getInsertMapper().insert(po);
    }
}
