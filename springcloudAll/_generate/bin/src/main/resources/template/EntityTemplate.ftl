package ${domMainPath};

import ${baseDomainPath};
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.mi.hundsun.oxchains.base.common.baseMapper.GenericPo;
import java.util.function.Consumer;
import javax.persistence.Table;
import javax.persistence.Transient;

${inportPakges}

/**
 * ${codeName}实体信息<br>
 *
 * @author ${c_user}
 * @date   ${c_date}
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "${codeName}")
@Table(name = "${tableName}")
public class ${className} extends GenericPo<Integer> {

    public static final String TABLE_NAME = "${tableName}";

	${feilds}

    public ${className}(){}

    public ${className}(Consumer<${className}> consumer){
    consumer.accept(this);
    }
}

