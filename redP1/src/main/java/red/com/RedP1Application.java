package red.com;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients
@CrossOrigin
//@EnableCircuitBreaker//使用hystrix做降级则启用
@EnableHystrix
public class RedP1Application {
	
	public static void main(String[] args) {
		SpringApplication.run(RedP1Application.class, args);
		System.out.println("redP1微服务启动成功");
	}

	/**
	 * 微服务调用微服务-负载均衡
	 */
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	// 加载mybatis分页插件
//	@Bean
//	public PageInterceptor pageHelper() {
//		return new PageInterceptor();
//	}

	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		// 设置文件大小限制 ;
		factory.setMaxFileSize("200MB"); // KB,MB
		// 设置参数-数据总大小
		factory.setMaxRequestSize("200MB");
		return factory.createMultipartConfig();
	}
	
}
