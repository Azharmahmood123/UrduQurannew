package com.packageapp.quranvocabulary.quiz;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.packageapp.quranvocabulary.MainActivity;
import com.packageapp.quranvocabulary.adapters.QuizAdapter;
import com.packageapp.quranvocabulary.generalhelpers.DBManager;
import com.packageapp.quranvocabulary.models.QuizModel;
import com.packageapp.quranvocabulary.models.QuizOptions;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by cyber on 1/21/2016.
 */
public class QuizFragment extends Fragment implements QuizInterface {

    public LockableViewPager viewPager;
    private QuizAdapter adapter;
    private DBManager dbManager;
    ArrayList<QuizModel> questions;
    ArrayList<QuizOptions> quizOptions;
    public int quizPos;
    QuizModel model;
    Activity mActivity;
    private long stopClick = 0;
    private int pagePos = -1;
    View rootView;
    ViewHolder holder;
    int[] allIds;
    int questionIds[];
    private boolean dublicateId=false;
    private RelativeLayout relCalibaration;
    TextView toolbarTitle;
    private ImageView quizIcon,homeIcon;
    GlobalClass mGlobal;
    AnalyticSingaltonClass mAnalyticSingaltonClass;



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mGlobal= (GlobalClass) activity.getApplicationContext();
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbarTitle.setText("Quiz");
        toolbarTitle.setVisibility(View.VISIBLE);
        quizIcon.setVisibility(View.GONE);
        homeIcon.setVisibility(View.GONE);
        //   MainActivity.navigationDrawer = 2;
        //  MainActivity.mAdapter.notifyDataSetChanged();
       /* if (MainActivity.mAdapter != null) {

        }*/
        if( relCalibaration.getVisibility()==View.VISIBLE)
        {
            relCalibaration.setVisibility(View.GONE);

        }
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
            holder = new ViewHolder();
            toolbarTitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
            quizIcon=(ImageView) getActivity().findViewById(R.id.toolbar_Quiz);
            homeIcon= (ImageView) getActivity().findViewById(R.id.btnHomeVocab);
            setToolbar();
            viewPager = (LockableViewPager) rootView.findViewById(R.id.quiz_viewpager);
            relCalibaration= (RelativeLayout) getActivity().findViewById(R.id.calibrationlayout);
            fetchRecord();

            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();

        }
        mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(getActivity());
        mAnalyticSingaltonClass.sendScreenAnalytics("Vocab_Quiz_Screen");
        setPagerAdapter();


        return rootView;
    }

    private class ViewHolder {

    }

    public void setPagerAdapter() {


        adapter = new QuizAdapter(getActivity(), this, questions, quizOptions);
        viewPager.setAdapter(adapter);
        quizPos = 0;

      /*  viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                quizPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setPagingEnabled(false);
    }


    public void fetchRecord() {
        try {


            questionIds = new int[15];
            allIds = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326, 327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360, 361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394, 395, 396, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428, 429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495, 496, 497, 498, 499, 500, 501, 502, 503, 504, 505, 506, 507, 508, 509, 510, 511, 512, 513, 514, 515, 516, 517, 518, 519, 520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533, 534, 535, 536, 537, 538, 539, 540, 541, 542, 543, 544, 545, 546, 547, 548, 549, 550, 551, 552, 553, 554, 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578};
          /*  if (result != null) {
                result.moveToFirst();
                while (!result.isAfterLast()) {
                    allIds.add(result.getInt(0));
                    result.moveToNext();
                }
                result.close();*/

            int j = 0;
            while (j < 15) {

                int id = (new Random().nextInt(allIds.length));
                if (isDuplicateId(id)==false) {
                    questionIds[j] = allIds[id];
                    j++;
                }
            }

            dbManager = new DBManager(mActivity);

            // Cursor result = dbManager.getAllIds();
            if (questions != null) {
                questions.clear();

            }
            Cursor result1 = dbManager.getQuizWords(questionIds);
            questions = new ArrayList<>();

            result1.moveToFirst();
            while (!result1.isAfterLast()) {
                if (model != null) {
                    model = null;
                }
                model = new QuizModel();
                model.setWordId(result1.getInt(0));
                model.setCategoryId(result1.getInt(1));
                model.setUrduMeaning(result1.getString(2));
                model.setEngMeaning(result1.getString(3));
                model.setArabicWord(result1.getString(4));
                questions.add(model);
                result1.moveToNext();
            }
            result1.close();
            if(questions.size()<15)
            {
                fetchRecord();// call again if size is less
            }
            if (quizOptions != null) {
                quizOptions.clear();
            }
            quizOptions = new ArrayList<>();

            for (int i = 0; i < questions.size(); i++) {
                QuizOptions optionModel = new QuizOptions();
                Random random = new Random();
                boolean optionSet;
                int value = i;

                optionModel.setOption1(value);

                do {
                    value = random.nextInt(15);// here i changed 15 to 14
                    optionSet = false;
                    if (value != optionModel.getOption1()) {
                        optionSet = true;
                        optionModel.setOption2(value);
                    }
                } while (!optionSet);

                do {
                    value = random.nextInt(15);// changed 15 to 14
                    optionSet = false;
                    if ((value != optionModel.getOption1()) && (value != optionModel.getOption2())) {
                        optionSet = true;
                        optionModel.setOption3(value);
                    }
                } while (!optionSet);

                do {
                    value = random.nextInt(15);// changed 15 to 14
                    optionSet = false;
                    if ((value != optionModel.getOption1()) && (value != optionModel.getOption2()) && (value != optionModel.getOption3())) {
                        optionSet = true;
                        optionModel.setOption4(value);
                    }
                } while (!optionSet);

                quizOptions.add(optionModel);
            }
        } catch (Exception ex) {

        }
    }
    private boolean isDuplicateId(int idValue)
    {
        for(int i=0;i<questionIds.length;i++)
        {
            if(idValue==questionIds[i])
            {
                // dublicateId=false;
                return true;
            }
        }

        return  false;
    }

    @TargetApi(11)
    public void setToolbar() {
        try {
            MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
            getActivity().invalidateOptionsMenu();
        } catch (Exception ex) {
        }
    }

    @Override
    public void quizOptionSelected(int position, int correct) {
        if (SystemClock.elapsedRealtime()-stopClick < 1000) {
            return;
        }
        stopClick = SystemClock.elapsedRealtime();
        if (++position != questions.size()) { //  ++position != questions.size()
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt("correct", correct);
            int j=0;
            j=getActivity().getSupportFragmentManager().getBackStackEntryCount();
            for(int i=0;i<j-1;i++)
            {

                getActivity().getSupportFragmentManager().popBackStack();

            }

            ResultFragment fragment = new ResultFragment();
            fragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
           /* if(!mGlobal.isPurchase)
            {
                InterstitialAdSingleton.getInstance(mActivity).showInterstitial();

            }*/
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (quizOptions != null) {
            quizOptions.clear();
        }
        if (questions != null) {
            questions.clear();
        }
        adapter=null;
    }
}
