package red.com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;




@Component
public class HttpUtil {
	
	
	private static HttpUtil httpUtil;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PostConstruct
	public void init(){
		httpUtil = this;
	}
	
	/**
	 * 调用接口、发送报文
	 * 
	 * @param jieKouName
	 *            接口名称
	 * @param url
	 *            接口地址
	 * @param sendStr
	 *            发送报文
	 * @param token
	 *            jwt效验token
	 * @param logger
	 *            日志
	 */
	public static String sendData(String jieKouName, String url, String sendStr, String token, Logger logger)
			throws Exception {
		logger.info(jieKouName + "地址：" + url);

		// 字符编码
		String encode = "UTF-8";
		// 超时时间30秒
		int timeout = 60000;

		PostMethod postmethod = null;
		BufferedReader bf = null;
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

			HttpClientParams httpClientParams = new HttpClientParams();
			httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			httpClient.setParams(httpClientParams);

			// 访问地址
			postmethod = new PostMethod(url);
			// 添加请求头
			logger.info(jieKouName + "请求头token=" + token);
			postmethod.setRequestHeader("token", token);

			// 发送报文
			logger.info(jieKouName + "发送报文：" + sendStr);
			// byte[] by = sendStr.getBytes(encode);
			// InputStream in = new ByteArrayInputStream(by);
			// RequestEntity re = new InputStreamRequestEntity(in);
			RequestEntity re = new StringRequestEntity(sendStr, "application/json", encode);
			postmethod.setRequestEntity(re);
			httpClient.executeMethod(postmethod);

			// 返回报文
			StringBuffer lines = new StringBuffer();
			String line = "";
			bf = new BufferedReader(new InputStreamReader(postmethod.getResponseBodyAsStream(), encode));
			while ((line = bf.readLine()) != null) {
				lines.append(line);
			}

			logger.info(jieKouName + "返回报文：" + lines.toString());
			return lines.toString();
		} finally {
			if (postmethod != null) {
				// 释放连接
				postmethod.releaseConnection();
			}
			if (bf != null) {
				bf.close();
			}
		}
	}

	public static String send(String jieKouName, String url, String sendStr, String token, Logger logger) {
		logger.info(jieKouName + "地址：" + url);
		logger.info(jieKouName + "请求头token=" + token);
		logger.info(jieKouName + "发送报文：" + sendStr);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.APPLICATION_JSON);
		requestHeaders.set("token", token);
		HttpEntity<String> request = new HttpEntity<>(sendStr, requestHeaders);
		String result = httpUtil.restTemplate.postForObject(url, request, String.class);
//		String result = restTemplate.postForObject(url, request, String.class);
		logger.info(jieKouName + "返回报文：" + result);
		return result;
	}

	/**
	 * 集中报案-接口调用
	 * 
	 * @param url
	 *            接口地址
	 * @param paramMap
	 *            发送参数
	 * @param log
	 *            日志
	 */
	public static String sendUrl(String jieKouName, String url, Map<String, String> paramMap, String token,
			Logger logger) throws Exception {
		logger.info(jieKouName + "地址：" + url);

		// 字符编码
		String encode = "UTF-8";
		// 超时时间30秒
		int timeout = 30000;

		PostMethod postmethod = null;
		BufferedReader bf = null;
		try {
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(timeout);
			httpClient.getHttpConnectionManager().getParams().setSoTimeout(timeout);

			// 网络不通导致的超时,重试次数，默认是3次；当前是禁用掉
			HttpClientParams httpClientParams = new HttpClientParams();
			httpClientParams.setContentCharset(encode);
			httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			httpClient.setParams(httpClientParams);
			// 访问地址
			postmethod = new PostMethod(url);
			// 在头文件中设置转码
			postmethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encode);
			// 添加请求头
			logger.info(jieKouName + "请求头token=" + token);
			postmethod.setRequestHeader("token", token);

			// 添加发送参数
			if (paramMap != null) {
				Set<String> keySet = paramMap.keySet();
				for (String key : keySet) {
					String value = paramMap.get(key);
					if (value != null && value.length() != 0) {
						postmethod.addParameter(key, value);
						logger.info("发送参数:" + key + "=" + value);
					}
				}

			}

			httpClient.executeMethod(postmethod);

			// 返回报文
			StringBuffer lines = new StringBuffer();
			String line = "";
			bf = new BufferedReader(new InputStreamReader(postmethod.getResponseBodyAsStream(), encode));
			while ((line = bf.readLine()) != null) {
				lines.append(line);
			}
			logger.info(jieKouName + "返回报文：" + lines.toString());
			return lines.toString();
		} finally {
			if (postmethod != null) {
				// 释放连接
				postmethod.releaseConnection();
			}
			if (bf != null) {
				bf.close();
			}
		}
	}

	/**
	 * 获取前端传来的参数,打印日志
	 */
	public static void getParamToLog(HttpServletRequest request, Logger logger) {
		Enumeration<String> paramNames = request.getParameterNames();
		StringBuilder str = new StringBuilder();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement();
			String value = request.getParameter(key);
			str.append(key + "=" + value + "  ");
		}
		logger.info("前端上传的参数 " + str);
	}

	/**
	 * 获取前端传来的参数,转成map
	 */
	public static Map<String, Object> getParamToMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String key = paramNames.nextElement();
			String value = request.getParameter(key);
			map.put(key, value);
		}
		return map;
	}

//	public static ResourceBundle mobileConfig = ResourceBundle.getBundle("mobileConfig");
//	public static ResourceBundle mobileParam = ResourceBundle.getBundle("mobileParam");

	/**
	 * 获取服务路径
	 * 
	 * @param serviceName
	 * @return
	 * @throws Exception
	 */
	/*public static String getZhongTaiUrl(String serviceName, HttpServletRequest request) {
		String serviceUrl = mobileParam.getString("zhongTaiUrl");
		String servicePath = mobileConfig.getString(serviceName);
		if (StringUtils.isEmpty(serviceUrl) || StringUtils.isEmpty(servicePath)) {
			throw new RuntimeException("mobileConfig.properties配置文件中,找不到key:" + serviceName);
		}
		String sjComCode = request.getParameter("sjComCode");
		if (StringUtils.isEmpty(sjComCode)) {
			throw new RuntimeException("sjComCode省级机构码不能为空");
		}
		return serviceUrl + "/" + sjComCode + servicePath;
	}*/

	/**
	 * 获取token
	 * 
	 * @param tokenName
	 * @return
	 * @throws Exception
	 */
	/*public static String getToken(String tokenName) {
		try {
			String token = mobileConfig.getString(tokenName);
			return token;
		} catch (Exception e) {
			throw new RuntimeException("mobileConfig.properties配置文件中,找不到key:" + tokenName);
		}
	}*/

	/**
	 * 获取MobileConfig配置文件value
	 * 
	 */
	/*public static String getMobileConfigValue(String key) throws Exception {
		return mobileConfig.getString(key);
	}*/

	/**
	 * 获取MobileParam配置文件value
	 * 
	 */
	/*public static String getMobileParamValue(String key) {
		return mobileParam.getString(key);
	}*/


}
