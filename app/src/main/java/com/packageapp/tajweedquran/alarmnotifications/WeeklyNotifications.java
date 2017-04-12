package com.packageapp.tajweedquran.alarmnotifications;

/**
 * Created by Aamir Riaz on 7/22/2015.
 */

import java.io.IOException;
import java.util.Calendar;

        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;

public class WeeklyNotifications {

    private Context context;
    private final int alarmId = 1315;


    public WeeklyNotifications(Context context) {

        this.context = context;

    }

    public void setDailyAlarm() {
        Calendar c1 = setAlarmTime(6,00, "pm");
        setAlarmEveryDay(c1);
    }

    public Calendar setAlarmTime(int hour, int min, String am_pm) {
        Calendar time = Calendar.getInstance();

       /* if (hour == 12) {
            time.set(Calendar.HOUR, 0);
        } else {
            time.set(Calendar.HOUR, hour);
        }*/
        time.set(Calendar.HOUR, hour);
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


    public void cancelAlarm() {
        Intent intent = new Intent(context, AlarmReciever.class);
        intent.putExtra("ID", String.valueOf(alarmId));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
    }
    public void setAlarmEveryDay(Calendar targetCal)
    {
      //  int id=111;
        Intent intent = new Intent(context, AlarmReciever.class);
        intent.putExtra("ID", String.valueOf(alarmId));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context. getSystemService(Context.ALARM_SERVICE);
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), 1000 * 60 * 60 *24 *7, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

}
