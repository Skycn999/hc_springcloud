package ${servicePath};

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ${baseServicePath};
import ${baseServiceImplPath};
import com.moment.common.exception.BussinessException;
import ${mapperPath}.${className}Mapper;
import ${domMainPath}.${className};
import ${servicePath}.${className}Service;

/**
 * ${codeName}业务相关Service接口实现<br>
 *
 * @ClassName: ${className}ServiceImpl
 * @author ${c_user}
 * @date   ${c_date}
 */
@Service
@Scope("prototype")
@Transactional(rollbackFor = Exception.class)
public class ${className}ServiceImpl implements ${className}Service {

	@Resource
    private ${className}Mapper $!{lowerName}Mapper;

    @Override
    public GenericMapper<${className},Integer> _getMapper() {
        return $!{lowerName}Mapper;
    }

}
