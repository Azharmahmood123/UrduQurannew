package com.QuranReading.alarmReceiver;

import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.QuranReading.helper.AlarmCalss;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.quranfacts.AlarmClassFacts;
import com.packageapp.quranvocabulary.notifications.DailyNotification;
import com.packageapp.tajweedquran.alarmnotifications.WeeklyNotifications;

public class RebootAlarmReceiver extends BroadcastReceiver 
{
	int alarmId = 1212;
	Context context;
	SettingsSharedPref settngPref;

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		this.context = context;
		settngPref = new SettingsSharedPref(context);
		
		//Toast.makeText(context, "Enter Receiver", Toast.LENGTH_SHORT).show();
		HashMap<String, Boolean> tranSettngs = settngPref.getTransSettings();
		boolean chkNotification = tranSettngs.get(SettingsSharedPref.NOTIFICATION);
		boolean isQuranFacts_Notification=settngPref.getQuran_facts_Notification();
		boolean isQuranVocabulary_Notification=settngPref.getVocabulary_Notification();
		boolean isTajweed_Notification=settngPref.getTajweed_Notification();
		if(chkNotification)
		{
			AlarmCalss mAlarmHelper = new AlarmCalss(context);
			mAlarmHelper.cancelAlarm();
			mAlarmHelper.setAlarm();
			
			//Toast.makeText(context, "Reset Alarm", Toast.LENGTH_SHORT).show();
		}
		if(isQuranFacts_Notification)
		{
			AlarmClassFacts alarm=new AlarmClassFacts(context);
			alarm.cancelAlarm();
			alarm.setAlarm();

		}
		if(isQuranVocabulary_Notification)
		{
			DailyNotification dailyNotification=new DailyNotification(context);// this is weekly notification
			dailyNotification.cancelAlarm(1214);
			dailyNotification.setDailyAlarm();
		}
		if(isTajweed_Notification)
		{
			WeeklyNotifications notifications=new WeeklyNotifications(context);
			notifications.cancelAlarm();
			notifications.setDailyAlarm();
		}
		else
		{
			//Toast.makeText(context, "No Alarm", Toast.LENGTH_SHORT).show();
		}
	}
}