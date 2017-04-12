package com.packageapp.wordbyword;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ArabicText.Helper.ArabicUtilities;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.wordbyword.models.DataModel;
import com.packageapp.wordbyword.network_downloading.Constants;
import com.packageapp.wordbyword.network_downloading.DownloadDialogWBW;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class SurahFragment extends Fragment implements OnCompletionListener {
    private String[] surahAyahList, surahTranslationList, surahTransliterationList, arabicWordsList, translationWordsList, transliterationWordsList, verse_words = null;
    private String[][] fullSurahArabicWord, fullSurahTranslationWords, fullSurahTransliterationWords;
    FullSurahListAdapter fullSurahAdapter;
    private ArrayList<DataModel> surahAyahData = new ArrayList<DataModel>();
    private int[][] timeAyahSurah, timeAllSurah;
    public int play = 0, verse_length = 0;
    private int currentPlayingPosition = 0, delayIndexWords = 0, currentIndex = 0, selectedSuraPos, delayIndex = 0;

    private ListView fullSurahList, wordsList;
    //private FullSurahListAdapter fullSurahAdapter;
    private WbWListAdapter wordAdapter;
    private GlobalClass globalObject;

    private final Handler handler = new Handler();
    private boolean isWordTapped = false, isSingleWordPlaying = false, rowClick = false, firstTimeAudio = true, chkRowLastIndex = false, audioShuldBeNull = false;

    TextView ayahArabic, ayahTranslationTv, ayahTransliterationTv;
    LinearLayout wordLayout;
    RelativeLayout fullSuraLayout;
    ImageView nextWordButton, previousWordButton, playWordBtn;
    MediaPlayer mp1;
    public static int lastIndex = 0;
    public static String highlightedWordForFullSura = "", highlightedWordForWbW = "";
    public static Boolean isFullAudioPlaying = false, isDialoguDisplayed = false;
    public static final int requestDownload = 3;
    private Activity activitydetail;
    private long stopClick=0;
    private File f;
    AnalyticSingaltonClass mAnalyticSingaltonClass;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        globalObject = (GlobalClass) activitydetail.getApplicationContext();
        globalObject.mediaPlayerFullSurah = null;
        globalObject.mediaPlayerWordByWord = null;
        IntentFilter WordTabfilter = new IntentFilter();
        WordTabfilter.addAction("TabChangeBroadcast");
        mAnalyticSingaltonClass= AnalyticSingaltonClass.getInstance(getActivity());
        mAnalyticSingaltonClass.sendScreenAnalytics("WBW_Detail_Screen");

        IntentFilter PlayAudioBroadcastFilter = new IntentFilter();
        PlayAudioBroadcastFilter.addAction("PlayAudioBroadcastSurah");

        IntentFilter tappedWordFilter = new IntentFilter();
        tappedWordFilter.addAction("wordTapBroadcast");

        IntentFilter pageChangeFilter = new IntentFilter();
        pageChangeFilter.addAction("PageChangeBroadcastSura");

        IntentFilter stopAudioBroadcastFilter = new IntentFilter();
        stopAudioBroadcastFilter.addAction("StopAudioBroadcastSura");

        activitydetail.registerReceiver(checkTapPosition, WordTabfilter);
        activitydetail.registerReceiver(PlayAudioBroadcastSurah, PlayAudioBroadcastFilter);
        activitydetail.registerReceiver(wordTapBroadcast, tappedWordFilter);
        activitydetail.registerReceiver(PageChangeBroadcastSura, pageChangeFilter);
        activitydetail.registerReceiver(StopAudioBroadcastSura, stopAudioBroadcastFilter);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activitydetail = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        selectedSuraPos = getArguments().getInt("surahPos");
        surahAyahList = getArguments().getStringArray("ayasArray");
        surahTranslationList = getArguments().getStringArray("translationArray");
        surahTransliterationList = getArguments().getStringArray("transliterationArray");

        // String[] surahNAmeArray = activitydetail.getResources().getStringArray(R.array.surah_names);
        // surahName = surahNAmeArray[selectedSuraPos];

        View surahView = inflater.inflate(R.layout.surah_frgament_layout, null);

        wordLayout = (LinearLayout) surahView.findViewById(R.id.wordBywordLAyout);
        fullSuraLayout = (RelativeLayout) surahView.findViewById(R.id.fullSurahLayout);

        // word by word data
        ayahArabic = (TextView) surahView.findViewById(R.id.textArabicWord);
        ayahTranslationTv = (TextView) surahView.findViewById(R.id.textTranslationWord);
        ayahTransliterationTv = (TextView) surahView.findViewById(R.id.textTransliterationWord);
        wordsList = (ListView) surahView.findViewById(R.id.ayahListView);

        previousWordButton = (ImageView) surahView.findViewById(R.id.backButton);
        nextWordButton = (ImageView) surahView.findViewById(R.id.nextButton);
        playWordBtn = (ImageView) surahView.findViewById(R.id.playWord);

        // Data Manipulation
        if (surahAyahList != null && surahTranslationList != null && surahTransliterationList != null) {
            surahAyahData.clear();
            for (int pos = 0; pos < surahAyahList.length; pos++) {
                DataModel surahList = new DataModel();
                surahList.setTexts(surahAyahList[pos], surahTranslationList[pos], surahTransliterationList[pos]);
                surahAyahData.add(surahList);
            }
        }

        if (surahAyahData.size() > 0) {
            fullSurahArabicWord = new String[surahAyahList.length][];
            fullSurahTranslationWords = new String[surahAyahList.length][];
            fullSurahTransliterationWords = new String[surahAyahList.length][];

            for (int ayahPoss = 0; ayahPoss < surahAyahList.length; ayahPoss++) {
                arabicWordsList = (surahAyahData.get(ayahPoss).getArabicText().split("\\ "));
                translationWordsList = surahAyahData.get(ayahPoss).getTranslationText().split("\\ -");
                transliterationWordsList = surahAyahData.get(ayahPoss).gettransliterationText().split("\\ . ");

                fullSurahArabicWord[ayahPoss] = new String[arabicWordsList.length];
                fullSurahTranslationWords[ayahPoss] = new String[arabicWordsList.length];
                fullSurahTransliterationWords[ayahPoss] = new String[arabicWordsList.length];

                for (int wordPoss = 0; wordPoss < arabicWordsList.length; wordPoss++) {
                    fullSurahArabicWord[ayahPoss][wordPoss] = arabicWordsList[wordPoss];
                    fullSurahTranslationWords[ayahPoss][wordPoss] = translationWordsList[wordPoss];
                    fullSurahTransliterationWords[ayahPoss][wordPoss] = transliterationWordsList[wordPoss];
                }
            }
        }

        if (globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) {
            //previousWordButton.setImageResource(R.drawable.previous_h);
            nextWordButton.setImageResource(R.drawable.play_h_w_back);
        } else {
            //previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
            nextWordButton.setImageResource(R.drawable.next_word_selector_file);
        }

        if (globalObject.ayahPosWordbWord == (fullSurahArabicWord.length - 1) && globalObject.wordPos == fullSurahArabicWord[globalObject.ayahPosWordbWord].length - 1) {
            //nextWordButton.setImageResource(R.drawable.next_h);
            previousWordButton.setImageResource(R.drawable.previous_h);
        } else {
            previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
        }
        //ful surah Adapter Setting
        fullSurahList = (ListView) surahView.findViewById(R.id.fullSurahList);
        fullSurahAdapter = new FullSurahListAdapter(activitydetail, surahAyahData);
        fullSurahList.setAdapter(fullSurahAdapter);

        // word By Word
        wordAdapter = new WbWListAdapter(activitydetail, surahAyahData);
        wordsList.setAdapter(wordAdapter);

        ayahArabic.setText(ArabicUtilities.reshapeSentence(fullSurahArabicWord[0][0]));
        ayahArabic.setTypeface(globalObject.faceArabic1);
        ayahArabic.setPadding(0, 0, 0, 0);
        ayahTranslationTv.setText(fullSurahTranslationWords[0][0]);
        ayahTransliterationTv.setText(fullSurahTransliterationWords[0][0]);

        if (globalObject.selectedTabPosition == 1) {
            wordLayout.setVisibility(View.GONE);
            fullSuraLayout.setVisibility(View.VISIBLE);
        } else {
            wordLayout.setVisibility(View.VISIBLE);
            fullSuraLayout.setVisibility(View.GONE);
        }

        // /////////////for full surah only///////
        verse_words = surahAyahData.get(0).getArabicText().split(" ");
        verse_length = verse_words.length;
        if (selectedSuraPos == globalObject.surahPos) {
            highlightedWordForFullSura = verse_words[0];
        }

        //fullSurahAdapter.notifyDataSetChanged();
        timeAyahSurah = ArabicTimings.getSurahTiming(selectedSuraPos + 1);
        timeAllSurah = ArabicTimings.timeAllSurahs;

        // listeners
        nextWordButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isFullAudioPlaying)
                    nextWord();

            }
        });

        previousWordButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isFullAudioPlaying)
                    previousWord();

            }
        });

        playWordBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime()-stopClick<1000)
                {
                    return;
                }
                stopClick=SystemClock.elapsedRealtime();

                if (selectedSuraPos == globalObject.surahPos) {
                    audioShuldBeNull = true;
                    //
                    if (downloadAudios("bismillah_0_0")) {
                       /* File f = new File((GlobalClass.SurahsFolder).toString());
                        if (f.listFiles().length != GlobalClass.surahsTotalAud) {
                            Toast toast = Toast.makeText(activitydetail, "Tap on 'Play' to Download Audio", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else {*/
                        if (!isFullAudioPlaying) {
                            isSingleWordPlaying = true;
                            wordBywordAudioInitialization();
                            isSingleWordPlaying = false;
                        }

                    }

                }
            }
        });

/*		fullSurahList.setOnItemClickListener(new OnItemClickListener() {
            @Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if (globalObject.mediaPlayerFullSurah != null) {
					if (globalObject.mediaPlayerFullSurah.isPlaying()) {
						rowClick = false;
						handler.removeCallbacks(sendUpdatesToUI);
						globalObject.ayahPosFullSura = arg2;
						currentIndex = 0;
						lastIndex = 0;
						delayIndexWords = 0;
						delayIndex = globalObject.ayahPosFullSura + 1;
						setListScrollPosition(arg2);
						setWordtoHighlight();
						highlightedWordForFullSura = verse_words[currentIndex];
						//fullSurahAdapter.notifyDataSetChanged();
						globalObject.mediaPlayerFullSurah.seekTo(timeAyahSurah[globalObject.ayahPosFullSura][currentIndex]);
						handler.postDelayed(sendUpdatesToUI, 0);

					}
					else {
						rowClick = true;
						globalObject.ayahPosFullSura = arg2;
						delayIndex = globalObject.ayahPosFullSura;
						currentIndex = 0;
						lastIndex = 0;
						delayIndexWords = 0;
						setListScrollPosition(arg2);
						setWordtoHighlight();
						highlightedWordForFullSura = verse_words[currentIndex];
					//	fullSurahAdapter.notifyDataSetChanged();
					}
				}
				else {
					rowClick = true;
					globalObject.ayahPosFullSura = arg2;
					delayIndex = globalObject.ayahPosFullSura;
					currentIndex = 0;
					lastIndex = 0;
					delayIndexWords = 0;
					setListScrollPosition(arg2);
					setWordtoHighlight();
					highlightedWordForFullSura = verse_words[currentIndex];
					//fullSurahAdapter.notifyDataSetChanged();
				}
			}
		});*/

        return surahView;
    }

    private BroadcastReceiver checkTapPosition = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (globalObject.selectedTabPosition == 1) {

                if (globalObject.mediaPlayerWordByWord != null) {
                    if (globalObject.isPlayingWordByWord) {

                        globalObject.mediaPlayerWordByWord.pause();
                        globalObject.isPlayingWordByWord = false;

                        nextWordButton.setImageResource(R.drawable.next_word_selector_file); // /////
                        previousWordButton.setImageResource(R.drawable.previous_word_selector_file); // /////
                        playWordBtn.setImageResource(R.drawable.speaker_selector_file);

                        SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
                        isFullAudioPlaying = false;
                    }
                }
                play = 0;
                wordLayout.setVisibility(View.GONE);
                fullSuraLayout.setVisibility(View.VISIBLE);

            } else {
                if (globalObject.mediaPlayerFullSurah != null) {
                    if (globalObject.mediaPlayerFullSurah.isPlaying()) {
                        globalObject.mediaPlayerFullSurah.pause();
                        SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
                        currentPlayingPosition = globalObject.mediaPlayerFullSurah.getCurrentPosition();
                    }
                }
                play = 0;
                handler.removeCallbacks(sendUpdatesToUI);
                wordLayout.setVisibility(View.VISIBLE);
                fullSuraLayout.setVisibility(View.GONE);
            }

        }

    };


    private BroadcastReceiver PlayAudioBroadcastSurah = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //"bismillah_0_0.mp3 "

            if (selectedSuraPos == globalObject.surahPos) {
                //  f = new File((GlobalClass.SurahsFolder).toString());
                if (downloadAudios("bismillah_0_0")) {
                    /*if (f.listFiles().length == GlobalClass.surahsTotalAud) {*/

                    if (globalObject.selectedTabPosition == 1) {
                        if (globalObject.mediaPlayerFullSurah == null) {
                            fullSuraAudioInitialization();
                            playAudio();
                        } else {
                            playAudio();
                        }
                    } else // if in WordByWord fragment
                    {
                        isFullAudioPlaying = true;

                        wordBywordAudioInitialization();
                    }

                }
            }}};



    private BroadcastReceiver StopAudioBroadcastSura = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (selectedSuraPos == globalObject.surahPos) {
                if (globalObject.selectedTabPosition == 1) {
                    resetFullSuraValues();
                } else // if in WordByWord fragment
                {
                    resetWordByWordValues();
                }
            }
        }
    };

    private BroadcastReceiver wordTapBroadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (selectedSuraPos == globalObject.surahPos) {
                File f = new File((GlobalClass.SurahsFolder).toString());
                if (f.exists()) {
                    if (f.listFiles().length != GlobalClass.surahsTotalAud) {

                        Toast toast = Toast.makeText(activitydetail, "Tap on 'Play' to Download Audio", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        setTextViews();
                    } else {
                        audioShuldBeNull = true;
                        isWordTapped = true;
                        ayahArabic.setText( ArabicUtilities.reshapeSentence(fullSurahArabicWord[globalObject.ayahPosWordbWord][globalObject.wordPos]));

                        // next n previous button enable/disable
                        if (globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) {
                            //previousWordButton.setImageResource(R.drawable.previous_h); //here should enable next btn
                            nextWordButton.setImageResource(R.drawable.play_h_w_back);
                        } else {
                            //previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
                            nextWordButton.setImageResource(R.drawable.next_word_selector_file);
                        }

                        if (globalObject.ayahPosWordbWord == (fullSurahArabicWord.length - 1) && globalObject.wordPos == fullSurahArabicWord[globalObject.ayahPosWordbWord].length - 1) {
                            // nextWordButton.setImageResource(R.drawable.next_h);
                            previousWordButton.setImageResource(R.drawable.previous_h);

                        } else {
                            // nextWordButton.setImageResource(R.drawable.next_word_selector_file);
                            previousWordButton.setImageResource(R.drawable.previous_word_selector_file);

                        }
                        if(!(globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) && !(globalObject.ayahPosWordbWord == (fullSurahArabicWord.length - 1) && globalObject.wordPos == fullSurahArabicWord[globalObject.ayahPosWordbWord].length - 1))
                        {
                            nextWordButton.setImageResource(R.drawable.play_h_w_back);
                            previousWordButton.setImageResource(R.drawable.previous_h);
                        }

                        if (isFullAudioPlaying) {
                            SurahDetailActivity.playButton.setImageResource(R.drawable.pause_selector_file);
                        }
                        wordBywordAudioInitialization();
                    }
                } else // if not exist
                {
                    Toast toast = Toast.makeText(activitydetail, "Tap on 'Play' to Download Audio", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    setTextViews();
                }
                // next n previous button enable/disable
                if (globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) {
                    //previousWordButton.setImageResource(R.drawable.previous_h);
                    nextWordButton.setImageResource(R.drawable.play_h_w_back);
                } else {
                    //previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
                    nextWordButton.setImageResource(R.drawable.next_word_selector_file);
                }

                if (globalObject.ayahPosWordbWord == (fullSurahArabicWord.length - 1) && globalObject.wordPos == fullSurahArabicWord[globalObject.ayahPosWordbWord].length - 1) {
                    //nextWordButton.setImageResource(R.drawable.next_h);
                    previousWordButton.setImageResource(R.drawable.previous_h);
                } else {
                    previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
                }
                if(!(globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) && !(globalObject.ayahPosWordbWord == (fullSurahArabicWord.length - 1) && globalObject.wordPos == fullSurahArabicWord[globalObject.ayahPosWordbWord].length - 1))
                {
                    nextWordButton.setImageResource(R.drawable.play_h_w_back);
                    previousWordButton.setImageResource(R.drawable.previous_h);
                }


            }
        }
    };

    private BroadcastReceiver PageChangeBroadcastSura = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            resetFullSuraValues();
            resetWordByWordValues();
        }

    };

    private void previousWord() {
        nextWordButton.setImageResource(R.drawable.next_word_selector_file);
        if (globalObject.wordPos == 0) {

            if (globalObject.ayahPosWordbWord != 0) {
                previousWordButton.setImageResource(R.drawable.previous_h);
                nextWordButton.setImageResource(R.drawable.play_h_w_back);
                globalObject.ayahPosWordbWord--;
                int wordsLength = fullSurahArabicWord[globalObject.ayahPosWordbWord].length;
                globalObject.wordPos = wordsLength - 1;

                setTextViews();
            } else {
                // 1st word
                previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
                nextWordButton.setImageResource(R.drawable.play_h_w_back);
            }
        } else if (globalObject.wordPos != 0) {
            previousWordButton.setImageResource(R.drawable.previous_h);
            nextWordButton.setImageResource(R.drawable.play_h_w_back);
            globalObject.wordPos--;

            setTextViews();

            highlightedWordForWbW = fullSurahArabicWord[globalObject.ayahPosWordbWord][globalObject.wordPos];
            wordAdapter.notifyDataSetChanged();
            wordsList.post(new Runnable() {

                @Override
                public void run() {
                    wordsList.setSelection(globalObject.ayahPosWordbWord);
                }
            });

            if (globalObject.wordPos == 0 && globalObject.ayahPosWordbWord == 0) {
                previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
                nextWordButton.setImageResource(R.drawable.play_h_w_back);
            }
        }
        else
        {
            previousWordButton.setImageResource(R.drawable.previous_h);
            nextWordButton.setImageResource(R.drawable.play_h_w_back);
        }

    }

    private void nextWord() {
        previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
        int wordsLength = fullSurahArabicWord[globalObject.ayahPosWordbWord].length;
        if (globalObject.wordPos == wordsLength - 1) // last word of this ayah
        {
            if (globalObject.ayahPosWordbWord == fullSurahArabicWord.length - 1) // if last ayah
            {
                nextWordButton.setImageResource(R.drawable.next_word_selector_file);
                previousWordButton.setImageResource(R.drawable.previous_h);
            } else {
                nextWordButton.setImageResource(R.drawable.play_h_w_back);
                previousWordButton.setImageResource(R.drawable.previous_h);
                globalObject.ayahPosWordbWord++;
                globalObject.wordPos = 0;

                setTextViews();

                highlightedWordForWbW = fullSurahArabicWord[globalObject.ayahPosWordbWord][globalObject.wordPos];
                wordAdapter.notifyDataSetChanged();
                wordsList.post(new Runnable() {

                    @Override
                    public void run() {
                        wordsList.setSelection(globalObject.ayahPosWordbWord);
                    }
                });

            }

        }
        else if (globalObject.wordPos < wordsLength - 1) {
            nextWordButton.setImageResource(R.drawable.play_h_w_back);
            previousWordButton.setImageResource(R.drawable.previous_h);
            globalObject.wordPos++;

            setTextViews();

            highlightedWordForWbW = fullSurahArabicWord[globalObject.ayahPosWordbWord][globalObject.wordPos];
            wordAdapter.notifyDataSetChanged();
            wordsList.post(new Runnable() {
                @Override
                public void run() {
                    wordsList.setSelection(globalObject.ayahPosWordbWord);
                }
            });

            if (globalObject.wordPos == wordsLength - 1 && globalObject.ayahPosWordbWord == fullSurahArabicWord.length - 1) // if last ayah
            {
                nextWordButton.setImageResource(R.drawable.next_word_selector_file);
                previousWordButton.setImageResource(R.drawable.previous_h);
            }

        }
    }


    private void playAudio() {
        if (play == 0 && globalObject.mediaPlayerFullSurah != null) {
            play = 1;

            if (rowClick) {
                rowClick = false;
                globalObject.mediaPlayerFullSurah.seekTo(timeAllSurah[globalObject.surahPos][delayIndex]);
                globalObject.mediaPlayerFullSurah.start();
                SurahDetailActivity.playButton.setImageResource(R.drawable.pause_selector_file);
                fullSurahList.setSelection(delayIndex);
                delayIndex++;
            } else {
                if (currentPlayingPosition > 0) {
                    globalObject.mediaPlayerFullSurah.seekTo(currentPlayingPosition);
                }
                if (firstTimeAudio) {
                    globalObject.mediaPlayerFullSurah.seekTo(timeAllSurah[globalObject.surahPos][delayIndex]);
                    fullSurahList.setSelection(delayIndex);
                    firstTimeAudio = false;
                }
                globalObject.mediaPlayerFullSurah.start();
                SurahDetailActivity.playButton.setImageResource(R.drawable.pause_selector_file);
            }

            handler.removeCallbacks(sendUpdatesToUI);
            handler.postDelayed(sendUpdatesToUI, 0);
        } else {
            handler.removeCallbacks(sendUpdatesToUI);

            if (globalObject.mediaPlayerFullSurah.isPlaying()) {
                globalObject.mediaPlayerFullSurah.pause();
                SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
                currentPlayingPosition = globalObject.mediaPlayerFullSurah.getCurrentPosition();
                rowClick = false;
                firstTimeAudio = false;
            }

            play = 0;

        }
    }

    private void initializeNextWordAudio() {
        String audioName;
        if (globalObject.ayahPosWordbWord == 0) {
            audioName = "bismillah_" + globalObject.ayahPosWordbWord + "_" + globalObject.wordPos;
            if(globalObject.wordPos==0){
                nextWordButton.setImageResource(R.drawable.play_h_w_back); // /////
                previousWordButton.setImageResource(R.drawable.previous_word_selector_file);


            }
            else{
               nextWordButton.setImageResource(R.drawable.play_h_w_back); // /////
                previousWordButton.setImageResource(R.drawable.previous_h);
            }
        } else {
            audioName = "surah_" + (globalObject.surahPos + 1) + "_" + globalObject.ayahPosWordbWord + "_" + (globalObject.wordPos);
        }

        String path = (Environment.getExternalStorageDirectory()).toString();
        try {
            if (globalObject.mediaPlayerWordByWord != null) {
                globalObject.mediaPlayerWordByWord.release();
                globalObject.mediaPlayerWordByWord = null;
            }

            globalObject.mediaPlayerWordByWord = new MediaPlayer();
            globalObject.mediaPlayerWordByWord.setDataSource(path + "/QuranNow/WBW/" + audioName + ".mp3");
            globalObject.mediaPlayerWordByWord.prepare();
        } catch (Exception e) {

            e.printStackTrace();
        }
        globalObject.mediaPlayerWordByWord.start();

        // word tapping n single word play shuld not effect footer
        if (!isWordTapped && !isSingleWordPlaying) {
                SurahDetailActivity.playButton.setImageResource(R.drawable.pause_selector_file);
            if(currentPlayingPosition > 0&&currentPlayingPosition!=fullSurahArabicWord.length) {

                nextWordButton.setImageResource(R.drawable.play_h_w_back); // /////
                previousWordButton.setImageResource(R.drawable.previous_h);
            }// /////


            playWordBtn.setImageResource(R.drawable.speaker_h);
        } else {
            // during play check for play button
            if (isFullAudioPlaying) {
                SurahDetailActivity.playButton.setImageResource(R.drawable.pause_selector_file);
            } else {
                SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
            }

            playWordBtn.setImageResource(R.drawable.speaker_selector_file);

            if (globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) // if 1st word
            {
                previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
                nextWordButton.setImageResource(R.drawable.play_h_w_back);//changed
            } else {
                previousWordButton.setImageResource(R.drawable.previous_h);
            }

            int wordsLength = fullSurahArabicWord[globalObject.ayahPosWordbWord].length;
            if (globalObject.ayahPosWordbWord == fullSurahArabicWord.length - 1 && globalObject.wordPos == wordsLength - 1) // if last word of last ayay
            {
                // nextWordButton.setImageResource(R.drawable.next_h);//enable previous btn
                previousWordButton.setImageResource(R.drawable.previous_h);
            } else {
                nextWordButton.setImageResource(R.drawable.play_h_w_back);//new changed
            }
        }

        globalObject.isPlayingWordByWord = true;
        setTextViews();
        highlightedWordForWbW = fullSurahArabicWord[globalObject.ayahPosWordbWord][globalObject.wordPos];

        wordsList.post(new Runnable() {

            @Override
            public void run() {
                wordsList.setSelection(globalObject.ayahPosWordbWord);
            }
        });

        wordAdapter.notifyDataSetChanged();

        globalObject.mediaPlayerWordByWord.setOnCompletionListener(this);
        isWordTapped = false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

        if (!isFullAudioPlaying) {
            globalObject.mediaPlayerWordByWord.release();
            globalObject.mediaPlayerWordByWord = null;

        } else {
            int lengthofAya = fullSurahArabicWord[globalObject.ayahPosWordbWord].length;
            if (globalObject.wordPos < (lengthofAya - 1)) {
                globalObject.wordPos++;
                initializeNextWordAudio();
            } else // if last word of aya
            {
                if (globalObject.ayahPosWordbWord < ((fullSurahArabicWord.length) - 1)) {
                    globalObject.ayahPosWordbWord++;
                    globalObject.wordPos = 0;
                    initializeNextWordAudio();
                } else // if last ayah
                {
                    resetWordByWordValues();
                    ((SurahDetailActivity) activitydetail).resetWordMediaPlayer();
                    SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);

                }

            }
        }
        if (globalObject.selectedTabPosition == 1) // for tab change during completeion of audio
        {
            if (globalObject.mediaPlayerWordByWord != null) {
                if (globalObject.mediaPlayerWordByWord.isPlaying()) {
                    globalObject.mediaPlayerWordByWord.pause();
                    globalObject.isPlayingWordByWord = false;
                    SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
                }
            }
        }
    }

    private boolean downloadAudios(String audioFile) {
        try {
            if (!Constants.rootPathWBW.exists()) {
                Constants.rootPathWBW.mkdirs();
            }

            File myFile = new File(Constants.rootPathWBW.getAbsolutePath(), audioFile + ".mp3");

            if (myFile.exists()) {
                return  true;
            } else {
                Intent downloadDialog = new Intent(activitydetail, DownloadDialogWBW.class);
                startActivity(downloadDialog);
            }

        } catch (Exception e) {
            Log.e("File", e.toString());
        }
        return false;
    }

    private void wordBywordAudioInitialization() {
        if (audioShuldBeNull) {
            if (globalObject.mediaPlayerWordByWord != null) {
                globalObject.mediaPlayerWordByWord.release();
                globalObject.mediaPlayerWordByWord = null;
            }
            audioShuldBeNull = false;
        }

        if (globalObject.mediaPlayerWordByWord == null) {
            globalObject.mediaPlayerWordByWord = new MediaPlayer();
            initializeNextWordAudio();

        } else {
            if (globalObject.isPlayingWordByWord) {
                globalObject.mediaPlayerWordByWord.pause();
                globalObject.isPlayingWordByWord = false;

                nextWordButton.setImageResource(R.drawable.next_word_selector_file); // /////
                previousWordButton.setImageResource(R.drawable.previous_word_selector_file); // /////
                playWordBtn.setImageResource(R.drawable.speaker_selector_file);

                SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
                isFullAudioPlaying = false;

                if (globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) // if 1st word
                {
                    previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
                } else {
                   //uncomment previousWordButton.setImageResource(R.drawable.previous_h);
                }

                int wordsLength = fullSurahArabicWord[globalObject.ayahPosWordbWord].length;
                if (globalObject.ayahPosWordbWord == fullSurahArabicWord.length - 1 && globalObject.wordPos == wordsLength - 1) // if last
                // word of
                // last ayay
                {
                    nextWordButton.setImageResource(R.drawable.next_word_selector_file);
                } else {
                    nextWordButton.setImageResource(R.drawable.play_h_w_back);
                }
                if(!(globalObject.ayahPosWordbWord == 0 && globalObject.wordPos == 0) && !(globalObject.ayahPosWordbWord == (fullSurahArabicWord.length - 1) && globalObject.wordPos == fullSurahArabicWord[globalObject.ayahPosWordbWord].length - 1))
                {
                    nextWordButton.setImageResource(R.drawable.play_h_w_back);
                    previousWordButton.setImageResource(R.drawable.previous_h);
                }

            } else {
                String audioName;
                if (globalObject.ayahPosWordbWord == 0) {
                    audioName = "bismillah_" + globalObject.ayahPosWordbWord + "_" + globalObject.wordPos;
                } else {
                    audioName = "surah_" + (globalObject.surahPos + 1) + "_" + globalObject.ayahPosWordbWord + "_" + (globalObject.wordPos);
                }

                String path = (Environment.getExternalStorageDirectory()).toString();
                try {
                    if (globalObject.mediaPlayerWordByWord != null) {
                        globalObject.mediaPlayerWordByWord.release();
                        globalObject.mediaPlayerWordByWord = null;
                    }
                    globalObject.mediaPlayerWordByWord = new MediaPlayer();
                    globalObject.mediaPlayerWordByWord.setDataSource(path + "QuranNow/WBW/" + audioName + ".mp3");
                    globalObject.mediaPlayerWordByWord.prepare();
                    //initAudioFile(audioName);
                } catch (Exception e) {

                    e.printStackTrace();
                }

                SurahDetailActivity.playButton.setImageResource(R.drawable.pause_selector_file);
                globalObject.mediaPlayerWordByWord.start();
                globalObject.isPlayingWordByWord = true;
                nextWordButton.setImageResource(R.drawable.play_h_w_back);
                previousWordButton.setImageResource(R.drawable.previous_h);
                playWordBtn.setImageResource(R.drawable.speaker_h);
                SurahDetailActivity.playButton.setImageResource(R.drawable.pause_selector_file);
                isFullAudioPlaying = true;
                globalObject.mediaPlayerWordByWord.setOnCompletionListener(this);
            }
        }
    }

    private void fullSuraAudioInitialization() {

        try {
            int audioPos = globalObject.surahPos + 1;
            String audioName = "fullsurah_" + audioPos;

            String path = (Environment.getExternalStorageDirectory()).toString();

            if (globalObject.mediaPlayerFullSurah != null) {
                globalObject.mediaPlayerFullSurah.release();
                globalObject.mediaPlayerFullSurah = null;
            }
            globalObject.mediaPlayerFullSurah = new MediaPlayer();
            globalObject.mediaPlayerFullSurah.setDataSource(path + "/AlifIslam/Surahs/" + audioName + ".mp3");
            globalObject.mediaPlayerFullSurah.prepare();

            globalObject.mediaPlayerFullSurah.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
                    resetFullSuraValues();
                    ((SurahDetailActivity) activitydetail).resetFullSuraMediaPlayer();
                }
            });
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            resetFullSuraValues();
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            resetFullSuraValues();
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            resetFullSuraValues();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            resetFullSuraValues();
            e.printStackTrace();
        }
    }

    private final void focusOnView() {
        // List Item Highlight
        if (globalObject.mediaPlayerFullSurah != null) {
            if (globalObject.mediaPlayerFullSurah.getCurrentPosition() >= timeAyahSurah[globalObject.ayahPosFullSura][verse_length]) {
                Log.e("ENTER MAIN POSITION", "" + delayIndex);
                globalObject.ayahPosFullSura = delayIndex;
                currentIndex = 0; // For Highlight Word on next Row/Ayah
                delayIndexWords = 0;
                lastIndex = 0;
                chkRowLastIndex = true;
                setWordtoHighlight();
                highlightedWordForFullSura = verse_words[currentIndex];
                //fullSurahAdapter.notifyDataSetChanged();
                setListScrollPosition(globalObject.ayahPosFullSura);
                delayIndex++;
            }

            // To Highlight last word of every list item
            if (currentIndex == verse_length && chkRowLastIndex) {
                highlightedWordForFullSura = verse_words[currentIndex - 1];
                //fullSurahAdapter.notifyDataSetChanged();
                chkRowLastIndex = false;
            } else {
                // Highlight Word in List
                if (globalObject.mediaPlayerFullSurah.getCurrentPosition() >= timeAyahSurah[globalObject.ayahPosFullSura][delayIndexWords] && currentIndex < verse_length) {
                    // Log.e(String.valueOf(globalObject.mediaPlayerFullSurah.getCurrentPosition()), String.valueOf(timeAyahSurah[globalObject.ayahPosFullSura][delayIndexWords]) + "-----" + String.valueOf(delayIndexWords));
                    highlightedWordForFullSura = verse_words[currentIndex];
                    //fullSurahAdapter.notifyDataSetChanged();
                    fullSurahList.setSelection(globalObject.ayahPosFullSura);

                    currentIndex++;
                    delayIndexWords++;
                }
            }
        }
    }

    private void setWordtoHighlight() {
        verse_words = surahAyahData.get(globalObject.ayahPosFullSura).getArabicText().split(" ");
        verse_length = verse_words.length;
    }

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {

            try {
                focusOnView();
                handler.postDelayed(this, 0);
            } catch (Exception e) {
                resetFullSuraValues();
                e.printStackTrace();
            }
        }
    };

    private void setListScrollPosition(final int position) {
        fullSurahList.setSelection(position);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        activitydetail.unregisterReceiver(checkTapPosition);
        activitydetail.unregisterReceiver(PlayAudioBroadcastSurah);
        activitydetail.unregisterReceiver(wordTapBroadcast);
        activitydetail.unregisterReceiver(PageChangeBroadcastSura);
        activitydetail.unregisterReceiver(StopAudioBroadcastSura);

    }

    private void setTextViews() {
        ayahArabic.setText(fullSurahArabicWord[globalObject.ayahPosWordbWord][globalObject.wordPos]);
        ayahTranslationTv.setText(fullSurahTranslationWords[globalObject.ayahPosWordbWord][globalObject.wordPos]);
        ayahTransliterationTv.setText(fullSurahTransliterationWords[globalObject.ayahPosWordbWord][globalObject.wordPos]);
    }

    @Override
    public void onPause() {

        super.onPause();
        if (SurahDetailActivity.ISBACKPRESSED) {
            resetFullSuraValues();
            resetWordByWordValues();
            SurahDetailActivity.ISBACKPRESSED = false;
        }

    }

    private void resetFullSuraValues() {
        if (globalObject.mediaPlayerFullSurah != null) {
            globalObject.mediaPlayerFullSurah.release();
            globalObject.mediaPlayerFullSurah = null;
        }

        SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
        handler.removeCallbacks(sendUpdatesToUI);

        globalObject.ayahPosFullSura = 0;
        currentIndex = 0;
        lastIndex = 0;
        delayIndex = 0;
        delayIndexWords = 0;
        verse_length = 0;
        verse_words = null;
        currentPlayingPosition = 0;

        setWordtoHighlight();
        highlightedWordForFullSura = verse_words[0];
        /*if (fullSurahAdapter != null) {
			fullSurahAdapter.notifyDataSetChanged();
		}*/
        fullSurahList.setSelection(0);
        play = 0;
        firstTimeAudio = true;
        rowClick = false;
    }

    private void resetWordByWordValues() {
		/*
		 * if(globalObject.mediaPlayerWordByWord!=null) globalObject.mediaPlayerWordByWord.release();
		 */
        globalObject.ayahPosWordbWord = 0;
        globalObject.wordPos = 0;
        isWordTapped = false;
        globalObject.isPlayingWordByWord = false;
        isFullAudioPlaying = false;
        currentPlayingPosition = 0;

        highlightedWordForWbW = fullSurahArabicWord[0][0];
        if (wordAdapter != null)
            wordAdapter.notifyDataSetChanged();

        wordsList.setSelection(0);
        setTextViews();
        previousWordButton.setImageResource(R.drawable.previous_word_selector_file);
        nextWordButton.setImageResource(R.drawable.play_h_w_back);


        playWordBtn.setImageResource(R.drawable.speaker_selector_file);
        SurahDetailActivity.playButton.setImageResource(R.drawable.play_selector_file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestDownload && resultCode == Activity.RESULT_OK) {
            if (selectedSuraPos == globalObject.surahPos) {
                if (globalObject.selectedTabPosition == 1) {
                    if (globalObject.mediaPlayerFullSurah == null) {
                        fullSuraAudioInitialization();
                        playAudio();
                    } else {
                        playAudio();
                    }
                } else // if in WordByWord fragment
                {
                    isFullAudioPlaying = true;
                    wordBywordAudioInitialization();
                }
            }
        }
    }
}
