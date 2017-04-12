package com.packageapp.wordbyword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SurahListActivity extends AppCompatActivity  {
    private ListView dataListView;
    private String[] titleList, titleListArabic;
    private String headerText;
    private ListAdapter adapter;
    private int SURASADCOUNTER = 0;
    Context context;
    TextView tvHeaderwbw;
    GlobalClass mGlobal;
    AdView adview;
    Boolean chkdualActivity = false;
    //ImageView adImage;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;
    int adsCounter=0;
    AnalyticSingaltonClass mAnalyticSingaltonClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fragment);
        context = this;
        mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);
        mAnalyticSingaltonClass.sendScreenAnalytics("WBW_Index_Screen");
        dataListView = (ListView) findViewById(R.id.duaListView);
        tvHeaderwbw = (TextView) findViewById(R.id.tv_header);
        mGlobal = (GlobalClass) getApplicationContext();
        tvHeaderwbw.setTypeface(mGlobal.faceHeading);
        titleList = getResources().getStringArray(R.array.surah_list_names);
        titleListArabic = getResources().getStringArray(R.array.surah_names_arabic);
        headerText = "Word by Word Surahs";
        tvHeaderwbw.setText(headerText);
        adapter = new ListAdapter(context, titleList, titleListArabic, true, false);// true means not from favourites
        dataListView.setAdapter(adapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {

                Intent duaIntent = new Intent(context,SurahDetailActivity.class);
                duaIntent.putExtra("Title", titleList[pos]);
                duaIntent.putExtra("titleList", titleList);
                duaIntent.putExtra("position", pos);
                startActivity(duaIntent);
                //showIntAds();

            }
        });
        initAds();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!mGlobal.isPurchase) {
            startAdsCall();
        } else {
            adview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!mGlobal.isPurchase) {
            destroyAds();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!mGlobal.isPurchase) {
            stopAdsCall();
        }
    }

   /* private void showIntAds() {

        if (!mGlobal.isPurchase&&GlobalClass.wbwAdsCounter!=0) {
            GlobalClass.wbwAdsCounter=0;
            InterstitialAdSingleton mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(context);
            mInterstitialAdSingleton.showInterstitial();
        }
        else
        {
            GlobalClass.wbwAdsCounter++;
        }

    }*/
    private void initAds() {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalClass.wbwAdsCounter=0;
    }
    public void onHomeClickWBW(View v)
    {
        if(v.getId()==R.id.btnHomeWBW)
        {
            this.finish();
        }
    }
}


