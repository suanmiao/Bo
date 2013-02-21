package com.zake.Renren.Util;

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
