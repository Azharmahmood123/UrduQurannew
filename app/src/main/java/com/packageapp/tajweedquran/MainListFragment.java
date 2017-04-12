package com.packageapp.tajweedquran;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;

public class MainListFragment extends Fragment implements OnClickListener {

	LinearLayout btnIntro, btnMukhraj, btnComponents, btnRules;
	TextView heading1, heading2, heading3, heading4;

	int anyId;
	GlobalClass mGlobal;
	InterstitialAdSingleton mInterstitialAdSingleton;
	private long stopClick=0;
	private int adsCounter=0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.tajweed_main_list, container,
				false);

		mGlobal = ((GlobalClass) getActivity().getApplicationContext());

		btnIntro = (LinearLayout) rootView.findViewById(R.id.btnIntroduction);
		btnMukhraj = (LinearLayout) rootView.findViewById(R.id.btnmukhraj);
		btnComponents = (LinearLayout) rootView
				.findViewById(R.id.btnComponents);
		btnRules = (LinearLayout) rootView.findViewById(R.id.btnRules);
		heading1= (TextView) rootView.findViewById(R.id.textHeading1);
		heading2= (TextView) rootView.findViewById(R.id.textHeading2);
		heading3= (TextView) rootView.findViewById(R.id.textHeading3);
		heading4= (TextView) rootView.findViewById(R.id.textHeading4);


		heading1.setTypeface(mGlobal.robotoLight);
		heading2.setTypeface(mGlobal.robotoLight);
		heading3.setTypeface(mGlobal.robotoLight);
		heading4.setTypeface(mGlobal.robotoLight);

		btnIntro.setOnClickListener(this);
		btnMukhraj.setOnClickListener(this);
		btnComponents.setOnClickListener(this);
		btnRules.setOnClickListener(this);

		return rootView;
	}

	public void onButtonClick(View v) {
		if(SystemClock.elapsedRealtime()-stopClick<1000)
		{
			return;
		}
		stopClick=SystemClock.elapsedRealtime();


		int tag = Integer.parseInt(v.getTag().toString());
	//	showIntAds();

		switch (tag) {
			case 0:
				anyId = 0;
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction ftx = fragmentManager.beginTransaction();
				ftx.replace(R.id.mainFrame, newFragment());
				ftx.addToBackStack(null);
				ftx.commit();
				break;

			case 1:
				anyId = 1;
				FragmentManager fragmentManager2 = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction ftx2 = fragmentManager2.beginTransaction();
				ftx2.replace(R.id.mainFrame, newFragment());
				ftx2.addToBackStack(null);
				ftx2.commit();
				break;

			case 2:
				anyId = 0;
				FragmentManager fragmentManager3 = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction ftx3 = fragmentManager3.beginTransaction();
				ftx3.replace(R.id.mainFrame, subFragment());
				ftx3.addToBackStack(null);
				ftx3.commit();
				//* intent.putExtra("detailID", anyId); startActivity(intent);


				break;

			case 3:
				anyId = 1;
				FragmentManager fragmentManager4 = getActivity()
						.getSupportFragmentManager();
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
		SubListFragment fragment = new SubListFragment();
		fragment.setArguments(arg);
		return fragment;
	}

	/*private void showIntAds() {
		if(!mGlobal.isPurchase&&GlobalClass.tajweedAdsCounter!=0)
		{
			GlobalClass.tajweedAdsCounter=0;
			mInterstitialAdSingleton = InterstitialAdSingleton
					.getInstance(getActivity());
			mInterstitialAdSingleton.showInterstitial();

		}
		else{
			GlobalClass.tajweedAdsCounter++;
		}


	}*/

	@Override
	public void onClick(View v) {

		onButtonClick(v);

	}
}
