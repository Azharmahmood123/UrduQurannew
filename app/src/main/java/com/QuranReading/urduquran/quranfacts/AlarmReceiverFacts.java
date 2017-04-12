package com.QuranReading.urduquran.quranfacts;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.R;
import com.packageapp.HomeSplashActivity;

import java.util.Calendar;
import java.util.Random;

public class AlarmReceiverFacts extends BroadcastReceiver {
    String[] data,titles;
    Context context;
    int tabPos;
    int rNum = 1;
    SettingsSharedPref sharedPref;
    Boolean dataType;
    NotificationCompat.Builder mBuilder;
    private final int ID = 2;
    private final int requestID = 4;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        sharedPref = new SettingsSharedPref(context);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int hour=calendar.get(Calendar.HOUR);
        int mode=calendar.get(Calendar.AM_PM);

        if ((day == Calendar.SATURDAY)&&(hour>=2)&&(mode== Calendar.PM)) {
            DataSetting();
            Intent pendIntent = new Intent(context,HomeSplashActivity.class);
            pendIntent.putExtra("listItemPos",rNum);
            pendIntent.putExtra("tabPosition",tabPos);
            pendIntent.putExtra("data",data);
            pendIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            PendingIntent contentIntent = PendingIntent.getActivity(context, requestID, pendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder =  new NotificationCompat.Builder(context);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
            {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher_notification);
            }
            else
            {
                mBuilder.setSmallIcon(R.drawable.ic_launcher);
            }
            mBuilder.setContentTitle("Quran Facts");
            mBuilder.setTicker("Quran Facts");
            mBuilder.setContentText("Fact of The Week"+": "+titles[rNum]);
            mBuilder.setAutoCancel(true);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setContentIntent(contentIntent);
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_launcher));
            Notification notification=mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS  | Notification.DEFAULT_VIBRATE;

            NotificationManager mgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mgr.notify(ID, notification);
        }


    }

    public void DataSetting() {
        dataType = sharedPref.getFacts_random();
        Random r = new Random();
        if (dataType == true) {
            rNum = r.nextInt(31);
            data = context.getResources().getStringArray(R.array.interesting_facts_data);
            titles= context.getResources().getStringArray(R.array.interesting_facts_list);
            tabPos = 0;
            dataType=false;
            sharedPref.setFacts_random(dataType);

        } else {
            rNum = r.nextInt(25);
            data = context.getResources().getStringArray(R.array.scientific_facts_data);
            titles= context.getResources().getStringArray(R.array.scietific_facts_list);
            tabPos = 1;
            dataType=true;
            sharedPref.setFacts_random(dataType);
        }
    }
}
