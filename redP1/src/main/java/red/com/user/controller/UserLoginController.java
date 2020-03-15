package red.com.user.controller;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Jedis;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import red.com.util.JsonUtil;
import red.com.util.JsonWebTokenUtil;

@RestController
@RequestMapping("/user")
public class UserLoginController {
	private Logger log = Logger.getLogger(UserLoginController.class);

	/**
	 * POST用户登录签发JWT
	 * @param request
	 * @return
	 */
	@PostMapping("/login")
	public Object login(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> params = this.getParamToMap(request);
		log.info("前端上传参数为："+JSON.toJSONString(params));
		// 根据appId获取其对应所拥有的角色(这里设计为角色对应资源,没有权限对应资源)
		String appId = (String) params.get("appId");
		log.info("前端上传参数appId="+appId);
//		String roles = accountService.loadAccountRole(appId);
		String roles="";
		// 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
		long refreshPeriodTime = 60000L;
		String jwt = JsonWebTokenUtil.issueJWT(IdUtil.simpleUUID(), appId, "token-server", refreshPeriodTime
				, roles, null, SignatureAlgorithm.HS512);
		//将签发的jwt存到Redis:{JWT-SESSION-{appID} , jwt} 并且设置超时时间为2倍jwt的时间
		Map<String,Object> map = new HashMap<>();
		map.put("userInfo", jwt);
		response.setHeader("jwt-token", jwt);
		return JsonUtil.returnSuccessOrNot(true, JsonUtil.MSG_SUCCESS, map, JsonUtil.STATE_SUCCESS);
	}
	/**
	 * 获取前端传来的参数,转成map
	 */
	public Map<String, Object> getParamToMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement();
			String value = request.getParameter(key);
			map.put(key, value);
		}
		return map;
	}
	
	public static void main(String[] args) {
		JwtBuilder jwtBuilder = Jwts.builder().setId("88").setSubject("小白").setClaims(new DefaultClaims()).setExpiration(new Date())
		.signWith(SignatureAlgorithm.HS256,IdUtil.simpleUUID());
		String token = jwtBuilder.compact();
		System.out.println(token);
	}
	
}
