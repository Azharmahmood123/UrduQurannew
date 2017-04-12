package com.packageapp.quranvocabulary.quiz;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.packageapp.quranvocabulary.MainActivity;

/**
 * Created by cyber on 1/27/2016.
 */
public class ResultFragment extends Fragment {

    TextView textView, backTxt, quizTxt;
    String text1 = " out of 15 answers were correct.\n";
    String text2 = " answers were wrong.";
    GlobalClass mGlobal;
   private Activity mActivity;
    private long stopClick=0;
    int j;
    private ImageView quizIcon,homeIcon;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_quizresult, container, false);
        Bundle bundle = this.getArguments();
        int correct = bundle.getInt("correct");
        int wrong = 15 - correct;
        mGlobal=(GlobalClass)mActivity.getApplication();

        textView = (TextView) rootView.findViewById(R.id.result_txt2);
        backTxt = (TextView) rootView.findViewById(R.id.go_back_txt);
        quizTxt = (TextView) rootView.findViewById(R.id.take_quiz_txt);
        quizIcon= (ImageView) mActivity.findViewById(R.id.toolbar_Quiz);
        homeIcon= (ImageView) mActivity.findViewById(R.id.btnHomeVocab);
        backTxt.setOnClickListener(listener);
        quizTxt.setOnClickListener(listener);

        textView.setText(correct+text1+wrong+text2);
        return rootView;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(SystemClock.elapsedRealtime()-stopClick<1000)
            {
               return;
            }
            stopClick=SystemClock.elapsedRealtime();

            switch (v.getId()) {
                case R.id.go_back_txt:
                    if(mGlobal.isResultFragment)
                    {
                        //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        //fm.popBackStack();
                        getActivity().getSupportFragmentManager().popBackStack();
                      //  getActivity().getSupportFragmentManager().popBackStack();
                    }

                   /* getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();*/
                    break;
                case R.id.take_quiz_txt:
                   // getActivity().getSupportFragmentManager().popBackStack();
                   // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                   // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    j=getActivity().getSupportFragmentManager().getBackStackEntryCount();
                    for(int i=0;i<j-1;i++)
                    {

                        getActivity().getSupportFragmentManager().popBackStack();

                    }
                   // getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.container, new QuizFragment()).addToBackStack(null)
                            .commit();
                  //  MainActivity.navigationDrawer=2;
                  //  MainActivity.mAdapter.notifyDataSetChanged();
                  /*  if (!mGlobal.isPurchase) {
                        InterstitialAdSingleton mInterstitialAdSingleton = InterstitialAdSingleton.getInstance(getActivity());
                        mInterstitialAdSingleton.showInterstitial();
                    }*/
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        quizIcon.setVisibility(View.GONE);
        homeIcon.setVisibility(View.GONE);
        mGlobal.isResultFragment=true;
    }

    @Override
    public void onStop() {
        super.onStop();
        mGlobal.isResultFragment=false;
    }
}
