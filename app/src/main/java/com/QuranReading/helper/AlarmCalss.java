package com.QuranReading.helper;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.QuranReading.alarmReceiver.AlarmReceiver;

public class AlarmCalss {
	static int alarmId = 1212;
	private Context mContext;

	public AlarmCalss(Context context) {
		this.mContext = context;
	}

	public void setAlarm() {
		// Toast.makeText(ctx, "Alarm has been set", Toast.LENGTH_SHORT).show();
		Calendar time = Calendar.getInstance();

		time.set(Calendar.HOUR, 6);
		time.set(Calendar.MINUTE, 00);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);
		time.set(Calendar.AM_PM, Calendar.AM);

		if (System.currentTimeMillis() > time.getTimeInMillis()) {
			long add = AlarmManager.INTERVAL_DAY;
			long oldTime = time.getTimeInMillis();
			time.setTimeInMillis(oldTime + add);// Okay, then tomorrow ...
		}

		setAlarmEveryDay(mContext, time);
	}

	private void setAlarmEveryDay(Context ctx, Calendar targetCal) {
		Intent intent = new Intent(ctx, AlarmReceiver.class);
		intent.putExtra("ID", String.valueOf(alarmId));

		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, alarmId,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) ctx
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
				pendingIntent);
	}

	public void cancelAlarm() {
		// Toast.makeText(ctx, "Alarm has been Cancelled",
		// Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		intent.putExtra("ID", String.valueOf(alarmId));

		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarmId,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}

	// ////////////////////////
	// /////////////////////////
	public Calendar setAlarmTimeQuranNotify(int hour, int min, String am_pm) {
		Calendar time = Calendar.getInstance();

		if (hour == 12) {
			// time.set(Calendar.HOUR_OF_DAY, hour);
			time.set(Calendar.HOUR, 0);
		} else {
			time.set(Calendar.HOUR, hour);
		}

		time.set(Calendar.MINUTE, min);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);

		if (am_pm.equals("am")) {
			time.set(Calendar.AM_PM, Calendar.AM);
		} else {
			time.set(Calendar.AM_PM, Calendar.PM);
		}

		if (System.currentTimeMillis() > time.getTimeInMillis()) {
			long add = AlarmManager.INTERVAL_DAY;
			long oldTime = time.getTimeInMillis();
			time.setTimeInMillis(oldTime + add);// Okay, then tomorrow ...
		}

		return time;
	}
}
