package com.QuranReading.urduquran.quranfacts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.QuranReading.alarmReceiver.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by cyber on 12/16/2016.
 */

public class AlarmClassFacts {
    static int alarmId = 1613;
    private Context mContext;

    public AlarmClassFacts(Context context) {
        this.mContext = context;
    }

    public void setAlarm() {
        // Toast.makeText(ctx, "Alarm has been set", Toast.LENGTH_SHORT).show();
        Calendar time = Calendar.getInstance();

        time.set(Calendar.HOUR,2);
        time.set(Calendar.MINUTE, 00);
        time.set(Calendar.SECOND, 0);
        time.set(Calendar.MILLISECOND, 0);
        time.set(Calendar.AM_PM, Calendar.PM);

        if (System.currentTimeMillis() > time.getTimeInMillis()) {
            long add = AlarmManager.INTERVAL_DAY;
            long oldTime = time.getTimeInMillis();
            time.setTimeInMillis(oldTime + add);// Okay, then tomorrow ...
        }

        setAlarmEveryDay(mContext, time);
    }

    private void setAlarmEveryDay(Context ctx, Calendar targetCal) {
        Intent intent = new Intent(ctx, AlarmReceiverFacts.class);
        intent.putExtra("ID", String.valueOf(alarmId));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, alarmId,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) ctx
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
                pendingIntent);
    }

    public void cancelAlarm() {
        // Toast.makeText(ctx, "Alarm has been Cancelled",
        // Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(mContext, AlarmReceiverFacts.class);
        intent.putExtra("ID", String.valueOf(alarmId));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, alarmId,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mContext
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
}
