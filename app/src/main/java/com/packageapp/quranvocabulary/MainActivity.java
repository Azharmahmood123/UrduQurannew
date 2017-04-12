package com.packageapp.quranvocabulary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.QuranReading.sharedPreference.PurchasePreferences;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.urduquran.ads.InterstitialAdSingleton;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.packageapp.quranvocabulary.adapters.IndexAdapter;
import com.packageapp.quranvocabulary.generalhelpers.DBManager;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;
import com.packageapp.quranvocabulary.models.Categories;
import com.packageapp.quranvocabulary.networkhelpers.DownloadDialogQuranVocabulary;
import com.packageapp.quranvocabulary.networkhelpers.DownloadUtilsQuranVocabulary;
import com.packageapp.quranvocabulary.notifications.DailyNotification;
import com.packageapp.quranvocabulary.quiz.QuizBackInterface;
import com.packageapp.quranvocabulary.quiz.QuizFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements QuizBackInterface {


    private DBManager dbManager;
    private static IndexAdapter engAdapter;
    public static Activity activity;


    String TITLES[];    // = {"Home", "Remove Ads", "Settings", "More Apps", "Share", "Rate Us", "About Us"};
    int ICONS[] = {R.drawable.home, R.drawable.quiz, R.drawable.languages, R.drawable.notification, R.drawable.donate_remove_add,
            R.drawable.feedback, R.drawable.about_us, R.drawable.more_apps, R.drawable.share};
    RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    public static DrawerLayout Drawer;
    public static ActionBarDrawerToggle mDrawerToggle;
    public static int navigationDrawer = 1;
    private static Toolbar mToolbar;
    private static Activity mActivity;
    private AdView adView;
    private AdRequest adRequest;
    GlobalClass mGlobal;
    public static TextView toolbarTitle;
    ImageView toolbarQuiz,toolbarHome;
    Button btnCalibarationOk;
    RelativeLayout relCalibaration;
    SettingsSharedPref prefs;
    private long stopClick = 0;
    Handler handler = new Handler();
    Runnable r;
    int j;
    private long stopQuizClick=0;
    AnalyticSingaltonClass mAnalyticSingaltonClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                       FacebookSdk.sdkInitialize(getApplicationContext());
                  }
        setContentView(R.layout.activity_main);
        prefs = new SettingsSharedPref(this);
        mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);
        mAnalyticSingaltonClass.sendScreenAnalytics("Vocab_Index_Screen");
        //mInterstitialAdSingleton=InterstitialAdSingleton.getInstance(this);
        mActivity = this;
        adView = (AdView) findViewById(R.id.adView);
        mGlobal = (GlobalClass) getApplication();
        activity = this;
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        toolbarQuiz = (ImageView) findViewById(R.id.toolbar_Quiz);
        relCalibaration = (RelativeLayout) findViewById(R.id.calibrationlayout);
        btnCalibarationOk = (Button) findViewById(R.id.ok);
        setToolbar();
        if (savedInstanceState == null) {

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new PlaceholderFragment()).addToBackStack(null)
                    .commit();
        }
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        setNotification();
        callNotification();
        toolbarQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SystemClock.elapsedRealtime()-stopQuizClick<1000)
                {
                    return;

                }
                stopQuizClick= SystemClock.elapsedRealtime();

                j = getSupportFragmentManager().getBackStackEntryCount();
                for (int i = 0; i < j - 1; i++) {

                    getSupportFragmentManager().popBackStack();

                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, new QuizFragment())
                        .addToBackStack(null)
                        .commit();

            }
        });
        btnCalibarationOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relCalibaration.setVisibility(View.GONE);
                prefs.calibrationValueQuranVocabulary(false);
            }
        });
    }

    public void onHomeClickVocab(View v)
    {
        this.finish();
    }

    private void callNotification() {
        Intent receiveIntent;

        receiveIntent = this.getIntent();
        int catId = receiveIntent.getIntExtra("cat_id", -1);
        String catName = receiveIntent.getStringExtra("cat_name");
        if (catId > -1) {
            DetailFragment fragment = new DetailFragment();
            Bundle mBundle = new Bundle();
            mBundle.putInt("id", catId);
            mBundle.putString("cat_name", catName);
            fragment.setArguments(mBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
        else
        {
            isAudioFileExist();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            // Logs 'install' and 'app activate' App Events.
            AppEventsLogger.activateApp(this);
        }
        if (!checkPurchase()) {
            loadBanner();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            // Logs 'install' and 'app activate' App Events.
            AppEventsLogger.deactivateApp(this);
        }

        handler.removeCallbacks(r);
    }

    private void saveImage() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.resultscreen_icon);
        File sd = getExternalCacheDir();
        String fileName = "ic_launcher.png";
        File dest = new File(sd, fileName);
        try {
            FileOutputStream out;
            out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public void setNotification() {
        try {

            SettingsSharedPref mSharedPref = new SettingsSharedPref(MainActivity.this);
            DailyNotification notification = new DailyNotification(MainActivity.this);
            if (mSharedPref.getVocabulary_Notification() == true) {
                notification.setDailyAlarm();
            }
        } catch (Exception ex) {
        }
    }


    @SuppressLint("NewApi")
    public void setToolbar() {
        try {
            mToolbar = (Toolbar) MainActivity.this.findViewById(R.id.toolbar);

        } catch (Exception ex) {

        }
    }
    private void showQuizDialog()
    {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Restart Quiz");
        dialog.setMessage("Do you want to take another Quiz?");
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
               // getSupportFragmentManager().popBackStack();
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, new QuizFragment()).addToBackStack(null)
                        .commit();
                mGlobal.isResultFragment=false;

            }
        });
        dialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getSupportFragmentManager().popBackStack();
                dialogInterface.dismiss();
                mGlobal.isResultFragment=false;
            }
        });
        dialog.show();


    }

    @Override
    public void onBackPressed() {


        if (mGlobal.isResultFragment) {
            showQuizDialog();


        } else {
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                        this.finish();
                    GlobalClass.vocabAdsCounter=0;
                    }
                super.onBackPressed();
                }
    }

    public void loadBanner() {
        adRequest = new AdRequest.Builder()
                .build();                                           //s3 black//B16471B7DEACE38B9800DD1A0CB3538C
        adView.loadAd(adRequest);                                   //qmobile//702089A8F4EA158303DD0C14095B8D02
        adView.setAdListener(new AdListener() {                     //nexus//F37A04415FF9FE1FBD0408C58D2A2B1B
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                adView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }
    private boolean isAudioFileExist()
    {
        String audioFile="a1_0_0.mp3";

        try
        {
            if(!DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.exists())
            {
                DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.mkdirs();
            }

            File myFile = new File(DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath(), audioFile);
            if(myFile.exists())
            {
                return true;
                //  new MainActivity13LineQuran.loadImagesFiles().execute();
            }
            else
            {
                startActivity(new Intent(this, DownloadDialogQuranVocabulary.class));

            }
        }
        catch (Exception e)
        {
            Log.e("File", e.toString());
        }
        return  false;
    }

    public boolean checkPurchase() {
        if (((GlobalClass) getApplication()).purchasePref == null) {
            ((GlobalClass) getApplication()).purchasePref = new PurchasePreferences(MainActivity.this);
        }
        ((GlobalClass) getApplication()).isPurchase = ((GlobalClass) getApplication()).purchasePref.getPurchased();

        if (((GlobalClass) getApplication()).isPurchase)
            return true;
        else
            return false;
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private ListView listView;
        private ArrayList<Categories> catList;
        TextView toolbarTitle;
        private ImageView toolbarQuiz,toolbarHome;
        private Activity mainActivity;
        private RelativeLayout relCalibaration;
        SharedPref prefs;
        private GlobalClass mGlobal;
        private int  adsCounter=0;
        InterstitialAdSingleton mInterstitialAdSingleton;


        public PlaceholderFragment() {
        }
       /* private void showIntAds() {
            if(!mGlobal.isPurchase&& GlobalClass.vocabAdsCounter!=0)
            {
                GlobalClass.vocabAdsCounter=0;
                mInterstitialAdSingleton = InterstitialAdSingleton
                        .getInstance(getActivity());
                mInterstitialAdSingleton.showInterstitial();

            }
            else{
                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isAudioFileExist();
                    }
                },2000);
                GlobalClass.vocabAdsCounter++;

            }

        }*/

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mGlobal=(GlobalClass)getActivity().getApplicationContext();
            listView = (ListView) rootView.findViewById(R.id.main_listview);
            toolbarTitle = (TextView) mainActivity.findViewById(R.id.toolbar_title);
            toolbarQuiz = (ImageView) mainActivity.findViewById(R.id.toolbar_Quiz);
            toolbarHome= (ImageView) mainActivity.findViewById(R.id.btnHomeVocab);
            relCalibaration = (RelativeLayout) mActivity.findViewById(R.id.calibrationlayout);
            setToolbar();
            fetchCategories();

            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mainActivity = activity;
        }

        @Override
        public void onResume() {
            super.onResume();
            toolbarTitle.setText(R.string.app_name_QuranVocabulary);
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarQuiz.setVisibility(View.VISIBLE);
            toolbarHome.setVisibility(View.VISIBLE);
            if (relCalibaration.getVisibility() == View.VISIBLE) {
                relCalibaration.setVisibility(View.GONE);
            }
        }

        public void fetchCategories() {
            try {
                DBManager dbManager = new DBManager(getActivity());
                Cursor result = dbManager.getCategories();
                if (result != null) {
                    int i = 0;
                    String list[] = new String[result.getCount()];
                    catList = new ArrayList<>();
                    result.moveToFirst();
                    while (!result.isAfterLast()) {
                        Categories catData = new Categories();
                        catData.setId(result.getInt(0));
                        catData.setEngCat(result.getString(1));
                        catData.setUrduCat(result.getString(2));
                        list[i++] = result.getString(1);

                        catList.add(catData);
                        result.moveToNext();
                    }

                    engAdapter = new IndexAdapter(getActivity(), R.layout.index_layout, catList);
                    listView.setAdapter(engAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                            SettingsSharedPref mSharedPref = new SettingsSharedPref(getActivity());
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", catList.get(position).getId());
                            if (mSharedPref.getLanguageQuranVocabulary() == 0) {
                                bundle.putString("cat_name", catList.get(position).getUrduCat());
                            } else {
                                bundle.putString("cat_name", catList.get(position).getEngCat());

                            }

                            GlobalClass.isDetailedScreen = true;
                            DetailFragment fragment = new DetailFragment();
                            fragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, fragment)
                                    .addToBackStack(null)
                                    .commitAllowingStateLoss();
                          //  showIntAds();


                        }
                    });
                }
            } catch (Exception ex) {
                Log.d("exception", "" + ex.getMessage());
            }
        }

        @SuppressLint("NewApi")
        public void setToolbar() {
            try {
                mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
                ImageView toolbarQuiz = (ImageView) mToolbar.findViewById(R.id.toolbar_Quiz);
                toolbarQuiz.setVisibility(View.VISIBLE);
                ImageView toolbarHome=(ImageView)mToolbar.findViewById(R.id.btnHomeVocab);
                toolbarHome.setVisibility(View.VISIBLE);


            } catch (Exception ex) {
            }
        }
    }
}
