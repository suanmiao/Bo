package com.zake.Bo;

import java.lang.reflect.Type;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zake.Bo.R;
import com.zake.Util.AuthoSharePreference;
import com.zake.Util.AuthConstParam;
import com.zake.Weibo.Activity.MyDebug;
import com.zake.Weibo.Util.IWeiboClientListener;
import com.zake.Weibo.Util.MyWeiboManager;
import com.zake.Weibo.net.AccessToken;
import com.zake.Weibo.net.Utility;
import com.zake.Weibo.net.WeiboException;

public class WebViewActivity extends Activity implements IWeiboClientListener {

	// public final static int

	private WebView mWebView;

	private View progressBar;

	private WeiboWebViewClient mWeiboWebViewClient;

	private MyWeiboManager mWeiboManager;

	private int oauthType = 0;

	// Type 0:nothing
	// type 1:weibo
	// type 2:renren

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

		Intent getIntent = this.getIntent();
		this.oauthType = getIntent.getIntExtra("oauth_type", 0);

		initView();
		initData();

	}

	public void initView() {
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.requestFocus();

		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		progressBar = findViewById(R.id.show_request_progress_bar);

	}

	public void initData() {
		
			mWeiboWebViewClient = new WeiboWebViewClient();
			mWebView.setWebViewClient(mWeiboWebViewClient);

		switch (oauthType) {
		case 0:

			break;

		case 1:

			mWeiboManager = MyWeiboManager.getInstance(
					AuthConstParam.SINA_CONSUMER_KEY,
					AuthConstParam.SINA_CONSUMER_SECRET,
					AuthConstParam.SINA_REDIRECT_URL); 
			String authoUrl = mWeiboManager.getAuthoUrl();

			mWebView.loadUrl(authoUrl);
			break;

		case 2:
			
//			http://graph.renren.com/oauth/login_success.html?error=invalid_request
//				&error_description=You+missed+a+required+parameter%3A+response_type.
//				&error_uri=http%3a%2f%2fgraph.renren.com%2foauth%2ferror%3ferror%3dinvalid_request%26error_description%3dYou%2Bmissed%2Ba%2Brequired%2Bparameter%253A%2Bresponse_type.
//			
//				
//						https://graph.renren.com/oauth/authorize?
//						    response_type=code&
//						    client_id=...&
//						    redirect_uri=http://www.example.com/callback&
//						    display=mobile
			String authUrl = "https://graph.renren.com/oauth/authorize?client_id="+"83d87ef9fa46455da5c607cd7dfe9f91" +
					"&redirect_uri=http://graph.renren.com/oauth/login_success.html" +
					"&scope=read_user_album+read_user_feed" +
					"&response_type=token"; 
			mWebView.loadUrl(authUrl);
			break;
		default:
			break;
		}

	}

	private void showProgress() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
			}
		});

	}

	private void hideProgress() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.INVISIBLE);
			}
		});

	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Auth cancel", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub

		/*
		 * String access_token = values.getString("access_token"); String
		 * expires_in = values.getString("expires_in"); String remind_in =
		 * values.getString("remind_in"); String uid = values.getString("uid");
		 * 
		 * MyDebug.print("onComplete", "access_token = " + access_token +
		 * "\nexpires_in = " + expires_in);
		 * 
		 * AuthoSharePreference.putToken(this, access_token);
		 * AuthoSharePreference.putExpires(this, expires_in);
		 * AuthoSharePreference.putRemind(this, remind_in);
		 * AuthoSharePreference.putUid(this, uid);
		 * 
		 * AccessToken accessToken = new AccessToken(access_token,
		 * WeiboConstParam.CONSUMER_SECRET);
		 * mWeiboManager.setAccessToaken(accessToken);
		 * 
		 * setResult(RESULT_OK); finish();
		 */}

	@Override
	public void onWeiboException(WeiboException e) {
		// TODO Auto-generated method stub
		Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
	}

	private class WeiboWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			MyDebug.print("WeiboWebViewClient",
					"shouldOverrideUrlLoading url = " + url);
			showProgress();
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {

			MyDebug.printErr("WeiboWebViewClient",
					"onReceivedError failingUrl = " + failingUrl);
			super.onReceivedError(view, errorCode, description, failingUrl);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			MyDebug.print("WeiboWebViewClient", "onPageStarted url = ");

			showProgress();
			// if (url.startsWith(mWeiboManager.getRedictUrl())) {
			// handleRedirectUrl(view, url, WebViewActivity.this);
			// view.stopLoading();
			// return;
			// }
//			API Key：83d87ef9fa46455da5c607cd7dfe9f91
//			Secret Key：e2172148b52f48bda8bb284a2b4e33c7
//			
//			https://graph.renren.com/oauth/authorize?
//			    client_id=83d87ef9fa46455da5c607cd7dfe9f91&
//			    redirect_uri=http://graph.renren.com/oauth/login_success.html
			
//			"http://graph.renren.com/oauth/login_success.html
//			#access_token=217604%7C6.5db18ab29448eef482e9842704917bf4.2592000.1364004000-411265499
//			&expires_in=2592539
//			&scope=read_user_album+read_user_feed"
			if (url.contains("access_token") && url.contains("remind_in")) {
				saveSinaOauthData(url);
			}else if(url.contains("access_token")){
				saveRenrenOauthData(url);
			}

			super.onPageStarted(view, url, favicon);

		}
		
		public void saveSinaOauthData(String url){
			// get access token

			String aString = url.substring(url.indexOf("#") + 1);
			String eString = aString.substring(aString.indexOf("&") + 1);
			String rString = eString.substring(eString.indexOf("&") + 1);
			String uString = rString.substring(rString.indexOf("&") + 1);

			/*
			 * Log.e("get the result",
			 * aString+"||"+eString+"||"+rString+"||"+uString);
			 */
			String access_token = aString.substring(13,
					aString.indexOf("&"));
			String expires_in = eString.substring(10, eString.indexOf("&"));
			String remind_in = rString.substring(11, rString.indexOf("&"));
			String uid = uString.substring(4, uString.length());

			/*
			 * Log.e("get the aaaaaa",
			 * access_token+"||"+expires_in+"||"+remind_in+"||"+uid);
			 */

			AuthoSharePreference.putSinaToken(WebViewActivity.this,
					access_token);
			AuthoSharePreference.putSinaExpires(WebViewActivity.this,
					expires_in);
			AuthoSharePreference.putSinaRemind(WebViewActivity.this, remind_in);
			AuthoSharePreference.putSinaUid(WebViewActivity.this, uid);

			AccessToken accessToken = new AccessToken(access_token,
					AuthConstParam.SINA_CONSUMER_SECRET);
			mWeiboManager.setAccessToaken(accessToken);
			WebViewActivity.this.setResult(RESULT_OK);
			WebViewActivity.this.finish();

		}

		public void saveRenrenOauthData(String url){
			// get access token
//			"http://graph.renren.com/oauth/login_success.html
//			#access_token=217604%7C6.5db18ab29448eef482e9842704917bf4.2592000.1364004000-411265499
//			&expires_in=2592539
//			&scope=read_user_album+read_user_feed"
			Log.e("renren", url);

			
			
			String aString = url.substring(url.indexOf("#") + 1);
			String eString = aString.substring(aString.indexOf("&") + 1);
			String rString = eString.substring(eString.indexOf("&") + 1);
			String uString = rString.substring(rString.indexOf("&") + 1);

			/*
			 * Log.e("get the result",
			 * aString+"||"+eString+"||"+rString+"||"+uString);
			 */
			String access_token = aString.substring(13,
					aString.indexOf("&"));
			String expires_in = eString.substring(10, eString.indexOf("&"));
//			String remind_in = rString.substring(11, rString.indexOf("&"));
//			String uid = uString.substring(4, uString.length());

			/*
			 * Log.e("get the aaaaaa",
			 * access_token+"||"+expires_in+"||"+remind_in+"||"+uid);
			 */

			AuthoSharePreference.putRenrenToken(WebViewActivity.this,
					access_token);
			AuthoSharePreference.putRenrenExpires(WebViewActivity.this,
					expires_in);
//			AuthoSharePreference.putSinaRemind(WebViewActivity.this, remind_in);
//			AuthoSharePreference.putSinaUid(WebViewActivity.this, uid);

			WebViewActivity.this.setResult(RESULT_OK);
			WebViewActivity.this.finish();

		}


		@Override
		public void onPageFinished(WebView view, String url) {
			MyDebug.print("WeiboWebViewClient", "onPageFinished url = " + url);
			hideProgress();
			super.onPageFinished(view, url);
		}

		// private boolean handleRedirectUrl(WebView view, String url,
		// IWeiboClientListener listener) {
		// Bundle values = Utility.parseUrl(url);
		// String error = values.getString("error");
		// String error_code = values.getString("error_code");
		//
		// MyDebug.print("handleRedirectUrl", "error = " + error
		// + "\n error_code = " + error_code);
		// if (error == null && error_code == null) {
		// listener.onComplete(values);
		// } else if (error.equals("access_denied")) {
		// listener.onCancel();
		// } else {
		// WeiboException weiboException = new WeiboException(error,
		// Integer.parseInt(error_code));
		// listener.onWeiboException(weiboException);
		// }
		//
		// return false;
		// }
	}

}
