package com.QuranReading.urduquran.ads;

import android.content.Context;

public class InterstitialAdSingleton implements InterstitialAdListener {

	private static InterstitialAdSingleton mInstance = null;
	private GoogleInterstitialAds mGoogleAds;
	private Context mContext;
	InterstitialAdListener mInterstitialAdListener;

	public static InterstitialAdSingleton getInstance(Context context) {

		if (mInstance == null) {
			mInstance = new InterstitialAdSingleton(context);
		}
		return mInstance;
	}

	private InterstitialAdSingleton(Context context) {

		mContext = context.getApplicationContext();
		mGoogleAds = new GoogleInterstitialAds(mContext);
		mGoogleAds.setInterstitialAdListener(this);
	}

	public void firstInterstitialLoad() {
		mGoogleAds.callInterstetialAds(false);
	}

	public void setInterstitialCloseListner(InterstitialAdListener mInterstitialAdListener) {
		this.mInterstitialAdListener = mInterstitialAdListener;
	}

	public void showInterstitial() {

		mGoogleAds.showInterstetialAds();
	}

	public boolean isInterstitialAdLoaded() {
		return mGoogleAds.isInterstitialAdLoaded();
	}

	public void cancelInterstitialAd() {
		mGoogleAds.cancelIntersitialAds();
	}

	@Override
	public void adClosed() {
		if (mInterstitialAdListener != null)
			mInterstitialAdListener.adClosed();
	}
}
