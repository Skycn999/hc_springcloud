package com.mi.hundsun.oxchains.consumer.admin.controller.upload;

import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import com.mi.hundsun.oxchains.base.common.entity.ResultEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件上传接口
 *
 * @author Vector
 * @create 2017-06-16 14:54
 */
@Controller
@RequestMapping(value = BaseController.BASE_URI)
public class UploadJsonController {

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "file/upload", method = RequestMethod.POST)
    public ResultEntity fileUpload(MultipartFile file) throws Exception {
        ResultEntity resultEntity = new ResultEntity();
//        resultEntity.setCode(ResultEntity.SUCCESS);
//
//        //判断图片是否为空
//        if (file.isEmpty()) {
//            resultEntity.setCode(ResultEntity.FAIL);
//            resultEntity.setMessage("图片为空");
//            return resultEntity;
//        }
//        //上传到图片服务器
//        try {
//            //获取图片扩展名
//            String originalFilename = file.getOriginalFilename();
//            String extName = file.getOriginalFilename().substring(originalFilename.lastIndexOf(".") + 1);
//            String fileName = RandomUtil.randomUUID() + "." + extName;
//            String url = AliyunOSSClient.uploadFile(file.getBytes(), "/upload/" + fileName);
//            resultEntity.setData(url);
//        } catch (Exception e) {
//            e.printStackTrace();
//            resultEntity.setCode(ResultEntity.FAIL);
//            resultEntity.setMessage("图片上传失败");
//        }

        return resultEntity;
    }
}
