package com.packageapp.wordbyword;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.QuranReading.sharedPreference.PurchasePreferences;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SurahDetailActivity extends FragmentActivity {
    //ImageView adImage;
    AdView adview;
    String url;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;
    public boolean isPlayingWordByWord = false, isPlayingWordByWordKalma = false, isPlayingWordByWordDua = false;
    int faceArabic = 1, fontSizeIndex, arabicFont, englishFont, topPadding = 15;

    private String title, device, favSurahString = "";
    private String[] titleList;
    private ArrayList<String> favSuraList = new ArrayList<String>();
    private boolean isShareClicked = false;
    private ViewPager viewPager;
    private DetailPageAdapter pageAdapter;
    private GlobalClass globalObject;
    private SettingsSharedPref preferences;

    TextView fullSurahTv, wordByWordTv;
    ImageView shareButton;
    TextView suraTitle;
    View fullSuraView, wordSurahView;

    public static ImageView playButton, stopButton, favouriteButton;
    public static boolean ISBACKPRESSED = false;
    private long stopClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initialize();
        initializeAds();
        telephonyManeger = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManeger != null) {
            telephonyManeger.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void initializeAds() {
        adview = (AdView) findViewById(R.id.adView);
        adview.setVisibility(View.GONE);
        if (isNetworkConnected()) {
            this.adview.setVisibility(View.VISIBLE);
        } else {
            this.adview.setVisibility(View.GONE);
        }
        setAdsListener();

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

    private void initialize() {
        int surahPoss;
        globalObject = (GlobalClass) getApplicationContext();
        preferences = new SettingsSharedPref(this);
        titleList = getResources().getStringArray(R.array.surah_list_names);
        boolean isFromFav = getIntent().getBooleanExtra("FromFavourite", false);

        if (isFromFav) {
            title = getIntent().getStringExtra("duaTitle");
            globalObject.surahPos = (Arrays.asList(titleList)).indexOf(title);
            surahPoss = (Arrays.asList(titleList)).indexOf(title);
        } else {
            title = getIntent().getStringExtra("Title");
            globalObject.surahPos = getIntent().getIntExtra("position", 0);
            surahPoss = getIntent().getIntExtra("position", 0);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        playButton = (ImageView) findViewById(R.id.playButon);
        stopButton = (ImageView) findViewById(R.id.stopButton);
        shareButton = (ImageView) findViewById(R.id.shareButton);
        favouriteButton = (ImageView) findViewById(R.id.favButon);
        fullSurahTv = (TextView) findViewById(R.id.fullSuraText);
        wordByWordTv = (TextView) findViewById(R.id.wordByWordText);
        suraTitle = (TextView) findViewById(R.id.titleText);
        suraTitle.setText("Surah " + title);

        suraTitle.setTypeface(globalObject.faceHeading);

        fullSuraView = (View) findViewById(R.id.fullSuraView);
        wordSurahView = (View) findViewById(R.id.wordSuraView);

        getPreferences();
        //loadSettings();

        // ////////////////getFavorites////////////////
        if (!favSurahString.equals("")) {
            favSuraList.clear();
            String[] arr = favSurahString.split(",");
            for (int index = 0; index < arr.length; index++) {
                favSuraList.add(arr[index].trim());
            }
        }

        if (favSuraList.contains(title)) {
            favouriteButton.setImageResource(R.drawable.fav_h_w);
        } else {
            favouriteButton.setImageResource(R.drawable.favourite_selctor_file);
        }

        if (globalObject.selectedTabPosition == 1) {
            fullSuraView.setVisibility(View.VISIBLE);
            wordSurahView.setVisibility(View.INVISIBLE);

            fullSurahTv.setTypeface(globalObject.robotoLight, Typeface.BOLD);
            wordByWordTv.setTypeface(globalObject.robotoLight);

            fullSurahTv.setTextColor(getResources().getColor(R.color.white));
            wordByWordTv.setTextColor(getResources().getColor(R.color.tabsTextColr));

            // ////calibartion///////////
            if (preferences.getWordBWordCheck()) {
                final RelativeLayout calibrationLayout = (RelativeLayout) findViewById(R.id.calibrationlayoutWbW);
                calibrationLayout.setVisibility(View.VISIBLE);

                Button ok = (Button) findViewById(R.id.ok);
                ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        calibrationLayout.setVisibility(View.GONE);
                        preferences.setWordBWordCheck(false);

                    }
                });
            }
        } else {
            fullSuraView.setVisibility(View.INVISIBLE);
            wordSurahView.setVisibility(View.VISIBLE);

            fullSurahTv.setTypeface(globalObject.robotoLight);
            wordByWordTv.setTypeface(globalObject.robotoLight, Typeface.BOLD);

            fullSurahTv.setTextColor(getResources().getColor(R.color.tabsTextColr));
            wordByWordTv.setTextColor(getResources().getColor(R.color.white));

            if (preferences.getWordBWordCheck()) {
                final RelativeLayout calibrationLayout = (RelativeLayout) findViewById(R.id.calibrationlayoutWbW);
                calibrationLayout.setVisibility(View.VISIBLE);

                Button ok = (Button) findViewById(R.id.okWbW);
                ok.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        calibrationLayout.setVisibility(View.GONE);
                        preferences.setWordBWordCheck(false);

                    }
                });
            }
        }

        pageAdapter = new DetailPageAdapter(getSupportFragmentManager(), titleList, surahPoss, this);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(surahPoss);
        globalObject.mediaPlayerFullSurah = null;
        globalObject.mediaPlayerWordByWord = null;

        viewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                title = titleList[arg0];
                suraTitle.setText("Surah " + title);

                globalObject.surahPos = arg0;
                viewPager.setCurrentItem(globalObject.surahPos);

                if (favSuraList.contains(title)) {
                    favouriteButton.setImageResource(R.drawable.fav_h_w);
                } else {
                    favouriteButton.setImageResource(R.drawable.favourite_selctor_file);
                }

                resetFullSuraMediaPlayer();
                resetWordMediaPlayer();
                resetValues();

                Intent intent = new Intent("PageChangeBroadcastSura");
                sendBroadcast(intent);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public void onTabSelection(View view) {
        int tag = Integer.parseInt(view.getTag().toString());

        if (tag == 1) {
            if (globalObject.selectedTabPosition == 1) {
            } else {
                fullSuraView.setVisibility(View.VISIBLE);
                wordSurahView.setVisibility(View.INVISIBLE);

                fullSurahTv.setTypeface(globalObject.robotoLight, Typeface.BOLD);
                wordByWordTv.setTypeface(globalObject.robotoLight);

                fullSurahTv.setTextColor(getResources().getColor(R.color.white));
                wordByWordTv.setTextColor(getResources().getColor(R.color.tabsTextColr));
                globalObject.selectedTabPosition = 2;

                Intent intent1 = new Intent();
                intent1.setAction("TabChangeBroadcast");
                this.sendBroadcast(intent1);
            }
        } else {
            if (globalObject.selectedTabPosition == 2) // if on same tab
            {
            } else {
                fullSuraView.setVisibility(View.INVISIBLE);
                wordSurahView.setVisibility(View.VISIBLE);

                fullSurahTv.setTypeface(globalObject.robotoLight);
                wordByWordTv.setTypeface(globalObject.robotoLight, Typeface.BOLD);

                fullSurahTv.setTextColor(getResources().getColor(R.color.tabsTextColr));
                wordByWordTv.setTextColor(getResources().getColor(R.color.white));

                globalObject.selectedTabPosition = 2;

                if (preferences.getWordBWordCheck()) {
                    final RelativeLayout calibrationLayout = (RelativeLayout) findViewById(R.id.calibrationlayoutWbW);
                    calibrationLayout.setVisibility(View.VISIBLE);
                    LinearLayout adLayout = (LinearLayout) calibrationLayout.findViewById(R.id.adLayout);
                    if (!isNetworkConnected()) {

                        adLayout.getLayoutParams().height = 1;
                    } else {
                        adLayout.setVisibility(View.VISIBLE);
                    }

                    Button ok = (Button) findViewById(R.id.okWbW);
                    ok.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            calibrationLayout.setVisibility(View.GONE);
                            preferences.setWordBWordCheck(false);

                        }
                    });
                }
                Intent intent1 = new Intent();
                intent1.setAction("TabChangeBroadcast");
                this.sendBroadcast(intent1);
            }
        }
    }

    public void buttonClick(View view) {
        if (SystemClock.elapsedRealtime() - stopClick < 1000) {
            return;

        }
        stopClick = SystemClock.elapsedRealtime();
        int tag = Integer.parseInt(view.getTag().toString());
        switch (tag) {
            case 1:
                play();
                break;

            case 2:
                stop();
                break;

            case 3:
                favourite();
                break;

            case 4:
                if (!isShareClicked) {
                    isShareClicked = true;
                    share();
                }
                break;

            case 5:
                onBackPressed();

            default:
                break;

        }
    }

    public void play() {

        Intent intent11 = new Intent();
        intent11.setAction("PlayAudioBroadcastSurah");
        this.sendBroadcast(intent11);
    }

    private void favourite() {
        if (favSuraList.contains(title)) {
            favSuraList.remove(title);
            favouriteButton.setImageResource(R.drawable.favourite_selctor_file);
        } else {
            favSuraList.add(title);
            favouriteButton.setImageResource(R.drawable.fav_h_w);
        }
    }

    private void stop() {
        SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
        if (globalObject.selectedTabPosition == 1) {
            resetFullSuraMediaPlayer();
        } else {
            resetWordMediaPlayer();
        }

        Intent intenttt = new Intent();
        intenttt.setAction("StopAudioBroadcastSura");
        this.sendBroadcast(intenttt);
    }

    private void share() {
        String duaArabic = "", duaTranslation = "";

        int ayahListId = this.getResources().getIdentifier(title.replace("-", "_"), "array", this.getPackageName());
        String[] ayahList = this.getResources().getStringArray(ayahListId);

        int ayahListTranslationId = this.getResources().getIdentifier(title.replace("-", "_") + "_translation", "array", this.getPackageName());
        String[] ayahTranslationList = this.getResources().getStringArray(ayahListTranslationId);

        for (int i = 0; i < ayahList.length; i++) {
            duaArabic = duaArabic.concat(String.valueOf(ayahList[i]));
            duaArabic = duaArabic.concat("\n");
        }

        for (int i = 0; i < ayahTranslationList.length; i++) {
            duaTranslation = duaTranslation.concat(String.valueOf(ayahTranslationList[i]));
            duaTranslation = duaTranslation.concat("\n");
        }
        getPackageInfo("ShareApp");
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("text/plain");
        actionIntent.putExtra(Intent.EXTRA_SUBJECT, "Urdu Quran");
        actionIntent.putExtra(Intent.EXTRA_TEXT,
                "Surah Name:\n" + "      " + title + "\n\n" + "Surah:\n" + duaArabic + "\n\n" + "Translation:\n" + duaTranslation.replace("-", "") + "\nLearn to recite word by word Surahs from Urdu Quran & More" + "\n" + url);
        startActivityForResult(Intent.createChooser(actionIntent, "Share via"), 0);

    }

    private void getPackageInfo(String text) {

        final PackageManager packageManager = SurahDetailActivity.this.getPackageManager();

        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(SurahDetailActivity.this.getPackageName(), 0);
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

    private void resetValues() // separate reset methods jst for stop functionality
    {
        globalObject.ayahPosWordbWord = 0;
        globalObject.ayahPosFullSura = 0;
        globalObject.wordPos = 0;
        SurahFragment.highlightedWordForWbW = "";
        SurahFragment.highlightedWordForFullSura = "";
    }

    public void resetWordMediaPlayer() {
        playButton.setImageResource(R.drawable.play_selector_file);
        if (globalObject.mediaPlayerWordByWord != null) {
            if (globalObject.isPlayingWordByWord) {
                globalObject.mediaPlayerWordByWord.pause();
                globalObject.isPlayingWordByWord = false;
                globalObject.mediaPlayerWordByWord.seekTo(0);
                globalObject.mediaPlayerWordByWord.release();
                globalObject.mediaPlayerWordByWord = null;
            } else {
                globalObject.mediaPlayerWordByWord.release();
                globalObject.mediaPlayerWordByWord = null;
            }
        }
        globalObject.mediaPlayerWordByWord = null;
    }

    public void resetFullSuraMediaPlayer() {
        playButton.setImageResource(R.drawable.play_selector_file);
        if (globalObject.mediaPlayerFullSurah != null) {

            globalObject.mediaPlayerFullSurah.release();
            globalObject.mediaPlayerFullSurah = null;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferences();
        if (globalObject.mediaPlayerWordByWord != null) {
            globalObject.mediaPlayerWordByWord.start();
        }
        if (globalObject.mediaPlayerFullSurah != null) {

            globalObject.mediaPlayerFullSurah.start();

        }

        //loadSettings();

        if (!globalObject.isPurchase) {
            startAdsCall();
        } else {
            adview.setVisibility(View.GONE);
        }

        isShareClicked = false;
    }

    @Override
    public void onBackPressed() {
        resetFullSuraMediaPlayer();
        resetWordMediaPlayer();
        resetValues();
        String favouriteSurahs = "";
        ISBACKPRESSED = true;
        globalObject.selectedTabPosition = 2;

        for (int i = 0; i < favSuraList.size(); i++) {
            favouriteSurahs += (favSuraList.get(i)).trim();

            if (i != favSuraList.size() - 1)
                favouriteSurahs += ",";
        }
        preferences.saveFavSuraList(favouriteSurahs);

        //FavoriteListAdapter.isItemClickedd = false;
        super.onBackPressed();
    }

    ;

    @Override
    protected void onPause() {

        super.onPause();
        if (globalObject.mediaPlayerWordByWord != null) {
            globalObject.mediaPlayerWordByWord.pause();
            playButton.setImageResource(R.drawable.play_selector_file);


        }
        if (globalObject.mediaPlayerFullSurah != null) {

            globalObject.mediaPlayerFullSurah.pause();

        }

        //  stop();
        if (!globalObject.isPurchase) {
            stopAdsCall();
        }
    }

    private void getPreferences() {
        HashMap<String, Integer> fontSettings = preferences.getSettings();
        faceArabic = fontSettings.get(SettingsSharedPref.FACEARABIC);
        fontSizeIndex = fontSettings.get(SettingsSharedPref.FONTINDEX);
        arabicFont = fontSettings.get(SettingsSharedPref.ARABICFONT);
        englishFont = fontSettings.get(SettingsSharedPref.ENGLISHFONT);
        favSurahString = preferences.getFavSurahsList();
    }

/*	private void loadSettings() {
        int fontSize_A_1[] = {};
		int fontSize_A_2[] = {};

		if (fontSizeIndex == -1) {
			fontSizeIndex = 2;
		}

		if (device.equals("medium")) {
			topPadding = 50;
			fontSize_A_1 = globalObject.fontSize_A_med;
			fontSize_A_2 = globalObject.fontSize_A_med_1;

			globalObject.font_size_eng = globalObject.fontSize_E_med[fontSizeIndex];
		}
		else if (device.equals("large")) {
			topPadding = 30;
			fontSize_A_1 = globalObject.fontSize_A_large;
			fontSize_A_2 = globalObject.fontSize_A_large_1;

			globalObject.font_size_eng = globalObject.fontSize_E_large[fontSizeIndex];
		}
		else {
			topPadding = 40;
			fontSize_A_1 = globalObject.fontSize_A_small;
			fontSize_A_2 = globalObject.fontSize_A_small_1;

			globalObject.font_size_eng = globalObject.fontSize_E_small[fontSizeIndex];
		}

		if (faceArabic == 2) {
			globalObject.ayahPadding = topPadding;
			globalObject.faceArabic = globalObject.faceArabic2;
			globalObject.font_size_arabic = fontSize_A_1[fontSizeIndex];
		}
		else if (faceArabic == 3) {
			globalObject.ayahPadding = 0;
			globalObject.faceArabic = globalObject.faceArabic3;
			globalObject.font_size_arabic = fontSize_A_1[fontSizeIndex];
		}
		else {
			globalObject.ayahPadding = 0;
			globalObject.faceArabic = globalObject.faceArabic1;
			globalObject.font_size_arabic = fontSize_A_2[fontSizeIndex];
		}
	}*/

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (!globalObject.isPurchase) {
            destroyAds();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager mgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
    }

	/*
	 * private void startInterestritialAd() { if(!((GlobalClass) getApplication()).isPurchase) { if(SURASADCOUNTER ==1) { InterstitialAdSingleton mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(this); mInterstitialAdSingleton.showInterstitial(); SURASADCOUNTER++; } else
	 * if(SURASADCOUNTER==3) { InterstitialAdSingleton mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(this); mInterstitialAdSingleton.showInterstitial(); SURASADCOUNTER =0; } else { SURASADCOUNTER++; } } }
	 */

    TelephonyManager telephonyManeger;
    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        private boolean callCheck = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (globalObject.mediaPlayerFullSurah != null) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Incoming call: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (globalObject.mediaPlayerFullSurah.isPlaying()) {
                        callCheck = true;
                        globalObject.mediaPlayerFullSurah.pause();
                    }
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // Not in call: Play Audio
                    if (callCheck && globalObject.mediaPlayerFullSurah != null) {
                        callCheck = false;
                        // handler.removeCallbacks(sendUpdatesToUI);
                        // handler.postDelayed(sendUpdatesToUI, 0);
                        globalObject.mediaPlayerFullSurah.start();
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // A call is dialing, active or on hold: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (globalObject.mediaPlayerFullSurah.isPlaying()) {
                        callCheck = true;
                        globalObject.mediaPlayerFullSurah.pause();
                    }
                }
            }

            if (globalObject.mediaPlayerWordByWord != null) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Incoming call: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (globalObject.mediaPlayerWordByWord.isPlaying()) {
                        callCheck = true;
                        globalObject.mediaPlayerWordByWord.pause();
                    }
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // Not in call: Play Audio
                    if (callCheck && globalObject.mediaPlayerWordByWord != null) {
                        callCheck = false;
                        // handler.removeCallbacks(sendUpdatesToUI);
                        // handler.postDelayed(sendUpdatesToUI, 0);
                        globalObject.mediaPlayerWordByWord.start();
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // A call is dialing, active or on hold: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (globalObject.mediaPlayerWordByWord.isPlaying()) {
                        callCheck = true;
                        globalObject.mediaPlayerWordByWord.pause();
                    }
                }
            }

            super.onCallStateChanged(state, incomingNumber);
        }
    };
}
