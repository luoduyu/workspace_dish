package com.amt.wechat.domain.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


	public class ZXHttpClient {
	private static Logger logger = LoggerFactory.getLogger(ZXHttpClient.class);
	

	public JSONObject post(String url, JSONObject json) throws Exception{
		/*JSONObject j = null;
		HttpEntity entity = new ByteArrayEntity(json.toString().getBytes("UTF-8"));
		String result = post(url, entity);
		if(result != null && !"".equals(result)){
			j = JSONObject.parseObject(result);
		}
		return j;*/
		JSONObject j = null;
	    Map<String,String> map = JSON.parseObject(json.toJSONString(), Map.class);
	    String result = this.post(url, map);
	    if(result != null && !"".equals(result)){
	      j = JSONObject.parseObject(result);
	    }
	    return j;
	}
	
	public String postXML(String url, String xmlData) throws Exception{
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("xml", xmlData));
		HttpEntity entity = new UrlEncodedFormEntity(pairs, "UTF-8");
		return post(url, entity);
	}
	
	public static String postXMLStr(String url, String xmlData) throws Exception{
		StringEntity entity = new StringEntity(xmlData,"UTF-8");
		entity.setContentType("text/xml");
		entity.setContentEncoding("UTF-8");
		return post(url, entity);
	}

	public String post(String url, Map<String, String> params) throws Exception{
		
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		if(params != null && !params.isEmpty()){
			for (Map.Entry<String, String> entry : params.entrySet()) {
				pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		
		HttpEntity entity = new UrlEncodedFormEntity(pairs, "UTF-8");
		return post(url, entity);
	}
	
	private static String post(String url,HttpEntity entity){
		String result = null;
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try {
			post.setConfig(RequestConfig.custom().setConnectTimeout(3000).setConnectionRequestTimeout(3000).setSocketTimeout(3000).build());
			post.setEntity(entity);
			response = httpClient.execute(post);
			if (response != null && response.getStatusLine().getStatusCode() == 200) {
				entity = response.getEntity();
				result = entityToString(entity);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			logger.error(e.getMessage(),e);
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		} finally {
			try {
				httpClient.close();
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(),e);
			}

		}
		return result;
	}

	private static String entityToString(HttpEntity entity) throws IOException {
		String result = null;
		if (entity != null) {
			long lenth = entity.getContentLength();
			if (lenth != -1 && lenth < 2048) {
				result = EntityUtils.toString(entity, "UTF-8");
			} else {
				InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
				CharArrayBuffer buffer = new CharArrayBuffer(2048);
				char[] tmp = new char[1024];
				int l;
				while ((l = reader1.read(tmp)) != -1) {
					buffer.append(tmp, 0, l);
				}
				result = buffer.toString();
			}
		}
		return result;
	}
	
	/**
     * 发送HttpPost带参请求，jsonString
     * @param url
     * @param requestJson
     * @return
     */
    public String postJsonToJson(String url,String requestJson){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建http POST请求
        HttpPost httpPost = new HttpPost(url);
        if (requestJson != null) {
            // 构造一个请求实体
            StringEntity stringEntity = new StringEntity(requestJson, ContentType.APPLICATION_JSON);
            // 将请求实体设置到httpPost对象中
            httpPost.setEntity(stringEntity);
        }
        CloseableHttpResponse response = null;
        try {
            // 执行请求
            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String result = null;
            if(entity != null){
                result = EntityUtils.toString(entity, "UTF-8");
            }
            return result;
        }catch (IOException e) {
            e.printStackTrace();
        }catch (ParseException  e) {
			logger.error(e.getMessage(),e);
        } finally {
            try{
                if (response != null) {
                    response.close();
                }
            }catch (IOException e) {
				logger.error(e.getMessage(),e);
            }
        }
        return "";
    }

	public static String get(String url) throws IOException {
		CloseableHttpClient httpCilent = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setConfig(RequestConfig.custom().setConnectTimeout(3000).setConnectionRequestTimeout(3000).setSocketTimeout(3000).build());
			HttpResponse httpResponse =  httpCilent.execute(httpGet);
			String resultStr =  entityToString(httpResponse.getEntity());
			logger.info("httpclient response result : {}",resultStr);
			return resultStr;
		}catch (IOException e) {
			logger.error("httpclient request exception , errmsg : {}",e.getMessage(),e);
			throw e;
		} finally {
			try {
				httpCilent.close();//释放资源
			} catch (IOException e) {
				logger.error("httpclient request exception , errmsg : {}",e.getMessage(),e);
				throw e;
			}
		}

	}
	
}
