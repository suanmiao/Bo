package com.zake.Weibo.Activity;

import java.io.IOException;

import java.net.MalformedURLException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zake.Bo.R;
import com.zake.Bo.OauthTypeChooseActivity;
import com.zake.Renren.Util.RenrenManager;
import com.zake.Util.AuthoSharePreference;
import com.zake.Util.AuthConstParam;
import com.zake.Weibo.Util.MyWeiboManager;
import com.zake.Weibo.net.AsyncWeiboRunner.RequestListener;
import com.zake.Weibo.net.WeiboException;

public class PlayWeiboActivity extends Activity implements OnClickListener,
		TextWatcher, RequestListener {

	private ProgressDialog progressDialog = null;

	private Button mCloseBtn;
	private Button mSendBtn;
	private TextView mTextNum;
	private EditText mEditText;

	private MyWeiboManager mWeiboManager;
	private RenrenManager mRenrenManager;

	public static final int WEIBO_MAX_LENGTH = 140;

	private String weiboContentString = "";

	private int playType = 0;

	private final int PLAY_RENREN = 2;
	private final int PLAY_SINA = 1;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.play_weibo_layout);

		playType = this.getIntent().getIntExtra("play_type", 0);

		initView();

		initData();

	}

	public void initView() {

		mCloseBtn = (Button) this.findViewById(R.id.btnClose);
		mCloseBtn.setOnClickListener(this);

		mSendBtn = (Button) this.findViewById(R.id.btnSend);
		mSendBtn.setOnClickListener(this);

		LinearLayout total = (LinearLayout) this
				.findViewById(R.id.ll_text_limit_unit);
		total.setOnClickListener(this);

		mTextNum = (TextView) this.findViewById(R.id.tv_text_limit);

		mEditText = (EditText) this.findViewById(R.id.etEdit);
		mEditText.addTextChangedListener(this);

	}

	public void initData() {
		mWeiboManager = MyWeiboManager.getInstance();
		if (mWeiboManager == null) {
			mWeiboManager = MyWeiboManager.getInstance(
					AuthConstParam.SINA_CONSUMER_KEY,
					AuthConstParam.SINA_CONSUMER_SECRET,
					AuthConstParam.SINA_REDIRECT_URL);
		}

		mRenrenManager = RenrenManager.getInstance();
		if (mRenrenManager == null) {
			mRenrenManager = RenrenManager.getInstance(
					AuthConstParam.RENREN_CONSUMER_KEY,
					AuthConstParam.RENREN_CONSUMER_SECRET,
					AuthConstParam.RENREN_REDIRECT_URL);
		}
	}

	public void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this, "分享微博", "Sending...");

		}
	}

	public void dismissProgressDialog() {

		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public void exit() {
		dismissProgressDialog();

		Handler mHandler = new Handler();
		mHandler.postDelayed(new exitRunnable(), 1000);
	}

	class exitRunnable implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.exit(0);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnClose:
			close();
			break;
		case R.id.btnSend:
			send();
			break;
		case R.id.ll_text_limit_unit:
			limit();
			break;
		default:
			break;
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		String mText = mEditText.getText().toString();
		String mStr;
		int len = mText.length();
		if (len <= WEIBO_MAX_LENGTH) {
			len = WEIBO_MAX_LENGTH - len;
			mTextNum.setTextColor(R.color.text_num_gray);
			if (!mSendBtn.isEnabled())
				mSendBtn.setEnabled(true);
		} else {
			len = len - WEIBO_MAX_LENGTH;
			mTextNum.setTextColor(Color.RED);
			if (mSendBtn.isEnabled())
				mSendBtn.setEnabled(false);
		}
		mTextNum.setText(String.valueOf(len));
	}

	public void close() {
		Toast.makeText(this, "exit...", Toast.LENGTH_SHORT).show();
		exit();
	}

	public void send()
	{
		Log.i("", "send.............");
		
		 weiboContentString = mEditText.getText().toString();
		 if (weiboContentString.length() == 0)
		 {
			 Toast.makeText(this, "content can't be empty", Toast.LENGTH_SHORT).show();
			 return ;
		 }
		 
		 
		
		
		 
		 switch (playType) {
		case PLAY_RENREN:

			AuthoSharePreference mAuthoSharePreference = new AuthoSharePreference();
			
			mRenrenManager.updateStatu(mAuthoSharePreference.getRenrenToken(getApplicationContext()));
			break;

		case PLAY_SINA:
			 
			 if (!mWeiboManager.isSessionValid())
			 {
				 Toast.makeText(this, this.getString(R.string.please_login), Toast.LENGTH_SHORT).show();
				 AuthoSharePreference.putSinaToken(this, "");
				 goLoginActivity();
				 return ;
			 }

	         try {     
	             mWeiboManager.update(this, weiboContentString, this);
	             showProgressDialog();
	         } catch (MalformedURLException e) {
	             e.printStackTrace();
	         } catch (IOException e) {
	             e.printStackTrace();
	         } catch (WeiboException e) {
	             e.printStackTrace();
	             Log.e("", "e.errcode = " + e.getStatusCode());
	         }	
	
			break;
			
		} 
		 
		 
		 
		         
	}

	private void goLoginActivity() {
		Intent intent = new Intent();
		intent.setClass(this, OauthTypeChooseActivity.class);
		startActivity(intent);
		finish();
	}

	public void limit() {
		Dialog dialog = new AlertDialog.Builder(this)
				.setTitle(R.string.attention)
				.setMessage(R.string.delete_all)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								mEditText.setText("");
							}
						}).setNegativeButton(R.string.cancel, null).create();

		dialog.show();
	}

	@Override
	public void onComplete(String response) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(PlayWeiboActivity.this, R.string.send_sucess,
						Toast.LENGTH_LONG).show();

				dismissProgressDialog();

			}
		});

	}

	@Override
	public void onIOException(IOException e) {
		// TODO Auto-generated method stub

		runOnUiThread(new Runnable() {

			@Override
			public void run() {

				Toast.makeText(PlayWeiboActivity.this, "onIOException",
						Toast.LENGTH_LONG).show();

				dismissProgressDialog();

			}
		});
	}

	@Override
	public void onError(final WeiboException e) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(
						PlayWeiboActivity.this,
						String.format(
								PlayWeiboActivity.this
										.getString(R.string.send_failed)
										+ ":%s", e.getMessage()),
						Toast.LENGTH_LONG).show();

				Log.e("error", e + "");

				dismissProgressDialog();
			}
		});

	}

}
