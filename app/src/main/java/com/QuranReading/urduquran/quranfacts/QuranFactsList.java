package com.QuranReading.urduquran.quranfacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import android.widget.TextView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class QuranFactsList extends AppCompatActivity {

    WebView text;
    ViewPager viewPager;
    GlobalClass mGlobal;
    TextView tvHeaderqf;
    TabLayout tabs;
    String[] scientificList, interestingList;
    Context mContext;
    Typeface tf;
    private Bundle bundle,mBundle;
    //ImageView adImage;
    AdView adview;
    private static Activity mActivity = null;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;
    int adsCounter=0;
    InterstitialAdSingleton mInterstitialAdSingleton;
    public  static AppCompatActivity factsActivity;
    AnalyticSingaltonClass mAnalyticSingaltonClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quran_facts_list);
        factsActivity=this;
        mAnalyticSingaltonClass= AnalyticSingaltonClass.getInstance(this);
        mAnalyticSingaltonClass.sendScreenAnalytics("Facts_Index_Screen");
        tvHeaderqf = (TextView) findViewById(R.id.tv_header);
        mGlobal = (GlobalClass) getApplicationContext();
        tvHeaderqf.setTypeface(mGlobal.faceHeading);
        mContext = this;

        viewPager = (ViewPager) findViewById(R.id.tabviewpager);
        tabs = (TabLayout) findViewById(R.id.strip);
        viewPager.setAdapter(new viewpagerAdapter(getSupportFragmentManager()));
        tabs.setupWithViewPager(viewPager);
        scientificList = mContext.getResources().getStringArray(R.array.scietific_facts_list);
        interestingList = mContext.getResources().getStringArray(R.array.interesting_facts_list);
        initAds();
    }




    private void handleIntent(int pos){
        mBundle=new Bundle();//Notification Bundle
        bundle = new Bundle(); //Fragment arguments Bundle
        Intent mExtras = getIntent();
        mBundle = mExtras.getExtras();
        if(mBundle!=null&&mBundle.containsKey("tabPosition")) //Notification Data
        {
            int tabPos=mBundle.getInt("tabPosition");
            if(tabPos==0)
            {
                bundle.putSerializable("List_Facts", interestingList);
            }
            else
            {
                bundle.putSerializable("List_Facts", scientificList);
            }
            bundle.putInt("listItemPos",mBundle.getInt("listItemPos"));
            bundle.putInt("tabPosition",mBundle.getInt("tabPosition"));
            bundle.putStringArray("data",mBundle.getStringArray("data"));
        }
        else
        {  //Normal Flow
            if (pos == 0) {
                bundle.putSerializable("List_Facts", interestingList);
                bundle.putInt("tabPosition",pos);

            } else {
                bundle.putSerializable("List_Facts", scientificList);
                bundle.putInt("tabPosition",pos);
            }

        }

    }


    public class viewpagerAdapter extends FragmentPagerAdapter {
        public viewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            handleIntent(position);
            Fragment frag = new ListFragment();
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Interesting Facts";
            } else
                return "Scientific Facts";
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GlobalClass.factsAdsCounter=0;
    }
}
