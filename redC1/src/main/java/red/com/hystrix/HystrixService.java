package red.com.hystrix;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import cn.hutool.core.util.IdUtil;

@Service
public class HystrixService {

	/**
	 * 正常的访问
	 * @param id
	 * @return
	 */
	public String hystrixInfo_OK(String serverName){
		return "线程池："+Thread.currentThread().getName()+" hystrixInfo_OK,serverName："+serverName+"\t"+"O(∩_∩)O哈哈~";
	}
	/**
	 * 服务降级:超时异常、报错异常
	 * 只要服务不可用，立刻降级
	 * 当前线程超时时间的峰值：3秒
	 */
	@HystrixCommand(fallbackMethod="hystrixInfo_Fail_TimeOutHandler",commandProperties={
			@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="3000")
	})
	public String hystrixInfo_Fail_TimeOut(String serverName){
//		int a = 10/0;
		int timeout = 4;
		try{TimeUnit.SECONDS.sleep(timeout);}catch(Exception e){}
		return "线程池："+Thread.currentThread().getName()+" hystrixInfo_Fail_TimeOut,serverName："+serverName+"\t"+"O(∩_∩)O哈哈~";
	}
	
	public String hystrixInfo_Fail_TimeOutHandler(String serverName){
		return "线程池："+Thread.currentThread().getName()+","+serverName+" 系统繁忙请稍后再试！,"+"\t"+"o(╥﹏╥)o";
	}

//-------------上边服务降级------------------------------------------	
//-------------下边服务熔断------------------------------------------
	
	/**
	 * 服务熔断
	 */
	@HystrixCommand(fallbackMethod="hystrixCircuitBreaker_Handler",commandProperties={
			@HystrixProperty(name="circuitBreaker.enabled",value="true"),//是否开启断路器
			@HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value="10"),//请求次数
			@HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value="10000"),//时间范围
			@HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value="60"),//失败率达到多少后断路			
	})
	public String hystrixCircuitBreaker(Integer id){
		if(id<0){
			throw new RuntimeException("*******id不能小于0！");
		}
		return Thread.currentThread().getName()+"\t"+"调用成功,流水号："+IdUtil.simpleUUID();
	}
	public String hystrixCircuitBreaker_Handler(Integer id){
		return "服务熔断处理方法：id不能小于0！,"+"\t"+"o(╥﹏╥)o";
	}
	
	
	
}
