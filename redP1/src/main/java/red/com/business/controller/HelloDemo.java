package red.com.business.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.nacos.api.naming.CommonParams;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import red.com.business.service.FeignHelloDemoService;
import red.com.business.service.HelloDemoService;

/**
 * @DefaultProperties 全局的降级处理方法
 * @author sundonghao
 *
 */
@RestController
@RequestMapping("/hello")
@DefaultProperties(defaultFallback="global_FallbackMethod")
@RefreshScope//支持nacos动态刷新功能
public class HelloDemo {
	
	@Autowired
	private HelloDemoService helloDemoService;
	@Autowired
	private FeignHelloDemoService feignHelloDemoService;
	
//	@Value("${config.info}")
//	private String nacosConfigCenter_configInfo;
	

	@RequestMapping("cs")
	public Object helloInfo(){
//		System.out.println("读取nacos配置中心的配置="+nacosConfigCenter_configInfo);
//		helloDemoService.queryPrpLclaimFeeHisList(json);
		Map<String,Object> map = new HashMap<>();
//		return nacosConfigCenter_configInfo;
		return "哈哈哈";
	}
	
	/**
	 * 此接口测试discoverclient做负载均衡
	 * resttemplate做远程服务调用
	 * @param serverName
	 * @return
	 */
	@GetMapping("/csDiscoverclient/{serverName}")
	public Map<String,Object> csDiscoverclient(@PathVariable String serverName){
		return helloDemoService.csDiscoverclient(serverName);
	}
	
	/**
	 * 此接口测试ribbon做负载均衡+远程服务调用
	 * resttemplate做远程服务调用getForObject
	 * @param serverName
	 * @return
	 */
	@GetMapping("/csRibbonGet/{serverName}")
	public Map<String,Object> csribbon(@PathVariable String serverName){
		return helloDemoService.csribbon(serverName);
	}
	/**
	 * 此接口测试ribbon做负载均衡+远程服务调用
	 * resttemplate做远程服务调用postForObject
	 * @param serverName
	 * @return
	 */
	@GetMapping("/csRibbonPost/{serverName}")
	public Object csribbonpost(@PathVariable String serverName){
		return helloDemoService.csribbonPost(serverName);
	}
	
	/**
	 * 此接口测试fenign做远程服务调用
	 * @param serverName
	 * @return
	 */
	@GetMapping("/csFeignGet")
	public Map<String,Object> csfeign(){
		return feignHelloDemoService.getFeignResult();
	}
	
	/**
	 * 此接口测试fenign+hystrix 实现熔断限流
	 * @param serverName
	 * @return
	 */
	@HystrixCommand
	@GetMapping("/csFeignHystrix_OK/{serverName}")
	public Object csfeign_OK(@PathVariable(value="serverName") String serverName){
		return feignHelloDemoService.hystrixInfo_OK(serverName);
	}
	/**
	 * 此接口测试fenign+hystrix 实现熔断限流
	 * @param serverName
	 * @return
	 */
	@HystrixCommand(fallbackMethod="csfeign_TimeOutHandler",commandProperties={
			@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="5000")
	})
	@GetMapping("/csFeignHystrix_TimeOut/{serverName}")
	public Object csfeign_TimeOut(@PathVariable(value="serverName") String serverName){
		return feignHelloDemoService.hystrixInfo_Fail_TimeOut(serverName);
	}
	
	public String csfeign_TimeOutHandler(@PathVariable(value="serverName") String serverName){
		return "我是消费者，对方系统繁忙：请10秒后重试！"+"o(╥﹏╥)o";
	}
	
	//下面是全局fallback（降级方法）
	public Object global_FallbackMethod(){
		return "global异常处理信息：对方系统繁忙,请稍后重试！"+"o(╥﹏╥)o";
	}
	
}
