package redGetway.filter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import reactor.core.publisher.Mono;
import redGetway.util.JsonWebTokenUtil;
import redGetway.util.JwtAccount;

/**
 * 全局日志
 * 
 * @author ASUS
 *
 */
@Component
public final class MyGlobalLogFilter implements GlobalFilter, Ordered {

	private Logger log = Logger.getLogger(MyGlobalLogFilter.class);

	/**
	 * 配置免拦截接口地址
	 */
	private static List<String> uncheckUrls = new ArrayList<>();
	static {
		uncheckUrls.add("/user/login");
		uncheckUrls.add("/user/logout");
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("进入自定义全局过滤器...");
		ServerHttpRequest request = exchange.getRequest();
		URI uri = request.getURI();
		HttpMethod method = request.getMethod();
		log.info("接口地址uri=" + uri + ",访问的方法:" + method);

		// 后端接口免拦截效验
		for (String string : uncheckUrls) {
			boolean contains = uri.toString().contains(string);
			if (contains) {
				return chain.filter(exchange);
			}
		}

		//从请求头中取出jwt-token
		HttpHeaders headers = request.getHeaders();
		List<String> lists = headers.get("jwt-token");
		String jwtToken ="";
		if(!StringUtils.isEmpty(lists)){
			jwtToken = lists.get(0);
		}
		log.info("请求头jwt-token=" + jwtToken);
		if(StringUtils.isEmpty(jwtToken)){
			//返回401状态码和提示信息
            ServerHttpResponse response = exchange.getResponse();
            JSONObject message = new JSONObject();
            message.put("status", 500);
            message.put("data", "请先登录！");
            byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
		}
		//验签JWT
		try{
			JwtAccount parseJwt = JsonWebTokenUtil.parseJwt(jwtToken, JsonWebTokenUtil.SECRET_KEY);
			log.info("验签成功="+parseJwt+",刷新jwt-token!");
			String rejwt = JsonWebTokenUtil.issueJWT(IdUtil.simpleUUID(), parseJwt.getAppId(),
					parseJwt.getIssuer(), parseJwt.getRoles(), parseJwt.getPerms()
					, SignatureAlgorithm.HS512);
			ServerHttpResponse response = exchange.getResponse();
			response.getHeaders().add("jwt-token", rejwt);
			response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
		}catch(Exception e){
			log.info("验签失败！");
            ServerHttpResponse response = exchange.getResponse();
            JSONObject message = new JSONObject();
            message.put("status", 500);
            message.put("data", "账户已过期或鉴权失败,请重新登录!");
            byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = response.bufferFactory().wrap(bits);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            //指定编码，否则在浏览器中会中文乱码
            response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
            return response.writeWith(Mono.just(buffer));
		}

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}

	/**
	 * 获取前端传来的参数,转成map
	 */
	/*
	 * public static Map<String, Object> getParamToMap(HttpServletRequest
	 * request) { Map<String, Object> map = new HashMap<String, Object>();
	 * Enumeration<String> paramNames = request.getParameterNames(); while
	 * (paramNames.hasMoreElements()) { String key = paramNames.nextElement();
	 * String value = request.getParameter(key); map.put(key, value); } return
	 * map; }
	 */

}
