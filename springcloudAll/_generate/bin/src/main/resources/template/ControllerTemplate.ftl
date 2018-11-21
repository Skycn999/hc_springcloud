package ${contrPath};

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

import ${baseContrPath};
import ${domMainPath}.Pages;
import ${domMainPath}.${className};
import ${servicePath}.${className}Service;
/**
 *
 * @Description: TODO(这里用一句话描述这个类的作用)<br>
 * @author ${c_user}
 * @date   ${c_date}
 */
@RestController
@RequestMapping("/${modelName}/${lowerName}Controller/") 
public class ${className}Controller extends BaseController{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private ${className}DubboService ${lowerName}DubboService; 
	
	

}
