package com.packageapp.quranvocabulary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.quranvocabulary.adapters.DetailAdapter;
import com.packageapp.quranvocabulary.generalhelpers.DBManager;
import com.packageapp.quranvocabulary.models.CategoryDetail;

import java.util.ArrayList;

/**
 * Created by cyber on 1/20/2016.
 */
public class DetailFragment extends Fragment implements AbsListView.OnScrollListener    {

    private ListView listView;
    private ArrayList<CategoryDetail> catDetailList;
    private int catId;
    private String catName;
    private DetailAdapter detailAdapter;
    private ArrayList<Integer> clickedItem;
    SettingsSharedPref prefs;
    Activity mActivity;
    RelativeLayout relCalibaration;
    Button btnCalibarationOk;
    private long stopBackClick=0;
    FragmentManager fm;
    private long stopClickIcon=0;
    SettingsSharedPref sharedPref;
    Toolbar toolbar;
    ImageView QuizIcon,homeIcon;
    TextView toolbarTitle;
    ImageView toolbarIcon;
    GlobalClass mGlobal;
    AnalyticSingaltonClass mAnalyticSingaltonClass;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;
        mGlobal= (GlobalClass) activity.getApplicationContext();

    }



    @Nullable
    @Override
    @SuppressLint("NewApi")
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(getActivity());
        mAnalyticSingaltonClass.sendScreenAnalytics("Vocab_Detail_Screen");

        listView = (ListView) rootView.findViewById(R.id.main_listview);
        relCalibaration= (RelativeLayout) mActivity.findViewById(R.id.calibrationlayout);
        btnCalibarationOk= (Button) mActivity.findViewById(R.id.ok);
        sharedPref = new SettingsSharedPref(mActivity);
        toolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        QuizIcon = (ImageView) mActivity.findViewById(R.id.toolbar_Quiz);
        homeIcon = (ImageView) mActivity.findViewById(R.id.btnHomeVocab);
        toolbarTitle = (TextView) mActivity.findViewById(R.id.toolbar_title);
        toolbarIcon = (ImageView) mActivity.findViewById(R.id.toolbar_icon);
        prefs=new SettingsSharedPref(mActivity);
        AnalyticSingaltonClass mAnalyticSingaltonClass = AnalyticSingaltonClass.getInstance(mActivity);
        mAnalyticSingaltonClass.sendScreenAnalytics("Detail Screen");

        Bundle bundle = this.getArguments();
        catId = bundle.getInt("id", 0);
        catName = bundle.getString("cat_name");
        GlobalClass.isDetailedScreen = true;
        listView.setOnScrollListener(this);
        fm=getActivity().getSupportFragmentManager();//
        fetchCatDetail(catId);
        boolean calibarationValue = prefs.getCalibrationValue();
        if (calibarationValue) {
            relCalibaration.setVisibility(View.VISIBLE);
        }
        btnCalibarationOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relCalibaration.setVisibility(View.GONE);
                toolbar.setVisibility(View.VISIBLE);
                prefs.calibrationValueQuranVocabulary(false);
            }
        });

        toolbarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SystemClock.elapsedRealtime()-stopClickIcon<1000)
                {
                    return;

                }
                stopClickIcon=SystemClock.elapsedRealtime();
                AnalyticSingaltonClass mAnalyticSingaltonClass = AnalyticSingaltonClass.getInstance(getActivity());
                mAnalyticSingaltonClass.sendEventAnalytics("Detail Screen","Learn Mode On");
                if (sharedPref.getDetailQuranVocabulary()) {
                    toolbarIcon.setImageResource(R.drawable.unvisible);
                    sharedPref.setDetailQuranVocabulary(false);
                    if(detailAdapter!=null)
                    {
                        detailAdapter.pauseMedia();
                    }
                    clickedItem.clear();
                }else {
                    toolbarIcon.setImageResource(R.drawable.visible);
                    sharedPref.setDetailQuranVocabulary(true);
                }
                if(detailAdapter!=null)
                {
                    detailAdapter.pauseMedia();
                }
                if (detailAdapter != null) {
                    detailAdapter.notifyDataSetChanged();
                }
            }
        });



        return rootView;
    }


    public void fetchCatDetail(int catId) {
        try {
            DBManager dbManager = new DBManager(getActivity());
            Cursor result = dbManager.getCatDetail(catId);
            if (result != null) {
                int i = 0;
                catDetailList = new ArrayList<>();
                result.moveToFirst();
                while (!result.isAfterLast()) {
                    CategoryDetail catData = new CategoryDetail();
                    catData.setId(result.getInt(0));
                    catData.setCatId(result.getInt(1));
                    catData.setUrduWord(result.getString(2));
                    catData.setEngWord(result.getString(3));
                    catData.setArabicWord(result.getString(4));
                    catDetailList.add(catData);
                    result.moveToNext();
                }

                clickedItem = new ArrayList<>();
                detailAdapter = new DetailAdapter(getActivity(), R.layout.index_layout, catDetailList, clickedItem);
                listView.setAdapter(detailAdapter);
                listView.setOnItemClickListener(itemClickListener);
            }
        }catch (Exception ex) {
            Log.d("exception", "" + ex.getMessage());
        }
    }

    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (!clickedItem.contains(position)) {
                clickedItem.add(position);
            }else {
                clickedItem.remove(clickedItem.indexOf(position));
            }
            detailAdapter.pauseMedia();
            detailAdapter.notifyDataSetChanged();
        }
    };



    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        // detailAdapter.notifyDataSetChanged();
        detailAdapter.pauseMediaForce();

    }

    @Override

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //DetailAdapter.pauseMedia();
    }
    @SuppressLint("NewApi")
    public void setToolbar(){
        try {
            //backIcon.setVisibility(View.VISIBLE);
            toolbarIcon.setVisibility(View.VISIBLE);
            toolbarTitle.setText(catName);
            toolbar.setSelected(true);
            toolbarTitle.setVisibility(View.VISIBLE);

            if (sharedPref.getDetailQuranVocabulary()) {
                toolbarIcon.setImageResource(R.drawable.visible);
            }else {
                toolbarIcon.setImageResource(R.drawable.unvisible);
            }
            QuizIcon.setVisibility(View.GONE);
            homeIcon.setVisibility(View.GONE);

            //   MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
            //  getActivity().invalidateOptionsMenu();
        }catch (Exception ex){}
    }

    @Override

    public void onPause() {
        super.onPause();
        if(detailAdapter!=null)
        {
            detailAdapter.pauseMediaForce();

        }
        mGlobal.isDialogueAppear=false;



    }

    @Override
    @SuppressLint("NewApi")
    public void onResume() {
        super.onResume();
        QuizIcon.setVisibility(View.GONE);
        homeIcon.setVisibility(View.GONE);
        // MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(false);
        //  mActivity.invalidateOptionsMenu();
        //  MainActivity.Drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setToolbar();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    //  MainActivity.Drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);

                    toolbar.setVisibility(View.VISIBLE);
                    QuizIcon.setVisibility(View.VISIBLE);
                    homeIcon.setVisibility(View.VISIBLE);
                    toolbarIcon.setVisibility(View.GONE);
                    toolbarTitle.setText(R.string.app_name_QuranVocabulary);
                    if( relCalibaration.getVisibility()==View.VISIBLE)
                    {
                        relCalibaration.setVisibility(View.GONE);

                    }
                    //  MainActivity.mDrawerToggle.setDrawerIndicatorEnabled(true);
                    // mActivity.invalidateOptionsMenu();
                    if(fm.getBackStackEntryCount()>1)
                    {
                        fm.popBackStack();
                    }

                    //fm.popBackStack(null, fm.POP_BACK_STACK_INCLUSIVE);
                    if(detailAdapter!=null)
                    {
                        detailAdapter.pauseMedia();
                    }
                    GlobalClass.isDetailedScreen = false;

                    return true;
                }
                return false;
            }
        });


    }

}
