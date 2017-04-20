package com.QuranReading.urduquran;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.ScriptGroup;
import android.renderscript.Type;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.QuranReading.adapter.AudioTimeXMLParser;
import com.QuranReading.adapter.CustomAyaListAdapter;
import com.QuranReading.adapter.IndexListAdapter;
import com.QuranReading.adapter.XMLParser;
import com.QuranReading.helper.AlarmCalss;
import com.QuranReading.helper.DBManager;
import com.QuranReading.helper.XListView;
import com.QuranReading.helper.XListView.IXListViewListener;
import com.QuranReading.helper.XListViewFooter;
import com.QuranReading.model.IndexListModel;
import com.QuranReading.model.JuzModel;
import com.QuranReading.model.SurahModel;
import com.QuranReading.removeads.RemoveAdsActivity;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.ads.InterstitialAdListener;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.QuranReading.urduquran.R.array.revealedPlaces;

public class SurahActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, OnCompletionListener, OnErrorListener, SlidingMenu.OnCloseListener, InterstitialAdListener, IXListViewListener, TextWatcher {
    // google ads
    String url;
    AdView adview;
    ImageView adImage;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;
    int bookmarksBack = -1;
    private int counterAds = 0;
    int req;
    public static int secondAyaSelected = -5;
    private int positionIndexListClick;
    private int timerInterstitial = 0;
    Bundle bundle;
    private int appExitDialogPos = 0;
    Runnable runnableforAdsNotification;
    private static SurahActivity mActivity = null;
    private String bismillahText;
    private View viewRemoveAdLine;
    private TextView tvHeader, tvHeader2;
    private TextView tvReciter;
    private SettingsSharedPref settngPref;
    private MediaPlayer mp;
    private String suraName, audioFile;
    private ImageButton btnAudio, btnBack, btnNext;
    private Button btnTransprnt;
    private XmlPullParser xpp;
    private ProgressBar progressBar;
    private RelativeLayout innerMainLayout, tutorialLayout, removeAdLayout;
    private XListView ayahListView;
    private ListView indexList;
    private CustomAyaListAdapter customAdapter;
    private final Handler handler = new Handler();
    private final Handler handler2 = new Handler();
    private int surahIndex = 0, delayIndex = 0, play = 0; //f_index was not set i set it to 1
    public static int suraPosition = 1;
    private int dialogLanguagePosition;
    private String action = "";
    private int topPadding = 15, bookmarkAyahPos = -1, gotoIndex = 0, limit = 0, f_index, fontArabic, fontEnglish, reciter = 0, prev_Reciter = 0;
    private String word;
    private boolean chkTutorial = false, chkNotification = false, chkTafseer = false, playNextSurahAudio = false, audioFound = false, surahSelected = false, menuOpened = false, secMenuOpened = false, callCheck = false, chkTransliteration = true, chkCreate = true, rowClick = false, chkdualActivity = false,
            chkSettingSaved = false;
    public static final int LANGUAGE_NONE = 0;
    private boolean surahChange = false;
    private boolean playingState = false, reset = false;

    private ArrayList<Integer> timeAyahSurah = new ArrayList<Integer>();

    private List<SurahModel> surahList = new ArrayList<SurahModel>();
    private ArrayList<IndexListModel> dataList = new ArrayList<IndexListModel>();
    private ArrayList<IndexListModel> surahNamesList = new ArrayList<IndexListModel>();
    private TelephonyManager telephonyManeger;
    private SlidingMenu slideMenu;
    private IndexListAdapter listAdapter;

    public static final String actionGoto = "goto";
    public static final String actionRefresh = "refresh";
    public static final String actionSettings = "settings";
    public static final String actionRefreshBookmark = "refreshbookmark";
    public static String actionHomeScreen = "homescreen";

    public static final int requestGoto = 1;
    public static final int requestBookmark = 2;
    public static final int requestDownload = 3;
    public static final int requestRemoveAd = 4;
    public static final int requestWordSearch = 5;
    public static final int requestWordSearchAgain = 6;
    public static final int requestReset = 7;

    public static final int btnMenuC = 1;
    public static final int btnSettingsC = 2;
    public static final int btnPlayC = 3;
    public static final int btnStopC = 4;
    public static final int btnGotoC = 5;
    public static final int btnBackC = 6;
    public static final int btnNextC = 7;
    public static final int btnAyahShareC = 8;
    public static final int btnBookmarkC = 9;
    public static final int btnCloseC = 10;
    public static final int btnTutorialC = 11;
    //two more item are added
    public static final int btnSurahHeader = 12, btnJuzHeader = 13;
    public static final int btnReciter = 1;
    public static final int btnTranslationC = 2;
    public static final int btnTafseerC = 3;
    public static final int btnTransliterationC = 4;
    public static final int btnFontColor1C = 5;
    public static final int btnFontColor2C = 6;
    public static final int btnFontColor3C = 7;
    public static final int btnBgColor1C = 8;
    public static final int btnBgColor2C = 9;
    public static final int btnBgColor3C = 10;
    public static final int btnBookmarksC = 11;
    public static final int btnStopSignsC = 12;
    public static final int btnSajdahsC = 13;
    public static final int btnAboutC = 14;
    public static final int btnShareC = 15;
    public static final int btnMoreC = 16;
    public static final int btnSaveC = 17;
    public static final int btnResetC = 18;
    public static final int btnNotificationC = 19;
    public static final int btnFontStyle1C = 20;
    public static final int btnFontStyle2C = 21;
    public static final int btnFontStyle3C = 22;
    public static final int btnWordSearch = 25;
    public static final int btnRemoveAdC = 23;
    public static final int btnDisclaimer = 24;

    private boolean lastReadSurahCheck = false;
    private boolean notificationOccured = false;
    private int notificationAyaSeletion;
    private int surahNo;
    private EditText myFilter;
    private TextView tvArabic, txt_trans, tvTranslation;
    private SeekBar seekControl;
    private RelativeLayout ayahOptionsLayout;
    private ImageView imgTafseer, imgTransliteration, imgNotification, imgLastRead;
    private int tempPos = -1;

    private int fontSize_A[] = {};
    private int fontSize_A_1[] = {};
    private int fontSize_E[] = {};
    private int lastSurahPos = 0;
    private String bodyText = "I just found this Beautiful Islamic App \"Urdu Quran\" on Play store - Download Free Now\nhttps://play.google.com/store/apps/details?id=com.QuranReading.urduquran";
    private GlobalClass mGlobal;
    private int singleChoicePosForTranslation;
    private int singleChoicePosForReciter;
    private boolean goToDialogDismised = false;
    private AnalyticSingaltonClass mAnalyticSingaltonClass;
    public static Activity isQuranActivity = null;
    private boolean refreshNextORPrevious = false;
    private boolean isSurahHeaderClick = true;
    private boolean copyOnce = true, isJuzClick = false;
    public static ArrayList<JuzModel> juzDataList;
    public static int juzPosition = -1;
    public static boolean isJuzFound = false;
    public static boolean isSecondSurah = false;
    public static boolean isFourthSurah = false;
    public static ArrayList<IndexListModel> juzNamesList = new ArrayList<>(), juzIndexDataList = new ArrayList<>();
    private IndexListModel juzDataModel;
    private int juzListPos; //for current juz position
    private int resrcTimeArray;
    private String[] englishJuzArr, arabicJuzArr;
    private ArrayList<String> engSurahNames;
    private String[] arabicNamesData, revealedPlaces;
    private int[] surahSizes;
    private TextView tvNoResultFound;
    public static boolean isLastAya = false;
    private boolean isSurah = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah);
        mGlobal = ((GlobalClass) getApplicationContext());
        settngPref = new SettingsSharedPref(SurahActivity.this);
        //	mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);
        //	mAnalyticSingaltonClass.sendScreenAnalytics("Quran_Surah_Screen");

        // Analytics for Tracking how many times a user uses this App
        int counter = settngPref.getUserCounter();
        mAnalyticSingaltonClass = AnalyticSingaltonClass.getInstance(this);
        mAnalyticSingaltonClass.sendEventAnalytics("Retention", "ZZ-0" + counter);
        counter++;
        settngPref.setUserCounter(counter);

        mGlobal.selectedIndex = suraPosition - 1;
        slideMenu = new SlidingMenu(SurahActivity.this);
        slideMenu.setMode(SlidingMenu.LEFT_RIGHT);
        mActivity = this;
        slideMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slideMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        slideMenu.setFadeDegree(0.35f);
        slideMenu.attachToActivity(SurahActivity.this, SlidingMenu.SLIDING_CONTENT);
        slideMenu.setMenu(R.layout.slidemenu_surahindex);
        slideMenu.setSecondaryMenu(R.layout.slidemenu_settings);
        slideMenu.setOnCloseListener(this);
        initializeWidgets();
        initializeIndexList();
        HashMap<String, Integer> fontSettngs = settngPref.getFontSettings();
        reciter = fontSettngs.get(SettingsSharedPref.RECITER);
        prev_Reciter = reciter;
        getReciter();

        // ***************** For last read check *******************//

        int checklastread = settngPref.getSurahNo();

        if (checklastread > 0) {
            imgLastRead.setClickable(true);
        }

        try {
            surahNo = getIntent().getIntExtra("SURAHDAYNO", -1);
            Intent mExtras = getIntent();
            bundle = mExtras.getExtras();

            if (bundle != null && bundle.containsKey("fromHome")) {

                actionHomeScreen = bundle.getString("fromHome");
                suraPosition = bundle.getInt("SURAHNO", 0);
                surahIndex = suraPosition - 1;
                mGlobal.selectedIndex = surahIndex;
                delayIndex = bundle.getInt("AYAHNO", 0);
                mGlobal.ayahPos = delayIndex;
                refreshSurahData(actionRefresh);

            } else if (bundle != null && bundle.containsKey("fromHomeRubana")) {
                actionHomeScreen = bundle.getString("fromHomeRubana");
                suraPosition = bundle.getInt("SURAHNO", 0);
                surahIndex = suraPosition - 1;
                delayIndex = bundle.getInt("AYAHNO", 0);
                secondAyaSelected = bundle.getInt("AYAHNO2", -1);
                mGlobal.selectedIndex = surahIndex;
                mGlobal.ayahPos = delayIndex;
                refreshSurahData(actionRefresh);

            }
            // ***************** For daily notification *******************//

            else if (surahNo != -1) {
                suraPosition = surahNo;
                surahIndex = suraPosition - 1;
                notificationAyaSeletion = getIntent().getIntExtra("AYANO", -1);
                notificationOccured = true;
                refreshSurahData(actionRefresh);
            } else if (bundle == null) {

                startAsyncTask(actionRefresh);
            }
            ayahListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (ayahOptionsLayout.getVisibility() == View.VISIBLE) {
                        bookmarkAyahPos = position - 1;

                    }
                    mGlobal.ayahPos = position - 1;// new line added
                    settngPref.setLastReadSurahNo(suraPosition);//code added
                    settngPref.setLastReadAyahNo(mGlobal.ayahPos);//code added
                    secondAyaSelected = -1;
                    delayIndex = mGlobal.ayahPos;

                    customAdapter.notifyDataSetChanged();

                    chkSelection(position - 1);
                }

            });

            ayahListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    bookmarkAyahPos = position - 1;
                    ayahOptionsLayout.setVisibility(View.VISIBLE);
                    chkSelection(position - 1);
                    secondAyaSelected = -1;
                    customAdapter.notifyDataSetChanged();
                    return true;
                }
            });

            indexList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    positionIndexListClick = position;
                    indexListClickHandler();
                }
            });

            slideMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
                @Override
                public void onClosed() {

                    menuOpened = false;
                    hideSoftKeyboard();
                    if (surahSelected) {
                        btnAudio.setEnabled(false);
                        surahSelected = false;
                        startAsyncTask(actionRefresh);
                        tvHeader.setSelected(true);
                    }

                    if (secMenuOpened) {
                        if (prev_Reciter != reciter)//block added
                        {
                            delayIndex = 0;//these four lines are added
                            mGlobal.ayahPos = delayIndex;
                            ayahListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    ayahListView.setSelection(delayIndex);
                                }
                            });

                            customAdapter.notifyDataSetChanged();

                        }
                        secMenuOpened = false;
                        mGlobal.translationLanguage = dialogLanguagePosition;
                        mGlobal.hideTransliteration = chkTransliteration;
                        mGlobal.chkTafseer = chkTafseer;
                        mGlobal.font_size_arabic = fontArabic;
                        mGlobal.font_size_eng = fontEnglish;
                        setAlarm();
                        settngPref.saveSettings(f_index, fontArabic, fontEnglish, reciter, dialogLanguagePosition, chkTransliteration, chkNotification, chkTafseer);

                        startAsyncTask(actionSettings);
                    }
                    if (reset) {
                        reset = false;

                    } else {

                        if (!surahChange && audioFound) {
                            if (playingState) {
                                if (prev_Reciter == reciter)
                                    playAudio(false);
                                else
                                    playingState = false;
                            }
                        }
                    }
                }
            });

            slideMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
                @Override
                public void onOpened() {
                    menuOpened = true;
                    surahChange = false;
                    mAnalyticSingaltonClass.sendScreenAnalytics("Quran_Index_Screen");

                    if (slideMenu.isSecondaryMenuShowing()) {
                        mAnalyticSingaltonClass.sendScreenAnalytics("Quran_Settings_Screen");
                        secMenuOpened = true;
                        menuOpened = false;
                        prev_Reciter = reciter;
                    }
                    if (mp != null) {
                        if (mp.isPlaying()) {
                            playingState = true;
                            pauseAudio();
                        } else {
                            playingState = false;
                        }
                    }
                }
            });

            myFilter.addTextChangedListener(this);
            telephonyManeger = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

            if (telephonyManeger != null) {
                telephonyManeger.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            }


            sendAnalyticsData();
            hideSoftKeyboard();
            if (suraPosition == 1) {
                menuOpened = true;
                slideMenu.showMenu();
            } else {
                if (menuOpened || secMenuOpened) {
                    secMenuOpened = false;
                    menuOpened = false;
                    slideMenu.toggle();
                }
            }


        } catch (Exception e) {
            Log.i("Exception", e.getMessage().toString());
        }
    }

    public void onHomeClickQuran(View v) {
        if (v.getId() == R.id.btnHomeQuran) {
            this.finish();
        }

    }

    private void sendAnalyticsData() {
        AnalyticSingaltonClass.getInstance(this).sendScreenAnalytics("Quran_Surah_Screen");
    }

    private void initializeAds() {
        adview = (AdView) findViewById(R.id.adView);
        adImage = (ImageView) findViewById(R.id.adimg);
        adImage.setVisibility(View.GONE);
        adview.setVisibility(View.GONE);

        if (isNetworkConnected()) {
            this.adview.setVisibility(View.VISIBLE);
        } else {
            this.adview.setVisibility(View.GONE);
        }
        setAdsListener();

    }
    //Text watcher


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (mGlobal.selectedIndex != -1) {
            tempPos = mGlobal.selectedIndex;
            mGlobal.selectedIndex = -1;
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchText = myFilter.getText().toString().trim().toLowerCase(Locale.US);
        int textlength = searchText.length();

        if (isSurahHeaderClick) {
            //	myFilter.setInputType(InputType.TYPE_CLASS_TEXT);
            dataList.clear();
            indexList.setVisibility(View.VISIBLE);
            tvNoResultFound.setVisibility(View.GONE);

            int size = surahNamesList.size();

            for (int i = 0; i < size; i++) {
                String value = surahNamesList.get(i).getEngSurahName().trim();

                if (textlength <= value.length()) {
                    if (value.toLowerCase(Locale.US).contains(searchText)) {
                        dataList.add(surahNamesList.get(i));
                    } else {
                        value = value.replace("'", "");
                        value = value.replace("-", "");
                        value = value.replace(" ", "");
                        if (value.toLowerCase(Locale.US).contains(searchText)) {
                            dataList.add(surahNamesList.get(i));
                        }
                    }
                }

            }
            if (dataList.size() == 0) {
                indexList.setVisibility(View.GONE);
                tvNoResultFound.setVisibility(View.VISIBLE);
            }
        } else {//for Juz Part

            isJuzClick = true;
            juzIndexDataList.clear();
            int size = juzNamesList.size();
            indexList.setVisibility(View.VISIBLE);
            tvNoResultFound.setVisibility(View.GONE);

            for (int i = 0; i < size; i++) {
                String value = juzNamesList.get(i).getJuzEnglish().trim();

                if (textlength <= value.length()) {
                    if (value.contains(searchText)) {
                        juzIndexDataList.add(juzNamesList.get(i));
                    } else {
                        value = value.replace("'", "");
                        value = value.replace("-", "");
                        //	value = value.replace(" ", "");

                        if (value.contains(searchText)) {
                            juzIndexDataList.add(juzNamesList.get(i));
                        }
                    }
                }
            }
            if (juzIndexDataList.size() == 0) {
                indexList.setVisibility(View.GONE);
                tvNoResultFound.setVisibility(View.VISIBLE);
            }


        }
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (myFilter.getText().toString().length() <= 0) {
            mGlobal.selectedIndex = tempPos;
            listAdapter.notifyDataSetChanged();
        }
    }

    public void openLastRead(View v) {
        suraPosition = settngPref.getLastReadSurahNo();
        mGlobal.ayahPos = settngPref.getLastReadAyahNo();

        if (suraPosition > 0) {
            lastSurahPos = suraPosition;
            lastReadSurahCheck = true;
            refreshSurahData(actionRefresh);
            slideMenu.toggle();
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_last_read_saved), Toast.LENGTH_SHORT).show();
        }
    }

    PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (mp != null) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    // Incoming call: Pause Audio
                    handler.removeCallbacks(sendUpdatesToUI);
                    if (mp.isPlaying()) {
                        callCheck = true;
                        mp.pause();
                    }
                } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                    // Not in call: Play Audio
                    if (callCheck && mp != null) {
                        callCheck = false;
                        handler.removeCallbacks(sendUpdatesToUI);
                        handler.postDelayed(sendUpdatesToUI, 0);
                        mp.start();
                    }
                } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                    // A call is dialing, active or on hold: Pause Audio
                    handler.removeCallbacks(sendUpdatesToUI);
                    if (mp.isPlaying()) {
                        callCheck = true;
                        mp.pause();
                    }
                }
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };

    public void initializeWidgets() {

        imgLastRead = (ImageView) findViewById(R.id.last_read);
        innerMainLayout = (RelativeLayout) findViewById(R.id.inner_main_layout);
        tutorialLayout = (RelativeLayout) findViewById(R.id.tutorial_layout);
        removeAdLayout = (RelativeLayout) findViewById(R.id.layout_remove_ad);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.header_bg),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        btnTransprnt = (Button) findViewById(R.id.btn_transparent);
        ayahListView = (XListView) findViewById(R.id.listView);
        /*new added code amir 3.2*/
        ayahListView.setPullLoadEnable(true);
        ayahListView.setXListViewListener(this);
        tvHeader = (TextView) findViewById(R.id.tv_header);
        tvHeader2 = (TextView) findViewById(R.id.tv_header2);
        btnAudio = (ImageButton) findViewById(R.id.btn_audio);
        // btnStop = (ImageButton) findViewById(R.id.btn_stop);
        btnBack = (ImageButton) findViewById(R.id.btn_back);
        btnNext = (ImageButton) findViewById(R.id.btn_next);
        myFilter = (EditText) findViewById(R.id.edit_search);
        indexList = (ListView) findViewById(R.id.index_listView);
        tvNoResultFound = (TextView) findViewById(R.id.tvNoResultFound);
        seekControl = (SeekBar) findViewById(R.id.seek_bar);
        seekControl.setOnSeekBarChangeListener(this);
        tvArabic = (TextView) findViewById(R.id.tv_arabic);
        txt_trans = (TextView) findViewById(R.id.tv_trans);
        ayahOptionsLayout = (RelativeLayout) findViewById(R.id.ayah_options_layout);
        /*
         * img_reciter_1 = (ImageView) findViewById(R.id.img_reciter_1); img_reciter_2 = (ImageView) findViewById(R.id.img_reciter_2);
		 */
        tvTranslation = (TextView) findViewById(R.id.tv_translation);
        imgTafseer = (ImageView) findViewById(R.id.img_tafseer);
        imgTransliteration = (ImageView) findViewById(R.id.img_transliteration);
        imgNotification = (ImageView) findViewById(R.id.img_notification);
        viewRemoveAdLine = (View) findViewById(R.id.view_remove_ad);
        // btnStop.setEnabled(false);
        TextView tvHeaderSettings = (TextView) findViewById(R.id.tv_header_settings);
        tvReciter = (TextView) findViewById(R.id.tv_reciter);		/*
         * TextView tvReciter1 = (TextView) findViewById(R.id.tv_reciter_1); TextView tvReciter2 = (TextView) findViewById(R.id.tv_reciter_2);
		 */
        TextView tvReciterLabel = (TextView) findViewById(R.id.tv_reciter_label);
        TextView tvtranslationLabel = (TextView) findViewById(R.id.tv_translation_label);
        TextView tvTafseerLabel = (TextView) findViewById(R.id.tv_tafseer_label);
        TextView tvTransliteration = (TextView) findViewById(R.id.tv_transliteration);
        TextView tvNotification = (TextView) findViewById(R.id.tv_notification);
        TextView tvBookmarks = (TextView) findViewById(R.id.tv_bookmark);
        TextView tvStopSigns = (TextView) findViewById(R.id.tv_stop_sign);
        TextView tvSajdahs = (TextView) findViewById(R.id.tv_sajdahs);
        TextView tvRemoveAd = (TextView) findViewById(R.id.tv_remove_ad);
        TextView tvDisclaimer = (TextView) findViewById(R.id.tv_disclaimer);
        TextView tvTheme = (TextView) findViewById(R.id.tv_theme);
        TextView tvFontSize = (TextView) findViewById(R.id.tv_font_size);
        TextView tvFontStyle = (TextView) findViewById(R.id.tv_font_style);
        TextView tvFontColor = (TextView) findViewById(R.id.tv_font_color);
        TextView tvBgColor = (TextView) findViewById(R.id.tv_bg_color);
        Button btnReset = (Button) findViewById(R.id.btn_reset);
        Button btnSave = (Button) findViewById(R.id.btn_save);

        tvHeader.setTypeface(mGlobal.faceHeading);
        tvHeader2.setTypeface(mGlobal.faceHeading);
        tvHeaderSettings.setTypeface(mGlobal.faceHeading);
        btnReset.setTypeface(mGlobal.faceHeading);
        btnSave.setTypeface(mGlobal.faceHeading);
        tvArabic.setTypeface(mGlobal.faceArabic);
        /*
		 * tvReciter.setTypeface(mGlobal.faceContent1); tvReciter1.setTypeface(mGlobal.faceContent1); tvReciter2.setTypeface(mGlobal.faceContent1);
		 */
        tvReciterLabel.setTypeface(mGlobal.faceContent1);
        tvtranslationLabel.setTypeface(mGlobal.faceContent1);
        tvTafseerLabel.setTypeface(mGlobal.faceContent1);
        tvTransliteration.setTypeface(mGlobal.faceContent1);
        tvNotification.setTypeface(mGlobal.faceContent1);
        tvBookmarks.setTypeface(mGlobal.faceContent1);
        tvStopSigns.setTypeface(mGlobal.faceContent1);
        tvSajdahs.setTypeface(mGlobal.faceContent1);
        tvRemoveAd.setTypeface(mGlobal.faceContent1);
        tvDisclaimer.setTypeface(mGlobal.faceContent1);
        tvTheme.setTypeface(mGlobal.faceContent1);
        tvFontSize.setTypeface(mGlobal.faceContent1);
        tvFontStyle.setTypeface(mGlobal.faceContent1);
        tvFontColor.setTypeface(mGlobal.faceContent1);
        tvBgColor.setTypeface(mGlobal.faceContent1);
        txt_trans.setTypeface(mGlobal.faceContent1);
        tvReciter.setTypeface(mGlobal.faceContent1);

    }

    public void initializeIndexList() {
        dataList.clear();
        surahNamesList.clear();
        juzNamesList.clear();
        juzIndexDataList.clear();
        resrcTimeArray = this.getResources().getIdentifier("surah_names", "array", this.getPackageName());

        if (resrcTimeArray > 0) {
            //for surah names
            String[] engNamesData = getResources().getStringArray(resrcTimeArray);

            resrcTimeArray = this.getResources().getIdentifier("surahNamesArabic", "array", this.getPackageName());
            arabicNamesData = getResources().getStringArray(resrcTimeArray);

            resrcTimeArray = this.getResources().getIdentifier("noOfVerses", "array", this.getPackageName());
            surahSizes = getResources().getIntArray(resrcTimeArray);

            resrcTimeArray = this.getResources().getIdentifier("revealedPlaces", "array", this.getPackageName());
            revealedPlaces = getResources().getStringArray(resrcTimeArray);
            engSurahNames = new ArrayList<String>(Arrays.asList(engNamesData));
            //for juz names
            englishJuzArr = this.getResources().getStringArray(R.array.juzz_names);
            arabicJuzArr = this.getResources().getStringArray(R.array.Juz_Names_Arabic);

            for (int pos = 0; pos < engSurahNames.size(); pos++) {
                IndexListModel data = new IndexListModel(pos + 1, engSurahNames.get(pos), arabicNamesData[pos], revealedPlaces[pos], surahSizes[pos]);
                surahNamesList.add(data);
                if (pos < 30) {
                    juzDataModel = new IndexListModel(pos + 1, englishJuzArr[pos], arabicJuzArr[pos]);
                    juzNamesList.add(juzDataModel);
                }
            }
            dataList.addAll(surahNamesList);
            juzIndexDataList.addAll(juzNamesList);
        } else {
            showToast("Data Not Found");
        }
        boolean isSurah = true;
        listAdapter = new IndexListAdapter(this, dataList, isSurah);
        indexList.setAdapter(listAdapter);
    }

    private void initializeIndexListSearch() {
        if (isSurahHeaderClick) { //if surah header is clicked
            dataList.clear();
            surahNamesList.clear();
            if (resrcTimeArray > 0) {
                for (int pos = 0; pos < engSurahNames.size(); pos++) {
                    IndexListModel data = new IndexListModel(pos + 1, engSurahNames.get(pos), arabicNamesData[pos], revealedPlaces[pos], surahSizes[pos]);
                    surahNamesList.add(data);
                }
                dataList.addAll(surahNamesList);

            } else {
                showToast("Data Not Found");
            }
            isSurah = true;
            listAdapter = new IndexListAdapter(this, dataList, isSurah);
            mGlobal.selectedIndex = surahIndex;
            indexList.setAdapter(listAdapter);
            indexList.setSelection(surahIndex);

        } else {
            juzNamesList.clear();
            juzIndexDataList.clear();
            juzNamesList = new ArrayList<IndexListModel>();
            juzIndexDataList = new ArrayList<IndexListModel>();
            for (int i = 0; i < englishJuzArr.length; i++) {
                IndexListModel data = new IndexListModel(i + 1, englishJuzArr[i], arabicJuzArr[i]);
                juzNamesList.add(data);
            }
            juzIndexDataList.addAll(juzNamesList);
            isSurah = false;
            listAdapter = new IndexListAdapter(SurahActivity.this, juzIndexDataList, isSurah);
            mGlobal.selectedIndex = juzListPos;
            indexList.setAdapter(listAdapter);
            indexList.setSelection(juzListPos);
        }
    }

    public void showToast(String msg) {
        Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void initializeSettings() {
        String device = settngPref.getDevice();
        // Toast.makeText(getBaseContext(), device, Toast.LENGTH_SHORT).show();
        HashMap<String, Boolean> tranSettngs = settngPref.getTransSettings();
        HashMap<String, Integer> fontSettngs = settngPref.getFontSettings();

        dialogLanguagePosition = settngPref.getTranslationLanguage();
        chkTransliteration = tranSettngs.get(SettingsSharedPref.TRANSLITERATION);
        chkNotification = tranSettngs.get(SettingsSharedPref.NOTIFICATION);
        chkTafseer = tranSettngs.get(SettingsSharedPref.TAFSEER);
        f_index = fontSettngs.get(SettingsSharedPref.FONT_INDEX);
        fontArabic = fontSettngs.get(SettingsSharedPref.FONT_ARABIC);
        fontEnglish = fontSettngs.get(SettingsSharedPref.FONT_ENGLISH);
        reciter = fontSettngs.get(SettingsSharedPref.RECITER);

        if (f_index == -1) {
            f_index = 0;
        }

        if (device.equals("small")) {
            topPadding = 40;
            fontSize_A_1 = Constants.fontSize_A_small;

            fontSize_E = Constants.fontSize_E_small;
        } else if (device.equals("medium")) {
            topPadding = 50;
            fontSize_A_1 = Constants.fontSize_A_med;
            fontSize_E = Constants.fontSize_E_med;
        } else if (device.equals("large")) {
            topPadding = 60;
            fontSize_A_1 = Constants.fontSize_A_large;
            fontSize_E = Constants.fontSize_E_large;
        }

        fontSize_A = fontSize_A_1;
        mGlobal.ayahPadding = topPadding;

        fontArabic = fontSize_A[f_index];
        fontEnglish = fontSize_E[f_index];
        mGlobal.font_size_arabic = fontSize_A[f_index];
        mGlobal.font_size_eng = fontSize_E[f_index];

        mGlobal.translationLanguage = dialogLanguagePosition;
        mGlobal.hideTransliteration = chkTransliteration;
        mGlobal.chkTafseer = chkTafseer;

        boolean firstTime = settngPref.getFirstTime();

        if (chkNotification && firstTime) {
            AlarmCalss mAlarmHelper = new AlarmCalss(this);
            mAlarmHelper.cancelAlarm();
            mAlarmHelper.setAlarm();
        }
    }

    public void initializeSettingsWidgets() {
        tvArabic.setTextSize(fontArabic);
        txt_trans.setTextSize(fontEnglish);
        seekControl.setProgress(f_index);

        if (chkTransliteration) {
            imgTransliteration.setImageResource(R.drawable.img_on);
        } else {
            imgTransliteration.setImageResource(R.drawable.img_off);
        }

        if (chkNotification) {
            imgNotification.setImageResource(R.drawable.img_on);
        } else {
            imgNotification.setImageResource(R.drawable.img_off);
        }
        if (chkTafseer) {
            imgTafseer.setImageResource(R.drawable.img_on);
        } else {
            imgTafseer.setImageResource(R.drawable.img_off);
        }

        fontSize_A = fontSize_A_1;
        mGlobal.ayahPadding = topPadding;
        fontSize_A = fontSize_A_1;
        fontArabic = fontSize_A[f_index];
        tvArabic.setTextSize(fontArabic);
        tvArabic.setPadding(0, topPadding, 0, 0);

    }

    private void getReciter() {
        switch (reciter) {
            case 0:
                tvReciter.setText("Abdul Basit");
                break;
            case 1:
                tvReciter.setText("Alafasy");

                break;
            case 2:
                tvReciter.setText("Sudais");

                break;

            default:
                tvReciter.setText("Alafasy");
                break;
        }
    }

    private void getTranslation() {

        switch (dialogLanguagePosition) {

            case 0: {
                tvTranslation.setText("Off");
                xpp = this.getResources().getXml(R.xml.urdu_translation_ahmedrazakhan);
                bismillahText = getResources().getString(R.string.bismillahTextUrdu);
            }
            break;

            case 1: {
                tvTranslation.setText("Urdu (Ahmed Raza Khan)");
                xpp = this.getResources().getXml(R.xml.urdu_translation_ahmedrazakhan);
                bismillahText = getResources().getString(R.string.bismillahTextUrdu);
            }
            break;
            case 2: {
                tvTranslation.setText("Urdu (Maulana Mehmood-ul-Hassan)");
                xpp = this.getResources().getXml(R.xml.tafseer_uthmani_complete);
                bismillahText = getResources().getString(R.string.bismillah_text_translation);
            }
            break;
            case 3: {
                tvTranslation.setText("Urdu (Maududi)");
                xpp = this.getResources().getXml(R.xml.urdu_translation_maududi);
                bismillahText = getResources().getString(R.string.bismillahTextUrdu_mududi);

            }
            break;

            case 4: {
                tvTranslation.setText("Urdu (Jalandhari)");
                xpp = this.getResources().getXml(R.xml.urdu_translation_jalandhry);
                bismillahText = getResources().getString(R.string.bismillahTextUrdu_jalandri);

            }
            break;

            case 5: {
                tvTranslation.setText("Urdu (Junagarhi)");
                xpp = this.getResources().getXml(R.xml.urdu_translation_junagarhi);
                bismillahText = getResources().getString(R.string.bismillahTextUrdu_junagari);

            }
            break;
            case 6: {
                tvTranslation.setText("English (Saheeh)");
                xpp = this.getResources().getXml(R.xml.english_translation);
                bismillahText = getResources().getString(R.string.bismillahTextEngSaheeh);
            }
            break;
            case 7: {
                tvTranslation.setText("English (Pickthal)");
                xpp = this.getResources().getXml(R.xml.eng_translation_pickthal);
                bismillahText = getResources().getString(R.string.bismillahTextEngPickthal);
            }
            break;

            case 8: {
                tvTranslation.setText("English (Shakir)");
                xpp = this.getResources().getXml(R.xml.eng_translation_shakir);
                bismillahText = getResources().getString(R.string.bismillahTextEngShakir);
            }
            break;

            case 9: {
                tvTranslation.setText("English (Maududi)");
                xpp = this.getResources().getXml(R.xml.eng_translation_maududi);
                bismillahText = getResources().getString(R.string.bismillahTextEngMadudi);
            }
            break;

            case 10: {
                tvTranslation.setText("English (Daryabadi)");
                xpp = this.getResources().getXml(R.xml.eng_translation_daryabadi);
                bismillahText = getResources().getString(R.string.bismillahTextEngDarayabadi);
            }
            break;

            case 11: {
                tvTranslation.setText("English (Yousuf Ali)");
                xpp = this.getResources().getXml(R.xml.eng_translation_yusufali);
                bismillahText = getResources().getString(R.string.bismillahTextEngYusaf);
            }
            break;

            case 12: {
                tvTranslation.setText("Spanish");
                xpp = this.getResources().getXml(R.xml.spanish_cortes_trans);
                bismillahText = getResources().getString(R.string.bismillahTextSpanishCortes);
            }
            break;

            case 13: {
                tvTranslation.setText("French");
                xpp = this.getResources().getXml(R.xml.french_trans);
                bismillahText = getResources().getString(R.string.bismillahTextFrench);
            }
            break;

            case 14: {
                tvTranslation.setText("Chinese");
                xpp = this.getResources().getXml(R.xml.chinese_trans);
                bismillahText = getResources().getString(R.string.bismillahTextChinese);
            }
            break;

            case 15: {
                tvTranslation.setText("Persian");
                xpp = this.getResources().getXml(R.xml.persian_ghoomshei_trans);
                bismillahText = getResources().getString(R.string.bismillahTextPersianGhommshei);
            }
            break;

            case 16: {
                tvTranslation.setText("Italian");
                xpp = this.getResources().getXml(R.xml.italian_trans);
                bismillahText = getResources().getString(R.string.bismillahTextItalian);
            }
            break;

            case 17: {
                tvTranslation.setText("Dutch");
                xpp = this.getResources().getXml(R.xml.dutch_trans_keyzer);
                bismillahText = getResources().getString(R.string.bismillahTextDutchKeyzer);
            }
            break;

            case 18: {
                tvTranslation.setText("Indonesian");
                xpp = this.getResources().getXml(R.xml.indonesian_bhasha_trans);
                bismillahText = getResources().getString(R.string.bismillahTextIndonesianBahasha);
            }
            break;
            case 19: {
                tvTranslation.setText("Melayu");
                xpp = this.getResources().getXml(R.xml.malay_basmeih);
                bismillahText = getResources().getString(R.string.bismillahTextMalay);
            }
            break;
            case 20: {
                tvTranslation.setText("Hindi");
                xpp = this.getResources().getXml(R.xml.hindi_suhel_farooq_khan_and_saifur_rahman_nadwi);
                bismillahText = getResources().getString(R.string.bismillahTextHindi);
            }
            break;

            case 21: {
                tvTranslation.setText("Arabic");
                xpp = this.getResources().getXml(R.xml.arabic_jalaleen);
                bismillahText = getResources().getString(R.string.bismillah);
            }
            break;

            case 22: {
                tvTranslation.setText("Turkish");
                xpp = this.getResources().getXml(R.xml.turkish_diyanet_isleri);
                bismillahText = getResources().getString(R.string.bismillahTextTurkish);
            }
            break;
            case 23: {
                tvTranslation.setText("Bangla");
                xpp = this.getResources().getXml(R.xml.bangali_zohurul_hoque);
                bismillahText = getResources().getString(R.string.bismillahTextBengali);
            }
            break;
            case 24: {
                tvTranslation.setText("Russian");
                xpp = this.getResources().getXml(R.xml.russian_abuadel);
                bismillahText = getResources().getString(R.string.bismillahTextRussian);
            }
            break;
            case 25: {
                tvTranslation.setText("Japanese");
                xpp = this.getResources().getXml(R.xml.japanese);
                bismillahText = getResources().getString(R.string.bismillahTextJapan);
            }
            break;
            case 26: {
                tvTranslation.setText("Portuguese");
                xpp = this.getResources().getXml(R.xml.pourteges);
                bismillahText = getResources().getString(R.string.bismillahTextPortuguese);
            }
            break;
            case 27: {
                tvTranslation.setText("Thai");
                xpp = this.getResources().getXml(R.xml.thai);
                bismillahText = getResources().getString(R.string.bismillahTextThai);
            }
            break;

            default: {
                tvTranslation.setText("off");
                xpp = this.getResources().getXml(R.xml.urdu_translation_ahmedrazakhan);
                bismillahText = getResources().getString(R.string.bismillahTextUrdu);
            }
        }
    }

    public void initializaSurahData() {
        if (lastReadSurahCheck) {
            mGlobal.ayahPos = settngPref.getLastReadAyahNo();
        } else if (notificationOccured) {
            mGlobal.ayahPos = notificationAyaSeletion;
        } else if (chkSettingSaved) {
            chkSettingSaved = false;
        } else {

            if (bundle != null) {
                if (bundle.containsKey("fromHome") || bundle.containsKey("fromHomeRubana")) {

                }
            } else if (req == 2) {

            } else {
                if (isJuzClick)//for juzz click{
                {
                    isJuzClick = false;
                    delayIndex = mGlobal.ayahPos;
                } else {
                    mGlobal.ayahPos = 0;
                    delayIndex = mGlobal.ayahPos;
                }


            }
        }
        if (copyOnce) {
            //for Juzz part
            xpp = this.getResources().getXml(R.xml.juzz_index);
            juzDataList = new ArrayList<>();
            juzDataList = XMLParser.getJuzData(this, xpp);
            copyOnce = false;
        }


        switch (suraPosition) {
            case 1:
                juzPosition = 0;
                juzListPos = 0;
                isJuzFound = true;
                break;
            case 2:
                isSecondSurah = true;
                if (juzListPos == 1) { //second juz
                    juzPosition = 1;
                } else {  //third juz
                    if (juzListPos != 1 && juzListPos != 2)
                        juzListPos = 1;
                    juzPosition = 2;
                }

                isJuzFound = true;
                break;
            case 3:
                juzPosition = 3;
                juzListPos = 3;
                isJuzFound = true;
                break;
            case 4:
                isFourthSurah = true;
                if (juzListPos == 4) { //second juz
                    juzPosition = 4;
                } else {  //third juz
                    if (juzListPos != 4 && juzListPos != 5)
                        juzListPos = 4;
                    juzPosition = 5;
                }
                isJuzFound = true;
                break;
            case 5:
                juzListPos = 6;
                juzPosition = 6;
                isJuzFound = true;
                break;
            case 6:
                juzPosition = 7;
                juzListPos = 7;
                isJuzFound = true;
                break;
            case 7:
                juzPosition = 8;
                juzListPos = 8;
                isJuzFound = true;
                break;
            case 8:
                juzPosition = 9;
                juzListPos = 9;
                isJuzFound = true;
                break;
            case 9:
                juzPosition = 10;
                juzListPos = 10;
                isJuzFound = true;
                break;
            case 10:
                juzListPos = 10;
                isJuzFound = false;
                break;
            case 11:
                juzPosition = 11;
                juzListPos = 11;
                isJuzFound = true;
                break;
            case 12:
                juzPosition = 12;
                juzListPos = 12;
                isJuzFound = true;
                break;
            case 13:
                juzListPos = 12;
                isJuzFound = false;
                break;
            case 14:
                juzListPos = 12;
                isJuzFound = false;
                break;
            case 15:
                juzPosition = 13;
                juzListPos = 13;
                isJuzFound = true;
                break;
            case 16:
                juzListPos = 13;
                isJuzFound = false;
                break;
            case 17:
                juzPosition = 14;
                juzListPos = 14;
                isJuzFound = true;
                break;
            case 18:
                juzPosition = 15;
                juzListPos = 15;
                isJuzFound = true;
                break;
            case 19:
                juzListPos = 15;
                isJuzFound = false;
                break;
            case 20:
                juzListPos = 15;
                isJuzFound = false;
                break;
            case 21:
                juzPosition = 16;
                juzListPos = 16;
                isJuzFound = true;
                break;
            case 22:
                juzListPos = 16;
                isJuzFound = false;
                break;
            case 23:
                juzPosition = 17;
                juzListPos = 17;
                isJuzFound = true;
                break;
            case 24:
                juzListPos = 17;
                isJuzFound = false;
                break;
            case 25:
                juzPosition = 18;
                juzListPos = 18;
                isJuzFound = true;
                break;
            case 26:
                juzListPos = 18;
                isJuzFound = false;
                break;
            case 27:
                juzPosition = 19;
                juzListPos = 19;
                isJuzFound = true;
                break;
            case 28:
                juzListPos = 19;
                isJuzFound = false;
                break;
            case 29:
                juzPosition = 20;
                juzListPos = 20;
                isJuzFound = true;
                break;
            case 30:
                juzListPos = 20;
                isJuzFound = false;
                break;
            case 31:
                juzListPos = 20;
                isJuzFound = false;
                break;
            case 32:
                juzListPos = 20;
                isJuzFound = false;
                break;
            case 33:
                juzPosition = 21;
                juzListPos = 21;
                isJuzFound = true;
                break;
            case 34:
                juzListPos = 21;
                isJuzFound = false;
                break;
            case 35:
                juzListPos = 21;
                isJuzFound = false;
                break;
            case 36:
                juzPosition = 22;
                juzListPos = 22;
                isJuzFound = true;
                break;
            case 37:
                juzListPos = 22;
                isJuzFound = false;
                break;
            case 38:
                juzListPos = 22;
                isJuzFound = false;
                break;
            case 39:
                juzPosition = 23;
                juzListPos = 23;
                isJuzFound = true;
                break;
            case 40:
                juzListPos = 23;
                isJuzFound = false;
                break;
            case 41:
                juzPosition = 24;
                juzListPos = 24;
                isJuzFound = true;
                break;
            case 42:
                juzListPos = 24;
                isJuzFound = false;
                break;
            case 43:
                juzListPos = 24;
                isJuzFound = false;
                break;
            case 44:
                juzListPos = 24;
                isJuzFound = false;
                break;
            case 45:
                juzListPos = 24;
                isJuzFound = false;
                break;
            case 46:
                juzPosition = 25;
                juzListPos = 25;
                isJuzFound = true;
                break;
            case 47:
                juzListPos = 25;
                isJuzFound = false;
                break;
            case 48:
                juzListPos = 25;
                isJuzFound = false;
                break;
            case 49:
                juzListPos = 25;
                isJuzFound = false;
                break;
            case 50:
                juzListPos = 25;
                isJuzFound = false;
                break;
            case 51:
                juzPosition = 26;
                juzListPos = 26;
                isJuzFound = true;
                break;
            case 52:
                juzListPos = 26;
                isJuzFound = false;
                break;
            case 53:
                juzListPos = 26;
                isJuzFound = false;
                break;
            case 54:
                juzListPos = 26;
                isJuzFound = false;
                break;
            case 55:
                juzListPos = 26;
                isJuzFound = false;
                break;
            case 56:
                juzListPos = 26;
                isJuzFound = false;
                break;
            case 57:
                juzListPos = 26;
                isJuzFound = false;
                break;
            case 58:
                juzPosition = 27;
                juzListPos = 27;
                isJuzFound = true;
                break;
            case 59:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 60:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 61:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 62:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 63:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 64:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 65:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 66:
                juzListPos = 27;
                isJuzFound = false;
                break;
            case 67:
                juzPosition = 28;
                juzListPos = 28;
                isJuzFound = true;
                break;
            case 68:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 69:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 70:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 71:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 72:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 73:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 74:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 75:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 76:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 77:
                juzListPos = 28;
                isJuzFound = false;
                break;
            case 78:
                juzPosition = 29;
                juzListPos = 29;
                isJuzFound = true;
                break;
            case 79:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 80:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 81:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 82:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 83:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 84:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 85:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 86:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 87:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 88:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 89:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 90:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 91:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 92:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 93:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 94:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 95:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 96:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 97:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 98:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 99:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 100:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 101:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 102:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 103:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 104:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 105:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 106:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 107:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 108:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 109:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 110:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 111:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 112:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 113:
                juzListPos = 29;
                isJuzFound = false;
                break;
            case 114:
                juzListPos = 29;
                isJuzFound = false;
                break;
            default:
                isJuzFound = false;


        }
        if (isJuzFound && !isSurahHeaderClick) {

            surahIndex = suraPosition - 1;
            mGlobal.selectedIndex = juzListPos;
            mGlobal.ayahPos = juzDataList.get(juzPosition).getAyahNO();
            //	mGlobal.ayahPos-=1;
            delayIndex = mGlobal.ayahPos;
        }


        // Getting Arabic Ayas
        xpp = this.getResources().getXml(R.xml.quran_uthmani_corrected);
        ArrayList<String> arabicList = new ArrayList<String>();
        arabicList = XMLParser.getTranslatedAyaList(this, xpp, suraPosition, getResources().getString(R.string.bismillah));
        xpp = this.getResources().getXml(R.xml.tafseer_uthmani_complete);
        ArrayList<String> tafseerList = new ArrayList<>();
        tafseerList = XMLParser.getAyaTafseerList(this, xpp, suraPosition, getResources().getString(R.string.bismillah_text_tafseer));
        // *********** For word Search *************//

        // translationlist=new ArrayList<String>();
        xpp = this.getResources().getXml(R.xml.english_translation);
        // translationlist = XMLParser.getTranslatedAyaList(this, xpp,
        // suraPosition,
        // "In the name of Allah, the most Beneficent, the Marciful.");
        // Getting English Translation
        if (settngPref.isFirstTimeTranslation()) {
            settngPref.setFirstTimeTranslation(false);
            dialogLanguagePosition += 1;
            singleChoicePosForTranslation = dialogLanguagePosition;

        }
        getTranslation();
        ArrayList<String> translationList = new ArrayList<String>();
        translationList = XMLParser.getTranslatedAyaList(this, xpp, suraPosition, bismillahText);

        // Gating English Pronunciation
        xpp = this.getResources().getXml(R.xml.english_transliteration);
        ArrayList<String> transliterationList = new ArrayList<String>();
        transliterationList = XMLParser.getTranslatedAyaList(this, xpp, suraPosition, "Bismi Allahi arrahmani arraheem");
        surahList.clear();

        for (int pos = 0; pos < translationList.size(); pos++) {
            SurahModel model = new SurahModel(-1, arabicList.get(pos), translationList.get(pos), transliterationList.get(pos), tafseerList.get(pos));
            surahList.add(model);
        }

        chkSurahBookmarks();
        limit = surahList.size() - 1;
        arabicList.clear();
        translationList.clear();
        transliterationList.clear();

        if (suraPosition == 9) {
            surahList.remove(0);
            mGlobal.chkSurahTaubah = true;
        } else {
            mGlobal.chkSurahTaubah = false;
        }
        if (suraPosition == 114) {
            XListViewFooter.mHintView.setText(R.string.xlistview_footer_hint_last_surah);
        } else {
            XListViewFooter.mHintView.setText(R.string.xlistview_footer_hint_normal);
        }


    }


    public void showSurahData() {
        if (dataList.size() > 0) {
            // Set Header
            suraName = "Surah " + dataList.get(surahIndex).getEngSurahName();
            tvHeader.setText(suraName);

            if (customAdapter == null) {
                customAdapter = new CustomAyaListAdapter(SurahActivity.this, surahList);
                ayahListView.setAdapter(customAdapter);
            } else {
                if (lastReadSurahCheck) {
                    mGlobal.ayahPos = settngPref.getLastReadAyahNo();
                    delayIndex = mGlobal.ayahPos;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {

                            ayahListView.setSelection(delayIndex + 1);

                        }
                    });

                }

                if (!BookmarksActivity.longClick) {
                    customAdapter.notifyDataSetChanged();
                }
            }
            if (listAdapter != null) {
                indexList.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isSurahHeaderClick) {

                            indexList.setSelection(surahIndex);
                            mGlobal.selectedIndex = surahIndex;
                        } else {
                            indexList.setSelection(juzListPos);
                            mGlobal.selectedIndex = juzListPos;
                        }
                    }
                });

                listAdapter.notifyDataSetChanged();

            }
            if (refreshNextORPrevious) {
                delayIndex = 0;
                chkSelection(delayIndex);
            } else if (notificationOccured) {
                //chkSelection(notificationAyaSeletion+2);
                ayahListView.setSelection(notificationAyaSeletion + 1);
            } else {
                if (isSurahHeaderClick) {
                    chkSelection(delayIndex);
                } else if (!isSurahHeaderClick) {
                    chkSelection(delayIndex);
                }
            }

            if (suraPosition == 1) {
                btnBack.setEnabled(false);
                btnBack.setImageResource(R.drawable.back_h);
            } else if (suraPosition > 1) {
                if (!btnBack.isEnabled()) {
                    btnBack.setEnabled(true);
                    btnBack.setImageResource(R.drawable.back_btn);
                }
            }

            if (suraPosition == 114) {
                btnNext.setEnabled(false);
                btnNext.setImageResource(R.drawable.next_h);
            } else if (suraPosition < 114) {
                if (!btnNext.isEnabled()) {
                    btnNext.setEnabled(true);
                    btnNext.setImageResource(R.drawable.next_btn);
                }
            }
        } else {
            showSurahData();
        }

    }

    public void chkSurahBookmarks() {
        DBManager dbObj = new DBManager(this);
        dbObj.open();

        int id = -1, ayahPos = -1;

        Cursor c = dbObj.getOneBookmark(suraPosition);

        if (c.moveToFirst()) {
            do {
                id = c.getInt(c.getColumnIndex(Constants.FLD_ID));
                ayahPos = c.getInt(c.getColumnIndex(Constants.FLD_AYAH_NO));
                if (suraPosition == 9) {
                    ayahPos++;
                }
                SurahModel model = surahList.get(ayahPos);
                model.setBookMarkId(id);

                surahList.set(ayahPos, model);

            }
            while (c.moveToNext());
        }

        c.close();
        dbObj.close();
    }

    public void addRemoveSurahBookmarks(int suraNo, int ayahNo) {
        String name = dataList.get(surahIndex).getEngSurahName();
        DBManager dbObj = new DBManager(this);
        dbObj.open();

        SurahModel model = surahList.get(ayahNo);
        int id = model.getBookMarkId();

        if (id == -1) {
            id = (int) dbObj.addBookmark(name, suraNo, ayahNo);
            model.setBookMarkId(id);
            showToast("Added to Bookmarks");
        } else {
            dbObj.deleteOneBookmark(id);
            model.setBookMarkId(-1);
            showToast("Removed from Bookmarks");
        }

        surahList.set(ayahNo, model);
        customAdapter.notifyDataSetChanged();
        dbObj.close();
    }

    public void refreshSurahData(String action) {
        handler.removeCallbacks(sendUpdatesToUI);

        if (mp != null && mp.isPlaying()) {
            // mp.pause();
            // mp.stop();
            mp.release();
            mp = null;

            btnAudio.setImageResource(R.drawable.play_btn);
            // btnStop.setEnabled(false);
        }
        if (lastReadSurahCheck) {
            mGlobal.ayahPos = settngPref.getLastReadAyahNo();
            delayIndex = settngPref.getLastReadAyahNo();
        } else if (notificationOccured) {

            mGlobal.ayahPos = notificationAyaSeletion;
            delayIndex = notificationAyaSeletion;
        } else {
            if (bundle != null) {
                if (bundle.containsKey("fromHome") || bundle.containsKey("fromHomeRubana")) {

                }

            } else {
                mGlobal.ayahPos = 0;
                delayIndex = 0;
            }
        }
        play = 0;
        rowClick = false;
        surahIndex = suraPosition - 1;
        mGlobal.selectedIndex = surahIndex;
        startAsyncTask(action);
    }

    public void initializeAudios(boolean changeIndex) {

        int audioTimings;

        if (mp != null) {
            mp.release();
        }

        if (changeIndex) {
            if (bundle != null) {
                if (bundle.containsKey("fromHome") || req == 2 || bundle.containsKey("fromHomeRubana")) {

                }
            } else {
                if (!isJuzClick) {//if juzz is not clicked
                    delayIndex = 0;
                }

            }
        }

        mp = new MediaPlayer();

        mp = null;
        audioFound = false;
        if (reciter == 0) {
            audioTimings = R.xml.audio_timings_basit;
            audioFile = String.valueOf(suraPosition) + "_b.mp3";
        } else if (reciter == 1) {
            audioTimings = R.xml.audio_timings_afasy;
            audioFile = String.valueOf(suraPosition) + "_a.mp3";
        } else if (reciter == 2) {
            audioTimings = R.xml.audio_timings_sudais;
            audioFile = String.valueOf(suraPosition) + "_s.mp3";
        } else {
            reciter = 1;
            audioTimings = R.xml.audio_timings_afasy;
            audioFile = String.valueOf(suraPosition) + "_a.mp3";
        }

        try {
            if (!Constants.rootPath.exists()) {
                Constants.rootPath.mkdirs();
            }

            File myFile = new File(Constants.rootPath.getAbsolutePath(), audioFile);

            Uri audioUri = Uri.parse(myFile.getPath());

            if (myFile.exists()) {
                boolean chkSize = FileUtils.checkAudioFileSize(SurahActivity.this, audioFile, suraPosition, reciter);
                if (chkSize)
                    mp = MediaPlayer.create(SurahActivity.this, audioUri);
                if (mp != null) {
                    audioFound = true;
                    mp.setOnCompletionListener(SurahActivity.this);
                    mp.setOnErrorListener(SurahActivity.this);

                    timeAyahSurah.clear();
                    xpp = this.getResources().getXml(audioTimings);
                    timeAyahSurah = AudioTimeXMLParser.getTranslatedAyaList(this, xpp, suraPosition);

                    mp.seekTo(timeAyahSurah.get(0));
                }
            } else {
                btnAudio.setImageResource(R.drawable.play_btn);
                btnAudio.setEnabled(false);
            }

        } catch (Exception e) {
            Log.e("File", e.toString());
        }


    }

    public void playAudio(boolean reset) {
        try {
            if (audioFound) {
                if (play == 0 && mp != null && !menuOpened) {
                    play = 1;

                    if (rowClick) {
                        if (delayIndex == -1) {
                            delayIndex = 0;
                        }
                        if (reciter != prev_Reciter) {//just added block
                            //delayIndex=0;
                            ayahListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    ayahListView.setSelection(delayIndex);
                                }
                            });

                        } else {
                            chkSelection(delayIndex);
                        }

                        long temvalaue = timeAyahSurah.get(delayIndex);
                        mp.seekTo(timeAyahSurah.get(delayIndex));
                        secondAyaSelected = -1;
                        mGlobal.ayahPos = delayIndex;
                        customAdapter.notifyDataSetChanged();

                        rowClick = false;
                        mp.start();
                        ayahListView.post(new Runnable() {
                            @Override
                            public void run() {
                                ayahListView.setSelection(delayIndex);
                            }
                        });

                        delayIndex++;
                    } else {
                        mp.start();
                        if (delayIndex == -1)
                            ayahListView.post(new Runnable() {
                                @Override
                                public void run() {
                                    ayahListView.setSelection(delayIndex);
                                }
                            });

                    }

                    handler.removeCallbacks(sendUpdatesToUI);
                    handler.postDelayed(sendUpdatesToUI, 0);

                    btnAudio.setImageResource(R.drawable.pause_btn);
                } else {
                    pauseAudio();
                    btnAudio.setImageResource(R.drawable.play_btn);
                }

                // btnStop.setEnabled(true);
            } else {
                if (reset)
                    reset(true);
                if (!chkdualActivity) {
                    ((GlobalClass) getApplicationContext()).saveonpause = false;
                    chkdualActivity = true;
                    Intent downloadDialog = new Intent(SurahActivity.this, DownloadDialog.class);
                    downloadDialog.putExtra("SURAHNAME", suraName);
                    downloadDialog.putExtra("POSITION", suraPosition);
                    downloadDialog.putExtra("ANAME", audioFile);
                    downloadDialog.putExtra("RECITER", reciter);
                    startActivityForResult(downloadDialog, requestDownload);
                }
            }
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void pauseAudio() {
        handler.removeCallbacks(sendUpdatesToUI);

        if (mp != null && mp.isPlaying()) {
            mp.pause();
        }

        play = 0;

        btnAudio.setImageResource(R.drawable.play_btn);
    }

    public void reset(boolean adapterReset) {
        handler.removeCallbacks(sendUpdatesToUI);
        if (ayahOptionsLayout.getVisibility() == View.VISIBLE) {
            bookmarkAyahPos = 0;
        }

        if (mp != null && audioFound) {
            mp.seekTo(timeAyahSurah.get(0));
            if (mp.isPlaying()) {
                mp.pause();
            }

        }
        mGlobal.ayahPos = 0;
        if (adapterReset)
            customAdapter.notifyDataSetChanged();
        ayahListView.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ayahListView.requestFocusFromTouch();
                ayahListView.setSelection(0);
                btnAudio.setImageResource(R.drawable.play_btn);
            }
        });
        // btnStop.setEnabled(false);
        rowClick = false;
        delayIndex = 0;
        play = 0;
    }

    public void nextSurah(boolean showToast) {
        suraPosition = suraPosition + 1;
        lastSurahPos = suraPosition;
        if (suraPosition <= 114) {
            btnNext.setImageResource(R.drawable.next_btn);
            refreshSurahData(actionRefresh);

            if (!btnBack.isEnabled()) {
                btnBack.setEnabled(true);
                btnBack.setImageResource(R.drawable.back_btn);
            }
        } else {
            suraPosition = 114;
        }
    }

    public void previousSurah() {
        suraPosition = suraPosition - 1;
        lastSurahPos = suraPosition;
        if (suraPosition > 0) {
            refreshSurahData(actionRefresh);

            if (!btnNext.isEnabled()) {
                btnNext.setEnabled(true);
                btnNext.setImageResource(R.drawable.next_btn);
            }

        } else {
            suraPosition = 1;
        }
    }

//	public void showRemoveAdDilog(boolean ExitFromApp) {
//		if (!chkdualActivity) {
//			chkdualActivity = true;
//			if(!mGlobal.isPurchase) {
//				Intent removeAdDialog = new Intent(SurahActivity.this, RemoveAdsActivity.class);
//				removeAdDialog.putExtra("EXITFROMAPP", ExitFromApp);
//				startActivity(removeAdDialog);
//			}
//			// startActivityForResult(removeAdDialog, requestRemoveAd);
//		}
//	}

    private final void focusOnView() {
        if (timeAyahSurah.size() > 0) {
            if (mp.getCurrentPosition() >= timeAyahSurah.get(delayIndex)) {
                mGlobal.ayahPos = delayIndex;
                customAdapter.notifyDataSetChanged();
                bookmarkAyahPos = mGlobal.ayahPos;
                ayahListView.post(new Runnable() {
                    @Override
                    public void run() {
                        ayahListView.setSelection(mGlobal.ayahPos + 1);
                    }
                });

                if (lastReadSurahCheck) {
                    lastReadSurahCheck = false;
                } else {
                    delayIndex++;
                }
            }
        } else {
            handler.removeCallbacks(sendUpdatesToUI);
        }
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            try {
                focusOnView();
                handler.postDelayed(this, 0);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                handler.removeCallbacks(this);
            }
        }
    };

    private Runnable removeProgressbar = new Runnable() {
        public void run() {
            handler2.removeCallbacks(removeProgressbar);

            progressBar.setVisibility(View.GONE);
            btnTransprnt.setVisibility(View.GONE);

            if (playNextSurahAudio) {
                playAudio(true);
                playNextSurahAudio = false;
            }
        }
    };

    public void onTransliteration(View view) {
        if (mGlobal.hideTransliteration) {
            mGlobal.hideTransliteration = false;
            customAdapter.notifyDataSetChanged();
        } else {
            mGlobal.hideTransliteration = true;
            customAdapter.notifyDataSetChanged();
        }
    }

    public void chkSelection(int index) {
        delayIndex = index;

        if (mp != null && mp.isPlaying()) {
            handler.removeCallbacks(sendUpdatesToUI);
            mp.pause();
            play = 1;

            mGlobal.ayahPos = delayIndex;
            ayahListView.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    ayahListView.requestFocusFromTouch();
                    ayahListView.setSelection(((GlobalClass) getApplication()).ayahPos + 1);
                }
            });
            customAdapter.notifyDataSetChanged();
            // ayahListView.setSelection(delayIndex);

            mp.seekTo(timeAyahSurah.get(delayIndex));
            mp.start();
            delayIndex++;
            handler.removeCallbacks(sendUpdatesToUI);
            handler.postDelayed(sendUpdatesToUI, 0);
        } else {
            rowClick = true;
            mGlobal.ayahPos = delayIndex;
            if (action.equals(actionRefreshBookmark) || action.equals(actionGoto)) {
                action = "";

                ayahListView.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        if (mp != null && mp.isPlaying()) {
                            ayahListView.setSelection(mGlobal.ayahPos);
                            mGlobal.ayahPos -= 1;
                        } else {
                            ayahListView.requestFocusFromTouch();
                            ayahListView.setSelection(mGlobal.ayahPos + 1);
                        }


                    }
                });
            } else {
                if (!isSurahHeaderClick) {//block added

                    ayahListView.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            ayahListView.setSelection((delayIndex + 1));

                        }
                    });
                }
                if ((bundle != null)) {
                    if (bundle.containsKey("fromHomeRubana")) {
                        mGlobal.ayahPos = delayIndex;
                        customAdapter.notifyDataSetChanged();
                        ayahListView.setSelection((delayIndex + 1));
                    } else if (bundle.containsKey("fromHome")) {
                        mGlobal.ayahPos = delayIndex;
                        ayahListView.setSelection((delayIndex + 1));
                    }

                }
                if (refreshNextORPrevious) {
                    ayahListView.post(new Runnable() {
                        @Override
                        public void run() {

                            ayahListView.setSelection((delayIndex));
                        }
                    });
                }


                customAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (suraPosition == 114) {
            reset(true);
        } else {
            if (GotoDialog.isGoToDialogVisible) {
                GotoDialog.isGoToDialogVisible = false;
                GotoDialog.Finish();
                showSurahData();
            }
            playNextSurahAudio = true;
            nextSurah(false);
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        initializeAudios(true);
        playAudio(true);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            isQuranActivity = SurahActivity.this;
            if (BookmarksActivity.longClick) {
                refreshSurahData(actionRefresh);
                BookmarksActivity.longClick = false;
                BookmarksActivity.changeItemColor = 0;

            }

            handler2.removeCallbacks(removeProgressbar);
            handler2.postDelayed(removeProgressbar, 0);
            chkdualActivity = false;
            IntentFilter surahDownloadComplete = new IntentFilter(Constants.BroadcastActionComplete);
            registerReceiver(downloadComplete, surahDownloadComplete);

            try {
                if (mp != null) {
                    if (mp.isPlaying()) {
                        handler.removeCallbacks(sendUpdatesToUI);
                        handler.postDelayed(sendUpdatesToUI, 0);
                    }
                }
            } catch (Exception e) {

                e.printStackTrace();
            }
        } catch (Exception e) {

        }

	/*	if (mGlobal.isPurchase) {
			adImage.setVisibility(View.GONE);
			adview.setVisibility(View.GONE);
			removeAdLayout.setVisibility(View.GONE);
			viewRemoveAdLine.setVisibility(View.GONE);
		} else {
			startAdsCall();
		}*/

		/*
		 * AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE); // Request audio focus for playback audioFocus = am.requestAudioFocus(this, // Use the music stream. AudioManager.STREAM_MUSIC, // Request permanent focus. AudioManager.AUDIOFOCUS_GAIN);
		 */
    }

    private void sendAnalyticsEventData(String category, String event) {
        AnalyticSingaltonClass.getInstance(this).sendEventAnalytics(category, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
	/*	if (mGlobal.ayahPos != -1) {
			settngPref.setSurahNo(suraPosition);
			settngPref.setAyano(mGlobal.ayahPos);
		}*/

        handler.removeCallbacks(sendUpdatesToUI);
        handler2.removeCallbacks(removeProgressbar);

	/*	if (!mGlobal.isPurchase) {
			stopAdsCall();
		}
*/

        if (secMenuOpened) {
            mGlobal.translationLanguage = dialogLanguagePosition;
            mGlobal.hideTransliteration = chkTransliteration;
            mGlobal.chkTafseer = chkTafseer;
            mGlobal.font_size_arabic = fontArabic;
            mGlobal.font_size_eng = fontEnglish;
            setAlarm();
            settngPref.saveSettings(f_index, fontArabic, fontEnglish, reciter, dialogLanguagePosition, chkTransliteration, chkNotification, chkTafseer);
            if (((GlobalClass) getApplicationContext()).notificationOcurd) {
                notificationOccured = false;
            } else {
                startAsyncTask(actionSettings);
            }
        }
        if (delayIndex != -1 && suraPosition != -1) {
            settngPref.setLastReadSurahNo(suraPosition);
            settngPref.setLastReadAyahNo(mGlobal.ayahPos);

        }




		/*if (mGlobal.saveonpause ) {
			settngPref.setSurahNo(suraPosition);
			settngPref.setAyano(mGlobal.ayahPos);
		}*/

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
		/*if(delayIndex!=-1 && suraPosition!= -1)
		{
			*//*if(isLastAya){
				settngPref.setLastReadSurahNo(suraPosition-1);
				isLastAya=false;
			}
			else{
				settngPref.setLastReadSurahNo(suraPosition);
			}*//*
			settngPref.setLastReadSurahNo(suraPosition);
			if(delayIndex!=0)
			{
				if(mp!=null&&mp.isPlaying())
				{
					mGlobal.ayahPos-=1;
					delayIndex-=1;
				}


			}
			settngPref.setLastReadAyahNo(mGlobal.ayahPos);

		}


*/
        isSurahHeaderClick = true;
        isJuzFound = false;
        suraPosition = 1;
        mGlobal.ayahPos = 0;
        delayIndex = 0;
        //	settngPref.setSurahNo(suraPosition);
        //settngPref.setAyano(mGlobal.ayahPos);
        GlobalClass.surahAdsCounter = 0;

        if (mp != null) {
            mp.stop();
            mp.release();
        }

        unregisterReceiver(downloadComplete);
        if (telephonyManeger != null) {
            telephonyManeger.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        isQuranActivity = null;
		/*if (!mGlobal.isPurchase) {
			destroyAds();
		}*/

    }

    @Override
    public void onBackPressed() {

		/*if (secMenuOpened) {
			if(reciter!=prev_Reciter) {
				delayIndex = 0;//these four lines are added
				mGlobal.ayahPos = delayIndex;
				ayahListView.post(new Runnable() {
					@Override
					public void run() {
						ayahListView.setSelection(delayIndex);
					}
				});
				customAdapter.notifyDataSetChanged();
			}
			secMenuOpened = false;
			mGlobal.translationLanguage = dialogLanguagePosition;
			mGlobal.hideTransliteration = chkTransliteration;
			mGlobal.chkTafseer=chkTafseer;
			mGlobal.font_size_arabic = fontArabic;
			mGlobal.font_size_eng = fontEnglish;
			setAlarm();
			settngPref.saveSettings(f_index, fontArabic, fontEnglish, reciter, dialogLanguagePosition, chkTransliteration, chkNotification,chkTafseer);
			startAsyncTask(actionSettings);

		}*/

        if (chkTutorial) {
            chkTutorial = false;
            innerMainLayout.setVisibility(View.VISIBLE);
            tutorialLayout.setVisibility(View.GONE);
            settngPref.setFirstTime();
        } else if (menuOpened || secMenuOpened) {
            //secMenuOpened = false;  uncomment this if errors occurs
            menuOpened = false;
            slideMenu.toggle();
            if (suraPosition == 0) {
                suraPosition = 1;
            }
        } else {
            settngPref.setSurahNo(suraPosition);
            // settngPref.setAyano(mGlobal.ayahPos);
            if (GlobalClass.isOnHold) {
                GlobalClass.isOnHold = false;
                mGlobal.mInterstitialAdSingleton.showInterstitial();
            }
            super.onBackPressed();
        }
    }
    // /Premum gift

    public void onSurahClick(View view) {
        Integer btnClick = Integer.parseInt(view.getTag().toString());

        switch (btnClick) {
            case btnMenuC: {
                menuOpened = true;
                if (delayIndex != -1 && suraPosition != -1) {

                    settngPref.setLastReadSurahNo(suraPosition);
                    settngPref.setLastReadAyahNo(mGlobal.ayahPos);

                }
                if (isSurahHeaderClick) {
                    indexList.setSelection(surahIndex);
                    mGlobal.selectedIndex = surahIndex;
                    listAdapter.notifyDataSetChanged();


                } else {
                    indexList.post(new Runnable() {
                        @Override
                        public void run() {
                            indexList.setSelection(juzListPos);
                            mGlobal.selectedIndex = juzListPos;
                        }
                    });

                    listAdapter.notifyDataSetChanged();
                }
                slideMenu.showMenu();


            }
            break;
            case btnSettingsC: {
                prev_Reciter = reciter;
                menuOpened = true;
                slideMenu.showSecondaryMenu();
            }
            break;
            case btnPlayC: {
                playAudio(false);
            }
            break;
            case btnStopC: {
                reset(true);
            }
            break;
            case btnGotoC: {
                ((GlobalClass) getApplicationContext()).saveonpause = false;
                if (!chkdualActivity) {
                    chkdualActivity = true;
                    Intent gotoDialog = new Intent(SurahActivity.this, GotoDialog.class);
                    gotoDialog.putExtra("SURAHNO", suraPosition);
                    gotoDialog.putExtra("LIMIT", limit);
                    startActivityForResult(gotoDialog, requestGoto);
                }
            }
            break;
            case btnBackC: {
                Animation animation = AnimationUtils.loadAnimation(SurahActivity.this, R.anim.trans_anim_toleftward);
                ayahListView.setAnimation(animation);
                mGlobal.ayahPos = 0;
                delayIndex = 0;

                previousSurah();
                ayahListView.setSelection(0);
            }
            break;

            case btnNextC: {
                Animation animation = AnimationUtils.loadAnimation(SurahActivity.this, R.anim.trans_anim_torighward);
                ayahListView.setAnimation(animation);

                mGlobal.ayahPos = 0;// code added
                delayIndex = 0;//code added
                nextSurah(true);
                ayahListView.setSelection(0);
            }
            break;
            case btnAyahShareC: {
                String arabic = surahList.get(bookmarkAyahPos).getArabicAyah();
                String trans = surahList.get(bookmarkAyahPos).getTranslation();
                getPackageInfo("ShareApp");
                String bodyText = "Urdu Quran - \n" + url;

                String aya = arabic + "\n\n" + trans + "\n\n" + bodyText;

                shareMessage(suraName, aya);

            }
            break;
            case btnBookmarkC: {
                addRemoveSurahBookmarks(suraPosition, bookmarkAyahPos);
            }
            break;
            case btnCloseC: {
                bookmarkAyahPos = -1;
                ayahOptionsLayout.setVisibility(View.GONE);
            }
            break;
            case btnTutorialC: {
                chkTutorial = false;
                innerMainLayout.setVisibility(View.VISIBLE);
                tutorialLayout.setVisibility(View.GONE);
                settngPref.setFirstTime();
            }
            break;

            case btnSurahHeader: //in index search list surah header
                myFilter.getText().clear();
                myFilter.requestFocus();
                myFilter.setInputType(InputType.TYPE_CLASS_TEXT);
                tvNoResultFound.setVisibility(View.GONE);
                indexList.setVisibility(View.VISIBLE);
                isSurahHeaderClick = true;
                initializeIndexListSearch();
                //indexList.setAdapter(new IndexListAdapter(this,dataList, isSurahHeaderClick));
                //initializeIndexList();
                break;
            case btnJuzHeader://juzz heaer in index list search
                myFilter.getText().clear();
                myFilter.requestFocus();
                myFilter.setInputType(InputType.TYPE_CLASS_NUMBER);
                tvNoResultFound.setVisibility(View.GONE);
                indexList.setVisibility(View.VISIBLE);
                isSurahHeaderClick = false;
                initializeIndexListSearch();
                //indexList.setAdapter(new IndexListAdapter(this,juzIndexDataList, isSurahHeaderClick));
                break;
            default:
                return;
        }
    }

    public void onSettingsClick(View view) {
        Integer btnClick = Integer.parseInt(view.getTag().toString());

        switch (btnClick) {
            case btnReciter: {
                if (!chkdualActivity) {
                    chkdualActivity = true;

                    String[] translationList = {"Abdul Basit with Urdu Translation", "Alafasy", "Sudais"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(SurahActivity.this);
                    // Set the dialog title
                    builder.setTitle("Select Reciter").setCancelable(true).setPositiveButton("Ok", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            reciter = singleChoicePosForReciter;
                            getReciter();
                            dialog.dismiss();
                            String reciterName = "Mishray ALFahsay";
                            if (which == 0) {
                                reciterName = "Abdul Basit";
                            } else if (which == 1) {

                                reciterName = "Alafasy";
                            } else {
                                reciterName = "Sudais";
                            }
                            ;
                            sendAnalyticsEventData("Reciter", reciterName);
                            chkdualActivity = false;
                        }
                    }).setNegativeButton("Cancel", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            chkdualActivity = false;
                            dialog.dismiss();
                        }
                    }).setOnKeyListener(new Dialog.OnKeyListener() {

                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            // TODO Auto-generated method stub
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                // finish();
                                chkdualActivity = false;
                                dialog.dismiss();
                            }
                            return true;
                        }
                    })

                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    // TODO Auto-generated method stub
                                    chkdualActivity = false;
                                    dialog.dismiss();
                                }
                            })
                            // Specify the list array, the items to be selected by
                            // default (null for none),
                            // and the listener through which to receive callbacks
                            // when items are selected
                            .setSingleChoiceItems(translationList, reciter, new OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int pos) {
                                    try {
                                        singleChoicePosForReciter = pos;
                                        chkdualActivity = false;
                                        chkSettingSaved = true;
                                        initializaSurahData();
                                    } catch (Exception e) {
                                        Log.e("DIALOG EXCEPTION", e.toString());
                                    }
                                }
                            });

                    builder.create();
                    builder.show();
                }
            }
            break;

            case btnTranslationC: {
                if (!chkdualActivity) {
                    chkdualActivity = true;
                    String[] translationList = {"Off", "Urdu (Ahmed Raza Khan)", "Urdu (Maulana Mehmood-ul-Hassan)", "Urdu (Maududi)", "Urdu (Jalandhari)", "Urdu (Junagarhi)", "English (Saheeh)", "English (Pickthal)", "English (Shakir)", "English (Maududi)", "English (Daryabadi)", "English (Yusuf Ali)", "Spanish", "French", "Chinese",
                            "Persian", "Italian", "Dutch", "Indonesian", "Melayu", "Hindi", "Arabic", "Turkish", "Bangla", "Russian", "Japanese", "Portuguese", "Thai"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(SurahActivity.this);
                    // Set the dialog title
                    builder.setTitle("Select Translation").setCancelable(true).setPositiveButton("Ok", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            // TODO Auto-generated method stub
                            dialogLanguagePosition = singleChoicePosForTranslation;
                            chkSettingSaved = true;
                            initializaSurahData();
                            dialog.dismiss();
                            chkdualActivity = false;
                        }
                    }).setNegativeButton("Cancel", new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            chkdualActivity = false;
                            dialog.dismiss();
                        }
                    })
                            // Specify the list array, the items to be selected by
                            // default (null for none),
                            // and the listener through which to receive callbacks
                            // when items are selected
                            .setOnKeyListener(new Dialog.OnKeyListener() {

                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    // TODO Auto-generated method stub
                                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                                        // finish();
                                        chkdualActivity = false;
                                        dialog.dismiss();
                                    }
                                    return true;
                                }
                            })

                            .setOnCancelListener(new OnCancelListener() {

                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    // TODO Auto-generated method stub
                                    chkdualActivity = false;
                                    dialog.dismiss();
                                }
                            }).setSingleChoiceItems(translationList, dialogLanguagePosition, new OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int pos) {
                            try {
                                singleChoicePosForTranslation = pos;

                            } catch (Exception e) {
                                Log.e("DIALOG EXCEPTION", e.toString());
                            }
                        }
                    });

                    builder.create();
                    builder.show();
                }

            }
            break;
            case btnTafseerC:
                if (chkTafseer) {
                    chkTafseer = false;
                    imgTafseer.setImageResource(R.drawable.img_off);
                } else {
                    chkTafseer = true;
                    imgTafseer.setImageResource(R.drawable.img_on);
                    showTafseerDialog();

                }

                break;
            case btnTransliterationC: {
                if (chkTransliteration) {
                    chkTransliteration = false;
                    imgTransliteration.setImageResource(R.drawable.img_off);
                } else {
                    chkTransliteration = true;
                    imgTransliteration.setImageResource(R.drawable.img_on);
                }
            }
            break;

            case btnBookmarksC: {
                bookmarkAyahPos = -1;
                ayahOptionsLayout.setVisibility(View.GONE);
                if (!chkdualActivity) {
                    chkdualActivity = true;
                    mAnalyticSingaltonClass.sendScreenAnalytics("Quran_Bookmarks_Screen");
                    Intent bookmarksActivity = new Intent(SurahActivity.this, BookmarksActivity.class);
                    bookmarksActivity.putExtra("FROM", "BMARK");
                    startActivityForResult(bookmarksActivity, requestBookmark);
                }
            }
            break;
            case btnStopSignsC: {
                if (!chkdualActivity) {
                    chkdualActivity = true;
                    mAnalyticSingaltonClass.sendScreenAnalytics("Quran_StopSigns_Screen");
                    Intent stopSignActivity = new Intent(SurahActivity.this, StopSignsActivity.class);
                    startActivity(stopSignActivity);
                }
            }
            break;

            case btnSajdahsC: {
                bookmarkAyahPos = -1;
                ayahOptionsLayout.setVisibility(View.GONE);
                if (!chkdualActivity) {
                    // used for saving last read in surah activity
                    ((GlobalClass) getApplicationContext()).saveonpause = false;
                    chkdualActivity = true;
                    Intent sajdahsActivity = new Intent(SurahActivity.this, BookmarksActivity.class);
                    sajdahsActivity.putExtra("FROM", "SAJDAHS");
                    startActivityForResult(sajdahsActivity, requestBookmark);
                }
            }
            break;
            case btnAboutC: {
                if (!chkdualActivity) {
                    chkdualActivity = true;
                    Intent aboutActivity = new Intent(SurahActivity.this, AboutActivity.class);
                    // used for saving last read in surah activity
                    ((GlobalClass) getApplicationContext()).saveonpause = false;
                    startActivity(aboutActivity);
                }
            }
            break;
            case btnShareC: {

                getPackageInfo("ShareApp");
                shareMessage(getResources().getString(R.string.app_name_main), url);

            }
            break;
            case btnMoreC: {
                if (!chkdualActivity) {
                    chkdualActivity = true;
//					String uri = "https://play.google.com/store/apps/developer?id=Quran+Reading";
                    getPackageInfo("MoreApps");
                    Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                    startActivity(intent6);
                }

            }
            break;
            case btnSaveC: {
                mGlobal.translationLanguage = dialogLanguagePosition;
                mGlobal.hideTransliteration = chkTransliteration;
                mGlobal.chkTafseer = chkTafseer;
                mGlobal.font_size_arabic = fontArabic;
                mGlobal.font_size_eng = fontEnglish;
                setAlarm();
                settngPref.saveSettings(f_index, fontArabic, fontEnglish, reciter, dialogLanguagePosition, chkTransliteration, chkNotification, chkTafseer);
                startAsyncTask(actionSettings);
            }
            break;
            case btnResetC: {
                if (!chkdualActivity) {
                    chkdualActivity = true;
                    Intent resetSettings = new Intent(SurahActivity.this, ResetDialog.class);
                    // used for saving last read in surah activity
                    ((GlobalClass) getApplicationContext()).saveonpause = false;
                    startActivityForResult(resetSettings, requestReset);
                }

            }
            break;
            case btnNotificationC: {
                if (chkNotification) {
                    chkNotification = false;
                    imgNotification.setImageResource(R.drawable.img_off);
                } else {
                    chkNotification = true;
                    imgNotification.setImageResource(R.drawable.img_on);

                }
            }
            break;

            case btnRemoveAdC: {
                // used for saving last read in surah activity
                ((GlobalClass) getApplicationContext()).saveonpause = false;
//				showRemoveAdDilog(false);
            }
            break;

            case btnDisclaimer: {
                showDisclaimer();
            }
            break;

            case btnWordSearch: {
                showWordSearchDialog();
            }
            break;

            default:
                return;
        }
    }

    private void getPackageInfo(String text) {

        final PackageManager packageManager = SurahActivity.this.getPackageManager();

        try {
            final ApplicationInfo applicationInfo = packageManager.getApplicationInfo(SurahActivity.this.getPackageName(), 0);
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

    private void showDisclaimer() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.txt_disclaimer);
        builder.setMessage(R.string.disclaimer);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.show();
		/*TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);
		dialog.show();*/

    }

    private void showTafseerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_tafseer_dialog);
        builder.setMessage(R.string.body_text_tafseer_dialog);
        builder.setPositiveButton(R.string.txt_gotit, null);
        AlertDialog dialog = builder.show();
    }

    private void showWordSearchDialog() {
        Intent wordSearchDialog = new Intent(SurahActivity.this, WordSearch.class);
        startActivityForResult(wordSearchDialog, requestWordSearch);

    }

    public void resetSettings() {
        f_index = 0;//code added previously f_index was 1 i set it to 0
        fontArabic = fontSize_A[f_index];
        fontEnglish = fontSize_E[f_index];
        reciter = 0;
        prev_Reciter = reciter;
        chkTransliteration = false;
        chkNotification = true;
        chkTafseer = false;
        // fontSize_A = fontSize_A_2;
        // fontArabic = fontSize_A[f_index];
        dialogLanguagePosition = 1;
        tvReciter.setText("Abdul Basit");
        initializaSurahData();
        mGlobal.translationLanguage = dialogLanguagePosition;
        mGlobal.chkTafseer = chkTafseer;
        mGlobal.hideTransliteration = chkTransliteration;
        mGlobal.font_size_arabic = fontArabic;
        mGlobal.font_size_eng = fontEnglish;

        tvArabic.setTextSize(fontArabic);
        tvArabic.setPadding(0, 0, 0, 0);
        tvTranslation.setText("Urdu (Ahmed Raza Khan)");
        setAlarm();

        settngPref.saveSettings(f_index, fontArabic, fontEnglish, reciter, dialogLanguagePosition, chkTransliteration, chkNotification, chkTafseer);
    }

    public void shareMessage(String subject, String body) {
        if (!chkdualActivity) {
            chkdualActivity = true;
            Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        }
    }

    @SuppressLint("NewApi")
    public void startAsyncTask(String from) {
        AsyncTaskHandler task = new AsyncTaskHandler();
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {

            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, from);
        } else {
            task.execute(from);
        }
    }

    private class AsyncTaskHandler extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            btnTransprnt.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... values) {
            String action = values[0];

            try {
                if (!action.equals(actionSettings))
                    initializeSettings();

                if (action.equals(actionRefresh) || action.equals(actionRefreshBookmark)) {
                    initializeAudios(true);

                } else if (prev_Reciter != reciter)
                    initializeAudios(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return action;
        }

        @Override
        protected void onPostExecute(String action) {
            if (action.equals(actionRefresh) || action.equals(actionRefreshBookmark)) {
                initializaSurahData();
            }

            if (chkCreate || action.equals(actionSettings)) {
                progressBar.setVisibility(View.GONE);
                btnTransprnt.setVisibility(View.GONE);
                initializeSettingsWidgets();

                if (customAdapter != null)
                    customAdapter.notifyDataSetChanged();
            }

            if (!action.equals(actionSettings)) {
                showSurahData();
                if (refreshNextORPrevious) {
                    refreshNextORPrevious = false;
                    onLoad();
                }
                if (action.equals(actionRefreshBookmark))
                    chkSelection(bookmarkAyahPos);
                handler2.removeCallbacks(removeProgressbar);
                handler2.postDelayed(removeProgressbar, 1500);
            }

            chkCreate = false;
            bookmarkAyahPos = -1;
            ayahOptionsLayout.setVisibility(View.GONE);

            lastReadSurahCheck = false;

            // Default value for notification after notification triggered
            notificationOccured = false;
            btnAudio.setEnabled(true);
            if (prev_Reciter != reciter) {
                btnAudio.setImageResource(R.drawable.play_btn);
                play = 0;
                // btnStop.setEnabled(false);
            }
            tvHeader.setSelected(true);
        }
    }

    // /////////////////////////// Start of Intent Callback Handler
    // //////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == requestGoto) {
                gotoIndex = data.getIntExtra("INDEX", 0);
                Log.i("Index", String.valueOf(gotoIndex));
                bookmarkAyahPos = gotoIndex;
                action = actionGoto;
                chkSelection(gotoIndex);
            } else if (requestCode == requestBookmark) {
                bookmarksBack = 1;
                req = 2;
                if (secMenuOpened)
                    slideMenu.toggle();
                action = actionRefreshBookmark;

                int suraNo = data.getIntExtra("SURAHNO", 0);
                int ayahNo = data.getIntExtra("AYAHNO", 0);
                lastSurahPos = suraNo - 1;

                if (suraNo == suraPosition) {
                    chkSelection(ayahNo);
                } else {
                    surahChange = true;
                    suraPosition = suraNo;
                    bookmarkAyahPos = ayahNo;
                    refreshSurahData(actionRefreshBookmark);
                }
            } else if (requestCode == requestDownload) {
                initializeAudios(true);
                showToast(getResources().getString(R.string.already_downloaded));
            } else if (requestCode == requestRemoveAd) {
                if (mGlobal.isPurchase) {
                    // adImg.setVisibility(View.GONE);
                    // adview.setVisibility(View.GONE);
                    removeAdLayout.setVisibility(View.GONE);
                    viewRemoveAdLine.setVisibility(View.GONE);
                    // stopAdsCall();
                    // destroyAds();
                }
            } else if (requestCode == requestReset) {
                boolean resetok = data.getBooleanExtra("RESET", false);
                if (resetok) {
                    reset = true;
                    slideMenu.toggle();
                    reset(true);
                    resetSettings();
                    startAsyncTask(actionSettings);
                } else {
                    // Do Nothing
                }
            } else if (requestCode == requestWordSearch) {
                String from = data.getStringExtra("FROM");
                word = data.getStringExtra("WORD");

                if (from.equals("quran")) {
                    if (!chkdualActivity) {
                        chkdualActivity = true;
                        Intent wordSearch = new Intent(SurahActivity.this, BookmarksActivity.class);
                        wordSearch.putExtra("FROM", "WORDSEARCH");
                        wordSearch.putExtra("SurahPos", -1);
                        wordSearch.putExtra("WORD", word);
                        startActivityForResult(wordSearch, requestWordSearchAgain);

                    }
                } else if (from.equals("sura")) {
                    if (!chkdualActivity) {
                        chkdualActivity = true;
                        Intent wordSearch = new Intent(SurahActivity.this, BookmarksActivity.class);
                        wordSearch.putExtra("FROM", "WORDSEARCH");
                        wordSearch.putExtra("SurahPos", suraPosition);
                        wordSearch.putExtra("WORD", word);
                        startActivityForResult(wordSearch, requestWordSearchAgain);

                    }
                }
            } else if (requestCode == requestWordSearchAgain) {

            } else if (requestCode == RESULT_CANCELED) {

            }

        }
    }


    // /////////////////////////// End of Intent Callback Handler
    // //////////////////////////////////

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        // Log.v("Progress", String.valueOf(progress));
        f_index = progress;
        fontArabic = fontSize_A[f_index];
        fontEnglish = fontSize_E[f_index];

        tvArabic.setTextSize(fontArabic);
        txt_trans.setTextSize(fontEnglish);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // int progress = seekBar.getProgress();
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(myFilter.getWindowToken(), 0);
    }


    private BroadcastReceiver downloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                int position;
                boolean status = false;
                String from = "", name = "";

                status = intent.getBooleanExtra("STATUS", false);
                from = intent.getStringExtra("FROM");
                name = intent.getStringExtra("NAME");
                position = intent.getIntExtra("POSITION", -1);

                if (status) {
                    if (from.equals("zip") && !audioFound) {
                        initializeAudios(false);

                        mGlobal.showToast("Quran Download Completely");
                    } else {
                        if (position == suraPosition && name.equals(audioFile)) {
                            initializeAudios(false);
                        }

                        name = dataList.get(position - 1).getEngSurahName();
                        mGlobal.showToast("Downloading Surah " + name + " finished");
                    }
                } else {
                    String msg = "";

                    if (from.equals("zip")) {
                        msg = getResources().getString(R.string.error_occurred_downloading_quran);
                    } else {
                        if (name.contains("Surah "))
                            name = name.replace("Surah ", "");

                        msg = getResources().getString(R.string.error_occurred_downloading_surah) + " " + name;
                    }

                    mGlobal.showToast(msg);
                }
            }
        }
    };

    public void setAlarm() {
        AlarmCalss mAlarmHelper = new AlarmCalss(this);
        if (chkNotification) {

            mAlarmHelper.cancelAlarm();
            mAlarmHelper.setAlarm();
        } else {
            mAlarmHelper.cancelAlarm();
        }
    }

    @Override
    public void onClose() {
        if (myFilter.getText().toString().length() > 0)
            myFilter.setText("");
    }

    private void indexListClickHandler() {
        if (isSurahHeaderClick) {
            int pos;
            pos = dataList.get(positionIndexListClick).getSurahNo();

            if (pos != lastSurahPos) {

                surahChange = true;
            } else {
                surahChange = false;
            }

            lastSurahPos = pos;
            switch (suraPosition) {
                case 1:
                    juzPosition = 0;
                    isJuzFound = true;
                    break;
                case 2:
                    isSecondSurah = true;
					/*if(juzListPos==1){ //second juz
						juzPosition = 1;
					}
					else{  //third juz
						juzPosition = 2;
					}*/
                    juzPosition = 1;
                    juzListPos = 1;

                    isJuzFound = true;
                    break;
                case 3:
                    juzPosition = 3;
                    isJuzFound = true;
                    break;
                case 4:
                    isFourthSurah = true;
                    juzListPos = 4;
					/*if(juzListPos==4){ //second juz
						juzPosition = 4;
					}
					else{  //third juz
						juzPosition = 5;
					}*/
                    isJuzFound = true;
                    break;
                case 5:
                    juzPosition = 6;
                    isJuzFound = true;
                    break;
                case 6:
                    juzPosition = 7;
                    isJuzFound = true;
                    break;
                case 7:
                    juzPosition = 8;
                    isJuzFound = true;
                    break;
                case 8:
                    juzPosition = 9;
                    isJuzFound = true;
                    break;
                case 9:
                    juzPosition = 10;
                    isJuzFound = true;
                    break;
                case 10:
                    juzListPos = 10;
                    isJuzFound = false;
                    break;
                case 11:
                    juzPosition = 11;
                    isJuzFound = true;
                    break;
                case 12:
                    juzPosition = 12;
                    isJuzFound = true;
                    break;
                case 13:
                    juzListPos = 12;
                    isJuzFound = false;
                    break;
                case 14:
                    juzListPos = 12;
                    isJuzFound = false;
                    break;
                case 15:
                    juzPosition = 13;
                    isJuzFound = true;
                    break;
                case 16:
                    juzListPos = 13;
                    isJuzFound = false;
                    break;
                case 17:
                    juzPosition = 14;
                    isJuzFound = true;
                    break;
                case 18:
                    juzPosition = 15;
                    isJuzFound = true;
                    break;
                case 19:
                    juzListPos = 15;
                    isJuzFound = false;
                    break;
                case 20:
                    juzListPos = 15;
                    isJuzFound = false;
                    break;
                case 21:
                    juzPosition = 16;
                    isJuzFound = true;
                    break;
                case 22:
                    juzListPos = 16;
                    isJuzFound = false;
                    break;
                case 23:
                    juzPosition = 17;
                    isJuzFound = true;
                    break;
                case 24:
                    juzListPos = 17;
                    isJuzFound = false;
                    break;
                case 25:
                    juzPosition = 18;
                    isJuzFound = true;
                    break;
                case 26:
                    juzListPos = 18;
                    isJuzFound = false;
                    break;
                case 27:
                    juzPosition = 19;
                    isJuzFound = true;
                    break;
                case 28:
                    juzListPos = 19;
                    isJuzFound = false;
                    break;
                case 29:
                    juzPosition = 20;
                    isJuzFound = true;
                    break;
                case 30:
                    juzListPos = 20;
                    isJuzFound = false;
                    break;
                case 31:
                    juzListPos = 20;
                    isJuzFound = false;
                    break;
                case 32:
                    juzListPos = 20;
                    isJuzFound = false;
                    break;
                case 33:
                    juzPosition = 21;
                    isJuzFound = true;
                    break;
                case 34:
                    juzListPos = 21;
                    isJuzFound = false;
                    break;
                case 35:
                    juzListPos = 21;
                    isJuzFound = false;
                    break;
                case 36:
                    juzPosition = 22;
                    isJuzFound = true;
                    break;
                case 37:
                    juzListPos = 22;
                    isJuzFound = false;
                    break;
                case 38:
                    juzListPos = 22;
                    isJuzFound = false;
                    break;
                case 39:
                    juzPosition = 23;
                    isJuzFound = true;
                    break;
                case 40:
                    juzListPos = 23;
                    isJuzFound = false;
                    break;
                case 41:
                    juzPosition = 24;
                    isJuzFound = true;
                    break;
                case 42:
                    juzListPos = 24;
                    isJuzFound = false;
                    break;
                case 43:
                    juzListPos = 24;
                    isJuzFound = false;
                    break;
                case 44:
                    juzListPos = 24;
                    isJuzFound = false;
                    break;
                case 45:
                    juzListPos = 24;
                    isJuzFound = false;
                    break;
                case 46:
                    juzPosition = 25;
                    isJuzFound = true;
                    break;
                case 47:
                    juzListPos = 25;
                    isJuzFound = false;
                    break;
                case 48:
                    juzListPos = 25;
                    isJuzFound = false;
                    break;
                case 49:
                    juzListPos = 25;
                    isJuzFound = false;
                    break;
                case 50:
                    juzListPos = 25;
                    isJuzFound = false;
                    break;
                case 51:
                    juzPosition = 26;
                    isJuzFound = true;
                    break;
                case 52:
                    juzListPos = 26;
                    isJuzFound = false;
                    break;
                case 53:
                    juzListPos = 26;
                    isJuzFound = false;
                    break;
                case 54:
                    juzListPos = 26;
                    isJuzFound = false;
                    break;
                case 55:
                    juzListPos = 26;
                    isJuzFound = false;
                    break;
                case 56:
                    juzListPos = 26;
                    isJuzFound = false;
                    break;
                case 57:
                    juzListPos = 26;
                    isJuzFound = false;
                    break;
                case 58:
                    juzPosition = 27;
                    isJuzFound = true;
                    break;
                case 59:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 60:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 61:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 62:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 63:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 64:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 65:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 66:
                    juzListPos = 27;
                    isJuzFound = false;
                    break;
                case 67:
                    juzPosition = 28;
                    isJuzFound = true;
                    break;
                case 68:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 69:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 70:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 71:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 72:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 73:
                    isJuzFound = false;
                    break;
                case 74:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 75:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 76:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 77:
                    juzListPos = 28;
                    isJuzFound = false;
                    break;
                case 78:
                    juzPosition = 29;
                    isJuzFound = true;
                    break;
                case 79:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 80:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 81:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 82:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 83:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 84:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 85:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 86:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 87:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 88:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 89:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 90:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 91:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 92:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 93:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 94:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 95:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 96:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 97:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 98:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 99:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 100:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 101:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 102:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 103:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 104:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 105:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 106:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 107:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 108:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 109:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 110:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 111:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 112:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 113:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                case 114:
                    juzListPos = 29;
                    isJuzFound = false;
                    break;
                default:
                    isJuzFound = false;


            }
            mGlobal.selectedIndex = suraPosition - 1;

            if (pos != suraPosition) {
                reset(false);

                surahSelected = true;
                suraPosition = pos;
                surahIndex = suraPosition - 1;
                mGlobal.selectedIndex = surahIndex;
            }

        } else {  //juzz part
            surahSelected = true;
            isJuzClick = true;
            isSurahHeaderClick = false;
            juzListPos = juzIndexDataList.get(positionIndexListClick).getIndexNo();
            juzListPos -= 1;
            //initalizing juz part
            isSecondSurah = false;
            isFourthSurah = false;
            switch (juzListPos) {
                case 0:
                    suraPosition = 1;
                    juzPosition = 0;
                    isJuzFound = true;
                    break;
                case 1:
                    suraPosition = 2;
                    isSecondSurah = true;
                    juzPosition = 1;
                    isJuzFound = true;
                    break;
                case 2:
                    juzPosition = 2;
                    isSecondSurah = true;
                    suraPosition = 2;
                    isJuzFound = true;
                    break;
                case 3:
                    suraPosition = 3;
                    juzPosition = 3;
                    isJuzFound = true;
                    break;
                case 4:
                    juzPosition = 4;
                    suraPosition = 4;
                    isFourthSurah = false;
                    isJuzFound = true;
                    break;
                case 5:
                    suraPosition = 4;
                    isFourthSurah = false;
                    juzPosition = 5;
                    isJuzFound = true;
                    break;
                case 6:
                    suraPosition = 5;
                    juzPosition = 6;
                    isJuzFound = true;
                    break;
                case 7:
                    suraPosition = 6;
                    juzPosition = 7;
                    isJuzFound = true;
                    break;
                case 8:
                    suraPosition = 7;
                    juzPosition = 8;
                    isJuzFound = true;
                    break;
                case 9:
                    suraPosition = 8;
                    juzPosition = 9;
                    isJuzFound = true;
                    break;
                case 10:
                    suraPosition = 9;
                    juzPosition = 10;
                    isJuzFound = true;
                    break;
                case 11:
                    juzPosition = 11;
                    suraPosition = 11;
                    isJuzFound = true;
                    break;
                case 12:
                    suraPosition = 12;
                    juzPosition = 12;
                    isJuzFound = true;
                    break;
                case 13:
                    suraPosition = 15;
                    juzPosition = 13;
                    isJuzFound = true;
                    break;
                case 14:
                    suraPosition = 17;
                    juzPosition = 14;
                    isJuzFound = true;
                    break;
                case 15:
                    suraPosition = 18;
                    juzPosition = 15;
                    isJuzFound = true;
                    break;
                case 16:
                    suraPosition = 21;
                    juzPosition = 16;
                    isJuzFound = true;
                    break;
                case 17:
                    suraPosition = 23;
                    juzPosition = 17;
                    isJuzFound = true;
                    break;
                case 18:
                    suraPosition = 25;
                    juzPosition = 18;
                    isJuzFound = true;
                    break;
                case 19:
                    suraPosition = 27;
                    juzPosition = 19;
                    isJuzFound = true;
                    break;
                case 20:
                    suraPosition = 29;
                    juzPosition = 20;
                    isJuzFound = true;
                    break;
                case 21:
                    suraPosition = 33;
                    juzPosition = 21;
                    isJuzFound = true;
                    break;
                case 22:
                    suraPosition = 36;
                    juzPosition = 22;
                    isJuzFound = true;
                    break;
                case 23:
                    suraPosition = 39;
                    juzPosition = 23;
                    isJuzFound = true;
                    break;
                case 24:
                    suraPosition = 41;
                    juzPosition = 24;
                    isJuzFound = true;
                    break;
                case 25:
                    suraPosition = 46;
                    juzPosition = 25;
                    isJuzFound = true;
                    break;
                case 26:
                    suraPosition = 51;
                    juzPosition = 26;
                    isJuzFound = true;
                    break;
                case 27:
                    suraPosition = 58;
                    juzPosition = 27;
                    isJuzFound = true;
                    break;
                case 28:
                    suraPosition = 67;
                    juzPosition = 28;
                    isJuzFound = true;
                    break;
                case 29:
                    suraPosition = 78;
                    juzPosition = 29;
                    isJuzFound = true;
                    break;


            }
            if (isJuzFound && !isSurahHeaderClick) {
                surahIndex = suraPosition - 1;
                mGlobal.selectedIndex = juzListPos;
                mGlobal.ayahPos = juzDataList.get(juzListPos).getAyahNO();
                //	mGlobal.ayahPos-=1;
                delayIndex = mGlobal.ayahPos + 1;

            }


        }
        //listAdapter.notifyDataSetChanged();
        //hideSoftKeyboard();
	/*	if(isSurahHeaderClick){

			indexList.setSelection(surahIndex);
		}
		else{
			indexList.setSelection(juzListPos);

		}
		listAdapter.notifyDataSetChanged();*/
        slideMenu.toggle();

    }

    @Override
    public void adClosed() {
        // TODO Auto-generated method stub

        InterstitialAdSingleton mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(this);
        mInterstitialAdSingleton.setInterstitialCloseListner(null);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                indexListClickHandler();
            }
        }, timerInterstitial);
    }

	/*
	 * @Override public void onAudioFocusChange(int focusChange) { // TODO Auto-generated method stub if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) { // Pause mp.pause(); } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) { // Resume mp.start(); } else if (focusChange ==
	 * AudioManager.AUDIOFOCUS_LOSS) { // Stop or pause depending on your need pauseAudio(); } }
	 */

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

    //list view swipe features
    private void onLoad() {
        ayahListView.stopRefresh();
        ayahListView.stopLoadMore();
        play = 0;
        btnAudio.setEnabled(true);
        btnAudio.setImageResource(R.drawable.play_btn);
        //


        //ayahListView.setRefreshTime("just");
    }

    @Override
    public void onRefresh() {
        if (suraPosition != 1) {

            Animation animation = AnimationUtils.loadAnimation(SurahActivity.this, R.anim.trans_anim_downwards);
            ayahListView.setAnimation(animation);
            suraPosition -= 1;
            lastSurahPos = suraPosition;
            mGlobal.selectedIndex = suraPosition - 1;
            surahIndex = suraPosition - 1;
            refreshNextORPrevious = true;
            startAsyncTask(actionRefresh);
            //refreshNextORPrevious = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    indexList.setSelection(suraPosition);
                }
            }, 200);


        }
    }


    @Override
    public void onLoadMore() {

        if (suraPosition != 114) {
            Animation animation = AnimationUtils.loadAnimation(SurahActivity.this, R.anim.trans_anim_upward);
            ayahListView.setAnimation(animation);
            lastSurahPos = suraPosition;
            surahIndex = suraPosition;
            mGlobal.selectedIndex = suraPosition;
            refreshNextORPrevious = true;
            mGlobal.ayahPos = 0;
            delayIndex = 0;
            startAsyncTask(actionRefresh);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    indexList.setSelection(suraPosition);
                }
            }, 200);
            //indexList.setSelection(suraPosition);
            //refreshNextORPrevious=false;
            //onLoad();
            suraPosition += 1;

        }


    }


}