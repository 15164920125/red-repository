package red.com.user;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.util.IdUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

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
	public Object login(HttpServletRequest request){
		Map<String, Object> params = this.getParamToMap(request);
		log.info("前端上传参数为："+JSON.toJSONString(params));
		// 根据appId获取其对应所拥有的角色(这里设计为角色对应资源,没有权限对应资源)
		String appId = (String) params.get("appId");
//		String roles = accountService.loadAccountRole(appId);
		// 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
		long refreshPeriodTime = 36000L;
		//登录成功，则返回jwt生成的token
		return null;
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
