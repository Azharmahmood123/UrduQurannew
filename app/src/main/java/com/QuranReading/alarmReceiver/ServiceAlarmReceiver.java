package com.QuranReading.alarmReceiver;

import com.QuranReading.helper.DBManager;
import com.QuranReading.urduquran.Constants;
import com.QuranReading.urduquran.FileUtils;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.ServiceClass;

import android.app.DownloadManager.Query;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class ServiceAlarmReceiver extends BroadcastReceiver 
{
	private DBManager dbObj;
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.d("onReceive", "Enter");
		
		/*ArrayList<Integer> listIds = new ArrayList<Integer>();
		ArrayList<Integer> listDownloadIds = new ArrayList<Integer>();*/
		
		dbObj = new DBManager(context);
		dbObj.open();
		
		Cursor c = dbObj.getAllDownloads();
			
		if (c.moveToFirst())
		{
			if(!((GlobalClass) context.getApplicationContext()).isServiceRunning())
			{
				Intent serviceIntent = new Intent(context, ServiceClass.class);
				context.startService(serviceIntent);
				
				Log.d("onReceive", "Service Started");
			}
			else
			{
				Log.d("onReceive", "Service Running");
				
				do
				{
					int referenceId = c.getInt(c.getColumnIndex(Constants.FLD_DOWNLOAD_ID));
					int surahPos  = c.getInt(c.getColumnIndex(Constants.FLD_SURAH_NO));
					String tempName = c.getString(c.getColumnIndex(Constants.FLD_TEMP_NAME));
					
					try
					{
						Query myDownloadQuery = new Query();
						// set the query filter to our previously Enqueued download
						myDownloadQuery.setFilterById(referenceId);
	
						// Query the download manager about downloads that have been requested.
						Cursor cursor = ServiceClass.downloadManager.query(myDownloadQuery);
					
						if (cursor.moveToFirst()) 
						{
							FileUtils.chkDownloadStatus(context, cursor, tempName, referenceId, surahPos);
						}
						else
						{
							FileUtils.checkOneAudioFile(context, tempName, surahPos);
							//dbObj.deleteOneDownload(Constants.FLD_ID, index);
						}
					}
					catch(NullPointerException e)
					{
						e.printStackTrace();
					}
				} while (c.moveToNext());
			}
		}
		else
		{
			Intent serviceIntent = new Intent(context, ServiceClass.class);
			context.stopService(serviceIntent);
			abortBroadcast();
			Log.e("onReceive", "Service Stopped");
		}
		
		c.close();
		dbObj.close();
	}
}