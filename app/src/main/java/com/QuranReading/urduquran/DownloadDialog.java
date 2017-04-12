package com.QuranReading.urduquran;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.DownloadManager.Query;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.QuranReading.helper.DBManager;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;

public class DownloadDialog extends AppCompatActivity {

	int position, reciter;
	String name = "", audioName = "";
	// Button btnSurah, btnQuran, btnCancel, btnoky, btnback;
	// LinearLayout btnLayout;
	// TextView tvBody, header;
	String packageName = "com.android.providers.downloads";
	double sizeRequired;

	String msg = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.transperant_layout);
		name = getIntent().getStringExtra("SURAHNAME");
		position = getIntent().getIntExtra("POSITION", -1);
		audioName = getIntent().getStringExtra("ANAME");
		reciter = getIntent().getIntExtra("RECITER", -1);

		if (checkDownloadStatus())
		{
			if (msg != null)
			{
				showDownloadProcessingDialog();

			}
			else
			{
				msg = getString(R.string.download_body_surah) + " " + name + " audio file?";
				showDownloadDialog();
				sendAnalyticsData();
			}
		}



		// //////////////////////////Registering Notification Receiver ///////////////////
		IntentFilter dailySurah = new IntentFilter(Constants.BroadcastActionNotification);
		registerReceiver(dailySurahNotification, dailySurah);
	}

	private void sendAnalyticsData() {
		AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloads","Surah_Downloaded");
	}

	public boolean checkDownloadStatus() {

		msg = null;

		if (!checkDownloadManagerState())
		{
			return false;
		}

		HashMap<String, Boolean> chkDownload = new HashMap<String, Boolean>();
		boolean chkDownloadStatus = false;
		DBManager dbObj = new DBManager(DownloadDialog.this);
		dbObj.open();

		Cursor c = dbObj.getAllDownloads();

		if (c.moveToFirst())
		{
			do
			{
				int refId = c.getInt(c.getColumnIndex(DBManager.FLD_DOWNLOAD_ID));
				int surahPos = c.getInt(c.getColumnIndex(DBManager.FLD_SURAH_NO));
				String name = c.getString(c.getColumnIndex(DBManager.FLD_SURAH_NAME));
				String tempName = c.getString(c.getColumnIndex(DBManager.FLD_TEMP_NAME));

				if (name.equals(audioName) || name.equals(Constants.QuranFile2))
				{
					try
					{
						Query myDownloadQuery = new Query();
						// set the query filter to our previously Enqueued download
						myDownloadQuery.setFilterById(refId);

						// Query the download manager about downloads that have been requested.
						Cursor cursor = ServiceClass.downloadManager.query(myDownloadQuery);

						if (cursor.moveToFirst())
						{
							chkDownload = FileUtils.chkDownloadStatus(DownloadDialog.this, cursor, tempName, refId, surahPos);
						}
						else
						{
							chkDownloadStatus = FileUtils.checkOneAudioFile(DownloadDialog.this, tempName, surahPos);
						}

						if (chkDownload.get(FileUtils.CHK_RUNNING))
						{
							if (name.contains(".mp3"))
							{
								msg = getString(R.string.downloading_in_progress);

								// btnback.setVisibility(View.VISIBLE);
								// // btnSurah.setEnabled(false);
								// tvBody.setText(R.string.downloading_in_progress);
								// btnLayout.setVisibility(View.GONE);
							}
							else
							{
								// btnQuran.setEnabled(false);
								// btnQuran.setText("Downloading Quran In Progress");
							}
						}

						if (chkDownloadStatus || chkDownload.get(FileUtils.CHK_SUCCESSFUL))
						{
							if (name.contains(".mp3"))
							{
								// btnSurah.setEnabled(false);
								// btnSurah.setText("Already Downloaded");

								Intent end_actvty = new Intent();
								setResult(RESULT_OK, end_actvty);
								finish();
							}
							else
							{
								// btnQuran.setEnabled(false);
								// btnQuran.setText(R.string.already_downloaded);
							}
						}
					}
					catch (NullPointerException e)
					{
						e.printStackTrace();
						chkDownloadStatus = FileUtils.checkOneAudioFile(DownloadDialog.this, tempName, surahPos);

						if (chkDownloadStatus) // || chkDownload.get(Constants.CHK_SUCCESSFUL))
						{
							if (name.contains(".mp3"))
							{
								// btnSurah.setEnabled(false);
								// btnSurah.setText("Already Downloaded");

								Intent end_actvty = new Intent();
								setResult(RESULT_OK, end_actvty);
								finish();
							}
							else
							{
								// btnQuran.setEnabled(false);
								// btnQuran.setText(R.string.already_downloaded);
							}
						}
					}
				}

			}
			while (c.moveToNext());
		}

		c.close();
		dbObj.close();

		return true;
	}

	public boolean checkDownloadManagerState() {
		boolean status = false;

		int state = this.getPackageManager().getApplicationEnabledSetting(packageName);

		if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER || state == 4)
		{
			status = false;
		}
		else
		{
			status = true;
		}

		return status;
	}

	public void onDownloadSurah(View view) {

		double fileSize = 0;
		if(reciter == 0)
		{
			String[] sizesAlfasay = getResources().getStringArray(R.array.surah_sizes_basit);
			fileSize = Double.parseDouble(sizesAlfasay[position - 1].toString());
		}
		if (reciter == 1)
		{
			String[] sizesAlfasay = getResources().getStringArray(R.array.surah_sizes_alfasay);
			fileSize = Double.parseDouble(sizesAlfasay[position - 1].toString());
		}
		else if (reciter == 2)
		{
			String[] sizesSudais = getResources().getStringArray(R.array.surah_sizes_sudais);
			fileSize = Double.parseDouble(sizesSudais[position - 1].toString());
		}
		((GlobalClass) getApplicationContext()).saveonpause = true;

		if (!checkDownloadManagerState())
		{
			alertMessage();
			return;
		}

		if (isNetworkConnected())
		{
			if (!EnoughMemory(fileSize))
			{
				Toast.makeText(this, getResources().getString(R.string.not_enough_memory) + "(" + String.format("%.2f", sizeRequired * 1E-6) + "MB Wajib)", Toast.LENGTH_LONG).show();

			}
			else
			{
				if (!((GlobalClass) getApplication()).isServiceRunning())
				{
					Intent serviceIntent = new Intent(DownloadDialog.this, ServiceClass.class);
					serviceIntent.putExtra("NAME", name);
					serviceIntent.putExtra("POSITION", position);
					serviceIntent.putExtra("ANAME", audioName);
					serviceIntent.putExtra("RECITER", reciter);
					startService(serviceIntent);
				}
				else
				{
					Intent broadcastIntent = new Intent(Constants.BroadcastActionDownload);
					broadcastIntent.putExtra("NAME", name);
					broadcastIntent.putExtra("POSITION", position);
					broadcastIntent.putExtra("ANAME", audioName);
					broadcastIntent.putExtra("RECITER", reciter);
					sendBroadcast(broadcastIntent);
				}
			}
		}
		else
		{
			((GlobalClass) getApplication()).showToast(getResources().getString(R.string.no_network_connection));
		}

		finish();
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private boolean EnoughMemory(double fileSize) {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		stat.restat(Environment.getDataDirectory().getPath());
		double bytesAvailable;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
			bytesAvailable = (double) stat.getAvailableBytes();
		else
			bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
		if (bytesAvailable >= fileSize)
		{
			return true;
		}
		else
		{
			sizeRequired = fileSize - bytesAvailable;
			return false;
		}
	}

	public void onDownloadFull(View view) {
		((GlobalClass) getApplicationContext()).saveonpause = true;
		double fileSize = 0;
		if (reciter == 1)
		{
			fileSize = 1.670244293E9 * 2;
		}
		else if (reciter == 2)
		{
			fileSize = 4.271251143E9 * 2;
		}
		if (!checkDownloadManagerState())
		{
			alertMessage();
			return;
		}

		if (isNetworkConnected())
		{
			if (!EnoughMemory(fileSize))
			{
				// Toast.makeText(this, "Not Enough Memory (" +String.format("%.2f", sizeRequired * 1E-6) + "MB required)", Toast.LENGTH_LONG).show();
				Toast.makeText(this, getResources().getString(R.string.not_enough_memory) + "(" + String.format("%.2f", sizeRequired * 1E-6) + "MB Wajib)", Toast.LENGTH_LONG).show();
			}
			else
			{
				if (!((GlobalClass) getApplication()).isServiceRunning())
				{
					Intent serviceIntent = new Intent(DownloadDialog.this, ServiceClass.class);
					serviceIntent.putExtra("NAME", "Quran");
					serviceIntent.putExtra("POSITION", 115);
					if (reciter == 0)
					{
						serviceIntent.putExtra("ANAME", Constants.QuranFile1);
					}
					else if (reciter == 1)
					{
						serviceIntent.putExtra("ANAME", Constants.QuranFile2);
					}
					else
					{
						serviceIntent.putExtra("ANAME", Constants.QuranFile2);
					}

					serviceIntent.putExtra("RECITER", reciter);
					startService(serviceIntent);
				}
				else
				{
					Intent broadcastIntent = new Intent(Constants.BroadcastActionDownload);
					broadcastIntent.putExtra("NAME", "Quran");
					broadcastIntent.putExtra("POSITION", 115);
					if (reciter == 0)
					{
						broadcastIntent.putExtra("ANAME", Constants.QuranFile1);
					}
					else if (reciter == 1)
					{
						broadcastIntent.putExtra("ANAME", Constants.QuranFile2);
					}
					else
					{
						broadcastIntent.putExtra("ANAME", Constants.QuranFile2);
					}
					broadcastIntent.putExtra("RECITER", reciter);
					sendBroadcast(broadcastIntent);
				}

				((GlobalClass) getApplication()).downloadQuran = true;
			}

		}
		else
		{
			((GlobalClass) getApplication()).showToast(getResources().getString(R.string.no_network_connection));
		}
		finish();
	}

	private void alertMessage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getString(R.string.alert));
		builder.setMessage(getResources().getString(R.string.download_manager_disabled));

		builder.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try
				{
					// Open the specific App Info page:
					Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					intent.setData(Uri.parse("package:" + packageName));
					startActivity(intent);
				}
				catch (ActivityNotFoundException e)
				{
					// e.printStackTrace();
					// Open the generic Apps page:
					Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
					startActivity(intent);
				}
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onCancel(View view) {
		((GlobalClass) getApplicationContext()).saveonpause = true;
		finish();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		IntentFilter surahDownloadComplete = new IntentFilter(Constants.BroadcastActionComplete);
		registerReceiver(downloadComplete, surahDownloadComplete);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(dailySurahNotification);
		unregisterReceiver(downloadComplete);
	}

	private BroadcastReceiver dailySurahNotification = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			finish();
		}
	};

	private BroadcastReceiver downloadComplete = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null)
			{

				DownloadDialog.this.finish();

			}
		}
	};

	private void showDownloadProcessingDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle(R.string.download);
		builder.setMessage(msg);

		builder.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void showDownloadDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setTitle(R.string.download);
		builder.setMessage(msg);

		builder.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				onDownloadSurah(null);
			}
		});

		builder.setNegativeButton(getResources().getString(R.string.index_btn1), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();

	}

	public boolean isNetworkConnected() {
		ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = mgr.getActiveNetworkInfo();

		return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
	}

}
