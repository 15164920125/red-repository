package red.com.business.service;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.netflix.ribbon.proxy.annotation.Hystrix;

import red.com.business.service.impl.FeignHelloDemoServiceImpl;

/**
 * feign接口，实现服务远程调用
 */
@Component
@FeignClient(value="redC1",fallback = FeignHelloDemoServiceImpl.class)
public interface FeignHelloDemoService {
	
	@RequestMapping("/redC1Interf/testLoadBanced")
	Map<String,Object> getFeignResult();
	
	@GetMapping("/hystrix/hystrixInfo_OK/{serverName}")
	public Object hystrixInfo_OK(@PathVariable(value="serverName") String serverName);

	@GetMapping("/hystrix/hystrixInfo_Fail_TimeOut/{serverName}")
	public Object hystrixInfo_Fail_TimeOut(@PathVariable(value="serverName") String serverName);
	
	@GetMapping("/hystrix/hystrixCircuitBreaker/{serverName}")
	public Object hystrixCircuitBreaker(@PathVariable(value="serverName") String serverName);

}
