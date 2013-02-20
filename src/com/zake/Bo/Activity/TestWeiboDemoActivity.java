package com.zake.Bo.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.zake.Bo.R;
import com.zake.Weibo.net.AccessToken;
import com.zake.Weibo.util.AuthoSharePreference;
import com.zake.Weibo.util.MyWeiboManager;
import com.zake.Weibo.util.WeiboConstParam;

/**
 * 
 * @author genius
 * 更多精彩请关注我的CSDN博客:http://blog.csdn.net/geniuseoe2012
 * android开发交流群：200102476
 */
public class TestWeiboDemoActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
   
	public static final  int REQUEST_AUTH_ACTIVITY_CODE = 0x0001; 
	
	
	private TextView toasTextView;
	private Button authorizeBtn;
	private Button loginBtn;
	
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
    	authorizeBtn = (Button) findViewById(R.id.btnAuthrize);
    	authorizeBtn.setOnClickListener(this);
    	
    	loginBtn = (Button) findViewById(R.id.btnLogin);
    	loginBtn.setOnClickListener(this);
    }
    
    public void initData()
    {
    	String token = AuthoSharePreference.getToken(this);
    	mWeiboManager = MyWeiboManager.getInstance(WeiboConstParam.CONSUMER_KEY,
				WeiboConstParam.CONSUMER_SECRET, 
				WeiboConstParam.REDIRECT_URL);	

    	if (token.equals("") == false)
    	{
    		 AccessToken accessToken = new AccessToken(token, WeiboConstParam.CONSUMER_SECRET);     
    	        mWeiboManager.setAccessToaken(accessToken);      
    	    	   
	    		authorizeBtn.setVisibility(View.GONE);
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
			goAuthoActivity();
			break;
		case R.id.btnLogin:
			goPlayActivity();
			break;
			default:
				break;
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
			case RESULT_OK:
			{
				goPlayActivity();
				finish();
			}
			break;

		default:
			break;
		}
	}
	
	public void goAuthoActivity()
	{
		Intent intent = new Intent();
		intent.setClass(TestWeiboDemoActivity.this, WebViewActivity.class);
		startActivityForResult(intent, REQUEST_AUTH_ACTIVITY_CODE);
		
	}
	
	

	public void goPlayActivity()
	{
		Intent intent = new Intent();
		intent.setClass(TestWeiboDemoActivity.this, PlayWeiboActivity.class);
		startActivity(intent);
		finish();
	}
}