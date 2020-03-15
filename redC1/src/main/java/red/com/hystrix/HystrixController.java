package red.com.hystrix;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

	@Autowired
	private HystrixService hystrixService;
	@Value("${server.port}")
	private String  port;
	
	
	@GetMapping("/hystrixInfo_OK/{serverName}")
	public Object hystrixInfo_OK(@PathVariable(value="serverName") String serverName){
		Map<String,Object> map = new HashMap<>();
		String rstring = hystrixService.hystrixInfo_OK(serverName);
		map.put("data", rstring);
		map.put("port", port);
		System.out.println("***Result:"+JSON.toJSONString(map));
		return map;
	}
	
	/**
	 * 设置自身调用超时时间的峰值，峰值内可正常运行
	 * 超过了需要有兜底的方法处理，作为服务降级fallback
	 * 兜底方法：向调用方返回一个符合预期的、可处理的备选响应。这样保证了服务调用方的线程不会被长时间占用。
	 */
	@GetMapping("/hystrixInfo_Fail_TimeOut/{serverName}")
	public Object hystrixInfo_Fail_TimeOut(@PathVariable(value="serverName") String serverName){
		Map<String,Object> map = new HashMap<>();
		String rstring = hystrixService.hystrixInfo_Fail_TimeOut(serverName);
		map.put("data", rstring);
		map.put("port", port);
		System.out.println("***Result:"+JSON.toJSONString(map));
		return map;
	}

//-------上边是服务降级方法-----下边是服务熔断方法----------------------
	/**
	 * 服务熔断方法
	 */
	@GetMapping("/hystrixCircuitBreaker/{id}")
	public Object hystrixCircuitBreaker(@PathVariable(value="id") Integer id){
		Map<String,Object> map = new HashMap<>();
		String rstring = hystrixService.hystrixCircuitBreaker(id);
		map.put("data", rstring);
		map.put("port", port);
		System.out.println("***Result:"+JSON.toJSONString(map));
		return map;
	}
	
}

