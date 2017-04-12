package com.QuranReading.urduquran.quranfacts;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FactsViewPager extends AppCompatActivity {
    ViewPager factsviewpager;
    viewpagerAdapter adapter;
    String[] factsData;
    String[] factsTitle;
    int clickPos;
    TextView headerText;
    int tabPos;
    GlobalClass mGlobal;
    String title;
    TextView numbering;
    //ImageView adImage;
    AdView adview;
    private static final String LOG_TAG = "Ads";
    private final Handler adsHandler = new Handler();
    private int timerValue = 3000, networkRefreshTime = 10000;
    AnalyticSingaltonClass mAnalyticSingaltonClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facts_view_pager);
        mAnalyticSingaltonClass= AnalyticSingaltonClass.getInstance(this);
        mAnalyticSingaltonClass.sendScreenAnalytics("Facts_Detail_Screen");
        headerText = (TextView) findViewById(R.id.tv_header);
        factsviewpager = (ViewPager) findViewById(R.id.factsviewpager);
        numbering=(TextView)findViewById(R.id.number);
        Bundle b=this.getIntent().getExtras();
        factsData = b.getStringArray("data");
        clickPos = b.getInt("listItemPos", 0);
        tabPos = b.getInt("tabPosition", 0);
        adapter = new viewpagerAdapter(getSupportFragmentManager());
        mGlobal = (GlobalClass) getApplicationContext();
        headerText.setTypeface(mGlobal.faceHeading);
        headerText.setSelected(true);
        factsviewpager.setAdapter(adapter);
        factsviewpager.setCurrentItem(clickPos);
        if (tabPos == 0) {
            factsTitle = getResources().getStringArray(R.array.interesting_facts_list);
        } else {
            factsTitle = getResources().getStringArray(R.array.scietific_facts_list);
        }
        title=factsTitle[clickPos];
        int titlepos=clickPos+1;
        numbering.setText(titlepos+"/"+factsData.length);
        headerText.setText(title);
        factsviewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title = factsTitle[position];
                headerText.setText(title);
                int titPos=position+1;
                numbering.setText(titPos+"/"+factsData.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initAds();

    }
    public  void onHomeClickFacts(View v){
        this.finish();
        QuranFactsList.factsActivity.finish();
    }


    public class viewpagerAdapter extends FragmentPagerAdapter {
        public viewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            String factDetailString = factsData[position];
            Fragment frg = new FactDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putString("factDetail", factDetailString);
            frg.setArguments(bundle);
            return frg;
        }

        @Override
        public int getCount() {
            return factsData.length;
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


}