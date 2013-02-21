package com.zake.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AuthoSharePreference {

	private final static String SINA_SHAREDPREFERENCE_NAME = "sinaAuthoSharePreference";

	private final static String SINA_KEY_TOKEN = "token";

	private final static String SINA_KEY_EXPIRES = "expires_in";

	private final static String SINA_KEY_REMIND = "remind_in";

	private final static String SINA_KEY_UID = "uid";
	

	private final static String RENREN_SHAREDPREFERENCE_NAME = "renrenAuthoSharePreference";

	private final static String RENREN_KEY_TOKEN = "token";

	private final static String RENREN_KEY_EXPIRES = "expires_in";

	private final static String RENREN_KEY_REMIND = "remind_in";

	private final static String RENREN_KEY_UID = "uid";

	public AuthoSharePreference() {

	}

	public static boolean putSinaToken(Context context, String token) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(SINA_KEY_TOKEN, token);

		return editor.commit();
	}

	public static String getSinaToken(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(SINA_KEY_TOKEN, "");
	}

	public static boolean putSinaExpires(Context context, String expires) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(SINA_KEY_EXPIRES, expires);

		return editor.commit();
	}

	public static String getSinaExpires(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(SINA_KEY_EXPIRES, "");
	}

	public static boolean putSinaRemind(Context context, String remind) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(SINA_KEY_REMIND, remind);

		return editor.commit();
	}

	public static String getSinaRemind(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(SINA_KEY_REMIND, "");
	}

	public static boolean putSinaUid(Context context, String uid) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(SINA_KEY_UID, uid);

		return editor.commit();
	}

	public static String getSinaUid(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				SINA_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(SINA_KEY_UID, "");
	}
	
	
	public static boolean putRenrenToken(Context context, String token) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(RENREN_KEY_TOKEN, token);

		return editor.commit();
	}

	public static String getRenrenToken(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(RENREN_KEY_TOKEN, "");
	}

	public static boolean putRenrenExpires(Context context, String expires) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(RENREN_KEY_EXPIRES, expires);

		return editor.commit();
	}

	public static String getRenrenExpires(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(RENREN_KEY_EXPIRES, "");
	}

	public static boolean putRenrenRemind(Context context, String remind) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(RENREN_KEY_REMIND, remind);

		return editor.commit();
	}

	public static String getRenrenRemind(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(RENREN_KEY_REMIND, "");
	}

	public static boolean putRenrenUid(Context context, String uid) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		Editor editor = sharedPreferences.edit();
		editor.putString(RENREN_KEY_UID, uid);

		return editor.commit();
	}

	public static String getRenrenUid(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				RENREN_SHAREDPREFERENCE_NAME, 0);
		return sharedPreferences.getString(RENREN_KEY_UID, "");
	}
}
