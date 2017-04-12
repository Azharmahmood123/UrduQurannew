package com.packageapp.wordbyword;
import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class WordByWord extends AppCompatActivity {
    //ImageView adImage;
    AdView adview;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;

    private int surahPos, totalAyahs, selectedTabPosition = 1, pos, faceArabic = 1, fontSizeIndex, arabicFont, englishFont, topPadding = 15;
    private String device;
    private int delayIndex = 0, play = 0, currentPlayingPosition = 0, delayIndexWords = 0, currentIndex = 0;
    private boolean rowClick = false, firstTimeAudio = true, chkRowLastIndex = false, audioShuldBeNull = false, isPlayingWordByWord = false, isShareClicked = false;
    private boolean isWordTapped = false, isSingleWordPlaying = false;

    private GlobalClass globalObject;
    private SettingsSharedPref preferences;

    TextView fullSurahTv, wordByWordTv, duaTitle, ayahArabic, ayahTranslationTv, ayahTransliterationTv;
    public static ImageView playButton, stopButton, favouriteButton;
    View fullSuraView, wordSurahView;
    ListView fullSurahList, wordsList;
    LinearLayout wordLayout, fullSuraLayout;
    ImageView nextWordButton, previousWordButton, playWordBtn, shareButton;

    private String[] duaAyahList, duaTranslationList, duaTransliterationList, arabicWordsList, translationWordsList, transliterationWordsList, verse_words = null;
    private String[][] fullDuaArabicWord, fullDuaTranslationWords, fullDuaTransliterationWords;
    //private ArrayList<DataModel> duaAyahData = new ArrayList<DataModel>();
    private int[][] timeDuaQanootWords;
    private int[] timeFullDua;

    //private FullDuaQanootListAdapter fullDuaQanootAdapter;
    private DuaQanootWbWListAdapter wordAdapter;
    private final Handler handler = new Handler();
    private MediaPlayer mediaPlayerWordByWord, mediaPlayerFullSurah = null;

    // static var
    public static String highlightedWordForFullDuaQanoot = "", highlightedWordForWbWDuaQanoot = "";
    public static int verse_length = 0;
    public static Boolean isFullAudioPlaying = false, isDialogueDisplayed = false;
    ;
    public static int lastIndex = 0;
    public static final int requestDownload = 3;
    public static int ayahPosWordbWordQanoot = 0, ayahPosFullDuaQanot = 0, wordPosQanoot = 0, DUAQANOOTADSCOUNTER = 1;

    Activity activitydetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_by_word);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activitydetail = WordByWord.this;
        IntentFilter tappedWordFilter = new IntentFilter();
        tappedWordFilter.addAction("duaWordTapBroadcast");
        initialize();
        initializeAds();

        telephonyManeger = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        if (telephonyManeger != null) {
            telephonyManeger.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
    }

    private void initialize() {

        globalObject = (GlobalClass) getApplicationContext();
        preferences = new SettingsSharedPref(this);



        //device = globalObject.device;
        // GlobalClass.AudioFileName = "DuaQanoot";

        playButton = (ImageView) findViewById(R.id.playButon);
        stopButton = (ImageView) findViewById(R.id.stopButton);
        shareButton = (ImageView) findViewById(R.id.shareButton);
        favouriteButton = (ImageView) findViewById(R.id.favButon);

        fullSurahTv = (TextView) findViewById(R.id.fullSuraText);
        wordByWordTv = (TextView) findViewById(R.id.wordByWordText);

        duaTitle = (TextView) findViewById(R.id.titleText);
        duaTitle.setText("Dua-e-Qunoot");
        duaTitle.setTypeface(globalObject.robotoMedium);

        fullSuraView = (View) findViewById(R.id.fullSuraView);
        wordSurahView = (View) findViewById(R.id.wordSuraView);
        fullSuraView.setVisibility(View.INVISIBLE);
        wordSurahView.setVisibility(View.VISIBLE);
        fullSurahTv.setTypeface(globalObject.robotoLight);
        wordByWordTv.setTypeface(globalObject.robotoLight, Typeface.BOLD);
        fullSurahTv.setTextColor(getResources().getColor(R.color.tabsTextColr));
        wordByWordTv.setTextColor(getResources().getColor(R.color.white));
        wordLayout = (LinearLayout) findViewById(R.id.wordBywordLAyout);
        ayahArabic = (TextView) findViewById(R.id.textArabicWord);
        ayahTranslationTv = (TextView) findViewById(R.id.textTranslationWord);
        ayahTransliterationTv = (TextView) findViewById(R.id.textTransliterationWord);
        wordsList = (ListView) findViewById(R.id.ayahListView);

        previousWordButton = (ImageView) findViewById(R.id.backWordButton);
        nextWordButton = (ImageView) findViewById(R.id.nextWordButton);
        playWordBtn = (ImageView) findViewById(R.id.playWord);

    }

    // ///////////////////////
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
        super.onResume();
        if (!globalObject.isPurchase) {
            startAdsCall();
        } else {
            adview.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (!globalObject.isPurchase) {
            stopAdsCall();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (!globalObject.isPurchase) {
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

    TelephonyManager telephonyManeger;
    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        private boolean callCheck = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (mediaPlayerFullSurah != null) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Incoming call: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (mediaPlayerFullSurah.isPlaying()) {
                        callCheck = true;
                        mediaPlayerFullSurah.pause();
                    }
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // Not in call: Play Audio
                    if (callCheck && mediaPlayerFullSurah != null) {
                        callCheck = false;
                        // handler.removeCallbacks(sendUpdatesToUI);
                        // handler.postDelayed(sendUpdatesToUI, 0);
                        mediaPlayerFullSurah.start();
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // A call is dialing, active or on hold: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (mediaPlayerFullSurah.isPlaying()) {
                        callCheck = true;
                        mediaPlayerFullSurah.pause();
                    }
                }
            }

            if (mediaPlayerWordByWord != null) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Incoming call: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (mediaPlayerWordByWord.isPlaying()) {
                        callCheck = true;
                        mediaPlayerWordByWord.pause();
                    }
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // Not in call: Play Audio
                    if (callCheck && mediaPlayerWordByWord != null) {
                        callCheck = false;
                        // handler.removeCallbacks(sendUpdatesToUI);
                        // handler.postDelayed(sendUpdatesToUI, 0);
                        mediaPlayerWordByWord.start();
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // A call is dialing, active or on hold: Pause Audio
                    // handler.removeCallbacks(sendUpdatesToUI);
                    if (mediaPlayerWordByWord.isPlaying()) {
                        callCheck = true;
                        mediaPlayerWordByWord.pause();
                    }
                }
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };
}

