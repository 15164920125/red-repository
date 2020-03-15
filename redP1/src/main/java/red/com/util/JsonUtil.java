package red.com.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

public class JsonUtil {
	private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);
	// json返回状态码
	public final static String STATE = "state"; // 手机端的参数
	public final static String STATE_SUCCESS = "200"; // 正确返回
	public final static String STATE_ERROR_PARAM = "400"; // 参数错误
	public final static String STATE_ERROR_AUTH = "401"; // 权限错误
	public final static String STATE_ERROR_UNEXISTS = "404"; // 请求资源不存在
	public final static String STATE_ERROR_TYPE = "415"; // 请求类型错误
	public final static String STATE_ERROR_CHECK = "422"; // 校验错误
	public final static String STATE_ERROR = "500"; // 服务器内部错误

	// json返回中文信息
	public final static String MSG_SUCCESS = "获取数据成功"; // 正确返回
	public final static String MSG_ERROR = "获取数据失败"; // 正确返回

	private final static Map<String, String> STATE_NUMBER = new HashMap<String, String>();

	static {
		STATE_NUMBER.put(STATE_SUCCESS, "正确返回");
		STATE_NUMBER.put(STATE_ERROR_PARAM, "参数错误");
		STATE_NUMBER.put(STATE_ERROR_AUTH, "权限错误");
		STATE_NUMBER.put(STATE_ERROR_UNEXISTS, "请求资源不存在");
		STATE_NUMBER.put(STATE_ERROR_TYPE, "请求类型错误");
		STATE_NUMBER.put(STATE_ERROR_CHECK, "校验错误");
		STATE_NUMBER.put(STATE_ERROR, "服务器内部错误");
	}
	
	/**
	 * 返回成功与否
	 * 
	 * @param flag
	 *            true or false
	 * @return
	 */
	public static Map<String, Object> returnSuccessOrNot(boolean flag, String message, Object data, String state) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", state);
		if (data != null) {
			result.put("data", data);
		} else {
			result.put("data", Collections.emptyList());
		}

		result.put("flag", flag);
		result.put("msg", message);
		log.info("返回给移动端的数据：" + JSON.toJSONString(result));
		return result;
	}

	/**
	 * 给返回前端数据
	 * 
	 */
	public static Map<String, Object> returnMsg(String msg, Object data, int state, Logger log) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", state);
		if (data != null) {
			result.put("data", data);
		} else {
			result.put("data", Collections.emptyList());
		}
		result.put("msg", msg);

		log.info("返回给前端的数据：" + JSON.toJSONString(result));
		return result;
	}



}
