package com.mi.hundsun.oxchains.consumer.admin.controller.count;

import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import com.mi.hundsun.oxchains.consumer.admin.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(BaseController.BASE_URI)
public class UserFormJsonController extends GenericPo {

//    @Resource
    //UserFormInterface userFormInterface;

//    /**
//     * 平台用户统计
//     */
//    @ResponseBody
//    @RequestMapping(value = "user/json/form", method = RequestMethod.POST)
//    @RequiresPermissions("sys:user:form")
//    public ResultEntity formListJson(@RequestParam(value = "startTimeStr") String startTimeStr,
//                                     @RequestParam(value = "endTimeStr") String endTimeStr) throws Exception {
//        ResultEntity resultEntity = new ResultEntity();
//        Map<String,Object> map = new HashMap<>();
////        //resultEntity.setData(userFormInterface.userSum(startTimeStr,endTimeStr));
//        resultEntity.setCode(ResultEntity.SUCCESS);
//        return resultEntity;
//   }
}
