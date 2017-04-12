package com.packageapp.tajweedquran;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.google.android.gms.ads.AdView;

import java.util.Arrays;
import java.util.List;

public class DetailPageActivity extends Fragment {
	ViewPager mPager;
	TextView txtDetailTitle;
	MediaPlayer mpMain;
	GlobalClass mGlobal;
	// Ads
	AdView adview;
	ImageView adImg;
	//GoogleAds googleAds;

	String adId;
	SettingsSharedPref prefs;
	// Layout for calibaration screen
	LinearLayout linearLayout;
	InterstitialAdSingleton mInterstitialAdSingleton;
	public static Activity detailContext;
	// FOr Google Analytics
	String detailScreen = "Details Screen";

	int pos = 0, pageSize;
	int id, listItemPos=-1;
	public List<String> expSubList4;
	public List<String> getExpSubList5;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_detailpage,
				container, false);
		expSubList4 = Arrays.asList(getResources().getStringArray(
				R.array.sublist_4));
		getExpSubList5 = Arrays.asList(getResources().getStringArray(
				R.array.sublist_5));

		detailContext = getActivity();
/*
		linearLayout = (LinearLayout) rootView
				.findViewById(R.id.calibrationlayout);*/
		mPager = (ViewPager) rootView.findViewById(R.id.pager); // View Pager
			mPager.setAdapter(new DetailPager(getActivity()
				.getSupportFragmentManager(), id, getActivity(), pageSize));

			mPager.setCurrentItem(listItemPos);// it should be called before setting adapter
			mPager.setOffscreenPageLimit(1);

		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				int current=mPager.getCurrentItem();
				int last=pageSize-1;
				
				if (id == 0) {
					TajweedActivity.toolbarTitle.setText(expSubList4.get(position));
				} else {
					TajweedActivity.toolbarTitle.setText(getExpSubList5.get(position));
				}
				if(current!=last && current!=0){
				Components_Of_Tajweed.flag = false;
				if (CommonFragment.mp1 != null) {
					CommonFragment.mp1.pause();
					CommonFragment.mp1.stop();
				}

				if (Components_Of_Tajweed.mediaPlayer != null) {
					Components_Of_Tajweed.mediaPlayer.pause();
					Components_Of_Tajweed.mediaPlayer.stop();

				}
				if (position != pageSize) {

					if (Rules_Of_Tajweed.mp != null) {
						Rules_Of_Tajweed.mp.pause();
						Rules_Of_Tajweed.mp.stop();

					}
				}
				}


			}

			@Override
			public void onPageSelected(int position) {


			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		return rootView;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// initializeAds();
		prefs = new SettingsSharedPref(getActivity());
		Bundle arg = this.getArguments();
		id = arg.getInt("detailID"); // intent.getIntExtra("detailID", 0);//
		// id from Main activity of
		// child list items
		listItemPos= arg.getInt("itemPosition");
		if (id == 0) {
			pageSize = 10;
		} else {
			pageSize = 5;
		}
		mGlobal = ((GlobalClass) getActivity().getApplicationContext());// of
		// App
		// analytics
		//AnalyticSingaltonClass.getInstance(getActivity()).sendScreenAnalytics(
				//detailScreen);

	}


	@Override
	public void onDestroy() {
		try {
			releaseMpPlayer();
			CommonFragment.locPos = -1;
			prefs.setMukhrujPositon(-1);
			if (CommonFragment.mukAdapter != null) {
				CommonFragment.mukAdapter.notifyDataSetChanged();
			}

		} catch (Exception e) {

		}
		super.onDestroy();

	}

	private void releaseMpPlayer() {
		if (CommonFragment.mp1 != null) {
			CommonFragment.mp1.pause();
			// CommonFragment.mp1.stop();
			CommonFragment.mp1.release();
			CommonFragment.mp1 = null;
		}
		if (Components_Of_Tajweed.mediaPlayer != null) {
			Components_Of_Tajweed.mediaPlayer.pause();
			// Components_Of_Tajweed.mp.stop();
			Components_Of_Tajweed.mediaPlayer.release();
			Components_Of_Tajweed.mediaPlayer = null;

		}
		if (Rules_Of_Tajweed.mp != null) {
			Rules_Of_Tajweed.mp.pause();
			// Rules_Of_Tajweed.mp.stop();
			Rules_Of_Tajweed.mp.release();
			Rules_Of_Tajweed.mp = null;

		}
	}




}
