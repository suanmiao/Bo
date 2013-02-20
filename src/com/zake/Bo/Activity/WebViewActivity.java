package com.zake.Bo.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.zake.Bo.R;
import com.zake.Weibo.net.AccessToken;
import com.zake.Weibo.net.Utility;
import com.zake.Weibo.net.WeiboException;
import com.zake.Weibo.util.AuthoSharePreference;
import com.zake.Weibo.util.IWeiboClientListener;
import com.zake.Weibo.util.MyWeiboManager;
import com.zake.Weibo.util.WeiboConstParam;

public class WebViewActivity extends Activity implements IWeiboClientListener {

	// public final static int

	private WebView mWebView;

	private View progressBar;

	private WeiboWebViewClient mWeiboWebViewClient;

	private MyWeiboManager mWeiboManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);

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

		CookieSyncManager.createInstance(this);

		mWeiboManager = MyWeiboManager.getInstance(
				WeiboConstParam.CONSUMER_KEY, WeiboConstParam.CONSUMER_SECRET,
				WeiboConstParam.REDIRECT_URL);

		String authoUrl = mWeiboManager.getAuthoUrl();

		mWebView.loadUrl(authoUrl);

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

		String access_token = values.getString("access_token");
		String expires_in = values.getString("expires_in");
		String remind_in = values.getString("remind_in");
		String uid = values.getString("uid");

		MyDebug.print("onComplete", "access_token = " + access_token
				+ "\nexpires_in = " + expires_in);

		AuthoSharePreference.putToken(this, access_token);
		AuthoSharePreference.putExpires(this, expires_in);
		AuthoSharePreference.putRemind(this, remind_in);
		AuthoSharePreference.putUid(this, uid);

		AccessToken accessToken = new AccessToken(access_token,
				WeiboConstParam.CONSUMER_SECRET);
		mWeiboManager.setAccessToaken(accessToken);

		setResult(RESULT_OK);
		finish();
	}

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
			if (url.startsWith(mWeiboManager.getRedictUrl())) {
				handleRedirectUrl(view, url, WebViewActivity.this);
				view.stopLoading();
				return;
			}

			if (url.contains("access_token") && url.contains("remind_in")) {
				// get access token
				
						
						String aString = url.substring(url.indexOf("#")+1);
				String eString = aString.substring(aString.indexOf("&")+1);
				String rString = eString.substring(eString.indexOf("&")+1);
				String uString = rString.substring(rString.indexOf("&")+1);

				/*Log.e("get the result", aString+"||"+eString+"||"+rString+"||"+uString);
				*/
				String access_token = aString.substring(13, aString.indexOf("&"));
				String expires_in = eString.substring(10,eString.indexOf("&"));
				String remind_in = rString.substring(11,rString.indexOf("&"));
				String uid = uString.substring(4,uString.length());

				/*Log.e("get the aaaaaa", access_token+"||"+expires_in+"||"+remind_in+"||"+uid);
	*/
				
				AuthoSharePreference.putToken(WebViewActivity.this, access_token);
				AuthoSharePreference.putExpires(WebViewActivity.this, expires_in);
				AuthoSharePreference.putRemind(WebViewActivity.this, remind_in);
				AuthoSharePreference.putUid(WebViewActivity.this, uid);

				AccessToken accessToken = new AccessToken(access_token,
						WeiboConstParam.CONSUMER_SECRET);
				mWeiboManager.setAccessToaken(accessToken);

			}

			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			MyDebug.print("WeiboWebViewClient", "onPageFinished url = " + url);
			hideProgress();
			super.onPageFinished(view, url);
		}

		private boolean handleRedirectUrl(WebView view, String url,
				IWeiboClientListener listener) {
			Bundle values = Utility.parseUrl(url);
			String error = values.getString("error");
			String error_code = values.getString("error_code");

			MyDebug.print("handleRedirectUrl", "error = " + error
					+ "\n error_code = " + error_code);
			if (error == null && error_code == null) {
				listener.onComplete(values);
			} else if (error.equals("access_denied")) {
				listener.onCancel();
			} else {
				WeiboException weiboException = new WeiboException(error,
						Integer.parseInt(error_code));
				listener.onWeiboException(weiboException);
			}

			return false;
		}
	}

}
