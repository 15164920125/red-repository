package red.com.mybatis.commons;

import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * 映射mapper.xml文件的内容
 * @author ASUS
 *
 */
@Data
@Component
public class MapperStatement {
	
	private String namespace;
	private String id;
	private String resultType;
	private String sql;

}
