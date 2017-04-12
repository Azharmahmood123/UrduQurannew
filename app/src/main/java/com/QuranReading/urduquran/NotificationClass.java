package com.QuranReading.urduquran;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class NotificationClass extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_activity);

		int surahPos = getIntent().getIntExtra("SURAHDAYNO", -1);
		int ayaNo = getIntent().getIntExtra("AYANO", -1);

		/*if (isSplashActivityRunning() || isSurahActivityRunning()) {
			Intent broadcastIntent = new Intent(Constants.BroadcastActionNotification);
			broadcastIntent.putExtra("NOTIFICATIONOCCURRED", true);
			broadcastIntent.putExtra("SURAHDAYNO", surahPos);
			broadcastIntent.putExtra("AYANO", ayaNo);
			sendBroadcast(broadcastIntent);
			// Toast.makeText(getApplicationContext(), "Broadcaste Sent...", 0).show();
		}
		else {*/
			Intent indexActivity = new Intent(NotificationClass.this, SplashActivity.class);
			indexActivity.putExtra("NOTIFICATIONOCCURRED", true);
			indexActivity.putExtra("SURAHDAYNO", surahPos);
			indexActivity.putExtra("AYANO", ayaNo);
			startActivity(indexActivity);
			// Toast.makeText(getApplicationContext(), ".......New Intent.....", 0).show();
		//}

		finish();
	}

	public boolean isSplashActivityRunning() {
		boolean isActivityFound = false;
		ActivityManager activityManager = (ActivityManager) NotificationClass.this.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);

		for (int i = 0; i < activitys.size(); i++) {
			if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.SplashActivity}")) {
				isActivityFound = true;
				break;
			}

			if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.SplashActivity}")) {
				isActivityFound = true;
				break;
			}
		}
		return isActivityFound;
	}

	public boolean isSurahActivityRunning() {
		boolean isActivityFound = false;
		ActivityManager activityManager = (ActivityManager) NotificationClass.this.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);

		for (int i = 0; i < activitys.size(); i++) {
			if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.SurahActivity}")) {
				isActivityFound = true;
				break;
			}

			if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.SurahActivity}")) {
				isActivityFound = true;
				break;
			}
		}
		return isActivityFound;
	}
	/*
	 * public boolean isDownloadDialogRunning() { boolean isActivityFound = false; ActivityManager activityManager = (ActivityManager)NotificationClass.this.getSystemService (Context.ACTIVITY_SERVICE); List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * for (int i = 0; i < activitys.size(); i++) { if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.DownloadDialog}")) { isActivityFound = true; break; }
	 * 
	 * if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.DownloadDialog}")) { isActivityFound = true; break; } } return isActivityFound; }
	 * 
	 * public boolean isAboutUsRunning() { boolean isActivityFound = false; ActivityManager activityManager = (ActivityManager)NotificationClass.this.getSystemService (Context.ACTIVITY_SERVICE); List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * for (int i = 0; i < activitys.size(); i++) { if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.AboutActivity}")) { isActivityFound = true; break; }
	 * 
	 * if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.AboutActivity}")) { isActivityFound = true; break; } } return isActivityFound; }
	 * 
	 * public boolean isBookmarkRunning() { boolean isActivityFound = false; ActivityManager activityManager = (ActivityManager)NotificationClass.this.getSystemService (Context.ACTIVITY_SERVICE); List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * for (int i = 0; i < activitys.size(); i++) { if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.BookmarksActivity}")) { isActivityFound = true; break; }
	 * 
	 * if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.BookmarksActivity}")) { isActivityFound = true; break; } } return isActivityFound; }
	 * 
	 * public boolean isGotoDialogRunning() { boolean isActivityFound = false; ActivityManager activityManager = (ActivityManager)NotificationClass.this.getSystemService (Context.ACTIVITY_SERVICE); List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * for (int i = 0; i < activitys.size(); i++) { if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.GotoDialog}")) { isActivityFound = true; break; }
	 * 
	 * if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.GotoDialog}")) { isActivityFound = true; break; } } return isActivityFound; }
	 * 
	 * public boolean isStopSignsRunning() { boolean isActivityFound = false; ActivityManager activityManager = (ActivityManager)NotificationClass.this.getSystemService (Context.ACTIVITY_SERVICE); List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * for (int i = 0; i < activitys.size(); i++) { if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.StopSignsActivity}")) { isActivityFound = true; break; }
	 * 
	 * if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.StopSignsActivity}")) { isActivityFound = true; break; } } return isActivityFound; }
	 * 
	 * public boolean isUpdateDialogRunning() { boolean isActivityFound = false; ActivityManager activityManager = (ActivityManager)NotificationClass.this.getSystemService (Context.ACTIVITY_SERVICE); List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * for (int i = 0; i < activitys.size(); i++) { if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.UpdateDialog}")) { isActivityFound = true; break; }
	 * 
	 * if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.UpdateDialog}")) { isActivityFound = true; break; } } return isActivityFound; }
	 * 
	 * public boolean isWordSearchRunning() { boolean isActivityFound = false; ActivityManager activityManager = (ActivityManager)NotificationClass.this.getSystemService (Context.ACTIVITY_SERVICE); List<RunningTaskInfo> activitys = activityManager.getRunningTasks(Integer.MAX_VALUE);
	 * 
	 * for (int i = 0; i < activitys.size(); i++) { if (activitys.get(i).baseActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.WordSearch}")) { isActivityFound = true; break; }
	 * 
	 * if (activitys.get(i).topActivity.toString().equalsIgnoreCase("ComponentInfo{com.QuranReading.urduquran/com.QuranReading.urduquran.WordSearch}")) { isActivityFound = true; break; } } return isActivityFound; }
	 */
}
