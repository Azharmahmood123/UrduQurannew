package com.QuranReading.urduquran;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.quranfacts.AlarmClassFacts;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.packageapp.quranvocabulary.MainActivity;
import com.packageapp.quranvocabulary.dialogs.LanguageInterface;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;
import com.packageapp.quranvocabulary.notifications.DailyNotification;
import com.packageapp.tajweedquran.alarmnotifications.WeeklyNotifications;

public class SettingsActivity extends AppCompatActivity  {

    TextView tvHeaderSettings;
    GlobalClass mGlobal;
    ImageView tajweedNotifImage,vocabularyNotifImage,suraNotifImage,quranFactsImage;
    boolean tajweedNotifBoolean=false,vocabularyNotifBoolean=false,suraNotifImageBoolean=false,quranFactsNotifBoolean=false;
    SettingsSharedPref sharedPref;
    Context context;
    TextView tvTajweedLanguge,tvQuranVocabularyLanguage;
    //Alarm classes
    AlarmClassFacts alarmClassFacts; //Quran facts Alarm class
    DailyNotification alarmNotification;// Quran Vocabulary ALarm class
    WeeklyNotifications weeklyNotification;// Tajweed Quran Notification
    //ImageView adImage;
    AdView adview;
    private static Activity mActivity = null;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;
    AnalyticSingaltonClass mAnalyticSingaltonClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        tvHeaderSettings = (TextView) findViewById(R.id.tv_header);
        mGlobal = (GlobalClass) getApplicationContext();
        tvHeaderSettings.setTypeface(mGlobal.faceHeading);
        context = this;
        mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);
        mAnalyticSingaltonClass.sendScreenAnalytics("Home_Setting_Screen");
        tajweedNotifImage = (ImageView) findViewById(R.id.tajweedOn);
        vocabularyNotifImage = (ImageView) findViewById(R.id.vocabularyOn);
        suraNotifImage = (ImageView) findViewById(R.id.suraKahfOn);
        quranFactsImage=(ImageView) findViewById(R.id.quranFactsOn);
        tvTajweedLanguge= (TextView) findViewById(R.id.tvTajweedQuran);
        tvQuranVocabularyLanguage= (TextView) findViewById(R.id.tvQuranVocabulary);
        initDefaultSettings();
        initAds();
    }
    private void initDefaultSettings()
    {
        int languageTajweed=0,languageQuranVocabulary=0;
        sharedPref = new SettingsSharedPref(context);
        alarmClassFacts=new AlarmClassFacts(context); //Quran Facts Notification
      alarmNotification=new DailyNotification(context);// Quran Vocabulary Notifications
        weeklyNotification=new WeeklyNotifications(context); // Tajweed Alarm Class

        tajweedNotifBoolean = sharedPref.getTajweed_Notification();
        vocabularyNotifBoolean = sharedPref.getVocabulary_Notification();
        suraNotifImageBoolean = sharedPref.getSurah_Notification();
        languageTajweed=sharedPref.getLanguageTajweed();
        languageQuranVocabulary=sharedPref.getLanguageQuranVocabulary();
        quranFactsNotifBoolean=sharedPref.getQuran_facts_Notification();
        if(languageTajweed==0)
        {
            tvTajweedLanguge.setText(R.string.setUrdu);
        }
        else
        {
            tvTajweedLanguge.setText(R.string.setEnglish);
        }
        if(languageQuranVocabulary==0)
        {
            tvQuranVocabularyLanguage.setText(R.string.setUrdu);
        }
        else
        {
            tvQuranVocabularyLanguage.setText(R.string.setEnglish);
        }
        //Tajweed Notification
        if(!tajweedNotifBoolean) {
            tajweedNotifImage.setImageResource(R.drawable.img_off);
            weeklyNotification.cancelAlarm();

        }
        else {
            tajweedNotifImage.setImageResource(R.drawable.img_on);
            weeklyNotification.cancelAlarm();
            weeklyNotification.setDailyAlarm();

        }
        //Quran vocabulary Notification
        if(!vocabularyNotifBoolean) {
            alarmNotification.cancelAlarm(1214);
            vocabularyNotifImage.setImageResource(R.drawable.img_off);
        }
        else {
            vocabularyNotifImage.setImageResource(R.drawable.img_on);
            alarmNotification.cancelAlarm(1214);
            alarmNotification.setDailyAlarm();

        }
        //Surah Kahf Notification
        if(!suraNotifImageBoolean) {
            suraNotifImage.setImageResource(R.drawable.img_off);
        }
        else {
            suraNotifImage.setImageResource(R.drawable.img_on);
        }
        //Quran Facts Notification
        if (quranFactsNotifBoolean) {
            quranFactsImage.setImageResource(R.drawable.img_on);
            alarmClassFacts.cancelAlarm();
            alarmClassFacts.setAlarm();
        } else {
            quranFactsImage.setImageResource(R.drawable.img_off);
            alarmClassFacts.cancelAlarm();
        }

    }
    private void initAds() {
        adview = (AdView) findViewById(R.id.adView);
        adview.setVisibility(View.GONE);
        if (isNetworkConnected()) {
            this.adview.setVisibility(View.VISIBLE);
        } else {
            this.adview.setVisibility(View.GONE);
        }
        setAdsListener();

    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub

        super.onResume();
        if (!mGlobal.isPurchase) {
            startAdsCall();
        } else {
            //adImage.setVisibility(View.GONE);
            adview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!mGlobal.isPurchase) {
            stopAdsCall();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (!mGlobal.isPurchase) {
            destroyAds();
        }
    }

    public void onClickAdImage(View view) {

    }

    private boolean isNetworkConnected() {
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
    }

    private Runnable sendUpdatesAdsToUI = new Runnable() {
        public void run() {
            Log.v(LOG_TAG, "Recall");
            updateUIAds();
        }
    };

    private final void updateUIAds() {
        if (isNetworkConnected()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            adview.loadAd(adRequest);
        } else {
            timerValue = networkRefreshTime;
            adsHandler.removeCallbacks(sendUpdatesAdsToUI);
            adsHandler.postDelayed(sendUpdatesAdsToUI, timerValue);
        }
    }

    public void startAdsCall() {
        Log.i(LOG_TAG, "Starts");
        if (isNetworkConnected()) {
            this.adview.setVisibility(View.VISIBLE);
        } else {
            this.adview.setVisibility(View.GONE);
        }

        adview.resume();
        adsHandler.removeCallbacks(sendUpdatesAdsToUI);
        adsHandler.postDelayed(sendUpdatesAdsToUI, 0);
    }

    public void stopAdsCall() {
        Log.e(LOG_TAG, "Ends");
        adsHandler.removeCallbacks(sendUpdatesAdsToUI);
        adview.pause();
    }

    public void destroyAds() {
        Log.e(LOG_TAG, "Destroy");
        adview.destroy();
        adview = null;
    }

    private void setAdsListener() {
        adview.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Log.d(LOG_TAG, "onAdClosed");
            }

            @Override
            public void onAdFailedToLoad(int error) {
                String message = "onAdFailedToLoad: " + getErrorReason(error);
                Log.d(LOG_TAG, message);
                adview.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(LOG_TAG, "onAdLeftApplication");
            }

            @Override
            public void onAdOpened() {
                Log.d(LOG_TAG, "onAdOpened");
            }

            @Override
            public void onAdLoaded() {
                Log.d(LOG_TAG, "onAdLoaded");
                adview.setVisibility(View.VISIBLE);

            }
        });
    }

    private String getErrorReason(int errorCode) {
        String errorReason = "";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                errorReason = "Internal error";
                break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                errorReason = "Invalid request";
                break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                errorReason = "Network Error";
                break;
            case AdRequest.ERROR_CODE_NO_FILL:
                errorReason = "No fill";
                break;
        }
        return errorReason;
    }



    public void onClickEvent  (View view) {
        switch (view.getId())
        {
            case R.id.tajweedOn:
                if(tajweedNotifBoolean) {
                    tajweedNotifImage.setImageResource(R.drawable.img_off);
                    tajweedNotifBoolean=false;
                    sharedPref.setTajweed_Notification(tajweedNotifBoolean);
                    weeklyNotification.cancelAlarm();
                }
                else {
                    tajweedNotifImage.setImageResource(R.drawable.img_on);
                    tajweedNotifBoolean=true;
                    sharedPref.setTajweed_Notification(tajweedNotifBoolean);
                    weeklyNotification.cancelAlarm();
                    weeklyNotification.setDailyAlarm();
                }
                break;
            case R.id.vocabularyOn:
                if(vocabularyNotifBoolean) {
                    vocabularyNotifImage.setImageResource(R.drawable.img_off);
                    vocabularyNotifBoolean=false;
                    sharedPref.setVocabulary_Notification_(vocabularyNotifBoolean);
                    alarmNotification.cancelAlarm(1214);
                    //alarmNotification.setDailyAlarm();
                }
                else {
                    vocabularyNotifImage.setImageResource(R.drawable.img_on);
                    vocabularyNotifBoolean=true;
                    sharedPref.setVocabulary_Notification_(vocabularyNotifBoolean);
                    alarmNotification.cancelAlarm(1214);
                    alarmNotification.setDailyAlarm();
                }
                break;
            case R.id.suraKahfOn:
                if(suraNotifImageBoolean) {
                    suraNotifImage.setImageResource(R.drawable.img_off);
                    suraNotifImageBoolean=false;
                    sharedPref.setSurah_Notification(suraNotifImageBoolean);
                }
                else {
                    suraNotifImage.setImageResource(R.drawable.img_on);
                    suraNotifImageBoolean=true;
                    sharedPref.setSurah_Notification(suraNotifImageBoolean);
                }
                break;
            case R.id.quranFactsOn:
                if(quranFactsNotifBoolean) {
                    quranFactsImage.setImageResource(R.drawable.img_off);
                    quranFactsNotifBoolean=false;
                    alarmClassFacts.cancelAlarm();
                    sharedPref.setQuran_facts_Notification(quranFactsNotifBoolean);
                }
                else {
                    quranFactsImage.setImageResource(R.drawable.img_on);
                    quranFactsNotifBoolean=true;
                    alarmClassFacts.cancelAlarm();
                    alarmClassFacts.setAlarm();
                    sharedPref.setQuran_facts_Notification(quranFactsNotifBoolean);
                }
            break;
            case R.id.tvTajweedQuran:
                LanguageDialog dialogTajweed=new LanguageDialog(this,true);
                dialogTajweed.showDialog();
                break;
            case R.id.tvQuranVocabulary:
                LanguageDialog dialogVocabulary=new LanguageDialog(this,false);
                dialogVocabulary.showDialog();
                break;
            default:
            break;

        }
    }
    public class LanguageDialog {

        private Context context;
        private SettingsSharedPref sharedPref;
        private int value;
        Activity mActivity;
        boolean isTajweed=false;

        AnalyticSingaltonClass mAnalyticSingaltonClass;
        public LanguageDialog(Context context, boolean isTajweed) {
            this.context = context;
            mActivity = (AppCompatActivity) context;
            this.isTajweed=isTajweed;
        }

        public void showDialog( ) {
            sharedPref = new SettingsSharedPref(context);
            mAnalyticSingaltonClass = AnalyticSingaltonClass.getInstance(context);
            if(isTajweed)
            {
                value = sharedPref.getLanguageTajweed();
            }
            else
            {
                value = sharedPref.getLanguageQuranVocabulary();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Set Language")
                    .setSingleChoiceItems(R.array.eng_urdu, value, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            value = which;
                            if (value == 0) {

                                mAnalyticSingaltonClass.sendEventAnalytics("Language Dialogue", "English Mode On");
                            } else {
                                mAnalyticSingaltonClass.sendEventAnalytics("Language Dialogue", "Urdu Mode On");
                            }
                        }
                    })
                    .setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        if(isTajweed)
                                        {
                                            if(value==0)
                                            {
                                                tvTajweedLanguge.setText(R.string.setUrdu);
                                            }
                                            else{
                                                tvTajweedLanguge.setText(R.string.setEnglish);
                                            }

                                            sharedPref.setLanguageTajweed(value);
                                        }
                                        else
                                        {
                                            if(value==0)
                                            {
                                                tvQuranVocabularyLanguage.setText(R.string.setUrdu);
                                            }
                                            else{
                                                tvQuranVocabularyLanguage.setText(R.string.setEnglish);
                                            }
                                            sharedPref.setLanguageQuranVocabulary(value);
                                        }


                                        //languageInterface.setLanguage();
                                    } catch (Exception e)

                                    {

                                    }


                                }
                            }).create().show();
        }
    }

}
