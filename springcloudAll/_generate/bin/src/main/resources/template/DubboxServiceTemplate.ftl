package ${servicePath};

import com.erongdu.cf.commons.exception.BussinessException;
import com.erongdu.cf.domain.BasePageRequest;
import com.erongdu.cf.domain.Pages;
import ${domMainPath}.${className};

 /**
 * 
* @ClassName: ${className}DubboService
* @author ${c_user}
* @date   ${c_date}
* @Description: TODO(这里用一句话描述这个类的作用)<br>
*    ${c_info}
 */
public interface ${className}DubboService {
	/**
	 * 
	* @Title: get${className}PagesByModel 
	* @param @param brand 
	* @param @param pageParam
	* @param @return  列表数据
	* @return Pages<Brand>    返回类型 
	* @Description: TODO 根据模型分页查询
	* @throws
	 */
	public Pages<${className}> get${className}PagesByModel(${className} $!{lowerName},BasePageRequest pageParam);
	
	/**
	 * 
	* @Title: save${className} 
	* @param @param ${className}
	* @param @throws BussinessException   异常信息
	* @return void    返回类型 
	* @Description: TODO(保存${className}) 
	* @throws
	 */
	public ${className} save${className} (${className} $!{lowerName}) throws BussinessException;
	
	/**
	 * 
	* @Title: delete${className} 
	* @param @param ${className}
	* @param @throws BussinessException   异常信息
	* @return void    返回类型 
	* @Description: TODO(删除${className}，实际上做逻辑删除操作) 
	* @throws
	 */
	public void delete${className} (${className} $!{lowerName}) throws BussinessException;
	
	/**
	 * 
	* @Title: update${className} 
	* @param @param ${className}
	* @param @throws BussinessException   异常信息
	* @return void    返回类型 
	* @Description: TODO(修改${className}) 
	* @throws
	 */
	public void update${className} (${className} $!{lowerName}) throws BussinessException;
	
	/**
	 * 
	* @Title: delete${className} 
	* @param @param ${className}
	* @param @throws BussinessException   异常信息
	* @return void    返回类型 
	* @Description: TODO(删除${className}，实际上做逻辑删除操作) 
	* @throws
	 */
	public ${className} get${className}ById (Integer id) throws BussinessException;
	
}
