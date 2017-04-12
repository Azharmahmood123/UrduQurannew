package com.packageapp.tajweedquran;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.File;

import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.tajweedquran.networkdownloading.DownloadDialogTajweed;
import com.packageapp.tajweedquran.networkdownloading.DownloadingZipUtil;

public class Rules_Of_Tajweed extends Fragment {
	public static MediaPlayer mp;
	Activity mActivity;
	View rootView;
	int idValue;
	ViewHolder holder;
	boolean rulesFlag = false;
	// layout for tajweed rule 1 nun sakin
	int nunCounter = -1;
	int nunPrevious = -1;
	RelativeLayout relNun1, relNun2, relNun3, relNun4, relNun5;
	TextView txtNunHeading1, txtNunHeading2, txtNunHeading3, txtNunHeading4;
	TextView txtIzhar1, txtIdgham1, txtIdgham2, txtIqlaab1, txtIkhfa1;
	ImageView imgNun1, imgNun2, imgNun3, imgNun4, imgNun5;
	// layout for tajweed rule 2 Meem sakin widgets
	int meemCounter = -1;
	int meemPrevious = -1;
	RelativeLayout relMeem1, relMeem2, relMeem3;
	TextView txtMeemHeading1, txtMeemHeading2, txtMeemHeading3;
	TextView txtIdgham, txtIkhfa, txtIzhar;
	ImageView imgMeem1, imgMeem2, imgMeem3;
	// layout for tajweed rule 3 Raa letter
	int raaCounter = -1;
	int raaPrevious = -1;
	RelativeLayout relRaa1, relRaa2, relRaa3, relRaa4, relRaa5, relRaa6, relRaa7, relRaa8, relRaa9;
	TextView raaHeading1, raaHeading2;
	TextView txtTafkheemIntro1, txtTafkheemIntro2, txtTafkheemIntro3, txtTafkheemIntro4, txtTafkheemIntro5;
	TextView txtTarqeeqIntro1, txtTarqeeqIntro2, txtTarqeeqIntro3, txtTarqeeqIntro4;
	ImageView imgRaa1, imgRaa2, imgRaa3, imgRaa4, imgRaa5, imgRaa6, imgRaa7, imgRaa8, imgRaa9;
	String anyText, anyText1;
	String[] raaText, raaText1;
	// layout for tajweed rule4
	int laamCounter = -1;
	int laamPrevious = -1;
	RelativeLayout relLam1, relLam2, relLam3, relLam4, relLam5;
	TextView lamHeading1, lamHeading2;
	TextView txtLam1, txtLam2, txtLam3, txtLam4, txtLam5;
	ImageView imgLam1, imgLam2, imgLam3, imgLam4, imgLam5;
	// layout for tajweed rule 5
	int hamzaCounter = -1;
	int hamzaPrevious = -1;
	RelativeLayout relHamza1, relHamza2, relHamza3, relHamza4;
	TextView hamzaHeading1, hamzaHeading2;
	TextView txtWaslIntro, txtQataIntro1, txtQataIntro2, txtQataIntro3;
	ImageView imgHamza1, imgHamza2, imgHamza3, imgHamza4;
	String audioName;

	GlobalClass mGlobal;
	public static boolean isRuleofTajweed=false;
	AnalyticSingaltonClass mAnalyticSingaltonClass;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = activity;
		mGlobal = ((GlobalClass) mActivity.getApplicationContext());

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		Bundle b = this.getArguments();
		idValue = b.getInt("mPosition");
		holder = new ViewHolder();
		mAnalyticSingaltonClass= AnalyticSingaltonClass.getInstance(mActivity);
		mAnalyticSingaltonClass.sendScreenAnalytics("Tajweed_Detail_Screen");
		if (rootView == null)
		{
			if (idValue == 0)
			{
				rootView = inflater.inflate(R.layout.tajweed_rule1, null);
				// txtIzhar,txtIdgham,txtIqlaab,txtIkhfa;
				txtIzhar1 = (TextView) rootView.findViewById(R.id.txtNunSakinIntro);
				txtIdgham1 = (TextView) rootView.findViewById(R.id.txtNunIdghamInro1);
				txtIdgham2 = (TextView) rootView.findViewById(R.id.txtNunIdghamInro2);
				txtIqlaab1 = (TextView) rootView.findViewById(R.id.txtNunIqlabIntro);
				txtIkhfa1 = (TextView) rootView.findViewById(R.id.txtNunIkhfaIntro);
				// Nun Headings
				txtNunHeading1 = (TextView) rootView.findViewById(R.id.txtNunSakinHeading);
				txtNunHeading2 = (TextView) rootView.findViewById(R.id.txtNunIdghamHeading);
				txtNunHeading3 = (TextView) rootView.findViewById(R.id.txtNunIqlaabHeading);
				txtNunHeading4 = (TextView) rootView.findViewById(R.id.txtNunIkhfaHeading);
				// Font Styles
				txtNunHeading1.setTypeface(mGlobal.corbalBold);
				txtNunHeading2.setTypeface(mGlobal.corbalBold);
				txtNunHeading3.setTypeface(mGlobal.corbalBold);
				txtNunHeading4.setTypeface(mGlobal.corbalBold);

				relNun1 = (RelativeLayout) rootView.findViewById(R.id.relNun1);
				relNun2 = (RelativeLayout) rootView.findViewById(R.id.relNun2);
				relNun3 = (RelativeLayout) rootView.findViewById(R.id.relNun3);
				relNun4 = (RelativeLayout) rootView.findViewById(R.id.relNun4);
				relNun5 = (RelativeLayout) rootView.findViewById(R.id.relNun5);
				// speaker images
				imgNun1 = (ImageView) rootView.findViewById(R.id.speakerNun1);
				imgNun2 = (ImageView) rootView.findViewById(R.id.speakerNun2);
				imgNun3 = (ImageView) rootView.findViewById(R.id.speakerNun3);
				imgNun4 = (ImageView) rootView.findViewById(R.id.speakerNun4);
				imgNun5 = (ImageView) rootView.findViewById(R.id.speakerNun5);

			}
			else if (idValue == 1)
			{
				// Meem Sakin
				// txtIdgham,txtIkhfa,txtIzhar;
				rootView = inflater.inflate(R.layout.tajweed_rule2, null);
				txtIdgham = (TextView) rootView.findViewById(R.id.txtIdghamIntro);
				txtIkhfa = (TextView) rootView.findViewById(R.id.txtIkhfaIntro);
				txtIzhar = (TextView) rootView.findViewById(R.id.txtIzharIntro);
				// Meem Headings
				txtMeemHeading1 = (TextView) rootView.findViewById(R.id.txtIdghamHeading);
				txtMeemHeading2 = (TextView) rootView.findViewById(R.id.txtIkhfaHeading);
				txtMeemHeading3 = (TextView) rootView.findViewById(R.id.txtIzharHeading);
				// Font Style
				txtMeemHeading1.setTypeface(mGlobal.corbalBold);
				txtMeemHeading2.setTypeface(mGlobal.corbalBold);
				txtMeemHeading3.setTypeface(mGlobal.corbalBold);

				relMeem1 = (RelativeLayout) rootView.findViewById(R.id.rel1);
				relMeem2 = (RelativeLayout) rootView.findViewById(R.id.rel2);
				relMeem3 = (RelativeLayout) rootView.findViewById(R.id.rel3);
				// speaker images
				imgMeem1 = (ImageView) rootView.findViewById(R.id.speaker1);
				imgMeem2 = (ImageView) rootView.findViewById(R.id.speaker2);
				imgMeem3 = (ImageView) rootView.findViewById(R.id.speaker3);

			}
			else if (idValue == 2)
			{
				rootView = inflater.inflate(R.layout.tajweed_rule3, null);

				txtTafkheemIntro1 = (TextView) rootView.findViewById(R.id.txtTafkeemIntro);
				txtTafkheemIntro2 = (TextView) rootView.findViewById(R.id.txtTafkeemIntro2);
				txtTafkheemIntro3 = (TextView) rootView.findViewById(R.id.txtTafkeemIntro3);
				txtTafkheemIntro4 = (TextView) rootView.findViewById(R.id.txtTafkeemIntro4);
				txtTafkheemIntro5 = (TextView) rootView.findViewById(R.id.txtTafkeemIntro5);
				txtTarqeeqIntro1 = (TextView) rootView.findViewById(R.id.txtTarqeeqIntro);
				txtTarqeeqIntro2 = (TextView) rootView.findViewById(R.id.txtTarqeeqIntro2);
				txtTarqeeqIntro3 = (TextView) rootView.findViewById(R.id.txtTarqeeqIntro3);
				txtTarqeeqIntro4 = (TextView) rootView.findViewById(R.id.txtTarqeeqIntro4);
				// Raa Headings
				raaHeading1 = (TextView) rootView.findViewById(R.id.txtTafkeemHeading);
				raaHeading2 = (TextView) rootView.findViewById(R.id.txtTarqeeqHeading);
				// Font Style
				raaHeading1.setTypeface(mGlobal.corbalBold);
				raaHeading2.setTypeface(mGlobal.corbalBold);

				relRaa1 = (RelativeLayout) rootView.findViewById(R.id.relTafkheem);
				relRaa2 = (RelativeLayout) rootView.findViewById(R.id.relTafkheem2);
				relRaa3 = (RelativeLayout) rootView.findViewById(R.id.relTafkheem3);
				relRaa4 = (RelativeLayout) rootView.findViewById(R.id.relTafkheem4);
				relRaa5 = (RelativeLayout) rootView.findViewById(R.id.relTafkheem5);
				relRaa6 = (RelativeLayout) rootView.findViewById(R.id.relTarqeeq1);
				relRaa7 = (RelativeLayout) rootView.findViewById(R.id.relTarqeeq2);
				relRaa8 = (RelativeLayout) rootView.findViewById(R.id.relTarqeeq3);
				relRaa9 = (RelativeLayout) rootView.findViewById(R.id.relTarqeeq4);
				// speaker images
				imgRaa1 = (ImageView) rootView.findViewById(R.id.speakerRaa1);
				imgRaa2 = (ImageView) rootView.findViewById(R.id.speakerRaa2);
				imgRaa3 = (ImageView) rootView.findViewById(R.id.speakerRaa3);
				imgRaa4 = (ImageView) rootView.findViewById(R.id.speakerRaa4);
				imgRaa5 = (ImageView) rootView.findViewById(R.id.speakerRaa5);
				imgRaa6 = (ImageView) rootView.findViewById(R.id.speakerRaa6);
				imgRaa7 = (ImageView) rootView.findViewById(R.id.speakerRaa7);
				imgRaa8 = (ImageView) rootView.findViewById(R.id.speakerRaa8);
				imgRaa9 = (ImageView) rootView.findViewById(R.id.speakerRaa9);

			}
			else if (idValue == 3)
			{
				rootView = inflater.inflate(R.layout.tajweed_rule4, null);

				txtLam1 = (TextView) rootView.findViewById(R.id.txtLaamTafkheemIntro);
				txtLam2 = (TextView) rootView.findViewById(R.id.txtLaamTafkheemIntro2);
				txtLam3 = (TextView) rootView.findViewById(R.id.txtLaamTafkheemIntro3);
				txtLam4 = (TextView) rootView.findViewById(R.id.txtLamTarqeeqIntro1);
				txtLam5 = (TextView) rootView.findViewById(R.id.txtLamTarqeeqIntro2);
				// Lam Headings
				lamHeading1 = (TextView) rootView.findViewById(R.id.txtLamTafkheemHeading);
				lamHeading2 = (TextView) rootView.findViewById(R.id.txtLamTarqeeqHeading);
				// Font styles
				lamHeading1.setTypeface(mGlobal.corbalBold);
				lamHeading2.setTypeface(mGlobal.corbalBold);

				relLam1 = (RelativeLayout) rootView.findViewById(R.id.relLam1);
				relLam2 = (RelativeLayout) rootView.findViewById(R.id.relLam2);
				relLam3 = (RelativeLayout) rootView.findViewById(R.id.relLam3);
				relLam4 = (RelativeLayout) rootView.findViewById(R.id.relLam4);
				relLam5 = (RelativeLayout) rootView.findViewById(R.id.relLam5);
				// speaker images
				imgLam1 = (ImageView) rootView.findViewById(R.id.speakerLam1);
				imgLam2 = (ImageView) rootView.findViewById(R.id.speakerLam2);
				imgLam3 = (ImageView) rootView.findViewById(R.id.speakerLam3);
				imgLam4 = (ImageView) rootView.findViewById(R.id.speakerLam4);
				imgLam5 = (ImageView) rootView.findViewById(R.id.speakerLam5);

			}
			else if (idValue == 4)
			{
				rootView = inflater.inflate(R.layout.tajweed_rule5, null);
				// txtWaslIntro,txtQataIntro1,txtQataIntro2,txtQataIntro3;
				txtWaslIntro = (TextView) rootView.findViewById(R.id.txtWaslIntro);
				txtQataIntro1 = (TextView) rootView.findViewById(R.id.txtQataIntro1);
				txtQataIntro2 = (TextView) rootView.findViewById(R.id.txtQataIntro2);
				txtQataIntro3 = (TextView) rootView.findViewById(R.id.txtQataIntro3);
				// Hamza Headings
				hamzaHeading1 = (TextView) rootView.findViewById(R.id.txtWaslHeading);
				hamzaHeading2 = (TextView) rootView.findViewById(R.id.txtQataHeading);
				// Font styles
				hamzaHeading1.setTypeface(mGlobal.corbalBold);
				hamzaHeading2.setTypeface(mGlobal.corbalBold);

				relHamza1 = (RelativeLayout) rootView.findViewById(R.id.relWasl);
				relHamza2 = (RelativeLayout) rootView.findViewById(R.id.relQata1);
				relHamza3 = (RelativeLayout) rootView.findViewById(R.id.relQata2);
				relHamza4 = (RelativeLayout) rootView.findViewById(R.id.relQata3);
				// speaker images
				imgHamza1 = (ImageView) rootView.findViewById(R.id.speakerHamza1);
				imgHamza2 = (ImageView) rootView.findViewById(R.id.speakerHamza2);
				imgHamza3 = (ImageView) rootView.findViewById(R.id.speakerHamza3);
				imgHamza4 = (ImageView) rootView.findViewById(R.id.speakerHamza4);

			}
			rootView.setTag(holder);

		}
		else
		{
			holder = (ViewHolder) rootView.getTag();
		}
		switch (idValue) {
		case 0:
			txtIzhar1.setText(Html.fromHtml(mGlobal.dataList.get(25).toString()));
			txtIdgham1.setText(Html.fromHtml(mGlobal.dataList.get(26).toString()));
			txtIdgham2.setText(Html.fromHtml(mGlobal.dataList.get(27).toString()));
			txtIqlaab1.setText(Html.fromHtml(mGlobal.dataList.get(28).toString()));
			txtIkhfa1.setText(Html.fromHtml(mGlobal.dataList.get(29).toString()));
			// Font styles
			txtIzhar1.setTypeface(mGlobal.robotoRegular);
			txtIdgham1.setTypeface(mGlobal.robotoRegular);
			txtIdgham2.setTypeface(mGlobal.robotoRegular);
			txtIqlaab1.setTypeface(mGlobal.robotoRegular);
			txtIkhfa1.setTypeface(mGlobal.robotoRegular);

			relNun1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (nunCounter == 1)
					{
						nunCounter = 1;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (nunCounter) {
							case 1:
								imgNun1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgNun2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgNun3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgNun4.setImageResource(R.drawable.speaker);
								break;
							case 5:
								imgNun5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					nunCounter = 1;
					if (nunPrevious != -1)
					{
						switch (nunPrevious) {
						case 1:
							imgNun1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgNun2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgNun3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgNun4.setImageResource(R.drawable.speaker);
							break;
						case 5:
							imgNun5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					nunPrevious = nunCounter;
					stopPlaying();
					audioName="res_nun_izhar";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_nun_izhar);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgNun1.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgNun1.setImageResource(R.drawable.speaker_hover);
					}
				}
			});
			relNun2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (nunCounter == 2)
					{
						nunCounter = 2;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (nunCounter) {
							case 1:
								imgNun1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgNun2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgNun3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgNun4.setImageResource(R.drawable.speaker);
								break;
							case 5:
								imgNun5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					nunCounter = 2;
					if (nunPrevious != -1)
					{
						switch (nunPrevious) {
						case 1:
							imgNun1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgNun2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgNun3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgNun4.setImageResource(R.drawable.speaker);
							break;
						case 5:
							imgNun5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					nunPrevious = nunCounter;
					stopPlaying();
					audioName="res_nun_idgham0";
					initAudioFile(audioName);
				//	mp = MediaPlayer.create(mActivity, R.raw.res_nun_idgham0);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgNun2.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgNun2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relNun3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (nunCounter == 3)
					{
						nunCounter = 3;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (nunCounter) {
							case 1:
								imgNun1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgNun2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgNun3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgNun4.setImageResource(R.drawable.speaker);
								break;
							case 5:
								imgNun5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;

					nunCounter = 3;
					if (nunPrevious != -1)
					{
						switch (nunPrevious) {
						case 1:
							imgNun1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgNun2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgNun3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgNun4.setImageResource(R.drawable.speaker);
							break;
						case 5:
							imgNun5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					nunPrevious = nunCounter;
					stopPlaying();
					audioName="res_nun_idgham1";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_nun_idgham1);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgNun3.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgNun3.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relNun4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (nunCounter == 4)
					{
						nunCounter = 4;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (nunCounter) {
							case 1:
								imgNun1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgNun2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgNun3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgNun4.setImageResource(R.drawable.speaker);
								break;
							case 5:
								imgNun5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					nunCounter = 4;
					if (nunPrevious != -1)
					{
						switch (nunPrevious) {
						case 1:
							imgNun1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgNun2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgNun3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgNun4.setImageResource(R.drawable.speaker);
							break;
						case 5:
							imgNun5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					nunPrevious = nunCounter;
					stopPlaying();
					audioName="res_nun_iqlaab";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_nun_iqlaab);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgNun4.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgNun4.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relNun5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (nunCounter == 5)
					{
						nunCounter = 5;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (nunCounter) {
							case 1:
								imgNun1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgNun2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgNun3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgNun4.setImageResource(R.drawable.speaker);
								break;
							case 5:
								imgNun5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					nunCounter = 5;
					if (nunPrevious != -1)
					{
						switch (nunPrevious) {
						case 1:
							imgNun1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgNun2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgNun3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgNun4.setImageResource(R.drawable.speaker);
							break;
						case 5:
							imgNun5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					nunPrevious = nunCounter;
					stopPlaying();
					audioName="res_nun_ikhfa";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_nun_ikhfa);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgNun5.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgNun5.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			break;
		case 1:
			// txtIdgham,txtIkhfa,txtIzhar;
			txtIdgham.setText(Html.fromHtml(mGlobal.dataList.get(30).toString()));
			txtIkhfa.setText(Html.fromHtml(mGlobal.dataList.get(31).toString()));
			txtIzhar.setText(Html.fromHtml(mGlobal.dataList.get(32).toString()));
			// Font styles
			txtIdgham.setTypeface(mGlobal.robotoRegular);
			txtIkhfa.setTypeface(mGlobal.robotoRegular);
			txtIzhar.setTypeface(mGlobal.robotoRegular);

			relMeem1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (meemCounter == 1)
					{
						meemCounter = 1;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (meemCounter) {
							case 1:
								imgMeem1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgMeem2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgMeem3.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					meemCounter = 1;
					if (meemPrevious != -1)
					{
						switch (meemPrevious) {
						case 1:
							imgMeem1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgMeem2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgMeem3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					meemPrevious = meemCounter;
					stopPlaying();
					audioName="res_meem1";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_meem1);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgMeem1.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgMeem1.setImageResource(R.drawable.speaker_hover);
					}
				}
			});
			relMeem2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (meemCounter == 2)
					{
						meemCounter = 2;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (meemCounter) {
							case 1:
								imgMeem1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgMeem2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgMeem3.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					meemCounter = 2;
					if (meemPrevious != -1)
					{
						switch (meemPrevious) {
						case 1:
							imgMeem1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgMeem2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgMeem3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					meemPrevious = meemCounter;
					stopPlaying();
					audioName="res_meem2";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_meem2);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgMeem2.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgMeem2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relMeem3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (meemCounter == 3)
					{
						meemCounter = 3;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (meemCounter) {
							case 1:
								imgMeem1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgMeem2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgMeem3.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					meemCounter = 3;
					if (meemPrevious != -1)
					{
						switch (meemPrevious) {
						case 1:
							imgMeem1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgMeem2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgMeem3.setImageResource(R.drawable.speaker);
							break;

						}
					}
					meemPrevious = meemCounter;
					stopPlaying();
					audioName="res_meem3";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_meem3);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgMeem3.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgMeem3.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			break;
		case 2:
			// txtTafkheemIntro1,txtTafkheemIntro2,txtTafkheemIntro3,txtTafkheemIntro4,txtTafkheemIntro5;
			// txtTarqeeqIntro1,txtTarqeeqIntro2,txtTarqeeqIntro3,txtTarqeeqIntro4;
			anyText = String.valueOf(mGlobal.dataList.get(33));
			raaText = anyText.split(":");
			anyText1 = String.valueOf(mGlobal.dataList.get(34));
			raaText1 = anyText1.split(":");

			txtTafkheemIntro1.setText(Html.fromHtml(raaText[0]));
			txtTafkheemIntro2.setText(Html.fromHtml(raaText[1]));
			txtTafkheemIntro3.setText(Html.fromHtml(raaText[2]));
			txtTafkheemIntro4.setText(Html.fromHtml(raaText[3]));
			txtTafkheemIntro5.setText(Html.fromHtml(raaText[4]));
			txtTarqeeqIntro1.setText(Html.fromHtml(raaText1[0]));
			txtTarqeeqIntro2.setText(Html.fromHtml(raaText1[1]));
			txtTarqeeqIntro3.setText(Html.fromHtml(raaText1[2]));
			txtTarqeeqIntro4.setText(Html.fromHtml(raaText1[3]));
			// Font styles
			txtTafkheemIntro1.setTypeface(mGlobal.robotoRegular);
			txtTafkheemIntro2.setTypeface(mGlobal.robotoRegular);
			txtTafkheemIntro3.setTypeface(mGlobal.robotoRegular);
			txtTafkheemIntro4.setTypeface(mGlobal.robotoRegular);
			txtTafkheemIntro5.setTypeface(mGlobal.robotoRegular);
			txtTarqeeqIntro1.setTypeface(mGlobal.robotoRegular);
			txtTarqeeqIntro2.setTypeface(mGlobal.robotoRegular);
			txtTarqeeqIntro3.setTypeface(mGlobal.robotoRegular);
			txtTarqeeqIntro4.setTypeface(mGlobal.robotoRegular);

			relRaa1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 1)
					{
						raaCounter = 1;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 1;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_thkfm0";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_thkfm0);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa1.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa1.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 2)
					{
						raaCounter = 2;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 2;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_thkfm1";
					initAudioFile(audioName);
				//	mp = MediaPlayer.create(mActivity, R.raw.res_raa_thkfm1);
				//	mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa2.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 3)
					{
						raaCounter = 3;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 3;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_thkfm2";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_thkfm2);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa3.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa3.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 4)
					{
						raaCounter = 4;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 4;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_thkfm3";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_thkfm3);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa4.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa4.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 5)
					{
						raaCounter = 5;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 5;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_thkfm4";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_thkfm4);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa5.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa5.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa6.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 6)
					{
						raaCounter = 6;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 6;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_tarqeeq0";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_tarqeeq0);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa6.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa6.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa7.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 7)
					{
						raaCounter = 7;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 7;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_tarqeeq1";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_tarqeeq1);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa7.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa7.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa8.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 8)
					{
						raaCounter = 8;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 8;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_tarqeeq2";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_tarqeeq2);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa8.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa8.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relRaa9.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (raaCounter == 9)
					{
						raaCounter = 9;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (raaCounter) {
							case 1:
								imgRaa1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgRaa2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgRaa3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgRaa4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgRaa5.setImageResource(R.drawable.speaker);
								break;

							case 6:
								imgRaa6.setImageResource(R.drawable.speaker);
								break;

							case 7:
								imgRaa7.setImageResource(R.drawable.speaker);
								break;

							case 8:
								imgRaa8.setImageResource(R.drawable.speaker);
								break;

							case 9:
								imgRaa9.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					raaCounter = 9;
					if (raaPrevious != -1)
					{
						switch (raaPrevious) {
						case 1:
							imgRaa1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgRaa2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgRaa3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgRaa4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgRaa5.setImageResource(R.drawable.speaker);
							break;

						case 6:
							imgRaa6.setImageResource(R.drawable.speaker);
							break;

						case 7:
							imgRaa7.setImageResource(R.drawable.speaker);
							break;

						case 8:
							imgRaa8.setImageResource(R.drawable.speaker);
							break;

						case 9:
							imgRaa9.setImageResource(R.drawable.speaker);
							break;

						}
					}
					raaPrevious = raaCounter;
					stopPlaying();
					audioName="res_raa_tarqeeq3";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_raa_tarqeeq3);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgRaa9.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgRaa9.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			break;
		case 3:
			// ,txtLam2,txtLam3,txtLam4,txtLam5;
			anyText = String.valueOf(mGlobal.dataList.get(35));
			raaText = anyText.split(":");
			txtLam1.setText(Html.fromHtml(raaText[0]));
			txtLam2.setText(Html.fromHtml(raaText[1]));
			txtLam3.setText(Html.fromHtml(raaText[2]));

			//
			anyText1 = String.valueOf(mGlobal.dataList.get(36));
			raaText1 = anyText1.split(":");
			txtLam4.setText(Html.fromHtml(raaText1[0]));
			txtLam5.setText(Html.fromHtml(raaText1[1]));
			// Font styles
			txtLam1.setTypeface(mGlobal.robotoRegular);
			txtLam2.setTypeface(mGlobal.robotoRegular);
			txtLam3.setTypeface(mGlobal.robotoRegular);
			txtLam4.setTypeface(mGlobal.robotoRegular);
			txtLam5.setTypeface(mGlobal.robotoRegular);

			relLam1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					if (laamCounter == 1)
					{
						laamCounter = 1;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (laamCounter) {
							case 1:
								imgLam1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgLam2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgLam3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgLam4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgLam5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					laamCounter = 1;
					if (laamPrevious != -1)
					{
						switch (laamPrevious) {
						case 1:
							imgLam1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgLam2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgLam3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgLam4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgLam5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					laamPrevious = laamCounter;
					stopPlaying();
					audioName="res_laam1_0";
					initAudioFile(audioName);
				//	mp = MediaPlayer.create(mActivity, R.raw.res_laam1_0);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgLam1.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgLam1.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relLam2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (laamCounter == 2)
					{
						laamCounter = 2;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (laamCounter) {
							case 1:
								imgLam1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgLam2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgLam3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgLam4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgLam5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					laamCounter = 2;
					if (laamPrevious != -1)
					{
						switch (laamPrevious) {
						case 1:
							imgLam1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgLam2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgLam3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgLam4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgLam5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					laamPrevious = laamCounter;

					stopPlaying();
					audioName="res_laam1_1";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_laam1_1);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgLam2.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgLam2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relLam3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (laamCounter == 3)
					{
						laamCounter = 3;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (laamCounter) {
							case 1:
								imgLam1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgLam2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgLam3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgLam4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgLam5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					laamCounter = 3;
					if (laamPrevious != -1)
					{
						switch (laamPrevious) {
						case 1:
							imgLam1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgLam2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgLam3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgLam4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgLam5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					laamPrevious = laamCounter;

					stopPlaying();
					audioName="res_laam1_2";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_laam1_2);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgLam3.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgLam3.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relLam4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (laamCounter == 4)
					{
						laamCounter = 4;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (laamCounter) {
							case 1:
								imgLam1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgLam2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgLam3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgLam4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgLam5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					laamCounter = 4;
					if (laamPrevious != -1)
					{
						switch (laamPrevious) {
						case 1:
							imgLam1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgLam2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgLam3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgLam4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgLam5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					laamPrevious = laamCounter;
					stopPlaying();
					audioName="res_laam2_0";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_laam2_0);
				//	mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgLam4.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgLam4.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relLam5.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (laamCounter == 5)
					{
						laamCounter = 5;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (laamCounter) {
							case 1:
								imgLam1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgLam2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgLam3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgLam4.setImageResource(R.drawable.speaker);
								break;

							case 5:
								imgLam5.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					laamCounter = 5;
					if (laamPrevious != -1)
					{
						switch (laamPrevious) {
						case 1:
							imgLam1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgLam2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgLam3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgLam4.setImageResource(R.drawable.speaker);
							break;

						case 5:
							imgLam5.setImageResource(R.drawable.speaker);
							break;

						}
					}
					laamPrevious = laamCounter;
					stopPlaying();
					audioName="res_laam2_1";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_laam2_1);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgLam5.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgLam5.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			//
			break;
		case 4:
			anyText = String.valueOf(mGlobal.dataList.get(38));
			raaText = anyText.split(":");
			txtWaslIntro.setText(Html.fromHtml(mGlobal.dataList.get(37).toString()));
			// txtWaslIntro,txtQataIntro1,txtQataIntro2,txtQataIntro3;
			txtQataIntro1.setText(Html.fromHtml(raaText[0]));
			txtQataIntro2.setText(Html.fromHtml(raaText[1]));
			txtQataIntro3.setText(Html.fromHtml(raaText[2]));
			// Font styles
			txtWaslIntro.setTypeface(mGlobal.robotoRegular);
			txtQataIntro1.setTypeface(mGlobal.robotoRegular);
			txtQataIntro2.setTypeface(mGlobal.robotoRegular);
			txtQataIntro3.setTypeface(mGlobal.robotoRegular);

			relHamza1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (hamzaCounter == 1)
					{
						hamzaCounter = 1;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (hamzaCounter) {
							case 1:
								imgHamza1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgHamza2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgHamza3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgHamza4.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					hamzaCounter = 1;
					if (hamzaPrevious != -1)
					{
						switch (hamzaPrevious) {
						case 1:
							imgHamza1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgHamza2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgHamza3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgHamza4.setImageResource(R.drawable.speaker);
							break;

						}
					}
					hamzaPrevious = hamzaCounter;
					stopPlaying();
					audioName="res_hamza1";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_hamza1);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgHamza1.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgHamza1.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relHamza2.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (hamzaCounter == 2)
					{
						hamzaCounter = 2;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (hamzaCounter) {
							case 1:
								imgHamza1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgHamza2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgHamza3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgHamza4.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					hamzaCounter = 2;
					if (hamzaPrevious != -1)
					{
						switch (hamzaPrevious) {
						case 1:
							imgHamza1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgHamza2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgHamza3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgHamza4.setImageResource(R.drawable.speaker);
							break;

						}
					}
					hamzaPrevious = hamzaCounter;
					stopPlaying();
					audioName="res_hamza_2_0";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_hamza_2_0);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgHamza2.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgHamza2.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relHamza3.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (hamzaCounter == 3)
					{
						hamzaCounter = 3;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (hamzaCounter) {
							case 1:
								imgHamza1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgHamza2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgHamza3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgHamza4.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					hamzaCounter = 3;
					if (hamzaPrevious != -1)
					{
						switch (hamzaPrevious) {
						case 1:
							imgHamza1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgHamza2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgHamza3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgHamza4.setImageResource(R.drawable.speaker);
							break;

						}
					}
					hamzaPrevious = hamzaCounter;
					stopPlaying();
					audioName="res_hamza_2_1";
					initAudioFile(audioName);
					//mp = MediaPlayer.create(mActivity, R.raw.res_hamza_2_1);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgHamza3.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgHamza3.setImageResource(R.drawable.speaker_hover);
					}

				}
			});
			relHamza4.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (hamzaCounter == 4)
					{
						hamzaCounter = 4;
						if (mp != null && mp.isPlaying() && !rulesFlag)
						{
							mp.stop();

							switch (hamzaCounter) {
							case 1:
								imgHamza1.setImageResource(R.drawable.speaker);
								break;
							case 2:
								imgHamza2.setImageResource(R.drawable.speaker);
								break;
							case 3:
								imgHamza3.setImageResource(R.drawable.speaker);
								break;
							case 4:
								imgHamza4.setImageResource(R.drawable.speaker);
								break;

							}
							rulesFlag = true;
							return;
						}
					}
					rulesFlag = false;
					hamzaCounter = 4;
					if (hamzaPrevious != -1)
					{
						switch (hamzaPrevious) {
						case 1:
							imgHamza1.setImageResource(R.drawable.speaker);
							break;
						case 2:
							imgHamza2.setImageResource(R.drawable.speaker);
							break;
						case 3:
							imgHamza3.setImageResource(R.drawable.speaker);
							break;
						case 4:
							imgHamza4.setImageResource(R.drawable.speaker);
							break;

						}
					}
					hamzaPrevious = hamzaCounter;
					stopPlaying();
					audioName="res_hamza_2_2";
					initAudioFile(audioName);
				//	mp = MediaPlayer.create(mActivity, R.raw.res_hamza_2_2);
					//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
					if (mp != null) {
						mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								imgHamza4.setImageResource(R.drawable.speaker);

							}
						});

						mp.start();
						imgHamza4.setImageResource(R.drawable.speaker_hover);
					}

				}
			});

			break;
		default:
		}

		return rootView;

	}

	private void stopPlaying() {
		if (mp != null)
		{
			mp.release();
			mp = null;
		}
	}

	public class ViewHolder {
		// Data base related objects

		public ViewHolder() {
			// this.idValue = anyId;

		}

	}

	@Override
	public void onDestroyView() {

		if (mp != null)
		{
			if (!rulesFlag)
			{
				rulesFlag = true;

				stopPlaying();

			}
		}

		super.onDestroyView();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		rulesFlag = false;
	isRuleofTajweed=true;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isRuleofTajweed=false;
	if (idValue == 0)
	{
		imgNun1.setImageResource(R.drawable.speaker);
		imgNun2.setImageResource(R.drawable.speaker);
		imgNun3.setImageResource(R.drawable.speaker);
		imgNun4.setImageResource(R.drawable.speaker);
		imgNun5.setImageResource(R.drawable.speaker);
	}
	else	if (idValue == 1)
	{
		imgMeem1.setImageResource(R.drawable.speaker);
		imgMeem2.setImageResource(R.drawable.speaker);
		imgMeem3.setImageResource(R.drawable.speaker);
	}
	else	if (idValue == 2)
	{
		imgRaa1.setImageResource(R.drawable.speaker);
		imgRaa2.setImageResource(R.drawable.speaker);
		imgRaa3.setImageResource(R.drawable.speaker);
		imgRaa4.setImageResource(R.drawable.speaker);
		imgRaa5.setImageResource(R.drawable.speaker);
		imgRaa6.setImageResource(R.drawable.speaker);
		imgRaa7.setImageResource(R.drawable.speaker);
		imgRaa8.setImageResource(R.drawable.speaker);
		imgRaa9.setImageResource(R.drawable.speaker);
	}
	else	if (idValue == 3)
	{
		imgLam1.setImageResource(R.drawable.speaker);
		imgLam2.setImageResource(R.drawable.speaker);
		imgLam3.setImageResource(R.drawable.speaker);
		imgLam4.setImageResource(R.drawable.speaker);
		imgLam5.setImageResource(R.drawable.speaker);
	}
	else	if (idValue == 4)
	{
		imgHamza1.setImageResource(R.drawable.speaker);
		imgHamza2.setImageResource(R.drawable.speaker);
		imgHamza3.setImageResource(R.drawable.speaker);
		imgHamza4.setImageResource(R.drawable.speaker);

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
				mp = MediaPlayer.create(mActivity, audioUri);
				mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
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
