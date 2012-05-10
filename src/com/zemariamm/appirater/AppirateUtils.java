package com.zemariamm.appirater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class AppirateUtils {
	public static final int SHOW_APPIRATER = 4;
	
	public static final String DB_PREFERENCES="DB_APPIRATER";
		
		
	private static SharedPreferences getPreferences(Context context) {
			int mode = Activity.MODE_PRIVATE;
			return context.getSharedPreferences(DB_PREFERENCES, mode);
	}
	
	private static boolean isNeverShowAppirater(Context context){
		SharedPreferences prefs = AppirateUtils.getPreferences(context);
		return prefs.getBoolean("neverrate", false);
	}
	
	public static void markNeverRate(Context context) {
		SharedPreferences.Editor editor = AppirateUtils.getPreferences(context).edit();
		editor.putBoolean("neverrate", true);
		editor.commit();
	}
	
	private static int getTimesOpened(Context context)
	{
		SharedPreferences prefs = AppirateUtils.getPreferences(context);
		int times = prefs.getInt("times", 0);
		times++;
		//Log.d("Hooligans AppiraterUtils"," ******************************************* Current number: " + times);
		SharedPreferences.Editor editor = AppirateUtils.getPreferences(context).edit();
		editor.putInt("times", times);
		editor.commit();
		return times;
	}
	
	private static int getShowAppirater(Context context) {
		int default_times = AppirateUtils.SHOW_APPIRATER;
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle aBundle=ai.metaData;
			default_times = aBundle.getInt("appirater");
			// no divide by zero!
			if (default_times == 0)
				default_times = AppirateUtils.SHOW_APPIRATER;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return default_times;
	}
	public static boolean shouldAppirater(Context context) {
		int times = AppirateUtils.getTimesOpened(context);
		boolean shouldShow = false;
		if (times % getShowAppirater(context) == 0)
		{
			shouldShow = true;
		}
		if (AppirateUtils.isNeverShowAppirater(context))
		{
			//Log.d("Hooligans AppiraterUtils"," ******************************************* He once clicked NEVER RATE THIS APP or ALREADY Rated it");
			shouldShow =  false;
		}
		return shouldShow;
	}
	
	
	public static void appiraterDialog(final Context context,final AppiraterBase parent) {
		AlertDialog.Builder builderInvite = new AlertDialog.Builder(context);
		//String appName = context.getResources().getString(R.string.app_name);
		String packageName = "";
		String appName = ""	;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			packageName = info.packageName;
			appName = context.getResources().getString(info.applicationInfo.labelRes);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("Appirater","PackageName: " + packageName);
		final String marketLink = "market://details?id=" + packageName;
		String title = context.getString(R.string.appirate_utils_message_before_appname) + " " + appName + context.getString(R.string.appirate_utils_message_after_appname);
		builderInvite.setMessage(title);
		builderInvite.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
					parent.processRate();
					AppirateUtils.markNeverRate(context);
					Uri uri = Uri.parse(marketLink);
					Intent intent = new Intent(Intent.ACTION_VIEW,uri);
					context.startActivity(intent);
					dialog.dismiss();
			}
		}).setNeutralButton("Remind me later", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				parent.processRemindMe();
				dialog.dismiss();
			}
		}).setNegativeButton("No, Thanks", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				AppirateUtils.markNeverRate(context);
				parent.processNever();
				dialog.cancel();
			}
		});
		builderInvite.create().show();
	}
}
