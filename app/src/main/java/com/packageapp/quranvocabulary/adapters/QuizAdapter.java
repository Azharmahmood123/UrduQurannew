package com.packageapp.quranvocabulary.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ArabicText.Helper.ArabicUtilities;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;
import com.packageapp.quranvocabulary.models.QuizModel;
import com.packageapp.quranvocabulary.models.QuizOptions;
import com.packageapp.quranvocabulary.quiz.LockableViewPager;
import com.packageapp.quranvocabulary.quiz.QuizInterface;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by cyber on 1/21/2016.
 */
public class QuizAdapter extends PagerAdapter {

    private Context mContext;
    private QuizInterface quizInterface;
    private ArrayList<QuizModel> quizQuestions=new ArrayList<QuizModel>();
    private ArrayList<QuizOptions> quizOptions=new ArrayList<QuizOptions>();
    private int correctAnswers = 0;
    private SettingsSharedPref sharedPref;
    private long stopOption1=0,stopOption2=0,stopOption3=0,stopOption4=0;
    private ArrayList<Integer> sizeArray;
    GlobalClass mGlobal;

//    private TextView option1, option2, option3, option4;
    public QuizAdapter(Context context, QuizInterface quizInterface,
                       ArrayList<QuizModel> quizQuestions, ArrayList<QuizOptions> options) {
        this.mContext = context;
        this.quizInterface = quizInterface;
        this.quizQuestions = quizQuestions;
        this.quizOptions = options;
        mGlobal=(GlobalClass)context.getApplicationContext();
    }

    @Override
    public int getCount() {
        return quizQuestions.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /*  @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_quiz, container, false);

        sharedPref = new SettingsSharedPref(mContext);
        TextView arabicWord = (TextView) view.findViewById(R.id.quiz_word);
        if (quizQuestions.get(position).getArabicWord().length() > 15) {
            String[] split = quizQuestions.get(position).getArabicWord().split(" ");
           // arabicWord.setTypeface(mGlobal.faceArabic);
            arabicWord.setText((split[0].trim()));
        }else {
            arabicWord.setText(quizQuestions.get(position).getArabicWord());
        }
        final int arabicId = quizQuestions.get(position).getWordId();
        final int optionsId[] = new int[4];

        final TextView option1 = (TextView) view.findViewById(R.id.quiz_option1);
        final TextView option2 = (TextView) view.findViewById(R.id.quiz_option2);
        final TextView option3 = (TextView) view.findViewById(R.id.quiz_option3);
        final TextView option4 = (TextView) view.findViewById(R.id.quiz_option4);

        ////////////
        try {
            Random random = new Random();
            if (sizeArray != null) {
                sizeArray = null;
            }
            sizeArray = new ArrayList<>();
            sizeArray.add(1);
            sizeArray.add(2);
            sizeArray.add(3);
            sizeArray.add(4);
            for (int i = 0; i < 4; i++) {
                int val = sizeArray.get(random.nextInt(sizeArray.size()));
                sizeArray.remove(sizeArray.indexOf(val));
                switch (val) {
                    case 1:
                        switch (i + 1) {
                            case 1:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getEngMeaning());
                                } else {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption1()).getWordId();
                                break;
                            case 2:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getEngMeaning());
                                } else {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption1()).getWordId();
                                break;
                            case 3:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getEngMeaning());
                                } else {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption1()).getWordId();
                                break;
                            case 4:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getEngMeaning());
                                } else {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption1()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption1()).getWordId();
                                break;
                        }
                        break;
                    case 2:
                        switch (i + 1) {
                            case 1:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getEngMeaning());
                                } else {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption2()).getWordId();
                                break;
                            case 2:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getEngMeaning());
                                } else {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption2()).getWordId();
                                break;
                            case 3:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getEngMeaning());
                                } else {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption2()).getWordId();
                                break;
                            case 4:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getEngMeaning());
                                } else {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption2()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption2()).getWordId();
                                break;
                        }
                        break;
                    case 3:
                        switch (i + 1) {
                            case 1:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getEngMeaning());
                                } else {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                            case 2:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getEngMeaning());
                                } else {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                            case 3:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getEngMeaning());
                                } else {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                            case 4:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getEngMeaning());
                                } else {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption3()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                        }
                        break;
                    case 4:
                        switch (i + 1) {
                            case 1:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getEngMeaning());
                                } else {
                                    option1.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                            case 2:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getEngMeaning());
                                } else {
                                    option2.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                            case 3:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getEngMeaning());
                                } else {
                                    option3.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                            case 4:
                                if (sharedPref.getLanguageQuranVocabulary() != 0) {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getEngMeaning());
                                } else {
                                    option4.setText(quizQuestions.get(quizOptions.get(position).getOption4()).getUrduMeaning());
                                }
                                optionsId[i] = quizQuestions.get(quizOptions.get(position).getOption3()).getWordId();
                                break;
                        }
                        break;
                }
            }

            TextView quizNumber = (TextView) view.findViewById(R.id.quiz_number);
            String quizNum = "Question " + (position + 1) + " of " + quizQuestions.size();
            quizNumber.setText(quizNum);
            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - stopOption1 < 1000) {
                        return;
                    }
                    stopOption1 = SystemClock.elapsedRealtime();

                    if (arabicId == optionsId[0]) {
                        option1.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                        correctAnswers++;
                    } else {
                        option1.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    }

                    option1.setEnabled(false);
                    option2.setEnabled(false);
                    option3.setEnabled(false);
                    option4.setEnabled(false);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 1 second
                            quizInterface.quizOptionSelected(position, correctAnswers);
                        }
                    }, 1000);
                }
            });
            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - stopOption2 < 1000) {
                        return;
                    }
                    stopOption2 = SystemClock.elapsedRealtime();
                    if (arabicId == optionsId[1]) {
                        option2.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                        correctAnswers++;
                    } else {
                        option2.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    }

                    option1.setEnabled(false);
                    option2.setEnabled(false);
                    option3.setEnabled(false);
                    option4.setEnabled(false);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 1 second
                            quizInterface.quizOptionSelected(position, correctAnswers);
                        }
                    }, 1000);//changed 1000 to 500
                }
            });
            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - stopOption3 < 1000) {
                        return;
                    }
                    stopOption3 = SystemClock.elapsedRealtime();
                    if (arabicId == optionsId[2]) {
                        option3.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                        correctAnswers++;
                    } else {
                        option3.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    }
                    option1.setEnabled(false);
                    option2.setEnabled(false);
                    option3.setEnabled(false);
                    option4.setEnabled(false);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 1 second
                            quizInterface.quizOptionSelected(position, correctAnswers);
                        }
                    }, 1000);
                }
            });
            option4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SystemClock.elapsedRealtime() - stopOption4 < 1000) {
                        return;
                    }
                    stopOption4 = SystemClock.elapsedRealtime();
                    if (arabicId == optionsId[3]) {
                        option4.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                        correctAnswers++;
                    } else {
                        option4.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    }
                    option1.setEnabled(false);
                    option2.setEnabled(false);
                    option3.setEnabled(false);
                    option4.setEnabled(false);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            // Actions to do after 1 second
                            quizInterface.quizOptionSelected(position, correctAnswers);
                        }
                    }, 1000);
                }
            });

        /*if (position == 3 || position == 6 || position == 9 || position == 12 || position == 15) {
            if (!checkPurchase()) {
                InterstitialAdSingleton mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(mContext);
                mInterstitialAdSingleton.showInterstitial();
            }
        }*/

            ((LockableViewPager) container).addView(view);// commented today
        }
        catch ( Exception e)
        {

        }

        return view;
    }

    @Override  // cmmented today
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((LockableViewPager) container).removeView((LinearLayout) object);
    }


}
