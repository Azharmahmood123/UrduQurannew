package com.packageapp.wordbyword;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.QuranReading.helper.DBManager;
import com.QuranReading.urduquran.Constants;
import com.QuranReading.urduquran.FileUtils;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ServiceClass;


public class DownloadDialogActivity extends Activity {
	int position, reciter;
	String name = "", audioName = "";
	Button btnSurah, btnQuran, btnCancel;
	String packageName = "com.android.providers.downloads";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_download_dialog);

		position = getIntent().getIntExtra("POSITION", -1);
		audioName = getIntent().getStringExtra("ANAME");
		btnQuran = (Button) findViewById(R.id.btn_full_audio);

		if (position == 3) {
			btnQuran.setText("Download Audio");
		}
		else if (position == 4) {
			btnQuran.setText("Download All Surahs");
		}
		else if (position == 5) {
			btnQuran.setText("Download 6 Kalmas");
		}
		else if (position == 6) {
			btnQuran.setText("Download All Duas");
		}
		else if (position == 2) {
			btnQuran.setText("Download Ayah");
		}
		else {
			btnQuran.setText("Download Dua");
		}

		btnCancel = (Button) findViewById(R.id.btn_cancel);

		checkDownloadStatus();
	}

	public void checkDownloadStatus() { // check either any download is in progrress or not...
		if (!checkDownloadManagerState()) {
			return;
		}

		HashMap<String, Boolean> chkDownload = new HashMap<String, Boolean>();
		boolean chkDownloadStatus = false;
		DBManager dbObj = new DBManager(DownloadDialogActivity.this);
		dbObj.open();

		Cursor c = dbObj.getAllDownloads();

		if (c.moveToFirst()) {
			do {

				int size = c.getCount();
				// Toast.makeText(this, "Size: " + size, 0).show();
				int refId = c.getInt(c.getColumnIndex(DBManager.FLD_DOWNLOAD_ID));
				int surahPos = c.getInt(c.getColumnIndex(DBManager.FLD_SURAH_NO));
				String name = c.getString(c.getColumnIndex(DBManager.FLD_SURAH_NAME));
				String tempName = c.getString(c.getColumnIndex(DBManager.FLD_TEMP_NAME));

				if (name.equals(audioName) || name.equals(audioName + ".zip")) {
					try {
						Query myDownloadQuery = new Query();
						// set the query filter to our previously Enqueued
						// download
						myDownloadQuery.setFilterById(refId);

						// Query the download manager about downloads that have
						// been requested.
						Cursor cursor = ServiceClass.downloadManager.query(myDownloadQuery);

						if (cursor.moveToFirst()) {
							// it returns statuses
							chkDownload = FileUtils.chkDownloadStatus(DownloadDialogActivity.this, cursor, tempName, refId, surahPos);
						}
						else {
							chkDownloadStatus = FileUtils.checkOneAudioFile(DownloadDialogActivity.this, tempName, surahPos);
						}

						if (chkDownload.get(DBManager.CHK_RUNNING)) {
							if (name.contains(".mp3")) {
								btnSurah.setEnabled(false);
								btnSurah.setText("Downloading surah in progress. . . ");
							}
							else {
								btnQuran.setEnabled(false);
								btnQuran.setText("Downloading in progress. . . ");
							}
						}

						if (chkDownloadStatus || chkDownload.get(DBManager.CHK_SUCCESSFUL)) {
							if (name.contains(".mp3")) {
								// btnSurah.setEnabled(false);
								// btnSurah.setText("Already Downloaded");

								Intent end_actvty = new Intent();
								setResult(RESULT_OK, end_actvty);
								finish();
							}
							else {
								btnQuran.setEnabled(false);
								// btnQuran.setText("Already Downloaded");
								btnQuran.setText("Downloading in progress. . . ");
							}
						}
					}
					catch (NullPointerException e) {
						e.printStackTrace();
						chkDownloadStatus = FileUtils.checkOneAudioFile(DownloadDialogActivity.this, tempName, surahPos);

						if (chkDownloadStatus)// ||
												// chkDownload.get(Constants.CHK_SUCCESSFUL))
						{
							if (name.contains(".mp3")) {
								// btnSurah.setEnabled(false);
								// .setText("Already Downloaded");

								Intent end_actvty = new Intent();
								setResult(RESULT_OK, end_actvty);
								finish();
							}
							else {
								btnQuran.setEnabled(false);
								// btnQuran.setText("Already Downloaded");
								btnQuran.setText("Downloading in progress. . . ");
							}
						}
					}
				}

			}
			while (c.moveToNext());
		}

		c.close();
		dbObj.close();
	}

	public boolean checkDownloadManagerState() {
		boolean status = false;

		int state = this.getPackageManager().getApplicationEnabledSetting(packageName);

		if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER || state == 4) {
			status = false;
		}
		else {
			status = true;
		}

		return status;
	}

	/*
	 * public void onDownloadSurah(View view) { //on "download surah" click if (!checkDownloadManagerState()) { alertMessage(); return; }
	 * 
	 * if (isNetworkConnected()) { if (!((GlobalClass) getApplication()).isServiceRunning()) { Intent serviceIntent = new Intent(DownloadDialogActivity.this,ServiceClass.class); // serviceIntent.putExtra("NAME", name); // serviceIntent.putExtra("POSITION", position); serviceIntent.putExtra("ANAME",
	 * audioName); serviceIntent.putExtra("RECITER", reciter); startService(serviceIntent); } else { Intent broadcastIntent = new Intent(GlobalClass.BroadcastActionDownload); // broadcastIntent.putExtra("NAME", name); // broadcastIntent.putExtra("POSITION", position);
	 * broadcastIntent.putExtra("ANAME", audioName); broadcastIntent.putExtra("RECITER", reciter); sendBroadcast(broadcastIntent); } } else { ((GlobalClass) getApplication()).showToast("No Network Connection"); }
	 * 
	 * finish(); }
	 */

	public void onDownloadFull(View view) // on "download quran" click
	{
		//AyatulKursiActivity.isDialogDisplayed = false;
		SurahFragment.isDialoguDisplayed = false;
	/*	DuaQanootActivity.isDialogueDisplayed = false;
		NamesDetailActivity.isDialoDisplayed = false;
		NamesGridFragment.isDialogDisplyed = false;
		KalmaFragment.isDialoguDisplayedd = false;
		DuaFragment.isDialogDisplayed = false;*/

		if (!checkDownloadManagerState()) {
			alertMessage();
			return;
		}

		if (isNetworkConnected()) {
			if (!((GlobalClass) getApplication()).isServiceRunning()) {
				Intent serviceIntent = new Intent(DownloadDialogActivity.this, ServiceClass.class);
				// serviceIntent.putExtra("NAME", "DuaQanoot");
				serviceIntent.putExtra("POSITION", position);
				serviceIntent.putExtra("ANAME", (audioName)); // + ".zip"
				// serviceIntent.putExtra("RECITER", reciter); //////////////////////////
				startService(serviceIntent);
			}
			else {
				Intent broadcastIntent = new Intent(Constants.BroadcastActionDownload);
				/* broadcastIntent.putExtra("NAME", "Quran"); */
				broadcastIntent.putExtra("POSITION", position);
				broadcastIntent.putExtra("ANAME", (audioName)); // +".zip"
				/* broadcastIntent.putExtra("RECITER", reciter); */
				sendBroadcast(broadcastIntent);
			}

			((GlobalClass) getApplication()).downloadQuran = true;

			finish();
		}
		else {
			((GlobalClass) getApplication()).showToast("No Network Connection");
		}

	}

	private void alertMessage() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Alert!");
		builder.setMessage("Download Manager disabled.\nDo you want to enable it?");

		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				try {
					// Open the specific App Info page:
					Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
					intent.setData(Uri.parse("package:" + packageName));
					startActivity(intent);
				}
				catch (ActivityNotFoundException e) {
					// e.printStackTrace();
					// Open the generic Apps page:
					Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
					startActivity(intent);
				}
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				finish();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onCancel(View view) {

		finish();
	}

	public boolean isNetworkConnected() {
		ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = mgr.getActiveNetworkInfo();

		return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		finish();

	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter surahDownloadComplete = new IntentFilter(Constants.BroadcastActionComplete);
		registerReceiver(downloadComplete, surahDownloadComplete);
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		//AyatulKursiActivity.isDialogDisplayed = false;
		SurahFragment.isDialoguDisplayed = false;
	/*	DuaQanootActivity.isDialogueDisplayed = false;
		NamesDetailActivity.isDialoDisplayed = false;
		NamesGridFragment.isDialogDisplyed = false;
		KalmaFragment.isDialoguDisplayedd = false;
		DuaFragment.isDialogDisplayed = false;*/
		unregisterReceiver(downloadComplete);
	}

	private BroadcastReceiver downloadComplete = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {

				DownloadDialogActivity.this.finish();

			}
		}
	};
}
