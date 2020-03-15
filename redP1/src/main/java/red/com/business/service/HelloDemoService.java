package red.com.business.service;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import red.com.util.GetServerUtils;

@Service
public class HelloDemoService {
	
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private DiscoveryClient discoveryClient;
	
	
	/**
	 *  基于restTemplate实现服务调用
	 * 	restTemplate 使用url去调用其他微服务 http://ip:port/redC1Interf/testLoadBanced
	 * @return
	 */
	public Map<String,Object> csDiscoverclient(String serverName){
		Map<String,Object> map = new HashMap<>();
		List<ServiceInstance> instances = discoveryClient.getInstances(serverName);
		int i=0;
		for (ServiceInstance Instance : instances) {
			map.put("uri"+i, Instance.getUri());
			i++;
		}
		return map;
	}
	/**
	 *  基于ribbon+restTemplate实现服务调用
	 * 	restTemplate 使用url去调用其他微服务 http://微服务名/redC1Interf/testLoadBanced
	 *  ribbon做负载均衡，默认轮询机制，在注入resttemplate时加@loadbanced就可以
	 * @return
	 */
	public Map<String,Object> csribbon(String serverName){
		//第一种get
		Map<String,Object> map = new HashMap<>();
		String url = "http://"+serverName+"/redC1Interf/testLoadBanced";
		map = restTemplate.getForObject(url, Map.class);
		map.put("url", url);
		return map;
	}
	/**
	 *  基于ribbon+restTemplate实现服务调用
	 * 	restTemplate 使用url去调用其他微服务 http://微服务名/redC1Interf/testLoadBanced
	 *  ribbon做负载均衡，默认轮询机制，在注入resttemplate时加@loadbanced就可以
	 * @return
	 */
	public Object csribbonPost(String serverName){
		//第二种post
		String url = "http://"+serverName+"/redC1Interf/testLoadBancedPost";
		String json="{serverName:'"+serverName+"'}";
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> requestEntity = new HttpEntity<>(json, requestHeaders);
		return restTemplate.postForObject(url, requestEntity, Object.class);
	}

	/**
	   * 测试读取yml配置
	   */

	public Object queryPrpLclaimFeeHisList(String json) {
		HttpHeaders requestHeaders = new HttpHeaders();
	    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<String> requestEntity = new HttpEntity<>(json, requestHeaders);
	    String serverUrl = GetServerUtils.getServerUrl("claim");
	    Object object = restTemplate.postForObject(GetServerUtils.getServerUrl("claim")+"/queryPrpLclaimFeeHisList", requestEntity, Object.class);
	    return restTemplate.postForObject(GetServerUtils.getServerUrl("claim")+"/queryPrpLclaimFeeHisList", requestEntity, Object.class);
	}
	
}
