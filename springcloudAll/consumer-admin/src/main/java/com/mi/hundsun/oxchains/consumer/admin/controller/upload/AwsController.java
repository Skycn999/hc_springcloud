package com.mi.hundsun.oxchains.consumer.admin.controller.upload;

import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import com.mi.hundsun.oxchains.base.common.utils.RandomUtils;
import com.mi.hundsun.oxchains.base.common.utils.StringUtils;
import com.mi.hundsun.oxchains.base.core.aws.s3.S3Unit;
import com.mi.hundsun.oxchains.base.core.exception.BussinessException;
import com.mi.hundsun.oxchains.base.core.util.MatrixToImageWriter;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.consumer.admin.controller.GenericController;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(BaseController.BASE_URI)
public class AwsController extends GenericController {

    @Value("${basePath:C:\\Users\\64649\\}")
    private String basePath;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "base64Pic", value = "base64图片文件", required = true, dataType = "String")
            , @ApiImplicitParam(name = "type", value = "文件分类", required = true, dataType = "String")
    })
    @ApiOperation(value = "上传图片", notes = "上传图片")
    @RequestMapping("/admin/aws/uploadPic")
    public ResultEntity uploadPic(@RequestParam("uploadFile")  MultipartFile uploadFile) throws Exception {
        File imgFile= new File(basePath + RandomUtils.randomCustomUUID() + ".jpg");
        uploadFile.transferTo(imgFile);
        String bucketName = "idt-fund-s3-bucket-banner";
        if (imgFile.length() > 1024 * 1024 * 2) {
            log.error("pictures can not be more than 2M");
            throw new BussinessException("图片不能超过2M");
        }
        S3Unit instance = S3Unit.getInstance();
        String fileKey = instance.saveFile(bucketName, imgFile);
        if (imgFile.exists()) {
            boolean delete = imgFile.delete();
            if (delete) {
                log.info("Image Cache Delete SUCCESS");
            } else {
                log.error("Image Cache Delete Failed");
            }
        }
        imgFile.delete();
        Map<String, Object> map = new HashMap<>();
        map.put("fileKey", fileKey);
        map.put("bucketName", bucketName);
        map.put("filePath", "/admin/aws/getPic?fileKey=" + fileKey + "&bucketName=" + bucketName);
        return ok(map);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileKey", value = "文件key", required = true, dataType = "String")
            , @ApiImplicitParam(name = "bucketName", value = "bucketName", required = true, dataType = "String")
    })
    @ApiOperation(value = "显示图片", notes = "显示图片")
    @RequestMapping("/admin/aws/getPic")
    public ResultEntity getPic(@RequestParam String fileKey, @RequestParam String bucketName ,HttpServletResponse response) throws Exception {
        long s1 = System.currentTimeMillis();
        if (StringUtils.isBlank(fileKey) || StringUtils.isBlank(bucketName)) {
            return fail("no fileKey or bucketName");
        }
        S3Unit instance = S3Unit.getInstance();
        ServletOutputStream outputStream = response.getOutputStream();
        InputStream ins = instance.getFile(bucketName, fileKey);
        System.out.println("---------------- get pic took :" + (System.currentTimeMillis() - s1));
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        outputStream.close();
        ins.close();
        return ok();
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
                String fileKey = instance.saveFile(bucketName, image);
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

    /**
     * 把base64字符串转成file对象
     *
     * @param file      空file对象
     * @param imageData 图片内容
     * @throws Exception 转换异常
     */
    private void base64ToImage(File file, String imageData) throws Exception {
        if (imageData.contains("base64,")) {
            imageData = imageData.substring(imageData.indexOf("base64,"), imageData.length());
        }
        OutputStream out = null;
        try {
            // 将base64转图片
            BASE64Decoder decoder = new BASE64Decoder();
            //Base64解码
            byte[] b = decoder.decodeBuffer(imageData);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            out = new FileOutputStream(file);
            out.write(b);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    private String fileToBase64(File file) throws Exception {
        String base64 = null;
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] bytes = new byte[in.available()];
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;

    }

}
