package com.QuranReading.urduquran;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AboutActivity extends Activity {
	// google ads
	AdView adview;
	ImageView adImage;
	private static final String LOG_TAG = "Ads";
	private final Handler adsHandler = new Handler();
	private int timerValue = 3000, networkRefreshTime = 10000;
	AnalyticSingaltonClass mAnalyticSingaltonClass;

	GlobalClass mGlobal;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.about_activity);
		mGlobal = ((GlobalClass) getApplicationContext());

		if (mGlobal.deviceS3)
		{
			// Samsung S3
			setContentView(R.layout.about_activity_s3);
		}
		else
		{
			setContentView(R.layout.about_activity);
		}
		mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);
		mAnalyticSingaltonClass.sendScreenAnalytics("AboutUs_Screen");

		initializeAds();

		TextView tvHeader = (TextView) findViewById(R.id.tv_header);
		TextView tvAppName = (TextView) findViewById(R.id.tv_app_name);
		TextView tvVersion = (TextView) findViewById(R.id.tv_version);
		TextView tvCopyRights = (TextView) findViewById(R.id.tv_copy_right);
		TextView tvRights = (TextView) findViewById(R.id.tv_rights);
		TextView tvLink = (TextView) findViewById(R.id.tv_link);

		tvHeader.setTypeface(mGlobal.faceHeading);
		tvAppName.setTypeface(mGlobal.faceHeading);

		tvVersion.setTypeface(mGlobal.facefutral);
		tvCopyRights.setTypeface(mGlobal.facefutral);
		tvRights.setTypeface(mGlobal.facefutral);
		tvLink.setTypeface(mGlobal.facefutral);
		sendAnalyticsData();

		// //////////////////////////Registering Notification Receiver ///////////////////

		IntentFilter dailySurah = new IntentFilter(Constants.BroadcastActionNotification);
		registerReceiver(dailySurahNotification, dailySurah);

	}

	private void sendAnalyticsData() {
		AnalyticSingaltonClass.getInstance(this).sendScreenAnalytics("About Us Screen");
	}

	private void initializeAds() {
		adview = (AdView) findViewById(R.id.adView);
		adImage = (ImageView) findViewById(R.id.adimg);
		adImage.setVisibility(View.GONE);
		adview.setVisibility(View.GONE);

		if (isNetworkConnected())
		{
			this.adview.setVisibility(View.VISIBLE);
		}
		else
		{
			this.adview.setVisibility(View.GONE);
		}
		setAdsListener();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!mGlobal.isPurchase)
		{
			startAdsCall();
		}
		else
		{
			adImage.setVisibility(View.GONE);
			adview.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (!mGlobal.isPurchase)
		{
			stopAdsCall();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(dailySurahNotification);
		if (!mGlobal.isPurchase)
		{
			destroyAds();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		// used for saving last read in surah activity
		((GlobalClass) getApplicationContext()).saveonpause = true;
	}

	private BroadcastReceiver dailySurahNotification = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			finish();
		}
	};

	// /////////////////////////
	// ////////////////////////
	// ////////////////////////
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
		if (isNetworkConnected())
		{
			AdRequest adRequest = new AdRequest.Builder().build();
			adview.loadAd(adRequest);
		}
		else
		{
			timerValue = networkRefreshTime;
			adsHandler.removeCallbacks(sendUpdatesAdsToUI);
			adsHandler.postDelayed(sendUpdatesAdsToUI, timerValue);
		}
	}

	public void startAdsCall() {
		Log.i(LOG_TAG, "Starts");
		if (isNetworkConnected())
		{
			this.adview.setVisibility(View.VISIBLE);
		}
		else
		{
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
}
