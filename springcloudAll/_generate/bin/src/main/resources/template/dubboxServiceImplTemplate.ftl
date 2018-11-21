package ${servicePath}.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erongdu.cf.commons.base.BaseServiceImpl;
import ${mapperPath}.${className}Mapper;
import ${domMainPath}.${className};
import ${servicePath}.${className}Service;

/**
 * 
 * <br>
 * <b>功能：</b>${className}ServiceImpl<br>
 * <b>作者：</b>${c_user}<br>
 * <b>日期：</b>${c_date} <br>
 * <b>版权所有：${c_info}<br>
 */ 
// @Service(protocol = {"rest", "dubbo"},version="1.0.0")
@Service(version = "1.0.0")
@Path("brandDubboService")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces({ ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8 })
public class ${className}DubboServiceImpl extends Base implements ${className}DubboService {
		
	private static final long serialVersionUID = 1L;

	@Autowired
    private ${className}Service $!{lowerName}Service;
    
    @Override
	public Pages<${className}> get${className}PagesByModel (${className} $!{lowerName}, BasePageRequest pageParam) {
		L.info("action dubbo method get${className}PagesByModel , $!{lowerName}:{},pageParam:{}", toJsonStr($!{lowerName}),
				toJsonStr(pageParam));
		return $!{lowerName}Service.findeModelPage($!{lowerName}, pageParam);
	}
	
	@Override
	public Brand add${className}(${className} $!{lowerName}) throws BussinessException {
		L.info("action dubbo method add${className} , $!{lowerName}:{}", toJsonStr($!{lowerName}));
		return $!{lowerName}Service.add${className}($!{lowerName});
	}

	@Override
	public void update${className}(${className} $!{lowerName}) throws BussinessException {
		L.info("action dubbo method update${className} , $!{lowerName}:{}", toJsonStr($!{lowerName}));
		$!{lowerName}Service.update${className}($!{lowerName});
	}

	@Override
	public void delete${className}(${className} $!{lowerName}) throws BussinessException {
		L.info("action dubbo method delete${className} , $!{lowerName}:{}", toJsonStr($!{lowerName}));
		$!{lowerName}Service.delete${className}($!{lowerName});
	}

	@Override
	public Brand get${className}(Integer id) throws BussinessException {
		L.info("action dubbo method get${className} , id:{}", id);
		return $!{lowerName}Service.getModelById(id);
	}

}
