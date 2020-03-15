package redGetway.filter;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

/**
 * 全局日志
 * @author ASUS
 *
 */
@Component
public final class MyGlobalLogFilter implements GlobalFilter,Ordered{
	
	private Logger log = Logger.getLogger(MyGlobalLogFilter.class);

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("进入自定义全局日志过滤器...");
		ServerHttpRequest request = exchange.getRequest();
		log.info("请求uri:"+request.getURI());
		log.info("请求path:"+request.getPath());
		log.info("请求数据:"+request.getQueryParams());
		
		
		return chain.filter(exchange);
	}
	
	@Override
	public int getOrder() {
		return 0;
	}

	/**
	 * 获取前端传来的参数,转成map
	 */
	/*public static Map<String, Object> getParamToMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement();
			String value = request.getParameter(key);
			map.put(key, value);
		}
		return map;
	}*/

}
