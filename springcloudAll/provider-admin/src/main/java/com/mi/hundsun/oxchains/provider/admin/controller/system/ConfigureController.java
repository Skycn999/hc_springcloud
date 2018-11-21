package com.mi.hundsun.oxchains.provider.admin.controller.system;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.entity.dtgrid.DtGrid;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.constant.CacheID;
import com.mi.hundsun.oxchains.base.core.constant.State;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.po.system.Configure;
import com.mi.hundsun.oxchains.base.core.service.cache.RedisService;
import com.mi.hundsun.oxchains.base.core.service.system.ConfigureService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员账号业务相关Service接口<br>
 *
 * @author donfy
 * @ClassName: ConfigureController
 * @date 2017-08-17 04:10:06
 */
@Slf4j
@RestController
@RequestMapping("/sys/configure")
public class ConfigureController extends GenericController {
    @Autowired
    ConfigureService configureService;
    @Autowired
    RedisService redisService;

    @ApiOperation(value = "通过配置标识获取配置具体值")
    @PostMapping(value = "/getByNid")
    String getByNid(String webServerUrl){
        return configureService.getByNid(webServerUrl);
    }

    @ApiOperation(value = "新增信息")
    @PostMapping(value = "/insert")
    public ResultEntity insert(@RequestBody Configure configure) throws BussinessException {
        try {
            configureService.insert(configure);
            if (State.YES == configure.getStatus()) {
                redisService.put(CacheID.CONFIG_PREFIX + configure.getNid(), configure.getValue());
            }
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }

    }

    @ApiOperation(value = "更新信息")
    @PostMapping(value = "/updateByPrimaryKeySelective")
    public ResultEntity updateByPrimaryKeySelective(@RequestBody Configure configure) throws BussinessException {

        try {
            configureService.updateByPrimaryKeySelective(configure);
            if (State.YES == configure.getStatus()) {
                redisService.put(CacheID.CONFIG_PREFIX + configure.getNid(), configure.getValue());
            } else {
                redisService.del(CacheID.CONFIG_PREFIX + configure.getNid());
            }
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }
    }

    @ApiOperation(value = "物理删除")
    @PostMapping(value = "/deleteByPrimaryKey")
    public void deleteByPrimaryKey(int id) throws BussinessException {
        configureService.deleteByPrimaryKey(id);
    }

    @ApiOperation(value = "逻辑删除")
    @PostMapping(value = "/removeById")
    public ResultEntity removeById(@RequestBody Configure configure) throws BussinessException {
        try {
            configureService.removeById(configure);
            Configure config = configureService.selectOne(new Configure(c -> {
                c.setId(configure.getId());
                c.setDelFlag(Configure.DELFLAG.NO.code);
            }));
            if (config != null) {
                redisService.del(CacheID.CONFIG_PREFIX + config.getNid());
            }
            return ok();
        } catch (Exception e) {
            e.printStackTrace();
            return fail();
        }

    }

    @ApiOperation(value = "主键查询")
    @PostMapping(value = "/getNormalModelById")
    public Configure getNormalModelById(@RequestBody Configure configure) throws BussinessException {
        return configureService.getNormalModelById(configure);
    }

    @ApiOperation(value = "单个查询")
    @PostMapping(value = "/selectOne")
    public Configure selectOne(@RequestBody Configure configure) throws BussinessException {
        return configureService.selectOne(configure);
    }


    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/select")
    public List<Configure> select(@RequestBody Configure configure) throws BussinessException {
        return configureService.select(configure);
    }

    @ApiOperation(value = "列表查询")
    @PostMapping(value = "/selectAll")
    public List<Configure> selectAll() throws BussinessException {
        return configureService.selectAll();
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/getDtGridList")
    public DtGrid getDtGridList(String dtGridPager) throws Exception {
        return configureService.getDtGridList(dtGridPager);
    }
}
