package com.packageapp.tajweedquran;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.tajweedquran.networkdownloading.DownloadDialogTajweed;

import java.io.File;

public class Components_Of_Tajweed extends Fragment {
	Activity mActivity;
	View rootView;
	int idValue;
	ViewHolder holder;
	// Layout 1 and 2
	TextView txtBody;
	// Layout for single text and image holders like sukoon,shaddah,silent
	// letters
	TextView txtBodyImage;
	ImageView imgBodyImage;
	 ImageView imgSpeaker;
	RelativeLayout relSingle;
	// layout for leen letters widgets
	int leenCounter = -1;
	int leenPrevious = -1;
	RelativeLayout relLeen1, relLeen2;
	TextView txtLeenHeading1, txtLeenHeading2, txtLeenHeading3;
	TextView txtLeenIntro, txtWaoIntro, txtYaleenIntro;
	ImageView imgLeen1, imgLeen2;
	// layout for harkat letters widgets
	int harkatCounter = -1;
	int harkatPrevious = -1;
	RelativeLayout relHarkat1, relHarkat2, relHarkat3;
	TextView txtHarkatHeading1, txtHarkatHeading2, txtHarkatHeading3, txtHarkatHeading4;
	TextView txtHarkatIntro, txtFatahIntro, txtKasrahIntro, txtDammahIntro;
	ImageView imgHarkat1, imgHarkat2, imgHarkat3;
	// layout for tanween letters widgets
	int tanweenCounter = -1;
	int tanweenPrevious = -1;
	RelativeLayout relTanween1, relTanween2, relTanween3;
	TextView txtTanweenHeading1, txtTanweenHeading2, txtTanweenHeading3, txtTanweenHeading4;
	TextView txtTanweenIntro, txtFathatainIntro, txtKasratainIntro, txtDammatainIntro;
	ImageView imgTanween1, imgTanween2, imgTanween3;
	// layout for madd letters widgets
	int madCounter = -1;
	int previousValue = -1;
	RelativeLayout relMadd1, relMadd2, relMadd3, relMadd4;
	TextView txtMadHeading1, txtMadHeading2, txtMadHeading3, txtMadHeading4, txtMadHeading5;
	TextView txtMaddIntro, txtMaddeMutassilIntro, txtMaddeMunfasilIntro, txtMaddeAarithWaqfiIntro, txtMaddeLazimIntro;
	ImageView imgMad1, imgMad2, imgMad3, imgMad4;

	GlobalClass mGlobal;
	public static MediaPlayer mediaPlayer;
	public static boolean flag = false;
	boolean completionFlag = false;
	private String audioFileName;
	AnalyticSingaltonClass mAnalyticSingaltonClass;


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		Bundle b = this.getArguments();
		idValue = b.getInt("mPosition");
		holder = new ViewHolder();
		mGlobal = ((GlobalClass) mActivity.getApplicationContext());
		mAnalyticSingaltonClass= AnalyticSingaltonClass.getInstance(mActivity);
		mAnalyticSingaltonClass.sendScreenAnalytics("Tajweed_Detail_Screen");
		if(rootView == null)
		{
			if(idValue == 0 || idValue == 1)
			{
				rootView = inflater.inflate(R.layout.component_of_tajweed1_2, null);
				txtBody = (TextView) rootView.findViewById(R.id.txt_tajweed1);// title
																				// of
																				// the
																				// page

			}

			else if(idValue == 2 || idValue == 3 || idValue == 7 || idValue == 8)
			{
				rootView = inflater.inflate(R.layout.component_of_tajweed_single_text_and_image, null);
				txtBodyImage = (TextView) rootView.findViewById(R.id.txt_tajweed_single_1);
				imgBodyImage = (ImageView) rootView.findViewById(R.id.img_tajweed_single_1);
				imgSpeaker = (ImageView) rootView.findViewById(R.id.speakerSilent);

				relSingle = (RelativeLayout) rootView.findViewById(R.id.relSingle);

			}
			else if(idValue == 4)
			{
				// leen letters
				rootView = inflater.inflate(R.layout.component_of_tajweed_leen_letters, null);
				txtLeenIntro = (TextView) rootView.findViewById(R.id.txt_leenIntroduction);
				txtWaoIntro = (TextView) rootView.findViewById(R.id.txt_waoleen_Intro);
				txtYaleenIntro = (TextView) rootView.findViewById(R.id.txt_yaleen_Intro);
				// Leen Headings
				txtLeenHeading1 = (TextView) rootView.findViewById(R.id.txt_leenTitle);
				txtLeenHeading2 = (TextView) rootView.findViewById(R.id.txt_waoleen_Title);
				txtLeenHeading3 = (TextView) rootView.findViewById(R.id.txt_yaleen_Title);
				// Font styles
				txtLeenHeading1.setTypeface(mGlobal.corbalBold);
				txtLeenHeading2.setTypeface(mGlobal.corbalBold);
				txtLeenHeading3.setTypeface(mGlobal.corbalBold);

				relLeen1 = (RelativeLayout) rootView.findViewById(R.id.relWaoLeen);
				relLeen2 = (RelativeLayout) rootView.findViewById(R.id.relYaLeen);
				// leen speaker images
				imgLeen1 = (ImageView) rootView.findViewById(R.id.speakerWaoleen);
				imgLeen2 = (ImageView) rootView.findViewById(R.id.speakeryaleen);

			}
			else if(idValue == 5)
			{
				// Harkat Letters
				rootView = inflater.inflate(R.layout.component_of_tajweed_harkat_letters, null);
				txtHarkatIntro = (TextView) rootView.findViewById(R.id.txt_harkatIntroduction);
				txtFatahIntro = (TextView) rootView.findViewById(R.id.txt_Fatah_Or_Zabr_Title_Intro);
				txtKasrahIntro = (TextView) rootView.findViewById(R.id.txt_kasrah_or_zer_Intro);
				txtDammahIntro = (TextView) rootView.findViewById(R.id.txt_dammah_or_pesh_Intro);
				// Harkat Headings
				txtHarkatHeading1 = (TextView) rootView.findViewById(R.id.txt_harkatTitle);
				txtHarkatHeading2 = (TextView) rootView.findViewById(R.id.txt_Fatah_Or_Zabr_Title);
				txtHarkatHeading3 = (TextView) rootView.findViewById(R.id.txt_kasrah_or_zer_Title);
				txtHarkatHeading4 = (TextView) rootView.findViewById(R.id.txt_dammah_or_pesh_Title);
				// Font Style
				txtHarkatHeading1.setTypeface(mGlobal.corbalBold);
				txtHarkatHeading2.setTypeface(mGlobal.corbalBold);
				txtHarkatHeading3.setTypeface(mGlobal.corbalBold);
				txtHarkatHeading4.setTypeface(mGlobal.corbalBold);

				relHarkat1 = (RelativeLayout) rootView.findViewById(R.id.relFatah);
				relHarkat2 = (RelativeLayout) rootView.findViewById(R.id.relZer);
				relHarkat3 = (RelativeLayout) rootView.findViewById(R.id.relPesh);

				// speaker images
				imgHarkat1 = (ImageView) rootView.findViewById(R.id.speakerFatah_Or_Zabr);
				imgHarkat2 = (ImageView) rootView.findViewById(R.id.speakerkasrah_or_zer);
				imgHarkat3 = (ImageView) rootView.findViewById(R.id.speakerdammah_or_pesh);

			}
			else if(idValue == 6)
			{
				rootView = inflater.inflate(R.layout.component_of_tajweed_tanween_letters, null);
				txtTanweenIntro = (TextView) rootView.findViewById(R.id.txt_tanweenIntroduction);
				txtFathatainIntro = (TextView) rootView.findViewById(R.id.txt_Fathatain_Intro);
				txtKasratainIntro = (TextView) rootView.findViewById(R.id.txt_kasratain_Intro);
				txtDammatainIntro = (TextView) rootView.findViewById(R.id.txt_dammatain_Intro);
				// Tanween headings
				txtTanweenHeading1 = (TextView) rootView.findViewById(R.id.txt_tanweenTitle);
				txtTanweenHeading2 = (TextView) rootView.findViewById(R.id.txt_Fathatain_Title);
				txtTanweenHeading3 = (TextView) rootView.findViewById(R.id.txt_kasratain_Title);
				txtTanweenHeading4 = (TextView) rootView.findViewById(R.id.txt_dammatain_Title);
				// setting font style
				txtTanweenHeading1.setTypeface(mGlobal.corbalBold);
				txtTanweenHeading2.setTypeface(mGlobal.corbalBold);
				txtTanweenHeading3.setTypeface(mGlobal.corbalBold);
				txtTanweenHeading4.setTypeface(mGlobal.corbalBold);

				relTanween1 = (RelativeLayout) rootView.findViewById(R.id.relFatah);
				relTanween2 = (RelativeLayout) rootView.findViewById(R.id.relKasrahtain);
				relTanween3 = (RelativeLayout) rootView.findViewById(R.id.relDamatain);
				// speaker icons
				imgTanween1 = (ImageView) rootView.findViewById(R.id.speakerFathatain);
				imgTanween2 = (ImageView) rootView.findViewById(R.id.speakerkasratain);
				imgTanween3 = (ImageView) rootView.findViewById(R.id.speakerdammatain);

			}
			else if(idValue == 9)
			{

				rootView = inflater.inflate(R.layout.components_of_tajweed_madd_letters, null);
				txtMaddIntro = (TextView) rootView.findViewById(R.id.txt_maddIntroduction);
				txtMaddeMutassilIntro = (TextView) rootView.findViewById(R.id.txt_madd_e_mutassil_Intro);
				txtMaddeMunfasilIntro = (TextView) rootView.findViewById(R.id.txt_madd_e_munfasil_Intro);
				txtMaddeAarithWaqfiIntro = (TextView) rootView.findViewById(R.id.txt_madd_e_aarith_waqfi_Intro);
				txtMaddeLazimIntro = (TextView) rootView.findViewById(R.id.txt_madd_e_lazim_Intro);
				// Madd Headings
				txtMadHeading1 = (TextView) rootView.findViewById(R.id.txt_maddTitle);
				txtMadHeading2 = (TextView) rootView.findViewById(R.id.txt_madd_e_mutassil_Title);
				txtMadHeading3 = (TextView) rootView.findViewById(R.id.txt_madd_e_munfasil_Title);
				txtMadHeading4 = (TextView) rootView.findViewById(R.id.txt_madd_e_aarith_waqfi_Title);
				txtMadHeading5 = (TextView) rootView.findViewById(R.id.txt_madd_e_lazim_Title);
				// Font styles
				txtMadHeading1.setTypeface(mGlobal.corbalBold);
				txtMadHeading2.setTypeface(mGlobal.corbalBold);
				txtMadHeading3.setTypeface(mGlobal.corbalBold);
				txtMadHeading4.setTypeface(mGlobal.corbalBold);
				txtMadHeading5.setTypeface(mGlobal.corbalBold);

				relMadd1 = (RelativeLayout) rootView.findViewById(R.id.relMutassil);
				relMadd2 = (RelativeLayout) rootView.findViewById(R.id.relMunfasil);
				relMadd3 = (RelativeLayout) rootView.findViewById(R.id.relWaqfi);
				relMadd4 = (RelativeLayout) rootView.findViewById(R.id.relLazim);
				imgMad1 = (ImageView) rootView.findViewById(R.id.speakermadd_e_mutassil);
				imgMad2 = (ImageView) rootView.findViewById(R.id.speakermadd_e_munfasil);
				imgMad3 = (ImageView) rootView.findViewById(R.id.speakermadd_e_aarith_waqfi);
				imgMad4 = (ImageView) rootView.findViewById(R.id.speakermadd_e_lazim);

			}
			rootView.setTag(holder);

		}
		else
		{
			holder = (ViewHolder) rootView.getTag();
		}
		completionFlag = false;

		switch (idValue)
		{
		case 0:
			txtBody.setText(Html.fromHtml(mGlobal.dataList.get(3).toString()));
			txtBody.setTypeface(mGlobal.robotoRegular);

			break;
		case 1:
			txtBody.setText(Html.fromHtml(mGlobal.dataList.get(4).toString()));
			txtBody.setTypeface(mGlobal.robotoRegular);

			break;
		case 2:
			// silent letters
			txtBodyImage.setText(Html.fromHtml(mGlobal.dataList.get(5).toString()));
			txtBodyImage.setTypeface(mGlobal.robotoRegular);
			imgBodyImage.getLayoutParams().height = (int) getResources().getDimension(R.dimen.size_fourty_five);
			imgBodyImage.setImageResource(R.drawable.img_arabic_silent_letters);
			//imgSpeaker.setImageResource(R.drawable.speaker);

			break;
		case 3:
			// Qalqalah letters
			txtBodyImage.setText(Html.fromHtml(mGlobal.dataList.get(6).toString()));
			txtBodyImage.setTypeface(mGlobal.robotoRegular);
			imgBodyImage.getLayoutParams().height = (int) getResources().getDimension(R.dimen.size_fourty);
			imgBodyImage.setImageResource(R.drawable.img_arabic_qalqalah_letters);
			//imgSpeaker.setImageResource(R.drawable.speaker);


			break;
		case 4:
			// leen letters
			txtLeenIntro.setText(Html.fromHtml(mGlobal.dataList.get(7).toString()));
			txtWaoIntro.setText(Html.fromHtml(mGlobal.dataList.get(8).toString()));
			txtYaleenIntro.setText(Html.fromHtml(mGlobal.dataList.get(9).toString()));
			// font style
			txtLeenIntro.setTypeface(mGlobal.robotoRegular);
			txtWaoIntro.setTypeface(mGlobal.robotoRegular);
			txtYaleenIntro.setTypeface(mGlobal.robotoRegular);

			relLeen1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if(leenCounter == 1)
					{
						leenCounter = 1;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();

							switch (leenCounter)
							{
							case 1:
								imgLeen1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgLeen2.setImageResource(R.drawable.speaker);
								break;

							}
							flag = true;
							return;
						}
					}
					leenCounter = 1;
					if(leenPrevious != -1)
					{
						switch (leenPrevious)
						{
						case 1:
							imgLeen1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgLeen2.setImageResource(R.drawable.speaker);
							break;

						}
					}
					flag = false;
					leenPrevious = leenCounter;
					stopPlaying();
					audioFileName="res_wow_leen";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_wow_leen);

					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgLeen1.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgLeen1.setImageResource(R.drawable.speaker_hover);
					}
				}
			});
			relLeen2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(leenCounter == 2)
					{
						leenCounter = 2;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();

							switch (leenCounter)
							{
							case 1:
								imgLeen1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgLeen2.setImageResource(R.drawable.speaker);
								break;

							}
							flag = true;
							return;
						}
					}
					flag = false;
					leenCounter = 2;
					if(leenPrevious != -1)
					{
						switch (leenPrevious)
						{
						case 1:
							imgLeen1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgLeen2.setImageResource(R.drawable.speaker);
							break;

						}
					}
					leenPrevious = leenCounter;
					stopPlaying();
					audioFileName="res_yaa_leen";
					initAudioFile(audioFileName);

					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_yaa_leen);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgLeen2.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgLeen2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			break;
		case 5:
			txtHarkatIntro.setText(Html.fromHtml(mGlobal.dataList.get(10).toString()));
			txtFatahIntro.setText(Html.fromHtml(mGlobal.dataList.get(11).toString()));
			txtKasrahIntro.setText(Html.fromHtml(mGlobal.dataList.get(12).toString()));
			txtDammahIntro.setText(Html.fromHtml(mGlobal.dataList.get(13).toString()));
			// font style
			txtHarkatIntro.setTypeface(mGlobal.robotoRegular);
			txtFatahIntro.setTypeface(mGlobal.robotoRegular);
			txtKasrahIntro.setTypeface(mGlobal.robotoRegular);
			txtDammahIntro.setTypeface(mGlobal.robotoRegular);
			relHarkat1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(harkatCounter == 1)
					{
						harkatCounter = 1;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();

							switch (harkatPrevious)
							{
							case 1:
								imgHarkat1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgHarkat2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgHarkat3.setImageResource(R.drawable.speaker);
								break;

							}
							flag = true;
							return;
						}
						// imgTanween1.setImageResource(R.drawable.speaker);

					}

					harkatCounter = 1;
					if(harkatPrevious != -1)
					{
						switch (harkatPrevious)
						{
						case 1:
							imgHarkat1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgHarkat2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgHarkat3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					harkatPrevious = harkatCounter;
					flag = false;
					stopPlaying();
					audioFileName="res_harkat_fatah";
					initAudioFile(audioFileName);

					/*mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_harkat_fatah);
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);*/
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgHarkat1.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgHarkat1.setImageResource(R.drawable.speaker_hover);
					}
				}
			});
			relHarkat2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if(harkatCounter == 2)
					{
						harkatCounter = 2;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();
							flag = true;
							switch (harkatPrevious)
							{
							case 1:
								imgHarkat1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgHarkat2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgHarkat3.setImageResource(R.drawable.speaker);
								break;

							}
							return;
						}
						// imgTanween1.setImageResource(R.drawable.speaker);
						// flag = true;

					}
					harkatCounter = 2;
					flag = false;
					if(harkatPrevious != -1)
					{
						switch (harkatPrevious)
						{
						case 1:
							imgHarkat1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgHarkat2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgHarkat3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					harkatPrevious = harkatCounter;
					stopPlaying();
					audioFileName="res_harkat_kasrah";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_harkat_kasrah);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgHarkat2.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgHarkat2.setImageResource(R.drawable.speaker_hover);
					}
				}
			});
			relHarkat3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(harkatCounter == 3)
					{
						harkatCounter = 3;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();
							flag = true;
							switch (harkatPrevious)
							{
							case 1:
								imgHarkat1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgHarkat2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgHarkat3.setImageResource(R.drawable.speaker);
								break;

							}
							return;
						}
						// imgTanween1.setImageResource(R.drawable.speaker);
						// flag = true;

					}
					harkatCounter = 3;
					flag = false;
					if(harkatPrevious != -1)
					{
						switch (harkatPrevious)
						{
						case 1:
							imgHarkat1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgHarkat2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgHarkat3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					harkatPrevious = harkatCounter;
					stopPlaying();
					audioFileName="res_harkat_dammah";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_harkat_dammah);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgHarkat3.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();

						imgHarkat3.setImageResource(R.drawable.speaker_hover);
					}
				}
			});

			break;
		case 6:
			txtTanweenIntro.setText(Html.fromHtml(mGlobal.dataList.get(14).toString()));
			txtFathatainIntro.setText(Html.fromHtml(mGlobal.dataList.get(15).toString()));
			txtKasratainIntro.setText(Html.fromHtml(mGlobal.dataList.get(16).toString()));
			txtDammatainIntro.setText(Html.fromHtml(mGlobal.dataList.get(17).toString()));
			// font style
			txtTanweenIntro.setTypeface(mGlobal.robotoRegular);
			txtFathatainIntro.setTypeface(mGlobal.robotoRegular);
			txtKasratainIntro.setTypeface(mGlobal.robotoRegular);
			txtDammatainIntro.setTypeface(mGlobal.robotoRegular);

			relTanween1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if(tanweenCounter == 1)
					{
						tanweenCounter = 1;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();
							if(tanweenPrevious != -1)
							{
								switch (tanweenPrevious)
								{
								case 1:
									imgTanween1.setImageResource(R.drawable.speaker);
									break;
								case 2:
									imgTanween2.setImageResource(R.drawable.speaker);
									break;
								case 3:
									imgTanween3.setImageResource(R.drawable.speaker);
									break;

								}
							}
							// imgTanween1.setImageResource(R.drawable.speaker);
							flag = true;
							return;
						}
					}
					tanweenCounter = 1;

					if(tanweenPrevious != -1)
					{
						switch (tanweenPrevious)
						{
						case 1:
							imgTanween1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgTanween2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgTanween3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					flag = false;
					tanweenPrevious = tanweenCounter;
					stopPlaying();
					audioFileName="rest_tanween1_fata";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.rest_tanween1_fata);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

						if(mediaPlayer != null) {
							mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
								@Override
								public void onCompletion(MediaPlayer mp) {
									imgTanween1.setImageResource(R.drawable.speaker);

								}
							});

							mediaPlayer.start();
							imgTanween1.setImageResource(R.drawable.speaker_hover);
						}

				}
			});
			relTanween2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if(tanweenCounter == 2)
					{
						tanweenCounter = 2;

						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							flag = true;
							mediaPlayer.stop();
							if(tanweenPrevious != -1)
							{
								switch (tanweenPrevious)
								{
								case 1:
									imgTanween1.setImageResource(R.drawable.speaker);
									break;
								case 2:
									imgTanween2.setImageResource(R.drawable.speaker);
									break;
								case 3:
									imgTanween3.setImageResource(R.drawable.speaker);
									break;

								}
							}
							return;
						}
					}
					tanweenCounter = 2;

					if(tanweenPrevious != -1)
					{
						switch (tanweenPrevious)
						{
						case 1:
							imgTanween1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgTanween2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgTanween3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					flag = false;
					tanweenPrevious = tanweenCounter;
					stopPlaying();
					audioFileName="res_tanween2_kasra";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_tanween2_kasra);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgTanween2.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgTanween2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relTanween3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(tanweenCounter == 3)
					{
						tanweenCounter = 3;

						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							flag = true;
							mediaPlayer.stop();
							if(tanweenPrevious != -1)
							{
								switch (tanweenPrevious)
								{
								case 1:
									imgTanween1.setImageResource(R.drawable.speaker);
									break;
								case 2:
									imgTanween2.setImageResource(R.drawable.speaker);
									break;
								case 3:
									imgTanween3.setImageResource(R.drawable.speaker);
									break;

								}
							}
							return;
						}
					}

					tanweenCounter = 3;

					if(tanweenPrevious != -1)
					{
						switch (tanweenPrevious)
						{
						case 1:
							imgTanween1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgTanween2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgTanween3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					flag = false;
					tanweenPrevious = tanweenCounter;

					stopPlaying();

					if(imgTanween3 != null)
					{
						imgTanween2.setImageResource(R.drawable.speaker);
					}
					audioFileName="res_tanween3_dam";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_tanween3_dam);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgTanween3.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgTanween3.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			break;

		case 7:
			// sukoon
			txtBodyImage.setText(Html.fromHtml(mGlobal.dataList.get(18).toString()));
			txtBodyImage.setTypeface(mGlobal.robotoRegular);
			// imgBodyImage.setImageResource(R.drawable.img_3);

			imgBodyImage.getLayoutParams().height = (int) getResources().getDimension(R.dimen.size_fourty_five);
			imgBodyImage.setImageResource(R.drawable.img_sukoon);

			break;
		case 8:
			// shaddah
			txtBodyImage.setText(Html.fromHtml(mGlobal.dataList.get(19).toString()));
			txtBodyImage.setTypeface(mGlobal.robotoRegular);
			// imgBodyImage.setImageResource(R.drawable.img_4);

			imgBodyImage.getLayoutParams().height = (int) getResources().getDimension(R.dimen.size_fifty);
			imgBodyImage.setImageResource(R.drawable.img_shaddah);

			break;
		case 9:
			txtMaddIntro.setText(Html.fromHtml(mGlobal.dataList.get(20).toString()));
			txtMaddeMutassilIntro.setText(Html.fromHtml(mGlobal.dataList.get(21).toString()));
			txtMaddeMunfasilIntro.setText(Html.fromHtml(mGlobal.dataList.get(22).toString()));
			txtMaddeAarithWaqfiIntro.setText(Html.fromHtml(mGlobal.dataList.get(23).toString()));
			txtMaddeLazimIntro.setText(Html.fromHtml(mGlobal.dataList.get(24).toString()));
			// font style
			txtMaddIntro.setTypeface(mGlobal.robotoRegular);
			txtMaddeMutassilIntro.setTypeface(mGlobal.robotoRegular);
			txtMaddeMunfasilIntro.setTypeface(mGlobal.robotoRegular);
			txtMaddeAarithWaqfiIntro.setTypeface(mGlobal.robotoRegular);
			txtMaddeLazimIntro.setTypeface(mGlobal.robotoRegular);

			relMadd1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(madCounter == 1)
					{
						madCounter = 1;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();

							switch (madCounter)
							{
							case 1:
								imgMad1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgMad2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgMad3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgMad4.setImageResource(R.drawable.speaker);
								break;
							}
							flag = true;
							return;
						}
					}
					flag = false;
					madCounter = 1;
					if(previousValue != -1)
					{
						switch (previousValue)
						{
						case 1:
							imgMad1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgMad2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgMad3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgMad4.setImageResource(R.drawable.speaker);
							break;
						}
					}
					previousValue = madCounter;
					stopPlaying();
					audioFileName="res_madd1";
					initAudioFile(audioFileName);

					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_madd1);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgMad1.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgMad1.setImageResource(R.drawable.speaker_hover);
					}

				}

			});
			relMadd2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(madCounter == 2)
					{
						madCounter = 2;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();

							switch (madCounter)
							{
							case 1:
								imgMad1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgMad2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgMad3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgMad4.setImageResource(R.drawable.speaker);
								break;
							}
							flag = true;
							return;
						}
					}
					flag = false;
					madCounter = 2;
					if(previousValue != -1)
					{
						switch (previousValue)
						{
						case 1:
							imgMad1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgMad2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgMad3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgMad4.setImageResource(R.drawable.speaker);
							break;
						}
					}
					previousValue = madCounter;

					stopPlaying();
					audioFileName="res_madd2";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_madd2);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgMad2.setImageResource(R.drawable.speaker);
							}
						});

						mediaPlayer.start();
						imgMad2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relMadd3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(madCounter == 3)
					{
						madCounter = 3;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();

							switch (madCounter)
							{
							case 1:
								imgMad1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgMad2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgMad3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgMad4.setImageResource(R.drawable.speaker);
								break;
							}
							flag = true;
							return;
						}
					}
					flag = false;
					madCounter = 3;
					if(previousValue != -1)
					{
						switch (previousValue)
						{
						case 1:
							imgMad1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgMad2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgMad3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgMad4.setImageResource(R.drawable.speaker);
							break;
						}
					}
					previousValue = madCounter;
					stopPlaying();
					audioFileName="res_madd3";
					initAudioFile(audioFileName);
					//mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_madd3);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgMad3.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgMad3.setImageResource(R.drawable.speaker_hover);
					}
				}
			});
			relMadd4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(madCounter == 4)
					{
						madCounter = 4;
						if(mediaPlayer != null && mediaPlayer.isPlaying() && !flag)
						{
							mediaPlayer.stop();

							switch (madCounter)
							{
							case 1:
								imgMad1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgMad2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgMad3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgMad4.setImageResource(R.drawable.speaker);
								break;
							}
							flag = true;
							return;
						}
					}
					flag = false;
					madCounter = 4;
					if(previousValue != -1)
					{
						switch (previousValue)
						{
						case 1:
							imgMad1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgMad2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgMad3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgMad4.setImageResource(R.drawable.speaker);
							break;
						}
					}
					previousValue = madCounter;

					stopPlaying();
					audioFileName="res_madd4";
					initAudioFile(audioFileName);
				//	mediaPlayer = MediaPlayer.create(mActivity, R.raw.res_madd4);
					//mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if(mediaPlayer != null) {
						mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {

								imgMad4.setImageResource(R.drawable.speaker);

							}
						});

						mediaPlayer.start();
						imgMad4.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			break;
		default:
		}

		if(relSingle != null)
		{
			relSingle.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (mediaPlayer != null && mediaPlayer.isPlaying()) {
						rootView.post(new Runnable() {
							@Override
							public void run() {
								imgSpeaker.setImageResource(R.drawable.speaker_hover);
							}
						});

					} else {
						rootView.post(new Runnable() {
							@Override
							public void run() {
								imgSpeaker.setImageResource(R.drawable.speaker);
							}
						});

					}

					if (idValue == 2) {
						if (flag) {
							stopPlaying();
							rootView.post(new Runnable() {
								@Override
								public void run() {
									imgSpeaker.setImageResource(R.drawable.speaker);
								}
							});

							flag = false;
						} else if (!flag) {
							audioFileName = "res_silent_letters";
							initAudioFile(audioFileName);
							if (mediaPlayer != null) {
								mediaPlayer.start();
								rootView.post(new Runnable() {
									@Override
									public void run() {
										imgSpeaker.setImageResource(R.drawable.speaker_hover);
									}
								});

								flag = true;
							}
						}

					} else if (idValue == 3) {
						if (flag) {
							stopPlaying();
							rootView.post(new Runnable() {
								@Override
								public void run() {
									imgSpeaker.setImageResource(R.drawable.speaker);
								}
							});


							flag = false;
						} else if (!flag) {
							audioFileName = "res_qalqalah_letters";
							initAudioFile(audioFileName);
							if (mediaPlayer != null) {
								mediaPlayer.start();
								rootView.post(new Runnable() {
									@Override
									public void run() {
										imgSpeaker.setImageResource(R.drawable.speaker_hover);
									}
								});


								flag = true;
							}
						}

						} else if (idValue == 7) {
							if (flag) {
								stopPlaying();
								rootView.post(new Runnable() {
									@Override
									public void run() {
										imgSpeaker.setImageResource(R.drawable.speaker);
									}
								});

								flag = false;
							} else if (!flag) {
								audioFileName = "res_sukoon";
								initAudioFile(audioFileName);
								if (mediaPlayer != null) {
									mediaPlayer.start();
									rootView.post(new Runnable() {
										@Override
										public void run() {
											imgSpeaker.setImageResource(R.drawable.speaker_hover);
										}
									});

									flag = true;

								}
							}

						} else if (idValue == 8) {
						if (flag) {
							stopPlaying();
							rootView.post(new Runnable() {
								@Override
								public void run() {
									imgSpeaker.setImageResource(R.drawable.speaker);
								}
							});

							flag = false;
						} else if (!flag) {
							audioFileName = "res_shaddah";
							initAudioFile(audioFileName);
							if (mediaPlayer != null) {
								mediaPlayer.start();
								rootView.post(new Runnable() {
									@Override
									public void run() {
										imgSpeaker.setImageResource(R.drawable.speaker_hover);
									}
								});
								flag = true;

							}
						}
					}

									// media player for all sing layout arabic
								if (mediaPlayer != null) {
									mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
										@Override
										public void onCompletion(MediaPlayer mp) {
											completionFlag = true;
											rootView.post(new Runnable() {
												@Override
												public void run() {
													imgSpeaker.setImageResource(R.drawable.speaker);
												}
											});

											flag = false;

										}
									});
									mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
										@Override
										public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

											rootView.post(new Runnable() {
												@Override
												public void run() {
													imgSpeaker.setImageResource(R.drawable.speaker);
												}
											});
											return false;
										}
									});
								}
					}});
					}

		return rootView;

	}

	private void stopPlaying() {
		

		if(mediaPlayer != null)
		{
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	public class ViewHolder {
		// Data base related objects

		public ViewHolder() {
			// this.idValue = anyId;

		}

	}

	@Override
	public void onResume() {
		super.onResume();
		flag = false;
	}

	@Override
	public void onPause() {
		super.onPause();
		
		 if(idValue == 2 || idValue == 3 || idValue == 7 || idValue == 8)
		{
			//ImageView imgSpeakertemp=(ImageView) rootView.findViewWithTag("speaker");
			imgSpeaker.setImageResource(R.drawable.speaker);

		}
		else if(idValue == 4)
		{
			imgLeen1.setImageResource(R.drawable.speaker);
			imgLeen2.setImageResource(R.drawable.speaker);
		}
		else if(idValue == 5)
		{
			imgHarkat1.setImageResource(R.drawable.speaker);
			imgHarkat2.setImageResource(R.drawable.speaker);
			imgHarkat3.setImageResource(R.drawable.speaker);
		}
		else if(idValue == 6)
		{
			imgTanween1.setImageResource(R.drawable.speaker);
			imgTanween2.setImageResource(R.drawable.speaker);
			imgTanween3.setImageResource(R.drawable.speaker);
		}
		else if(idValue == 9)
		{
			imgMad1.setImageResource(R.drawable.speaker);
			imgMad2.setImageResource(R.drawable.speaker);
			imgMad3.setImageResource(R.drawable.speaker);
			imgMad4.setImageResource(R.drawable.speaker);
		}
		
		
		
		stopPlaying();
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
				mediaPlayer = MediaPlayer.create(mActivity, audioUri);
			    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
