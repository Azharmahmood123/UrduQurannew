package com.packageapp.tajweedquran.alarmnotifications;

/**
 * Created by Aamir Riaz on 7/22/2015.
 */


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
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.SplashActivity;
import com.packageapp.HomeSplashActivity;
import com.packageapp.tajweedquran.TajweedActivity;

import java.util.Calendar;

public class AlarmReciever extends BroadcastReceiver {

    private Context context;
    private Notification notification;
    private SettingsSharedPref sharedPref;
    int sunnahPos;
    String chapterTitle;
    GlobalClass mGlobal;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        sharedPref = new SettingsSharedPref(context);
        randInt();// generate random number
        mGlobal=((GlobalClass)context.getApplicationContext());
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day==Calendar.SUNDAY)
        {
            int hour=calendar.get(Calendar.HOUR);
            int mode=calendar.get(Calendar.AM_PM);
            if( (hour>=6)&&(mode== Calendar.PM))
            {
                showNotification(sunnahPos);
            }


        }

    }

    public void showNotification(int pos){

        Intent intent = new Intent(context, HomeSplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
       intent.putExtra("detailID",pos);
        String anyText=getChapterTitle(pos);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
         NotificationCompat.Builder mBuilder= new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(context.getResources().getString(R.string.app_name_TajweedQuran));
        mBuilder.setContentText("Let\'s learn the lesson of"+ anyText + "today to improve your Tajweed.");
        mBuilder.setTicker(context.getResources().getString(R.string.app_name_TajweedQuran));
        mBuilder.setWhen(System.currentTimeMillis());
        mBuilder .setContentIntent(pendingIntent);
        mBuilder .setDefaults(Notification.DEFAULT_SOUND);
        mBuilder  .setAutoCancel(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
        {
            mBuilder  .setSmallIcon(R.mipmap.ic_launcher_notification);
        }
        else
        {
            mBuilder  .setSmallIcon(R.drawable.ic_launcher);
        }
        mBuilder  .setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_launcher));
        mBuilder  .build();
        notification=mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS  | Notification.DEFAULT_VIBRATE;
        if(sharedPref.getTajweed_Notification()==true){
            NotificationManager nm = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1315, notification);

        }
    }
    private String getChapterTitle(int generatedID)
    {

        switch (generatedID)
        {
            case 0:
                chapterTitle=" \"Introduction\"";
                break;
            case 1:
                chapterTitle=" \"Mukhraj ul Haroof\"";
                break;
           /* case 2:
                chapterTitle=" \"Stopping Signs\"";
                break;*/
            case 2:
                chapterTitle=" \"Throat Letters\"";
                break;
            case 3:
                chapterTitle=" \"Bold Letters\"";
                break;
            case 4:
                chapterTitle=" \"Silent Letters\"";
                break;
            case 5:
                chapterTitle=" \"Qalqalah Letters\"";
                break;
            case 6:
                chapterTitle=" \"Leen Letters\"";
                break;
            case 7:
                chapterTitle=" \"Harkat(Movement)\"";
                break;
            case 8:
                chapterTitle=" \"Tanween\"";
                break;
            case 9:
                chapterTitle=" \"Sukoon\"";
                break;
            case 10:
                chapterTitle=" \"Shaddah\"";
                break;
            case 11:
                chapterTitle=" \"Madd\"";
                break;
            case 12:
                chapterTitle=" \"Rules of Nun Sakin\"";
                break;
            case 13:
                chapterTitle=" \"Rules of Meem Sakin\"";
                break;
            case 14:
                chapterTitle=" \"Rules of Raa\"";
                break;
            case 15:
                chapterTitle=" \"Rules of Letter Laam\"";
                break;
            case 16:
                chapterTitle=" \"Rules of Hamza\"";
                break;
            default:
                break;

        }

        return chapterTitle;
    }

    public void randInt() {
        //  sunnahPos=30;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int min = 0, max = 16;


        do {
         //   Log.d("Random No", "Generate");
            sunnahPos = (int) (min + (Math.random() * max));

        } while (sunnahPos == sharedPref. getTajweedRandomNumber());

      //  Log.i("Random No", String.valueOf(sunnahPos));


        sharedPref.setTajweedRandomNumber(sunnahPos);

    }

}

