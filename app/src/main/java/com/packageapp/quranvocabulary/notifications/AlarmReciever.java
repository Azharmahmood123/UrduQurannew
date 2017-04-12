package com.packageapp.quranvocabulary.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.R;
import com.packageapp.HomeSplashActivity;
import com.packageapp.quranvocabulary.MainActivity;
import com.packageapp.quranvocabulary.generalhelpers.DBManager;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class AlarmReciever extends BroadcastReceiver {

    private Context context;

    private SettingsSharedPref sharedPref;
    private DBManager dbManager;
    private String word = "Notification";
    private int minId = 1;
    private int maxId = 40;
    private int randomId;
    private int sendRandomId=-1;
    private String strQuery, strCatName;


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        this.context = context;
        sharedPref = new SettingsSharedPref(context);
        dbManager = new DBManager(context);
        sendRandomId = getRandomID();
        strCatName=fetchCatName(sendRandomId);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if(day==Calendar.FRIDAY)
        {
            int hour=calendar.get(Calendar.HOUR);
            int mode=calendar.get(Calendar.AM_PM);
            if( hour>=8 &&mode== Calendar.PM)
            {
                showNotification();
            }

        }

    }

    public void showNotification() {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            String formattedDate = sdf.format(c.getTime());
//            if (formattedDate.equals("20:00")){
            Intent intent = new Intent(context, HomeSplashActivity.class);
            intent.putExtra("cat_id",sendRandomId);
            intent.putExtra("cat_name",strCatName);

            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                | Intent.FLAG_ACTIVITY_NEW_TASK);

            // context, notificationID, intnt,PendingIntent.FLAG_UPDATE_CURRENT
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
            mBuilder.setContentTitle(context.getResources().getString(R.string.app_name_QuranVocabulary));
            mBuilder.setContentText("Learn today\'s lesson for \"" +strCatName +"\"words.");
            mBuilder.setTicker(context.getResources().getString(R.string.app_name_QuranVocabulary));
            mBuilder.setWhen(System.currentTimeMillis());
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setDefaults(Notification.DEFAULT_SOUND);
            mBuilder.setAutoCancel(true);
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher_notification);

            }
            else
            {
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);
            }
            mBuilder.setLargeIcon(BitmapFactory.decodeResource(this.context.getResources(), R.drawable.ic_launcher));
            Notification notification=mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS  | Notification.DEFAULT_VIBRATE;
            if (sharedPref.getNotification() == 0) {
                NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                nm.notify(1214, notification);
            }
//                sendAnalyticsData();
//            }
        } catch (Exception ex) {
        }
    }

    public String fetchCatName(int id) {
        try {
            if (sharedPref.getLanguageQuranVocabulary() != 0) {
                strQuery = "Select English from tbl_categories where ID =='" + id+"\'";
            } else {
                strQuery = "Select Urdu from tbl_categories where ID ==\'" + id+"\'";

            }
            Cursor result = dbManager.getCatNames(strQuery);
            if (result != null) {
                int i = 0;
                result.moveToFirst();
                do {
                    strCatName = (result.getString(0));
                }
                while (result.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(context,"Exception in getting Cat name",Toast.LENGTH_LONG).show();
        }
        return strCatName;
    }


    /*public void sendAnalyticsData() {
        try {
            // TODO Auto-generated method stub
            // Get tracker.
            Tracker screenTracker= ((GlobalClass) context.getApplicationContext()).getTracker();

            // Set screen name.
            screenTracker.setScreenName("Notification Alert");

            // Send a screen view.
            screenTracker.send(new HitBuilders.AppViewBuilder().build());
        }catch (Exception ex) {

        }
    }*/

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.notify_icon_quranvocabulary : R.drawable.notify_icon_quranvocabulary;
    }

    private int getRandomID() {
        Random r = new Random();
        randomId = r.nextInt((maxId - minId) + 1) + minId;
        return randomId;
    }
}
