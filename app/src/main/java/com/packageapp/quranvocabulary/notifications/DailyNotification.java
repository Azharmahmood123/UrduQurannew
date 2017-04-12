package com.packageapp.quranvocabulary.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class DailyNotification {

	private Context context;
	private final int notificationId = 1214;

	public DailyNotification(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setDailyAlarm() {
		Calendar c1 = setAlarmTime(8, 00, "pm");
		setAlarmEveryDay(c1, notificationId);
	}

	public Calendar setAlarmTime(int hour, int min, String am_pm) {
		Calendar time = Calendar.getInstance();

		/*if(hour == 12)
		{
			time.set(Calendar.HOUR, 0);
		}
		else
		{
			time.set(Calendar.HOUR, hour);
		}*/
		time.set(Calendar.HOUR,hour);
		time.set(Calendar.MINUTE, min);
		time.set(Calendar.SECOND, 0);
		time.set(Calendar.MILLISECOND, 0);

		if(am_pm.equals("am"))
		{
			time.set(Calendar.AM_PM, Calendar.AM);
		}
		else
		{
			time.set(Calendar.AM_PM, Calendar.PM);
		}

		if(System.currentTimeMillis() > time.getTimeInMillis())
		{
			long add = AlarmManager.INTERVAL_DAY;
			long oldTime = time.getTimeInMillis();
			time.setTimeInMillis(oldTime + add);// Okay, then tomorrow ...
		}
		return time;
	}

	public void setAlarmEveryDay(Calendar targetCal, int alarmId) {
		Intent intent = new Intent(context, AlarmReciever.class);
		intent.putExtra("ID", alarmId);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
		//alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	}

	public void cancelAlarm(int alarmId) {
		Intent intent = new Intent(context, AlarmReciever.class);
		intent.putExtra("ID", alarmId);

		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		pendingIntent.cancel();
	}

    //////Change word of the day////////
    /*public void updateWordAlarm() {
        Calendar c1 = setAlarmTime(12, 00, "am");
        setAlarmEveryDay(c1, updateWordId);
    }*/
}
