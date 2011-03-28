package com.zemariamm.appirater;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppiraterPreferences {

	public static final String DB_PREFERENCES="DB_APPIRATER";
	
	public static SharedPreferences getPreferences(Context context) {
		int mode = Activity.MODE_PRIVATE;
		return context.getSharedPreferences(DB_PREFERENCES, mode);
	}
	
	public static boolean isDataNew(Context context) {
		SharedPreferences prefs = getPreferences(context);
		return prefs.getBoolean("newdata", true);
	}
	
	public static void markOldData(Context context) {
		SharedPreferences.Editor editor = getPreferences(context).edit();
		editor.putBoolean("newdata", false);
		editor.commit();
	}
	public static void markNewData(Context context) {
		SharedPreferences.Editor editor = getPreferences(context).edit();
		editor.putBoolean("newdata", true);
		editor.commit();
	}
	public static long havePassedSeconds(Context context)
	{
		SharedPreferences prefs = getPreferences(context);
		long lasttime = prefs.getLong("lasttime", 0);
		long now = System.currentTimeMillis();
		long answer = ( now - lasttime ) / 1000;
		return answer;
	}

}
