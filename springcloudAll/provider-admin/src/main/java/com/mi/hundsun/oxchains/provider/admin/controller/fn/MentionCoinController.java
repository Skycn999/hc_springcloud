package com.mi.hundsun.oxchains.provider.admin.controller.fn;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.model.fn.MentionCoinModel;
import com.mi.hundsun.oxchains.base.core.po.fn.MentionCoin;
import com.mi.hundsun.oxchains.base.core.service.fn.MentionCoinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("提币管理服务")
@Slf4j
@RestController
public class MentionCoinController {
    @Autowired
    MentionCoinService mentionCoinService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/fn/mentionCoin/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return mentionCoinService.getDtGridList(dtGridPager,MentionCoinModel.class);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/fn/mentionCoin/selectOne")
    public MentionCoin selectOne(@RequestBody MentionCoin mentionCoin) throws Exception {
        try {
            MentionCoin coin = mentionCoinService.selectOne(mentionCoin);
            return coin;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "提币审核")
    @PostMapping(value = "/fn/mentionCoin/audit")
    public ResultEntity audit(@RequestBody MentionCoin mentionCoin) throws Exception {
        try {
            mentionCoinService.audit(mentionCoin);
            return ResultEntity.success();
        }catch (BussinessException e){
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }

    @ApiOperation(value = "获取待录入提币")
    @PostMapping(value = "/fn/mentionCoin/getPendEnterList")
    public List<MentionCoin> getPendEnterList() throws Exception {
        try {
            List<MentionCoin> list = mentionCoinService.select(new MentionCoin(c ->{
                c.setState(MentionCoin.STATE.PEND_ENTER.code);
                c.setDelFlag(GenericPo.DELFLAG.NO.code);
            }));
            return list;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    @ApiOperation(value = "转账成功 录入txid")
    @PostMapping(value = "/fn/mentionCoin/input")
    public ResultEntity input(@RequestBody MentionCoin mentionCoin) throws Exception {
        try {
            if (mentionCoin.getState() != MentionCoin.STATE.PEND_ENTER.code) {
                throw new Exception("该记录已不在待录入状态！");
            }
            mentionCoin.setState(MentionCoin.STATE.SUCCESS.code);
            mentionCoinService.updateByPrimaryKeySelective(mentionCoin);
            return  ResultEntity.success();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.fail();
        }
    }
}
