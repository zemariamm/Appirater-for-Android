package com.zemariamm.appirater;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class AppiraterBase extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		checkAppirater();
	}
	
	protected boolean shoulAppiraterdRun() {
		return true;
	}
	
	public void processNever() {
		Log.d("Hooligans Appirater","Never");
	}
	
	public void processRate() {
		Log.d("Hooligans Appirater","Rate");
	}
	
	public void processRemindMe() {
		Log.d("Hooligans Appirater","Remind Me");
	}
	
	protected void checkAppirater() {
		if ( AppirateUtils.shouldAppirater(this) && this.shoulAppiraterdRun())
		{
			AppirateUtils.appiraterDialog(this,this);
		}
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		checkAppirater();
	}
	
}
