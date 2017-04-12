package com.QuranReading.urduquran;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.QuranReading.alarmReceiver.ServiceAlarmReceiver;
import com.QuranReading.helper.DBManager;
import com.QuranReading.helper.UnZipUtil;
import com.QuranReading.listeners.UnzipListener;
import com.QuranReading.sharedPreference.SettingsSharedPref;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class ServiceClass extends Service implements UnzipListener {
	private int alarmId = 1234;
	private boolean zipChk = false;
	private long downloadReference;
	public static DownloadManager downloadManager;

	@Override
	public void onCreate() {
		super.onCreate();

		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("Service", "onStartCommand");

		// downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

		IntentFilter surahDownloadFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, surahDownloadFilter);

		IntentFilter newDownloadFilter = new IntentFilter(Constants.BroadcastActionDownload);
		registerReceiver(newSurahDownload, newDownloadFilter);

		try
		{
			chkServiceRunning();

			if(intent != null)
			{
				extractValues(intent);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return START_STICKY;
	}

	public void chkServiceRunning() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 5);

		Intent intent = new Intent(getApplicationContext(), ServiceAlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 10000, pendingIntent);
	}

	public void cancelChkServiceRunning() {
		Intent intent = new Intent(getApplicationContext(), ServiceAlarmReceiver.class);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}

	public void extractValues(Intent intent) {

		int surahPosition, reciter;
		String url = "", audioName = "", fullName = "", tempName = "temp_";

		fullName = intent.getStringExtra("NAME");
		surahPosition = intent.getIntExtra("POSITION", -1);
		audioName = intent.getStringExtra("ANAME");
		reciter = intent.getIntExtra("RECITER", -1);

		tempName = tempName + audioName;
		// if reciter is abdul basit
		if(reciter == 0)
		{
			url = getLink(DBManager.RECITER_BASIT) + audioName;
		}

		// if reciter is alfasay
		else if(reciter == 1)
		{
			url = getLink(DBManager.RECITER_ALAFSAY) + audioName;
		}
		// if reciter is sudais
		else if(reciter == 2)
		{
			url = getLink(DBManager.RECITER_SUDAIS) + audioName;
		}

		if(!url.equals(""))
		{
			Log.i("Audio Name", fullName);
			Log.i("Temp Audio Name", tempName);
			Log.i("Audio Url", url);

			downloadSurah(surahPosition, fullName, audioName, tempName, url, reciter);
		}
	}

	private String getLink(String reciter) {

		DBManager db = new DBManager(this);
		db.open();
		String url;
		TimeZone tz = TimeZone.getDefault();
		long timeNow = new Date().getTime();
		double timezone = (double) ((tz.getOffset(timeNow) / 1000) / 60) / 60;
		if(timezone >= 4.0 && timezone <= 13.0) // Asia
		{
			url = db.getUrl(DBManager.FLD_ASIA, reciter);
		}
		else if(timezone >= -13.0 && timezone <= -4.0) // US
		{
			url = db.getUrl(DBManager.FLD_US, reciter);
		}
		else if(timezone >= -3.5 && timezone <= 3.5) // EU
		{
			url = db.getUrl(DBManager.FLD_EU, reciter);
		}
		else
		{
			url = db.getUrl(DBManager.FLD_EU, reciter);
		}

		db.close();

		return url;
	}

	public void downloadSurah(int pos, String fullName, String name, String tempName, String url, int reciter) {
		DBManager dbObj = new DBManager(getApplicationContext());
		dbObj.open();

		// Uri Download_Uri = Uri.parse("https://www.dropbox.com/s/wlzjgly26p6eucf/surah_rehman_0.mp3?dl=1");
		Uri Download_Uri = Uri.parse(url);
		DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

		// Restrict the types of networks over which this download may proceed.
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

		// Set whether this download may proceed over a roaming connection.
		request.setAllowedOverRoaming(false);

		// Set the title of this download, to be displayed in notifications (if enabled).
		request.setTitle("Downloading . . . ");

		// Set a description of this download, to be displayed in notifications (if enabled)
		request.setDescription(fullName);

		// Set the local destination for the downloaded file to a path within the application's external files directory
		request.setDestinationInExternalPublicDir(Constants.rootFolderName, tempName);

		// Enqueue a new download and same the referenceId
		try
		{
			downloadReference = downloadManager.enqueue(request);

			long refId = downloadReference;

			Log.i("New Download", String.valueOf(refId));
			dbObj.addDownload((int) refId, pos, name, tempName);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			sendDataToActivity(false, name, fullName, pos, reciter);
			Cursor c = dbObj.getAllDownloads();

			if(!c.moveToFirst() && zipChk == false)
			{
				Log.e("On Service", "stopSelf");
				stopSelf();
			}
		}

		dbObj.close();
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int index = -1, position = -1;
			String name = "", tempName = "";

			// check if the broadcast message is for our Enqueued download
			long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

			DBManager dbObj = new DBManager(getApplicationContext());
			dbObj.open();

			Cursor c = dbObj.getAllDownloads();

			if(c.moveToFirst())
			{
				do
				{
					int id = c.getInt(c.getColumnIndex(DBManager.FLD_DOWNLOAD_ID));

					if(id == (int) referenceId)
					{
						index = c.getInt(c.getColumnIndex(DBManager.FLD_ID));
						position = c.getInt(c.getColumnIndex(DBManager.FLD_SURAH_NO));
						name = c.getString(c.getColumnIndex(DBManager.FLD_SURAH_NAME));
						tempName = c.getString(c.getColumnIndex(DBManager.FLD_TEMP_NAME));

						break;
					}

				}
				while (c.moveToNext());
			}

			c.close();

			if(index != -1)
			{
				if(name.contains(".mp3"))
				{

					FileUtils.renameAudioFile(tempName, name);

					Log.e("On Service", "Downloading Complete");

					sendDataToActivity(true, "surah", name, position, -1);
				}
				else
				{
					zipChk = true;

					UnZipUtil unzip = new UnZipUtil();
					unzip.setReciter(position);
					unzip.setListener(ServiceClass.this);
					unzip.execute();
				}
			}

			dbObj.deleteOneDownload(DBManager.FLD_ID, index);

			c = dbObj.getAllDownloads();

			if(!c.moveToFirst() && zipChk == false)
			{
				Log.e("On Service", "stopSelf");
				stopSelf();
			}

			c.close();
			dbObj.close();
		}
	};

	private BroadcastReceiver newSurahDownload = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null)
			{
				extractValues(intent);
			}
		}
	};

	@Override
	public void onDestroy() {
		cancelChkServiceRunning();

		DBManager dbObj = new DBManager(getApplicationContext());
		dbObj.open();

		Cursor c = dbObj.getAllDownloads();

		if(c.moveToFirst())
		{
			do
			{
				int id = c.getInt(c.getColumnIndex(DBManager.FLD_DOWNLOAD_ID));
				int pos = c.getInt(c.getColumnIndex(DBManager.FLD_SURAH_NO));
				String tempName = c.getString(c.getColumnIndex(DBManager.FLD_TEMP_NAME));

				downloadManager.remove(id);
				FileUtils.checkOneAudioFile(getApplicationContext(), tempName, pos);

			}
			while (c.moveToNext());
		}

		c.close();
		dbObj.close();

		unregisterReceiver(newSurahDownload);
		unregisterReceiver(downloadReceiver);

		FileUtils.deleteTempFiles();

		super.onDestroy();

		Log.d("Service", "onDestroy");
	}

	@Override
	public void unzipStatus(boolean status, int reciter) {
		// TODO Auto-generated method stub
		((GlobalClass) getApplication()).downloadQuran = false;

		sendDataToActivity(status, "zip", "", -1, reciter);

		DBManager dbObj = new DBManager(getApplicationContext());
		dbObj.open();

		Cursor c = dbObj.getAllDownloads();

		if(!c.moveToFirst())
		{
			Log.e("On unzipStatus", "stopSelf");
			stopSelf();
		}

		c.close();
		dbObj.close();

		zipChk = false;
	}

	public void sendDataToActivity(boolean status, String from, String name, int position, int reciter) {
		Intent broadcastIntent = new Intent(Constants.BroadcastActionComplete);
		broadcastIntent.putExtra("STATUS", status);
		broadcastIntent.putExtra("FROM", from);
		broadcastIntent.putExtra("NAME", name);
		broadcastIntent.putExtra("POSITION", position);
		sendBroadcast(broadcastIntent);
	}
}