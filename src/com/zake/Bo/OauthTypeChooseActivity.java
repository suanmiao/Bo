package com.zake.Bo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zake.Bo.R;
import com.zake.Util.AuthoSharePreference;
import com.zake.Util.AuthConstParam;
import com.zake.Weibo.Activity.PlayWeiboActivity;
import com.zake.Weibo.Util.MyWeiboManager;
import com.zake.Weibo.net.AccessToken;

/**
 * 
 * @author genius
 * 更多精彩请关注我的CSDN博客:http://blog.csdn.net/geniuseoe2012
 * android开发交流群：200102476
 */
public class OauthTypeChooseActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
   
	public static final  int REQUEST_AUTH_ACTIVITY_CODE = 0x0001; 
	
	
	private TextView toasTextView;
	private Button weiboAuthorizeBtn;
	private Button renrenAuthorizeBtn;
	private Button loginBtn;
	

	private final int RESULT_RENREN_OK = 2;
	private final int RESULT_SINA_OK = 1;
	
	private MyWeiboManager mWeiboManager;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initView();

        initData();
    }
    
    
    
    public void initView()
    {
    	toasTextView = (TextView) findViewById(R.id.textToast);
    	weiboAuthorizeBtn = (Button) findViewById(R.id.btnAuthrize);
    	weiboAuthorizeBtn.setOnClickListener(this);
    	
    	renrenAuthorizeBtn = (Button) findViewById(R.id.btnRenrenOauth);
    	renrenAuthorizeBtn.setOnClickListener(this);
    	
    	
    	loginBtn = (Button) findViewById(R.id.btnLogin);
    	loginBtn.setOnClickListener(this);
    	
    	
    }
    
    public void initData()
    {
    	String token = AuthoSharePreference.getSinaToken(this);
    	mWeiboManager = MyWeiboManager.getInstance(AuthConstParam.SINA_CONSUMER_KEY,
				AuthConstParam.SINA_CONSUMER_SECRET, 
				AuthConstParam.SINA_REDIRECT_URL);	

    	if (token.equals("") == false)
    	{
    		 AccessToken accessToken = new AccessToken(token, AuthConstParam.SINA_CONSUMER_SECRET);     
    	        mWeiboManager.setAccessToaken(accessToken);      
    	    	   
    	        weiboAuthorizeBtn.setVisibility(View.GONE);
	    		loginBtn.setVisibility(View.VISIBLE);
	    		toasTextView.setVisibility(View.GONE);	
    	 
    	}
    }
 
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
		case R.id.btnAuthrize:
			goAuthoActivity(1);
			break;
		case R.id.btnLogin:
			goPlayActivity(RESULT_SINA_OK);
			break;
		case R.id.btnRenrenOauth:
			goAuthoActivity(2);
		}
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode)
		{
			case REQUEST_AUTH_ACTIVITY_CODE:
			{
				onResultForAuthActivity(resultCode);
			}
			break;
		}
	}
	
	private void onResultForAuthActivity(int resultCode)
	{
		switch (resultCode) {
			case RESULT_SINA_OK:
			{
				goPlayActivity(RESULT_SINA_OK);
				finish();
			}
			break;
			
			case RESULT_RENREN_OK:
				goPlayActivity(RESULT_RENREN_OK);
				break;
			

		default:
			break;
		}
	}
	
	public void goAuthoActivity(int oauthType)
	{
		Intent intent = new Intent();
		intent.putExtra("oauth_type", oauthType);
		intent.setClass(OauthTypeChooseActivity.this, WebViewActivity.class);
		//从webviewActivity退回后能够获得返回值
		startActivityForResult(intent, REQUEST_AUTH_ACTIVITY_CODE);
		
	}
	
	

	public void goPlayActivity(int playType)
	{
		Intent intent = new Intent();
		intent.putExtra("play_type", playType);
		intent.setClass(OauthTypeChooseActivity.this, PlayWeiboActivity.class);
		startActivity(intent);
		finish();
	}
}