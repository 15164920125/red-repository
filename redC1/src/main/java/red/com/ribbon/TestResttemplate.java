package red.com.ribbon;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import red.com.util.HttpUtil;

@RestController
@RequestMapping("/redC1Interf")
public class TestResttemplate {
	
	@GetMapping("testLoadBanced")
	public Map<String,Object> helloInfo(HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		map.put("url", request.getRequestURL());
		map.put("uri", request.getRequestURL());
		System.out.println("redC1返回数据："+JSON.toJSONString(map));
		return map;
	}
	@PostMapping("testLoadBancedPost")
	public Object helloInfoPost(@RequestBody String json,HttpServletRequest request){
		System.out.println("redC1接收到的数据："+json.toString());
		Map<String,Object> paramToMap = new HashMap<>();
		paramToMap.put("url", request.getRequestURL());
		paramToMap.put("uri", request.getRequestURL());
		paramToMap.put("requestdata", json);
		return paramToMap;
	}

}
