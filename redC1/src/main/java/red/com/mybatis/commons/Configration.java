package red.com.mybatis.commons;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Data;
@Data
@Component
public class Configration {
	
	/**
	 * 数据库url
	 */
	private String url;
	/**
	 * 数据库driverName
	 */
	private String driverName;
	private String userName;
	private String password;
	/**
	 * map的key是namespance+id
	 */
	private Map<String,MapperStatement> mapperStatement = new HashMap<String,MapperStatement>();

}
