package ${mapperPath};

import org.springframework.stereotype.Repository;
import com.mi.hundsun.oxchains.base.core.service.base.GenericService;
import ${baseMapperPath};
import org.apache.ibatis.annotations.Mapper;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericMapper;

import ${domMainPath}.${className};

/**
 * ${codeName}相关Dao接口<br>
 *
 * @author ${c_user}
 * @date   ${c_date}
 */
public interface ${className}Mapper extends GenericMapper< ${className},Integer> {


}
