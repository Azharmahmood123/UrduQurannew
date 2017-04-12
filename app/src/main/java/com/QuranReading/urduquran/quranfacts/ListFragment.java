package com.QuranReading.urduquran.quranfacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;

/**
 * Created by Aamir Riaz on 12/20/2016.
 */

public class ListFragment extends Fragment{
    ListView factList;
    String[] factsDataList;
    ListAdapter adapter;
    Context context;
    String[] factsDataArray;
    int tabPosition;
    private Bundle mBundle,bundle;
    private long stopClick=0;
    private int adsCounter=0;
    private GlobalClass mGlobal;
    private InterstitialAdSingleton mInterstitialAdSingleton;
   /* private void showIntAds() {
        if(!mGlobal.isPurchase&&GlobalClass.factsAdsCounter!=0)
        {
            GlobalClass.factsAdsCounter=0;
            mInterstitialAdSingleton = InterstitialAdSingleton
                    .getInstance(getActivity());
            mInterstitialAdSingleton.showInterstitial();

        }
        else
        {
            GlobalClass.factsAdsCounter++;
        }

    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        factList = (ListView) view.findViewById(R.id.factList);
        context = getActivity();
        mGlobal= (GlobalClass) context.getApplicationContext();
        adapter = new ListAdapter(context, factsDataList);
        factList.setAdapter(adapter);
        handleIntent(-1);

        factList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(SystemClock.elapsedRealtime()-stopClick<1000)
                {
                    return;
                }
                stopClick=SystemClock.elapsedRealtime();

                handleIntent(i);
                //showIntAds();

            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        factsDataList = (String[]) getArguments().getSerializable("List_Facts");
        tabPosition = getArguments().getInt("tabPosition", 0);
        if (tabPosition == 0) {
            factsDataArray = getActivity().getResources().getStringArray(R.array.interesting_facts_data);
        } else {
            factsDataArray = getActivity().getResources().getStringArray(R.array.scientific_facts_data);
        }
    }
    private void handleIntent(int position) {
        mBundle = new Bundle();//Notification Bundle
        bundle = new Bundle(); //Activity Bundle to Send
        Intent intent = new Intent(context, FactsViewPager.class);
        Intent mExtras = getActivity().getIntent();
        mBundle = mExtras.getExtras();
        if (mBundle != null && mBundle.containsKey("data")&& GlobalClass.isFactsNotification) //Notification Data
        {
            bundle.putInt("listItemPos", mBundle.getInt("listItemPos"));
            bundle.putInt("tabPosition", mBundle.getInt("tabPosition"));
            bundle.putStringArray("data", mBundle.getStringArray("data"));
            intent.putExtras(bundle);
            GlobalClass.isFactsNotification=false;
            startActivity(intent);
        } else { //Normal Flow
            if(position!=-1) {

                bundle.putStringArray("data", factsDataArray);
                bundle.putInt("listItemPos", position);
                bundle.putInt("tabPosition", tabPosition);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    }
}
