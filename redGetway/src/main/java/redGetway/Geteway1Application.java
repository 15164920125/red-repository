package redGetway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关启动类
 * @author sundonghao
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class Geteway1Application {
	public static void main(String[] args) {
		SpringApplication.run(Geteway1Application.class, args);
		System.out.println("Geteway1网关微服务启动成功！");
	}
}
