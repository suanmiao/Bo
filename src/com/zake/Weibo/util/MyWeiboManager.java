package com.zake.Weibo.util;

import java.io.IOException;
import java.net.MalformedURLException;

import android.content.Context;
import android.util.Log;

import com.zake.Weibo.net.AccessToken;
import com.zake.Weibo.net.AsyncWeiboRunner;
import com.zake.Weibo.net.AsyncWeiboRunner.RequestListener;
import com.zake.Weibo.net.Oauth2AccessTokenHeader;
import com.zake.Weibo.net.Utility;
import com.zake.Weibo.net.Weibo;
import com.zake.Weibo.net.WeiboException;
import com.zake.Weibo.net.WeiboParameters;
public class MyWeiboManager {

	private Weibo mWeibo;
	
	private static MyWeiboManager mWeiboManager;
	
	
	private String mAppkey;
	private String mRedictUrl;
	
	public static MyWeiboManager getInstance(String appkey, String secret, String redictUrl)
	{
		if (mWeiboManager == null)
		{
			return new MyWeiboManager(appkey, secret, redictUrl);
		}
		
		return mWeiboManager;
	}
	
	public static MyWeiboManager getInstance()
	{
		return mWeiboManager;
	}
	
	
	private MyWeiboManager(String appkey, String secret, String redictUrl)
	{
		mWeibo = Weibo.getInstance();	
		mWeibo.setupConsumerConfig(appkey, secret);	
		mWeibo.setRedirectUrl(redictUrl);
		Utility.setAuthorization(new Oauth2AccessTokenHeader());
		
		mAppkey = appkey;
		mRedictUrl = redictUrl;
		
		Utility.setAuthorization(new Oauth2AccessTokenHeader());
	}
		
	public String getRedictUrl()
	{
		return mRedictUrl;
	}
	
	public String getAppKey()
	{
		return mAppkey;
	}
	
	public boolean isSessionValid()
	{
		return mWeibo.isSessionValid();
	}
	
	// 		 获得OAUTH认证URL地址
	public  String getAuthoUrl()
	{
		WeiboParameters parameters = new WeiboParameters();
		parameters.add("client_id", mWeibo.getAppKey());
	    parameters.add("response_type", "token");
	    parameters.add("redirect_uri", mWeibo.getRedirectUrl());
	    parameters.add("display", "mobile");

	    if (mWeibo.isSessionValid()) {
	        parameters.add(Weibo.TOKEN, mWeibo.getAccessToken().getToken());
	    }
	     
	    String url = Weibo.URL_OAUTH2_ACCESS_AUTHORIZE + "?" + Utility.encodeUrl(parameters);

	    return url;
	}

	public void setAccessToaken(AccessToken accessToken)
	{
		mWeibo.setAccessToken(accessToken);
	}
	
	
	// 发送文本
	 public String update(Context context, String content, RequestListener listener) 
	 throws MalformedURLException, IOException, WeiboException 
	 {
		 Log.i("", "update.............");
		 WeiboParameters bundle = new WeiboParameters();
		 bundle.add("source", getAppKey());
		 bundle.add("status", content);
		
		 
		 String rlt = "";
		 
		 
		 String url = Weibo.SERVER + "statuses/update.json";
		 String aString = Weibo.SERVER ;
		 aString.charAt(9);
		 
		 AsyncWeiboRunner weiboRunner = new AsyncWeiboRunner(mWeibo);
		 weiboRunner.request(context, url, bundle, Utility.HTTPMETHOD_POST, listener);
		 
		 Log.e("updata result", rlt);
		 return rlt;
	 }
	
}
