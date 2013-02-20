/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zake.Weibo.net;

import java.io.IOException;
import java.net.MalformedURLException;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieSyncManager;

/**
 * Encapsulation main Weibo APIs, Include: 1. getRquestToken , 2.
 * getAccessToken, 3. url request. Used as a single instance class. Implements a
 * weibo api as a synchronized way.
 * 
 * @author ZhangJie (zhangjie2@staff.sina.com.cn)
 */
public class Weibo {

    // public static String SERVER = "http://api.t.sina.com.cn/";
    public static String SERVER = "https://api.weibo.com/2/";
    public static String URL_OAUTH_TOKEN = "http://api.t.sina.com.cn/oauth/request_token";
    public static String URL_AUTHORIZE = "http://api.t.sina.com.cn/oauth/authorize";
    public static String URL_ACCESS_TOKEN = "http://api.t.sina.com.cn/oauth/access_token";
    public static String URL_AUTHENTICATION = "http://api.t.sina.com.cn/oauth/authenticate";

    public static String URL_OAUTH2_ACCESS_TOKEN = "https://api.weibo.com/oauth2/access_token";

    // public static String URL_OAUTH2_ACCESS_AUTHORIZE =
    // "http://t.weibo.com:8093/oauth2/authorize";
    public static String URL_OAUTH2_ACCESS_AUTHORIZE = "https://api.weibo.com/oauth2/authorize";

    private static String APP_KEY = "3296839476";
    private static String APP_SECRET = "7e4c0c53d427722e0986ac036ef8f68d";

    private static Weibo mWeiboInstance = null;
    private Token mAccessToken = null;
/*
    private WeiboDialogListener mAuthDialogListener;

    private static final int DEFAULT_AUTH_ACTIVITY_CODE = 32973;
*/
    public static final String TOKEN = "access_token";
    public static final String EXPIRES = "expires_in";
    public static final String DEFAULT_REDIRECT_URI = "www.sina.com";// 暂不支持
    public static final String DEFAULT_CANCEL_URI = "www.baidu.com";// 暂不支持

    private String mRedirectUrl;

    private Weibo() {
        mRedirectUrl = DEFAULT_REDIRECT_URI;
    }

    public synchronized static Weibo getInstance() {
        if (mWeiboInstance == null) {
            mWeiboInstance = new Weibo();
        }
        return mWeiboInstance;
    }

    // 设置accessToken
    public void setAccessToken(AccessToken token) {
        mAccessToken = token;
    }

    public Token getAccessToken() {
        return this.mAccessToken;
    }

    public void setupConsumerConfig(String consumer_key, String consumer_secret) {
        Weibo.APP_KEY = consumer_key;
        Weibo.APP_SECRET = consumer_secret;
    }

    public static String getAppKey() {
        return Weibo.APP_KEY;
    }

    public static String getAppSecret() {
        return Weibo.APP_SECRET;
    }


    public static String getSERVER() {
        return SERVER;
    }

    public static void setSERVER(String sERVER) {
        SERVER = sERVER;
    }


    public String getRedirectUrl() {
        return mRedirectUrl;
    }

    public void setRedirectUrl(String mRedirectUrl) {
        this.mRedirectUrl = mRedirectUrl;
    }

    /**
     * Requst sina weibo open api by get or post
     * 
     * @param url
     *            Openapi request URL.
     * @param params
     *            http get or post parameters . e.g.
     *            gettimeling?max=max_id&min=min_id max and max_id is a pair of
     *            key and value for params, also the min and min_id
     * @param httpMethod
     *            http verb: e.g. "GET", "POST", "DELETE"
     * @throws IOException
				+ "\nexpires_in = " + expires_in);
     * @throws MalformedURLException
     * @throws WeiboException
     */
    public String request(Context context, String url, WeiboParameters params, String httpMethod,
            Token token) throws WeiboException {
        String rlt = Utility.openUrl(context, url, httpMethod, params, this.mAccessToken);
        return rlt;
    }

   
    public AccessToken getXauthAccessToken(Context context, String app_key, String app_secret,
            String usrname, String password) throws WeiboException {
        Utility.setAuthorization(new XAuthHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("x_auth_username", usrname);
        postParams.add("x_auth_password", password);
        postParams.add("oauth_consumer_key", APP_KEY);
        String rlt = Utility.openUrl(context, Weibo.URL_ACCESS_TOKEN, "POST", postParams, null);
        AccessToken accessToken = new AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }

    /**
     * 获取Oauth2.0的accesstoken
     * 
     * https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID&
     * client_secret=YOUR_CLIENT_SECRET&grant_type=password&redirect_uri=
     * YOUR_REGISTERED_REDIRECT_URI&username=USER_NAME&pasword=PASSWORD
     * 
     * @param context
     * @param app_key
     * @param app_secret
     * @param usrname
     * @param password
     * @return
     * @throws WeiboException
     */
    public Oauth2AccessToken getOauth2AccessToken(Context context, String app_key,
            String app_secret, String usrname, String password) throws WeiboException {
        Utility.setAuthorization(new Oauth2AccessTokenHeader());
        WeiboParameters postParams = new WeiboParameters();
        postParams.add("username", usrname);
        postParams.add("password", password);
        postParams.add("client_id", app_key);
        postParams.add("client_secret", app_secret);
        postParams.add("grant_type", "password");
        String rlt = Utility.openUrl(context, Weibo.URL_OAUTH2_ACCESS_TOKEN, "POST", postParams,
                null);
        Oauth2AccessToken accessToken = new Oauth2AccessToken(rlt);
        this.mAccessToken = accessToken;
        return accessToken;
    }



    /**
     * User-Agent Flow
     * 
     * @param activity
     * 
     * @param listener
     *            授权结果监听器
     */

    private void authorizeCallBack(int requestCode, int resultCode, Intent data) {

    }


    public boolean isSessionValid() {
        if (mAccessToken != null) {
            return (!TextUtils.isEmpty(mAccessToken.getToken()) && (mAccessToken.getExpiresIn() == 0 || (System
                    .currentTimeMillis() < mAccessToken.getExpiresIn())));
        }
        return false;
    }
}
