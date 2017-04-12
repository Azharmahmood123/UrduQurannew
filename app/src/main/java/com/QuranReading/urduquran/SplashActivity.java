package com.QuranReading.urduquran;

import java.io.IOException;
import java.util.HashMap;

import com.QuranReading.helper.DBManager;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.packageapp.HomeSplashActivity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {
	private Handler mHandler = new Handler();
	private Runnable mRunnable;
	private SettingsSharedPref settngPref;
	private int surah;
	private GlobalClass mGlobal;
	private int currentapiVersion;
	boolean isAppActive=false;
	private boolean isDbCopied=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if(currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		{
			FacebookSdk.sdkInitialize(getApplicationContext());
			// Initialize the SDK before executing any other operations,
			// especially, if you're using Facebook UI elements.
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		mGlobal = ((GlobalClass) getApplicationContext());
		surah = getIntent().getIntExtra("SURAHDAYNO", -1);



	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		settngPref = new SettingsSharedPref(SplashActivity.this);
		if(!settngPref.getDeviceChk())
		{
			TextView txtLink = (TextView) findViewById(R.id.txt_link);
			settngPref.setDevice(txtLink.getText().toString());
		}

		TextView tvAppName = (TextView) findViewById(R.id.tv_app_name);
		tvAppName.setTypeface(mGlobal.faceHeading);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		int height = dm.heightPixels;
		int width = dm.widthPixels;

		if((width == 720 && height == 1280) || (width >= 1080 && height <= 1920) || (width == 540 && height == 960))
		{
			// Samsung S3 && s4
			mGlobal.deviceS3 = true;
		}
		else
		{
			mGlobal.deviceS3 = false;
		}
		mRunnable=new Runnable() {
			@Override
			public void run() {
				if(isAppActive&&isDbCopied){
					Intent indexActivity = new Intent(SplashActivity.this, HomeSplashActivity.class);
					startActivity(indexActivity);
					finish();
				}
			}
		};




	}



	public void cancelNotification() {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) getSystemService(ns);
		nMgr.cancel(GlobalClass.notifID);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		{
			// Logs 'install' and 'app activate' App Events.
			AppEventsLogger.activateApp(this);
		}
		isAppActive=true;
		new ExecuteCopyDatabase().execute();
		mHandler.postDelayed(mRunnable,2000);



	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
		{
			// Logs 'app deactivate' App Event.
			AppEventsLogger.deactivateApp(this);
		}
		isAppActive=false;
		mHandler.removeCallbacks(mRunnable);

	}
	public class ExecuteCopyDatabase extends AsyncTask<Void,Void,Void>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			isDbCopied=true;


		}

		@Override
		protected Void doInBackground(Void... params) {
			copyDatabase();

			return null;
		}
		public void copyDatabase() {
			DBManager dbObj = new DBManager(SplashActivity.this);
			try
			{
				dbObj.createDataBase();
				dbObj.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}



}
