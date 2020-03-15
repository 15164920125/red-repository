package red.com.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import red.com.commons.ConfigRestUrlServer;
import red.com.commons.Protocol;
import red.com.commons.Server;

/**
 * 获取yml配置信息
 * @author ASUS
 *
 */
@Component
public class GetServerUtils {
	@Autowired
	private ConfigRestUrlServer configRestUrlServer;
	private static ConfigRestUrlServer configRestUrlServer1;
	private static Protocol protocol;
	private static Server server;
	
	
	private GetServerUtils() {}
	
	/**
	 * 初始化(static 注入是不生效得，因此通过configRestUrlServer1来置换注入对象)
	 */
	@PostConstruct
	public void init() {
		configRestUrlServer1 = configRestUrlServer;
		getProtocol();
		getServer();
	}
	
	/**
	 * 获取协议
	 */
	private void getProtocol(){
		this.protocol = configRestUrlServer1.getProtocol();
	}
	/**
	 * 获取服务地址
	 */
	private void getServer(){
		this.server = configRestUrlServer1.getServer();
	}
	
	/**
	 * 获取指定服务地址
	 * @param serverName
	 * @return
	 */
	public static String getServerUrl(String serverName){
		if(protocol == null || server == null) {
			new GetServerUtils().init();
		}
		Assert.notNull(protocol, "protocol is required");
		Assert.notNull(server, "server is required");
		return protocol.getName() + getValue(server, serverName, String.class);
	}
	
	/**
	 * 根据指定方法名获取get值
	 * @param obj 执行方法实体
	 * @param name 执行方法名
	 * @param responseType 返回类型
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getValue(Object obj, String name , Class<T> responseType) {
		Method[] m = obj.getClass().getMethods();
		for (int i = 0; i < m.length; i++) {
			if (("get" + name).toLowerCase().equals(m[i].getName().toLowerCase())) {
				try {
					return (T) m[i].invoke(obj);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}
