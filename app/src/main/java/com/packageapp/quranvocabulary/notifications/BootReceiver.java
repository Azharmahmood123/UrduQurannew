package com.packageapp.quranvocabulary.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;

/**
 * Created by cyber on 10/16/2015.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SettingsSharedPref sharedPref = new SettingsSharedPref(context);
        DailyNotification notification = new DailyNotification(context);
        if (sharedPref.getVocabulary_Notification() == true) {
            notification.setDailyAlarm();
        }
//        notification.updateWordAlarm();
    }
}
