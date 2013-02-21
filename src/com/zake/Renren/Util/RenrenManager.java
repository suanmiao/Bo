package com.zake.Renren.Util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class RenrenManager {

	private static RenrenManager mRenrenManager;

	private String mAppkey;
	private String mSecret;
	private String mRedictUrl;

	public static RenrenManager getInstance(String appkey, String secret,
			String redictUrl) {
		if (mRenrenManager == null) {
			return new RenrenManager(appkey, secret, redictUrl);
		}

		return mRenrenManager;
	}

	public static RenrenManager getInstance() {
		return mRenrenManager;
	}

	private RenrenManager(String appkey, String secret, String redictUrl) {

		mAppkey = appkey;
		mSecret = secret;
		mRedictUrl = redictUrl;

	}

	public String updateStatu(String accessToken) {
		String requestMethod = "status.set"; // ÇëÇóµÄœÓ¿Ú
		String v = "1.0"; // °æ±ŸºÅ

		String url = "https://api.renren.com/restserver.do";

		// Ç©Ãû
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("method", requestMethod);
		paramMap.put("v", v);
		paramMap.put("status", "Test for Renren API");
		paramMap.put("access_token", accessToken);
		String signature = this.getSignature(paramMap, mSecret);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
	
		params.add(new BasicNameValuePair("v", v));
		params.add(new BasicNameValuePair("access_token", accessToken));
		//params.add(new BasicNameValuePair("sig", signature));

		params.add(new BasicNameValuePair("method", requestMethod));
		params.add(new BasicNameValuePair("status", "Test for Renren API"));

		String result = null;
		try {
			HttpPost httpPost = new HttpPost(url);
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			httpPost.setEntity(httpEntity);
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse httpResponse = httpClient.execute(httpPost);

			result = EntityUtils.toString(httpResponse.getEntity());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

		}

		Log.d("result", result);

		return result;
	}
	
	
	public static String getSignature(Map<String, String> paramMap, String secret) {
		// ²ÎÊýžñÊœ»¯
		List<String> paramList = new ArrayList<String>(paramMap.size());
		for (Map.Entry<String, String> param: paramMap.entrySet()) {
			paramList.add(param.getKey() + "=" + param.getValue());
		}
		
		// ÆŽœÓ³É×Ö·ûŽ®
		Collections.sort(paramList);
		StringBuffer buffer = new StringBuffer();
		for (String param : paramList) {
			buffer.append(param);
		}
		buffer.append(secret);
		
		Log.d("signature", getMD5(buffer));
		
		
		return getMD5(buffer);
	}
	
	// »ñÈ¡MD5	
    public static String getMD5(String s) {  
        try {  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
  
            byte[] byteArray = s.getBytes("ISO-8859-1");  
            byte[] md5Bytes = md5.digest(byteArray);  
  
            StringBuffer hexValue = new StringBuffer();  
  
            for (int i = 0, n = md5Bytes.length; i < n; i++) {  
                int val = ((int) md5Bytes[i]) & 0xff;  
                if (val < 16)  
                    hexValue.append("0");  
                hexValue.append(Integer.toHexString(val));  
            }  
  
            return hexValue.toString();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }
    
    public static String getMD5(StringBuffer buffer) {
    	try {
		    java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		    StringBuffer result = new StringBuffer();
		    try {
		        for (byte b : md.digest(buffer.toString().getBytes("UTF-8"))) {
		            result.append(Integer.toHexString((b & 0xf0) >>> 4));
		            result.append(Integer.toHexString(b & 0x0f));
		        }
		    } catch (UnsupportedEncodingException e) {
		        for (byte b : md.digest(buffer.toString().getBytes())) {
		            result.append(Integer.toHexString((b & 0xf0) >>> 4));
		            result.append(Integer.toHexString(b & 0x0f));
		        }
		    }
		    return result.toString();
		} catch (java.security.NoSuchAlgorithmException ex) {
		    ex.printStackTrace();
		}	
    	return null;
    }

	public String getRedictUrl() {
		return mRedictUrl;
	}

	public String getAppKey() {
		return mAppkey;
	}

	public String getSecret() {
		return mSecret;
	}
}
