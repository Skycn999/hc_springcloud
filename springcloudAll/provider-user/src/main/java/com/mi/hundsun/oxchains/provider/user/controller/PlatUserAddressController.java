package com.mi.hundsun.oxchains.provider.user.controller;

import com.alibaba.fastjson.JSON;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.core.aws.s3.S3Unit;
import com.mi.hundsun.oxchains.base.core.config.GenericController;
import com.mi.hundsun.oxchains.base.core.po.fn.PlatUserAddress;
import com.mi.hundsun.oxchains.base.core.service.fn.PlatUserAddressService;
import com.mi.hundsun.oxchains.base.core.util.MatrixToImageWriter;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(value = "平台地址相关服务", description = "PlatUserAddressController Created By 枫亭 At 2018-04-08 20:49.")
@Slf4j
@RestController
@RequestMapping("/prod/user/platUserAddress")
public class PlatUserAddressController extends GenericController {

    @Autowired
    PlatUserAddressService platUserAddressService;
    @Value("${basePath:C:\\Users\\64649\\}")
    private String basePath;


    @ApiOperation(value = "获取用户绑定的充币地址", notes = "获取用户绑定的充币地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "code", value = "提币币种", required = true, dataType = "String")
    })
    @PostMapping(value = "/getByUserIdAndCode")
    public ResultEntity getByUserIdAndCode(@RequestParam Integer userId,@RequestParam String code) {
        if (null == userId) {
            return fail("用户id不能为空");
        }
        PlatUserAddress userAddress = new PlatUserAddress();
        userAddress.setUserId(userId);
        userAddress.setCoinCurrency(code);
        userAddress.setState(PlatUserAddress.STATE.DISTRIBUTED.code);
        userAddress = platUserAddressService.selectOne(userAddress);
        Map<String, Object> map = new HashMap<>();
        if (null != userAddress) {
            map.put("address", userAddress.getAddress());
            if(StrUtil.isBlank(userAddress.getAddressQrCodePath())) {
                String qrPath = generateQrCode(userAddress.getAddress());
                userAddress.setAddressQrCodePath(qrPath);
                platUserAddressService.updateByPrimaryKeySelective(userAddress);
            }
            map.put("qrPath", userAddress.getAddressQrCodePath());
            return ok(JSON.toJSON(map));
        }
        return fail("该用户暂未配置充币地址");
    }

    /**
     * 生成二维码并上传，返回路径
     *
     * @param content 二维码内容
     * @return 路径
     */
    public String generateQrCode(String content) {
        String qrCodeUrl = null;
        if (StrUtil.isBlank(content)) {
            log.error("二维码内容不能为空");
            return null;
        }
        String tempFilePath = basePath + UUID.randomUUID().toString();
        String format = "jpg";
        try {
            MatrixToImageWriter.getQrCodeImage(content, tempFilePath, format);
            //获取图片上传至图片服务器 aws s3
            S3Unit instance = S3Unit.getInstance();
            String bucketName = "idt-fund-s3-bucket-qr-code";
            File image = new File(tempFilePath + "." + format);
            if (image.exists()) {
                long s1 = System.currentTimeMillis();
                String fileKey = instance.saveFile(bucketName, image);
                System.out.println("saveFile took:" + (System.currentTimeMillis() - s1));
                qrCodeUrl = "/api/web/aws/getPic?fileKey=" + fileKey + "&bucketName=" + bucketName;
                if (image.exists()) {
                    boolean delete = image.delete();
                    if (delete) {
                        log.info("qr-code image cache delete success");
                    } else {
                        log.error("qr-code image cache delete failed");
                    }
                }
            }
        } catch (Exception e) {
            log.error("二维码生成失败,{}", e.getMessage());
        }
        return qrCodeUrl;
    }

}
