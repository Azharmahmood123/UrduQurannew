package com.packageapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.QuranReading.helper.AlarmCalss;
import com.QuranReading.removeads.RemoveAdsActivity;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.AboutActivity;
import com.QuranReading.urduquran.BookmarksActivity;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.RateUsDialog;
import com.QuranReading.urduquran.RubanaDuasActivity;
import com.QuranReading.urduquran.SettingsActivity;
import com.QuranReading.urduquran.SplashActivity;
import com.QuranReading.urduquran.SurahActivity;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.QuranReading.urduquran.quranfacts.AlarmClassFacts;
import com.QuranReading.urduquran.quranfacts.FactsViewPager;
import com.QuranReading.urduquran.quranfacts.QuranFactsList;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.packageapp._13linequran.MainActivity13LineQuran;
import com.packageapp.adapters.HomeScreenAdapter;
import com.packageapp.quranvocabulary.MainActivity;
import com.packageapp.quranvocabulary.generalhelpers.DBManager;
import com.packageapp.quranvocabulary.notifications.DailyNotification;
import com.packageapp.tajweedquran.TajweedActivity;
import com.packageapp.tajweedquran.alarmnotifications.WeeklyNotifications;
import com.packageapp.wordbyword.SurahListActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.content.pm.PackageManager.*;

public class HomeSplashActivity extends AppCompatActivity {
    ViewPager viewPager;
    ImageView imgDot1, imgDot2, imgDot3;
    AdView adview;
    Boolean chkdualActivity = false;
    //ImageView adImage;
    private static Activity mActivity = null;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;
    GlobalClass mGlobal;
    Context context;
    LinearLayout quranLayout;
    public static final int requestBookmark = 2;
    private SettingsSharedPref settngPref;
    private int appExitDialogPos = 0;
    private Bundle bundle;
    private long lastClick = 0;
    private DBManager dbManager;
    private AlarmClassFacts alarmClassFacts;
    private DailyNotification alarmNotification;
    private WeeklyNotifications weeklyNotification;
    //text views for  main page
    TextView tvUrduQuran, tvHeaderTitle;
    InterstitialAdSingleton mInterstitialAdSingleton;
    AnalyticSingaltonClass mAnalyticSingaltonClass;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_splash);
        mGlobal = ((GlobalClass) getApplicationContext());
        mGlobal.triggerAd();
        settngPref = new SettingsSharedPref(HomeSplashActivity.this);
        mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(this);
        mAnalyticSingaltonClass = AnalyticSingaltonClass.getInstance(this);
        mAnalyticSingaltonClass.sendScreenAnalytics("Home_Screen");
        settngPref.setAppActive(true);
        mActivity = this;
        context = this;
        mAnalyticSingaltonClass = AnalyticSingaltonClass.getInstance(context);
        imgDot1 = (ImageView) findViewById(R.id.dot1);
        imgDot2 = (ImageView) findViewById(R.id.dot2);
        imgDot3 = (ImageView) findViewById(R.id.dot3);
        tvUrduQuran = (TextView) findViewById(R.id.tvTitle);
        tvHeaderTitle = (TextView) findViewById(R.id.homeActionBar);
        tvHeaderTitle.setTypeface(mGlobal.faceHeading);
        tvUrduQuran.setTypeface(mGlobal.faceHeading);
        quranLayout = (LinearLayout) findViewById(R.id.quranTranlsatlion);
        viewPager = (ViewPager) findViewById(R.id.homeScreenViewPager);
        viewPager.setAdapter(new HomeScreenAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        imgDot1.setImageResource(R.drawable.green_dotcircle);
                        imgDot2.setImageResource(R.drawable.brown_dotcircle);
                        imgDot3.setImageResource(R.drawable.brown_dotcircle);
                        break;
                    case 1:
                        imgDot2.setImageResource(R.drawable.green_dotcircle);
                        imgDot1.setImageResource(R.drawable.brown_dotcircle);
                        imgDot3.setImageResource(R.drawable.brown_dotcircle);
                        break;
                    case 2:
                        imgDot3.setImageResource(R.drawable.green_dotcircle);
                        imgDot1.setImageResource(R.drawable.brown_dotcircle);
                        imgDot2.setImageResource(R.drawable.brown_dotcircle);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (settngPref.getFirstTime()) {
            showPrivacyPolicyDialog();
            settngPref.setFirstTime();
        }
        initializeAds();
        initDbQuranVocabulary();
        setAllNotifications();
        handleAllNotification();
    }

    public void initDbQuranVocabulary() {
        dbManager = new DBManager(this);
        dbManager.open();
        try {
            dbManager.createDataBase();
        } catch (Exception ex) {
        }
    }

    private void setAllNotifications() {
        alarmClassFacts = new AlarmClassFacts(context); //Quran Facts Notification
        alarmNotification = new DailyNotification(context);// Quran Vocabulary Notifications
        weeklyNotification = new WeeklyNotifications(context); // Tajweed Alarm Class
        AlarmCalss mAlarmHelper = new AlarmCalss(this); //Quran Surah Notifications
        HashMap<String, Boolean> tranSettngs = settngPref.getTransSettings(); //
        if (tranSettngs.get(SettingsSharedPref.NOTIFICATION)) {
            mAlarmHelper.cancelAlarm();
            mAlarmHelper.setAlarm();
        }

        if (settngPref.getTajweed_Notification()) {
            weeklyNotification.setDailyAlarm();
        }
        if (settngPref.getVocabulary_Notification()) {
            alarmNotification.setDailyAlarm();
        }
        if (settngPref.getQuran_facts_Notification()) {
            alarmClassFacts.setAlarm();
        }

    }

    private void handleAllNotification() {
        Intent mExtras = getIntent();
        bundle = mExtras.getExtras();

        if (bundle != null && bundle.containsKey("detailID")) {
            int pageNumber = bundle.getInt("detailID", -1);
            if (pageNumber != -1) {
                mAnalyticSingaltonClass.sendEventAnalytics("Notification", "TajweedNotification_Tapped");
                startActivity(new Intent(HomeSplashActivity.this, TajweedActivity.class).putExtra("detailID", pageNumber));
                bundle = null;

            }
        } else if (bundle != null && bundle.containsKey("cat_id")) {
            int id = bundle.getInt("cat_id", -1);
            String categoryName = bundle.getString("cat_name");
            mAnalyticSingaltonClass.sendEventAnalytics("Notification", "VocabularyNotificaion_Tapped");
            ;
            startActivity(new Intent(HomeSplashActivity.this, MainActivity.class).putExtra("cat_id", id).putExtra("cat_name", categoryName));
            bundle = null;
        } else if (bundle != null && bundle.containsKey("tabPosition")) {
            mAnalyticSingaltonClass.sendEventAnalytics("Notification", "QuranFacts_Tapped");
            startActivity(new Intent(HomeSplashActivity.this, QuranFactsList.class).putExtra("listItemPos", bundle.getInt("listItemPos")).putExtra("tabPosition", bundle.getInt("tabPosition")
            ).putExtra("data", bundle.getStringArray("data")));
            bundle = null;
        } else if (bundle != null && bundle.containsKey("SURAHDAYNO")) {
            mAnalyticSingaltonClass.sendEventAnalytics("Notification", "QuranNotification_Tapped");
            startActivity(new Intent(HomeSplashActivity.this, SurahActivity.class).putExtra("NOTIFICATIONOCCURRED", bundle.getBoolean("NOTIFICATIONOCCURRED")).putExtra("SURAHDAYNO", bundle.getInt("SURAHDAYNO", -1)
            ).putExtra("AYANO", bundle.getInt("AYANO", -1)));
            bundle = null;
        }

    }

    //Privacy policy dialogue
    private void showPrivacyPolicyDialog() {
        String messageText = getString(R.string.txt_privacyText);
        String link = "http://www.quranreading.com/apps/Quran-with-Urdu-Translation-privacy-policy.html";
        String text = "Urdu Quran Privacy Policy.";
        messageText += " " + "<a href=\"" + link + "\">" + text + "</a>";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.txt_titele_privacy);
        builder.setMessage((Html.fromHtml(messageText)));
        builder.setPositiveButton(R.string.txt_accept, null);
        builder.setCancelable(false);
        AlertDialog dialog = builder.show();
        TextView tvMessage = (TextView) dialog.findViewById(android.R.id.message);
        tvMessage.setClickable(true);
        tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
        dialog.show();
    }


    private void initializeAds() {
        adview = (AdView) findViewById(R.id.adView);
        //adImage = (ImageView) findViewById(R.id.adimg);
        // adImage.setVisibility(View.GONE);
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
        chkdualActivity = false;
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
        settngPref.setAppActive(false);
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
  /*  private void showInterstitialAd(){
        if(!mGlobal.isPurchase)
        {
            mInterstitialAdSingleton.showInterstitial();
        }
    }*/

    public void homeClickEvent(View view) {
        Toast toast;
        if (SystemClock.elapsedRealtime() - lastClick < 1000) {
            return;
        }
        lastClick = SystemClock.elapsedRealtime();
        switch (view.getId()) {
            case R.id.urduQuran:
                mAnalyticSingaltonClass.sendEventAnalytics("Quran_Access", "Quran small_Tap");
                startActivity(new Intent(HomeSplashActivity.this, SurahActivity.class));
                //showInterstitialAd();
                //will include in futrue
                // startActivity(new Intent(HomeSplashActivity.this, MainActivity13LineQuran.class));
                break;
            case R.id.quranTranlsatlion:
                mAnalyticSingaltonClass.sendEventAnalytics("Quran_Access", "Quran Big_Tap");
                /*indexActivity.putExtra("NOTIFICATIONOCCURRED", true);
            indexActivity.putExtra("SURAHDAYNO", getIntent().getIntExtra("SURAHDAYNO", -1));
			indexActivity.putExtra("AYANO", getIntent().getIntExtra("AYANO", -1));*/
                startActivity(new Intent(HomeSplashActivity.this, SurahActivity.class));
                // showInterstitialAd();
                break;
            case R.id.wordbyword:

                startActivity(new Intent(HomeSplashActivity.this, SurahListActivity.class));
                //showInterstitialAd();
                break;
            case R.id.quranVocabulary:
                startActivity(new Intent(HomeSplashActivity.this, com.packageapp.quranvocabulary.MainActivity.class));
                // showInterstitialAd();


                break;
            case R.id.learnTajweed:
                startActivity(new Intent(HomeSplashActivity.this, TajweedActivity.class));
                //showInterstitialAd();
                break;
            case R.id.sajdahs:
                mAnalyticSingaltonClass.sendEventAnalytics("Quran_Access", "Sajdah_Tap");
                mGlobal.saveonpause = false;
                Intent sajdahsActivity = new Intent(HomeSplashActivity.this, BookmarksActivity.class);
                sajdahsActivity.putExtra("FROM", "HOMESPLASHSCREEN");
                startActivity(sajdahsActivity);
                // showInterstitialAd();
                break;
            case R.id._100QuranFacts:
                mAnalyticSingaltonClass.sendEventAnalytics("Quran_Access", "Quran_FactsTap");
                startActivity(new Intent(this, QuranFactsList.class));
                //showInterstitialAd();
                break;
            case R.id.btn_rate_us:
                openRateUs(false);
                break;
            case R.id.share:
                shareApp();
                break;
            case R.id.rabanaDuas:
                mAnalyticSingaltonClass.sendEventAnalytics("Quran_Access", "RabanaTap");
                mGlobal.saveonpause = false;
                Intent rabanaDuasActivity = new Intent(HomeSplashActivity.this, RubanaDuasActivity.class);
                rabanaDuasActivity.putExtra("FROM", "RabanaduasHOMESPLASHSCREEN");
                startActivity(rabanaDuasActivity);
                //showInterstitialAd();
                break;
            case R.id.settings:
                mGlobal.saveonpause = false;
                Intent settingsActivity = new Intent(HomeSplashActivity.this, SettingsActivity.class);
                startActivity(settingsActivity);
                //showInterstitialAd();
                break;
            case R.id.btn_more:
                moreApps();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=Quran+Reading"));
//                startActivity(browserIntent);
                break;
            case R.id.Premium:
                mAnalyticSingaltonClass.sendEventAnalytics("Quran_Access", "Premium");
                Toast.makeText(context, "Not Available", Toast.LENGTH_SHORT).show();
//                showRemoveAdDilog(false);
                break;
            case R.id.btn_about:
                Intent aboutActivity = new Intent(HomeSplashActivity.this, AboutActivity.class);
                mGlobal.saveonpause = false;
                startActivity(aboutActivity);
                //showInterstitialAd();

                break;

        }
    }

//    url="";
//    final PackageManager packageManager = context.getPackageManager();
//
//    try
//
//    {
//        final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
//        String intaller_name = packageManager.getInstallerPackageName(applicationInfo.packageName);
//        Log.i("InstallerName", intaller_name);
//        if ("com.android.vending".equals(packageManager.getInstallerPackageName(applicationInfo.packageName))) {
//            // App was installed by Play Store
//            url = "market://search?q=pub:Quran+Reading"; // For show all apps in google
////                https://play.google.com/store/apps/details?id=com.quranreading.sahihmuslimurdu
////                url = "https://play.google.com/store/apps/details?id=com.QuranReading.urduquran";
//        } else if ((packageManager.getInstallerPackageName(applicationInfo.packageName).startsWith("com.amazon"))) {
//            url = "http://www.amazon.com/gp/mas/dl/android?p=com.QuranReading.urduquran&showAll=1";
//            //for show all
//
//        } else {
//            url = "";
//        }
//    }
//
//    catch(
//    final NameNotFoundException e
//    )
//
//    {
//        e.printStackTrace();
//    }
//
//    Intent browserIntent = new Intent(Intent.ACTION_SEND, Uri.parse(url));
//    context.startActivity(browserIntent);
//}

    public void moreApps() {
        getPackageInfo("MoreApps");

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    private void getImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_index);
        File sd = getExternalCacheDir();
        String fileName = "ic_launcher.png";
        File dest = new File(sd, fileName);
        try {
            FileOutputStream out;
            out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void getPackageInfo(String text) {

        final PackageManager packageManager = context.getPackageManager();

        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            if ("com.android.vending".equals(packageManager.getInstallerPackageName(applicationInfo.packageName))) {
                if (text.equals("ShareApp")) {
                    url = "https://play.google.com/store/apps/details?id=com.QuranReading.urduquran";
                } else {
                    url = "market://search?q=pub:Quran+Reading";
                }
            } else if ((packageManager.getInstallerPackageName(applicationInfo.packageName).startsWith("com.amazon"))) {
                if (text.equals("ShareApp")) {
                    url = "http://www.amazon.com/gp/mas/dl/android?p=com.QuranReading.urduquran";
                } else {
                    url = "http://www.amazon.com/gp/mas/dl/android?p=com.QuranReading.urduquran&showAll=1";
                }

            } else {
                url = "";
            }

        } catch (final PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void shareApp() {
        try {
            getPackageInfo("ShareApp");
            getImage();
            String fileName = "ic_launcher.png";
            String completePath = getExternalCacheDir() + "/" + fileName;

            File file = new File(completePath);
            Uri imageUri = Uri.fromFile(file);
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//            String text = "I just found this Beautiful Islamic App \"Urdu Quran\" on Play store - Download Free Now\n" +
//                    "https://play.google.com/store/apps/details?id=com.QuranReading.urduquran";

            String text = "I just found this Beautiful Islamic App \"Urdu Quran\" on Play store - Download Free Now\n" +
                    url;
            shareIntent.setType("*/*");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Urdu Quran");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } catch (Exception ex) {
        }
    }

    private void openRateUs(boolean exitFromApp) {
        appExitDialogPos = 0;
        Intent rateUs = new Intent(HomeSplashActivity.this, RateUsDialog.class);
        rateUs.putExtra("EXITFROMAPP", exitFromApp);
        startActivity(rateUs);
        settngPref.setPosForExitDialog(appExitDialogPos);
    }

    public static void Finish() {
        if (mActivity != null) {
            mActivity.finish();
        }

    }

//    public void showRemoveAdDilog(boolean ExitFromApp) {
//        if (!chkdualActivity) {
//            chkdualActivity = true;
//            Intent removeAdDialog = new Intent(HomeSplashActivity.this, RemoveAdsActivity.class);
//            removeAdDialog.putExtra("EXITFROMAPP", ExitFromApp);
//            startActivity(removeAdDialog);
//            // startActivityForResult(removeAdDialog, requestRemoveAd);
//        }
//    }

    @Override
    public void onBackPressed() {
        appExitDialogPos = settngPref.getPosForExitDialog();
        if (appExitDialogPos == 1) {
            openRateUs(true);
        } else {
            appExitDialogPos++;
//            if (!((GlobalClass) getApplication()).isPurchase) {
////                showRemoveAdDilog(true);
//            } else {
            openRateUs(true);
            //  settngPref.setSurahNo(suraPosition);
            // settngPref.setAyano(((GlobalClass) getApplication()).ayahPos);
//            }
            settngPref.setPosForExitDialog(appExitDialogPos);

        }

    }
}