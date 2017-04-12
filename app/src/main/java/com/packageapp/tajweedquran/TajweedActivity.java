package com.packageapp.tajweedquran;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.packageapp.HomeSplashActivity;
import com.packageapp.tajweedquran.alarmnotifications.WeeklyNotifications;
import com.packageapp.tajweedquran.networkdownloading.DownloadDialogTajweed;
import com.packageapp.tajweedquran.networkdownloading.DownloadingZipUtil;

public class TajweedActivity extends FragmentActivity {

	public static TextView toolbarTitle;
	GlobalClass mGlobal;
	public static boolean doubleTapChild = false;
	public static boolean doubleTapExp;
	private  boolean isNotification=false;
	//WeeklyNotifications notification; CustomSharedPrefrences prefrences;


	// Ads
	AdView adview;
	//ImageView adImage;
	private static final String LOG_TAG = "Ads";
	private final Handler adsHandler = new Handler();
	private int timerValue = 3000, networkRefreshTime = 10000;

	public List<String> expSubList4;
	public List<String> getExpSubList5;
	String queryText;
	DBManagerTajweed dbObj;
	//
	String mukhroojData;
	String[] mukhroojSplit;
	String[] stoppingSignSplit;
	//
	boolean checkAlarm;
	public static Activity mainActivity;

	ExpandableListView expListView;
	Activity mActivity;
	int anyId;
	int countPosition;
	long stopLastClick = 0;
	InterstitialAdSingleton mInterstitialAdSingleton;
	// String for google analytics
	String indexScreen = "Tajweed Index Screen";
	SettingsSharedPref prefs;
	private int tempValue=-1;
	private int pageNumber=-1;
	String columnName;
	int adsCounter=0;
	AnalyticSingaltonClass mAnalyticSingaltonClass;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_tajweed);
		prefs=new SettingsSharedPref(this);
		//AnalyticSingaltonClass.getInstance(this).sendScreenAnalytics("Tajweed Screen");
		mainActivity = this;
		mGlobal = ((GlobalClass) getApplicationContext());
		getDrawerData();
		setWeeklyNotification();
		toolbarTitle = (TextView) findViewById(R.id.txt_toolbar);
		toolbarTitle.setText("Learn Tajweed");
		toolbarTitle.setTypeface(((GlobalClass) getApplication()).faceHeading);
		mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);
		mAnalyticSingaltonClass.sendScreenAnalytics("Tajweed_Index_Screen");

		initializeDB();
		SettingsSharedPref pref=new SettingsSharedPref(this);
		if(pref.getLanguageTajweed()==0)//urdu Language
		{
			columnName="urdu_datatext";
		}
		else
		{
			columnName="datatext";
		}
		getDetails(columnName);
		getSplitData();
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ftx = fragmentManager.beginTransaction();
		ftx.replace(R.id.mainFrame, new MainListFragment());
		ftx.commit();
		initializeAds();
		// checkAlarm = this.getIntent().getBooleanExtra("isNotification", false);
		pageNumber=this.getIntent().getIntExtra("detailID",-1);
		if(getIntent().getExtras()!=null) {
			if (pageNumber != -1) {
				isNotification=true;
				callNotificationItem(pageNumber);

			}
			else
			{
				if(prefs.isTajweedFirstTime())
				{
					prefs.setTajweedFirstTime(false);
					checkAudioFile();

				}
			}
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

	/*
        * notification work*/
	private void callNotificationItem(int value) {
		if(value==0)
		{
			tempValue=0;
		}
		else if(value==1)
		{
			tempValue=1;

		}
		else if(value>1 &&value<12)
		{
			tempValue=2;
		}
		else if(value>11&&value<17)
		{
			tempValue=3;
		}




		switch (tempValue) {
			case 0:
				anyId = 0;
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction ftx = fragmentManager.beginTransaction();
				ftx.replace(R.id.mainFrame, newFragment());
				ftx.addToBackStack(null);
				ftx.commit();
				break;

			case 1:
				anyId = 1;
				FragmentManager fragmentManager2 = getSupportFragmentManager();
				FragmentTransaction ftx2 = fragmentManager2.beginTransaction();
				ftx2.replace(R.id.mainFrame, newFragment());
				ftx2.addToBackStack(null);
				ftx2.commit();
				break;

			case 2:
				anyId = 0;
				FragmentManager fragmentManager3 = getSupportFragmentManager();
				FragmentTransaction ftx3 = fragmentManager3.beginTransaction();
				ftx3.replace(R.id.mainFrame, subFragment());
				ftx3.addToBackStack(null);
				ftx3.commit();
				//* intent.putExtra("detailID", anyId); startActivity(intent);


				break;

			case 3:
				anyId = 1;
				FragmentManager fragmentManager4 = getSupportFragmentManager();
				FragmentTransaction ftx4 = fragmentManager4.beginTransaction();
				ftx4.replace(R.id.mainFrame, subFragment());
				ftx4.addToBackStack(null);
				ftx4.commit();

				break;
		}
	}
	private CommonFragment newFragment() {
		Bundle arg = new Bundle();
		arg.putInt("mPosition", anyId);
		CommonFragment fragment = new CommonFragment();
		fragment.setArguments(arg);
		return fragment;

	}

	private SubListFragment subFragment() {
		Bundle arg = new Bundle();
		arg.putInt("mPosition", anyId);
		arg.putInt("notificationPos",pageNumber);
		SubListFragment fragment = new SubListFragment();
		fragment.setArguments(arg);
		return fragment;
	}
	/*End of notification work*/

	private void setWeeklyNotification()
	{
		SettingsSharedPref pref=new SettingsSharedPref(this);
		WeeklyNotifications notifications=new WeeklyNotifications(this);

		if(pref.getTajweed_Notification()==true)
		{
			notifications.cancelAlarm();
			notifications.setDailyAlarm();
		}
		else
		{
			notifications.cancelAlarm();
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


	private void checkAudioFile()
	{
		String audioFile="res_shaddah.mp3";
		try
		{
			if(!ConstantsTajweedQuran.rootPathTajweed.exists())
			{
				ConstantsTajweedQuran.rootPathTajweed.mkdirs();
			}

			File myFile = new File(ConstantsTajweedQuran.rootPathTajweed.getAbsolutePath(), audioFile);

			Uri audioUri = Uri.parse(myFile.getPath());

			if(myFile.exists())
			{

			}
			else
			{
				startActivity(new Intent(TajweedActivity.this, DownloadDialogTajweed.class));
				//showDownloadDialog();
			}
		}
		catch (Exception e)
		{
			Log.e("File", e.toString());
		}
	}


	public void getDrawerData() {
		// List
		mGlobal.mukhroojHaroof = new ArrayList<String>();
		mGlobal.mukhroojMeanings = new ArrayList<String>();
		mGlobal.dataList = new ArrayList<String>();
		//
		mGlobal.stoppingWords = new ArrayList<String>();
		mGlobal.stoppingMeanings = new ArrayList<String>();
		mGlobal.expTitileList = new ArrayList<String>();
		mGlobal.indexSubTitles = new HashMap<String, List<String>>();

		mGlobal.expTitileList = Arrays.asList(getResources().getStringArray(R.array.list_heading));
		expSubList4 = Arrays.asList(getResources().getStringArray(R.array.sublist_4));
		getExpSubList5 = Arrays.asList(getResources().getStringArray(R.array.sublist_5));
		// hash map list
		// //////////////////finally assigning hashmap list all the child lists

		mGlobal.indexSubTitles.put(mGlobal.expTitileList.get(2), expSubList4);
		mGlobal.indexSubTitles.put(mGlobal.expTitileList.get(3), getExpSubList5);

	}

	private void initializeDB() {
		dbObj = new DBManagerTajweed(this);
		try
		{
			dbObj.createDataBase();
			dbObj.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void onHomeClick(View view) {

		finish();
	}

	public void getSplitData() {
		mukhroojData = mGlobal.dataList.get(1).toString();
		mukhroojSplit = mukhroojData.split(":");
		for (int i = 0; i < mukhroojSplit.length; i++)
		{

			mGlobal.mukhroojHaroof.add(mukhroojSplit[i]);
			mGlobal.mukhroojMeanings.add(mukhroojSplit[++i]);
		}
	}

	public void getDetails( String columnName) {
		try
		{
			//queryText = "select datatext from favorite where id==1";
			queryText = "select "+columnName+" from favorite where id==1";
			dbObj.open();
			dbObj.getData(queryText);
			// makhrajul haroof
			queryText = "select "+columnName+" from favorite where id==2";
			dbObj.getData(queryText);
			// stopping signs
			queryText = "select "+columnName+" from favorite where id==3";
			dbObj.getData(queryText);
			// id =4 componet of tajweed throat letters
			queryText = "select "+columnName+" from favorite where id==4";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Bold letters
			queryText = "select "+columnName+" from favorite where id==5";
			dbObj.getData(queryText);
			// id =6 componet of tajweed Silent letters
			queryText = "select "+columnName+" from favorite where id==10";
			dbObj.getData(queryText);
			// id =5 componet of tajweed qalqalah letters
			queryText = "select "+columnName+" from favorite where id==11";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Leen letters Introduction
			queryText = "select "+columnName+" from favorite where id==6";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Leen letters Wao leen
			queryText = "select "+columnName+" from favorite where id==14";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Leen letters ya leen
			queryText = "select "+columnName+" from favorite where id==15";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Harkat Letters Introduction
			queryText = "select "+columnName+" from favorite where id==7";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Harkat Letters Fatah or zabar
			queryText = "select "+columnName+" from favorite where id==16";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Harkat Letters Kasrah Or Zer
			queryText = "select "+columnName+" from favorite where id==17";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Harkat Letters Dammah Or Pesh
			queryText = "select "+columnName+" from favorite where id==18";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Tanween Letters Introduction
			queryText = "select "+columnName+" from favorite where id==8";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Tanween Letters Tanween-Fathatain
			queryText = "select "+columnName+" from favorite where id==19";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Tanween Letters Tanween-Kasratein
			queryText = "select "+columnName+" from favorite where id==20";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Tanween Letters Tanween-Dammatain
			queryText = "select "+columnName+" from favorite where id==0";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Sukoon Letters
			queryText = "select "+columnName+" from favorite where id==12";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Shaddah Letters
			queryText = "select "+columnName+" from favorite where id==13";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Madd Letters Introduction
			queryText = "select "+columnName+" from favorite where id==9";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Madd Letters Madd e mutassil
			queryText = "select "+columnName+" from favorite where id==22";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Madd Letters Madd e munfasil
			queryText = "select "+columnName+" from favorite where id==23";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Madd Letters Madd e Aarith Waqfi
			queryText = "select "+columnName+" from favorite where id==24";
			dbObj.getData(queryText);
			// id =5 componet of tajweed Madd Letters Madd e lazim
			queryText = "select "+columnName+" from favorite where id==25";
			dbObj.getData(queryText);
			// end of Componet of Tajweeed Letters

			// Rules of Tajweeed ,Rules of Nun Sakin Izhar
			queryText = "select "+columnName+" from favorite where id==26";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Nun Sakin Idgham
			queryText = "select "+columnName+" from favorite where id==33";
			dbObj.getData(queryText);
			// Idgham 2
			queryText = "select "+columnName+" from favorite where id==39";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Nun Sakin Iqlaab
			queryText = "select "+columnName+" from favorite where id==27";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Nun Sakin Ikhfa
			queryText = "select "+columnName+" from favorite where id==28";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Meem Sakin Idghame shafwi
			queryText = "select "+columnName+" from favorite where id==29";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Meem Sakin Ikhfa e shafwi
			queryText = "select "+columnName+" from favorite where id==30";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Meem Sakin Izhar e shafwi
			queryText = "select "+columnName+" from favorite where id==31";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Letter Raa Takfkeem // this and
			// second text portion come together
			queryText = "select "+columnName+" from favorite where id==34";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Letter Raa Tarqeeq // second portion
			// of text
			queryText = "select "+columnName+" from favorite where id==35";
			dbObj.getData(queryText);
			// Rule 4
			// Rules of Tajweeed ,Rules of Letter Laam- Tafkheem
			queryText = "select "+columnName+" from favorite where id==36";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Letter Laam- Tarqeeq
			queryText = "select "+columnName+" from favorite where id==37";
			dbObj.getData(queryText);
			// Rule 5
			// Rules of Tajweeed ,Rules of Letter Hamza- tul Wasl
			queryText = "select "+columnName+" from favorite where id==32";
			dbObj.getData(queryText);
			// Rules of Tajweeed ,Rules of Letter Hamza- tul Qat'a
			queryText = "select "+columnName+" from favorite where id==38";
			dbObj.getData(queryText);

		}
		catch (Exception e)

		{

		}
		finally
		{
			dbObj.close();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (!mGlobal.isPurchase) {
			stopAdsCall();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mGlobal.firstTimeShow = true;
		if (!mGlobal.isPurchase) {
			destroyAds();
		}
		GlobalClass.tajweedAdsCounter=0;
	}

	@Override
	protected void onResume() {
		super.onResume();
		doubleTapChild = false;
		if (!mGlobal.isPurchase) {
			startAdsCall();
		} else {
			adview.setVisibility(View.GONE);
		}
	}

}
