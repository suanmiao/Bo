package com.zake.Weibo.Activity;


import android.util.Log;

public class MyDebug {

	
	public static void print(String tag, String info)
	{
		String str = "<====" + tag + "====>";
		
		Log.e(str, info);
		
	}
	
	public static void printErr(String tag, String info)
	{
		String str = "<====" + tag + "====>";
		
		Log.e(str, info);
		
	}
}
