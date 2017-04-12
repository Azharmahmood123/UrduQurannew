package com.packageapp.tajweedquran;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.tajweedquran.networkdownloading.DownloadDialogTajweed;
import com.packageapp.tajweedquran.networkdownloading.DownloadingZipUtil;


public class CommonFragment extends Fragment implements OnErrorListener{

	// //////////////Query arguments variables//////////////
	ViewHolder holder;
	int idValue;
	View rootView=null;
	Activity mActivity;
	// layout position 0
	TextView txtIndroduction;
	// layout position 1
	public static Mukhroojul_Haroof_Adapter mukAdapter;
	ListView listMukhroojHaroof;
	// layout position 2
	ListView listStoppingSign;
	GlobalClass mGlobal;
	public static int locPos = -1;
	public static MediaPlayer mp1;
	public static boolean onComplete = false;
	public SettingsSharedPref prefs;
	public static int currentPos = -1;
	private String audioName;
	private long stopClick=0;
	AnalyticSingaltonClass mAnalyticSingaltonClass;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		prefs = new SettingsSharedPref(mActivity);

		Bundle b = this.getArguments();
		idValue = b.getInt("mPosition");
		holder = new ViewHolder();
		prefs= new SettingsSharedPref(getActivity());
		mGlobal = ((GlobalClass) mActivity.getApplicationContext());
		mAnalyticSingaltonClass= AnalyticSingaltonClass.getInstance(mActivity);
		mAnalyticSingaltonClass.sendScreenAnalytics("Tajweed_Detail_Screen");
		if(rootView == null)
		{

			if(idValue == 0)
			{
				rootView = inflater.inflate(R.layout.tajweed_introduction, null);
				txtIndroduction = (TextView) rootView.findViewById(R.id.tajweedIndroduction);

				TajweedActivity.toolbarTitle.setText("Introduction");

			}
			if(idValue == 1)
			{
				rootView = inflater.inflate(R.layout.mukhraj_ul_huroof, null);
				listMukhroojHaroof = (ListView) rootView.findViewById(R.id.listMukhroojHuroof);
				TajweedActivity.toolbarTitle.setText("Mukhraj ul Huroof");
			}

			rootView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) rootView.getTag();
		}
		switch (idValue)
		{
		case 0:
			txtIndroduction.setText(Html.fromHtml(mGlobal.dataList.get(0).toString()));
			txtIndroduction.setTypeface(mGlobal.robotoLight);

			break;
		case 1:
			mukAdapter = new Mukhroojul_Haroof_Adapter(mActivity);
			listMukhroojHaroof.setAdapter(mukAdapter);

			listMukhroojHaroof.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					if(SystemClock.elapsedRealtime()-stopClick<1000){
						return;

					}
					stopClick=SystemClock.elapsedRealtime();
					prefs.setMukhrujPositon(position);
					currentPos = position;
					if(currentPos != locPos)
					{

						if(mp1 == null)
						{
							audioName="makhrij_"+position;
							initAudioFile(audioName);
							//mp1 = MediaPlayer.create(mActivity, getActivity().getResources().getIdentifier("raw/" + "makhrij_" + position, null, getActivity().getPackageName()));
							if(mp1!=null)
							{
								mp1.setOnErrorListener(CommonFragment.this);
								mp1.start();
								onComplete = false;
							}

						}
						else if(mp1 != null)
						{
							if(mp1.isPlaying())
							{
								onComplete = true;
								mp1.release();
								// ///////////////run another audio
								// here///////////////
								audioName="makhrij_"+position;
								initAudioFile(audioName);
								//mp1 = MediaPlayer.create(mActivity, getActivity().getResources().getIdentifier("raw/" + "makhrij_" + position, null, getActivity().getPackageName()));
								mp1.setOnErrorListener(CommonFragment.this);
								mp1.start();
								onComplete = false;
							}
							else
							{
								audioName="makhrij_"+position;
								initAudioFile(audioName);
								//mp1 = MediaPlayer.create(mActivity, getActivity().getResources().getIdentifier("raw/" + "makhrij_" + position, null, getActivity().getPackageName()));
								mp1.setOnErrorListener(CommonFragment.this);
								mp1.start();
								onComplete = false;
							}

						}

					}
					else if(locPos == currentPos)
					{
						if(mp1 != null)
						{
							if(mp1.isPlaying())
							{
								mp1.release();
								mp1=null;
								onComplete = true;
							}
							else if(!mp1.isPlaying())
							{
								audioName="makhrij_"+position;
								initAudioFile(audioName);
								//mp1 = MediaPlayer.create(mActivity, getActivity().getResources().getIdentifier("raw/" + "makhrij_" + position, null, getActivity().getPackageName()));
								if(mp1!=null)
								{
									mp1.setOnErrorListener(CommonFragment.this);
									mp1.start();
									onComplete = false;
								}

							}

						}
						else if(mp1 == null)
						{
							audioName="makhrij_"+position;
							initAudioFile(audioName);
							//mp1 = MediaPlayer.create(mActivity, getActivity().getResources().getIdentifier("raw/" + "makhrij_" + position, null, getActivity().getPackageName()));
							if(mp1!=null)
							{
								mp1.setOnErrorListener(CommonFragment.this);
								mp1.start();
								onComplete = false;
							}

						}

					}
					if(mp1 != null)
					{
						mp1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								onComplete = true;
								mukAdapter.notifyDataSetChanged();
							}
						});
					}
					if(mp1!=null)
					{
						locPos = currentPos;
						mukAdapter.notifyDataSetChanged();
					}

				}
			});
			break;
		case 2:
			// listStoppingSign.setAdapter(new
			// Stopping_Sign_Adapter(mActivity));
			break;
		default:
		}

		return rootView;
	}

	@Override
	public void onDestroyView() {

		if(mp1 != null)
		{
			if(!onComplete)
			{
				onComplete = true;
				locPos = -1;
				mp1.release();
				mp1=null;
			}
		}
		
		TajweedActivity.toolbarTitle.setText("Tajweed Quran");
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		locPos = -1;
	}

	public class ViewHolder {
		// Data base related objects

		public ViewHolder() {

		}

	}
	
	 @Override
	    public void onPause() {
	    	// TODO Auto-generated method stub
		 if(mp1 != null)
			{
			mp1.release();  
			mp1=null;
			onComplete = true;
			
			 prefs.setMukhrujPositon(-1);
			 mukAdapter.notifyDataSetChanged();
			}
		
	     super.onPause();
	    }

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		if (mp1 != null)
		{
			mp1.release();
			mp1 = null;
			onComplete = true;
		}
		Toast.makeText(getActivity(), "Mp Error", Toast.LENGTH_LONG).show();
		return false;
	}
	private void initAudioFile(String audioFile)
	{
		try
		{
			if(!ConstantsTajweedQuran.rootPathTajweed.exists())
			{
				ConstantsTajweedQuran.rootPathTajweed.mkdirs();
			}

			File myFile = new File(ConstantsTajweedQuran.rootPathTajweed.getAbsolutePath(), audioFile+".mp3");

			Uri audioUri = Uri.parse(myFile.getPath());

			if(myFile.exists())
			{
				// boolean chkSize = FileUtils.checkAudioFileSize(mActivity, audioFile, suraPosition, reciter);

				//   if(chkSize)
				mp1 = MediaPlayer.create(mActivity, audioUri);
				mp1.setAudioStreamType(AudioManager.STREAM_MUSIC);
			}
			else
			{
				Intent downloadDialog = new Intent(mActivity, DownloadDialogTajweed.class);
				startActivity(downloadDialog);
			}
		}
		catch (Exception e)
		{
			Log.e("File", e.toString());
		}
	}



}
