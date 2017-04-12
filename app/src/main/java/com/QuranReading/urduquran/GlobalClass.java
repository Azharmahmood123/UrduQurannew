package com.QuranReading.urduquran;

import com.QuranReading.sharedPreference.PurchasePreferences;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GlobalClass extends Application {
	public PurchasePreferences purchasePref;
	public boolean deviceS3 = false;
	public boolean isPurchase = false;
	public boolean downloadQuran = false;
	public static boolean isLayoutVisible = true;
	public boolean	notificationCalTajweed=false;
	public static boolean isFactsNotification=true;
	public static int surahAdsCounter=0,tajweedAdsCounter=0,vocabAdsCounter=0,wbwAdsCounter=0,factsAdsCounter=0;
	public boolean isPlayingWordByWord = false, isPlayingWordByWordKalma = false, isPlayingWordByWordDua = false;

	public Typeface faceArabic, /* faceArabic1, faceArabic2, faceArabic3, faceArabic4, */faceHeading, faceContent1, faceContent3, facefutral;

	public int font_size_arabic, font_size_eng;
	// Typeface fonts
	public Typeface charpalBold;
	public Typeface corbalBold;
	public Typeface chapal_Light;
	// new fonts
	public Typeface robotoLight;
	public Typeface robotoMedium;
	public Typeface robotoRegular;
	public Typeface corbelFont,faceArabic1,faceArabic2;
	public boolean firstTimeShow = true;
	public HashMap<String, List<String>> indexSubTitles; // for Index sub
	// headings
	public List<String> expTitileList;
	// List to contain text
	public ArrayList<String> dataList = new ArrayList<String>();
	public ArrayList<String> mukhroojHaroof = new ArrayList<String>();
	public ArrayList<String> mukhroojMeanings = new ArrayList<String>();
	public ArrayList<String> stoppingWords = new ArrayList<String>();
	public ArrayList<String> stoppingMeanings = new ArrayList<String>();
	public static boolean isDetailedScreen = false;
	public boolean isFinishActivity=false;
	public boolean isResultFragment=false;
	public boolean isDialogueAppear=false;
	public int selectedTabPosition = 2, ayahPosWordbWord = 0, ayahPosFullSura = 0, wordPos = 0, surahPos;
	public MediaPlayer mediaPlayerFullSurah, mediaPlayerWordByWord;
	public MediaPlayer mediaPlayerFullKalma, mediaPlayerKalmaWordByWord;
	public MediaPlayer mediaPlayerFullDua, mediaPlayerDuaWordByWord;
	public int selectedTabPositionKalma = 1, ayahPosWordbWordKalma = 0, ayahPosFullKalma = 0, wordPosKalma = 0, kalmaPos;


	public int selectedTabPositionDua = 1, ayahPosWordbWordDua = 0, ayahPosFullDua = 0, wordPosDua = 0, duaPos;
	public int QuranReciter;
	public int translationLanguage;
	public boolean chkTafseer;
	public static File SurahsFolder;
	public boolean chkSurahTaubah = false;
	public boolean hideTransliteration = false;
	public int ayahPos = -1;
	public static String rootFolderName = "QuranNow/";
	public int ayahPadding = 50;
	public static File rootFolder;
	public static String Surahs = "WBW";
	public static int surahsTotalAud = 382;
	public int selectedIndex = 0;
	public static int notifID = 1212;
	public boolean notificationOcurd = false;
	public boolean saveonpause = false;

	/*
	 * public List mukhroojHaroof, mukhroojMeanings; public List stoppingWords, stoppingMeanings;
	 */

	private void setTypeFace() {
		faceArabic = Typeface.createFromAsset(getAssets(), Constants.ARABIC_FONT);
		/*
		 * faceArabic1 = Typeface.createFromAsset(getAssets(), Constants.ARABIC_FONT1); faceArabic2 = Typeface.createFromAsset(getAssets(), Constants.ARABIC_FONT2); faceArabic3 = Typeface.createFromAsset(getAssets(), Constants.ARABIC_FONT3); faceArabic4 = Typeface.createFromAsset(getAssets(),
		 * Constants.ARABIC_FONT4);
		 */ faceHeading = Typeface.createFromAsset(getAssets(), Constants.HEADING_FONT);
		faceContent1 = Typeface.createFromAsset(getAssets(), Constants.CONTENT_FONT_1);
		faceContent3 = Typeface.createFromAsset(getAssets(), Constants.CONTENT_FONT_3);
		facefutral = Typeface.createFromAsset(getAssets(), Constants.CONTENT_FONT_4);
		corbelFont = Typeface.createFromAsset(getAssets(), "corbel.ttf");
		faceArabic1 = Typeface.createFromAsset(getAssets(), Constants.ARABIC_FONT1);
		faceArabic2 = Typeface.createFromAsset(getAssets(), Constants.ARABIC_FONT2);
		charpalBold = Typeface.createFromAsset(getAssets(), "ChaparralPro-Bold.otf");
		corbalBold = Typeface.createFromAsset(getAssets(), "corbelb.ttf");
		// new fonts
		font_size_arabic = Constants.font_size_arabic;
		robotoLight = Typeface.createFromAsset(getAssets(), "Roboto-Light_1.ttf");
		robotoMedium = Typeface.createFromAsset(getAssets(), "Roboto-Medium_1.ttf");
		robotoRegular = Typeface.createFromAsset(getAssets(), "Roboto-Regular_1.ttf");
	}

	public void showToast(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
	private void createFolders() {
		rootFolder = new File(Environment.getExternalStorageDirectory(), GlobalClass.rootFolderName);
		if (!rootFolder.exists()) {
			rootFolder.mkdirs();
		}


		SurahsFolder = new File(rootFolder.getAbsolutePath(), "WBW");
		if (!SurahsFolder.exists()) {
			SurahsFolder.mkdir();
		}


	}

	public boolean isServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE))
		{
			if(ServiceClass.class.getName().equals(service.service.getClassName()))
			{
				return true;
			}
		}

		return false;
	}
	SettingsSharedPref settingsSharedPref;
	InterstitialAdSingleton mInterstitialAdSingleton;
	public static boolean isOnHold=false;
	Handler adsHandler;
	Runnable adsRunnable;
	long adsTimer;


	@Override
	public void onCreate() {
		super.onCreate();

		purchasePref = new PurchasePreferences(getApplicationContext());
		isPurchase = purchasePref.getPurchased();
		settingsSharedPref=new SettingsSharedPref(this);
		setTypeFace();
		createFolders();


	}
	public void triggerAd()
	{
		if(!isPurchase) {
			mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(this);
			mInterstitialAdSingleton.firstInterstitialLoad();
			adsHandler = new Handler();
			if (settingsSharedPref.isFirstTimeAppLaunched()) {
				settingsSharedPref.setFirstTimeAppLaunched(false);
				adsTimer = 50000;
			} else {
				adsTimer = 10000;
			}
			adsRunnable =new Runnable() {
				@Override
				public void run() {

					if (SurahActivity.isQuranActivity == null) {
						mInterstitialAdSingleton.showInterstitial();
						isOnHold = false;
					} else {

						isOnHold = true;
					}
					adsHandler.removeCallbacks(adsRunnable);

				}
			};
			//
			adsHandler.postDelayed(adsRunnable,adsTimer);
		}
	}
}
