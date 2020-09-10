package org.linlinjava.litemall.db.util;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.*;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HttpUtil {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private OkHttpClient client = new OkHttpClient();

	public String post(String url, String json) throws IOException {
		  RequestBody body = RequestBody.create(JSON, json);
		  Request request = new Request.Builder()
		      .url(url)
		      .post(body)
		      .build();
		  Response response = client.newCall(request).execute();
		  return response.body().string();
	}

	public static String postJsonString(String url, String json) {
		HttpUtil httpUtil = new HttpUtil();
		String result = "";
		try {
			result = httpUtil.post(url, json);
			System.out.print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static <T> String postJsonObject(String url,T t) {
//		Gson gson = new Gson();
//		String jsonData = gson.toJson(t);
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String jsonData = gson.toJson(t);

		HttpUtil httpUtil = new HttpUtil();
		String result = "";
		try {
			result = httpUtil.post(url, jsonData);
			System.out.print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 基于HttpClient 4.3的通用POST方法
	 *
	 * @param url       提交的URL
	 * @param paramsMap 提交<参数，值>Map
	 * @return 提交响应
	 */

	public static String postMap(String url, Map<String, String> paramsMap) {
		CloseableHttpClient client = HttpClients.createDefault();
		String responseText = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost method = new HttpPost(url);
			if (paramsMap != null) {
				List<NameValuePair> paramList = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> param : paramsMap.entrySet()) {
					NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
					paramList.add(pair);
				}
				method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
			}
			response = client.execute(method);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				responseText = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseText;
	}


	public static String postMapObject(String url, Map<String, Object> paramsMap) {
		JSONArray jArray = new JSONArray();
		jArray.add(paramsMap);
		String str = jArray.toString();
		String result = "";
		HttpUtil httpUtil = new HttpUtil();
		try {
			result = httpUtil.post(url, str);
			System.out.print(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
