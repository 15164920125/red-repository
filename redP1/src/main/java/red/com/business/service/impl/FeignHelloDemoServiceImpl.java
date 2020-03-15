package red.com.business.service.impl;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import red.com.business.service.FeignHelloDemoService;

@Component
public class FeignHelloDemoServiceImpl implements FeignHelloDemoService{

	@Override
	public Map<String, Object> getFeignResult() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", "---FeignHelloDemoServiceImpl	fallback getFeignResult");
		return map;
	}

	@Override
	public Object hystrixInfo_OK(String serverName) {
		return "---FeignHelloDemoServiceImpl	fallback	hystrixInfo_OK";
	}

	@Override
	public Object hystrixInfo_Fail_TimeOut(String serverName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object hystrixCircuitBreaker(String serverName) {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public Object hystrixInfo_Fail_TimeOut(String serverName) {
		return "---FeignHelloDemoServiceImpl	fallback	hystrixInfo_Fail_TimeOut";
	}*/

}
