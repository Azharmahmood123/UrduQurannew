package com.QuranReading.urduquran;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.QuranReading.adapter.BookmarksListAdapter;
import com.QuranReading.helper.DBManager;
import com.QuranReading.model.BookmarksListModel;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class BookmarksActivity extends Activity  {
	// google ads
	AdView adview;
	ImageView adImage;
	private static final String LOG_TAG = "Ads";
	private final Handler adsHandler = new Handler();
	private int timerValue = 3000, networkRefreshTime = 10000;

	String from;
	TextView tvHeader,tvEmptyBookMark;
	ListView bookmarksListView;
	Boolean setListener = false, checkAsian = false;
	int resrcTimeArray;
	String[] engNamesData = null;
	ArrayList<BookmarksListModel> bookmarksList = new ArrayList<BookmarksListModel>();
	GlobalClass mGlobal;

	private TextView tvName;
	private ImageView deleteItems;
	public static boolean longClick = false;
	public static ArrayList<Integer> delItemList = new ArrayList<Integer>();
	private BookmarksListAdapter customAdapter;
	public static int changeItemColor = 0;
	AnalyticSingaltonClass mAnalyticSingaltonClass;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_bookmarks);
		from = getIntent().getStringExtra("FROM");
		mGlobal = ((GlobalClass) getApplicationContext());
		mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);
		initializeAds();
		tvHeader = (TextView) findViewById(R.id.tv_header);
		deleteItems = (ImageView) findViewById(R.id.del_btn);
		tvEmptyBookMark= (TextView) findViewById(R.id.tvEmptyBookMark);
		tvEmptyBookMark.setTypeface(mGlobal.robotoLight);
		bookmarksListView = (ListView) findViewById(R.id.listView);

		tvHeader.setTypeface(mGlobal.faceHeading);

		bookmarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@SuppressLint("UseValueOf")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

					int surahNo = bookmarksList.get(position).getsurahNo();
					int ayahNo = bookmarksList.get(position).getAyahNo();


					if(from.equals("HOMESPLASHSCREEN"))// called from splashHome Screen
					{
						Intent end_actvty = new Intent(BookmarksActivity.this,SurahActivity.class);
						end_actvty.putExtra("SURAHNO", surahNo);
						if(surahNo == 9 || surahNo == 1)
							ayahNo = ayahNo - 1;
						end_actvty.putExtra("AYAHNO", ayahNo);
						//setResult(RESULT_OK, end_actvty);
						end_actvty.putExtra("fromHome",from);
						startActivity(end_actvty);
					}
					else if(from.equals("RabanaduasHOMESPLASHSCREEN"))
					{
						mAnalyticSingaltonClass.sendScreenAnalytics("RabanaDuas_Index_Screen");
						Intent end_actvty = new Intent(BookmarksActivity.this,SurahActivity.class);
						end_actvty.putExtra("SURAHNO", surahNo);

						if(surahNo == 9 || surahNo == 1)
							ayahNo = ayahNo - 1;
						end_actvty.putExtra("AYAHNO", ayahNo);
						//end_actvty.putExtra("AYAHNO2");
						//setResult(RESULT_OK, end_actvty);
						end_actvty.putExtra("fromHome",from);
						startActivity(end_actvty);
					}
					else{
						Intent end_actvty = new Intent(BookmarksActivity.this,SurahActivity.class);
						end_actvty.putExtra("SURAHNO", surahNo);
						if(surahNo == 9 || surahNo == 1)
							ayahNo = ayahNo - 1;
						end_actvty.putExtra("AYAHNO", ayahNo);
						end_actvty.putExtra("fromHomeResult",from);
						setResult(RESULT_OK, end_actvty);
						finish();

					}





			}
		});

		if(from.equals("BMARK"))
		{
			tvEmptyBookMark.setVisibility(View.GONE);
			deleteItems.setVisibility(View.VISIBLE);
			initializeBookmarksList();
			//bookmarksListView.setOnItemLongClickListener(this);
			bookmarksListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			bookmarksListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
				@Override
				public void onItemCheckedStateChanged(ActionMode actionMode, int position, long id, boolean checked) {
					final int checkedCount = bookmarksListView.getCheckedItemCount();
					// Set the CAB title according to total checked items
					actionMode.setTitle(checkedCount + " Selected");
					// Calls toggleSelection method from ListViewAdapter Class
					customAdapter.toggleSelection(position);
					if(checkedCount>0)
					{
						deleteItems.setVisibility(View.GONE);
					}
				}

				@Override
				public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
					actionMode.getMenuInflater().inflate(R.menu.menu_bookmark, menu);
					return true;
				}

				@Override
				public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
					return false;
				}

				@Override
				public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
					switch (menuItem.getItemId()) {
						case R.id.delete:
							// Calls getSelectedIds method from ListViewAdapter Class
							SparseBooleanArray selected =customAdapter.getSelectedIds();
							// Captures all selected ids with a loop
							for (int i = (selected.size() - 1); i >= 0; i--) {
								if (selected.valueAt(i)) {
									BookmarksListModel selecteditem = customAdapter.getItem(selected.keyAt(i));
									// Remove selected items following the ids
									delItemList.add(selecteditem.getBookMarkId());
									customAdapter.remove(selecteditem);
								}
							}

							//deleteItems.setVisibility(View.VISIBLE);
							DelBookmarks();
							// Close CAB
							actionMode.finish();
							return true;
						default:
							return false;
					}
				}

				@Override
				public void onDestroyActionMode(ActionMode actionMode) {
					// TODO Auto-generated method stub
					customAdapter.removeSelection();
					delItemList.clear();
					if(bookmarksList.size()!=0) {
						deleteItems.setVisibility(View.VISIBLE);
					}
				}
			});
		}


		else if(from.equals("WORDSEARCH"))
		{
			tvEmptyBookMark.setVisibility(View.GONE);
			int surahNo = getIntent().getIntExtra("SurahPos", -1);
			if(surahNo == -1)
			{
				initializeWordSearchListFromQuran();
			}
			else

				initializeWordSearchListFromSura(surahNo);
		}

		else
		{
			tvEmptyBookMark.setVisibility(View.GONE);
			if(getIntent().getStringExtra("FROM").contentEquals("RabanaduasHOMESPLASHSCREEN"))// get intent.getstringExtra("").equal("RabanaduaHomespp")
			{
				//Rabanadua();
				initRabanaDuasList();
			}
			else {
				mAnalyticSingaltonClass.sendScreenAnalytics("Sajdah_Index_Screen");
				initializeSajdahsList();
			}
		}
	}


	private void initializeAds() {
		adview = (AdView) findViewById(R.id.adView);
		adImage = (ImageView) findViewById(R.id.adimg);
		adImage.setVisibility(View.GONE);
		adview.setVisibility(View.GONE);

		if(isNetworkConnected())
		{
			this.adview.setVisibility(View.VISIBLE);
		}
		else
		{
			this.adview.setVisibility(View.GONE);
		}
		setAdsListener();
	}
	public void guideBookmark(View v)
	{
		final Toast toast= Toast.makeText(this,"Long Press Bookmark items To delete Bookmark",Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER,0,0);
		toast.show();

	}

	private void initializeWordSearchListFromSura(int suraNo) {
		// TODO Auto-generated method stub
		tvHeader.setText("Search");
		String surahName;
		int id, surahNo, ayahNo;
		surahNo = getIntent().getIntExtra("SurahPos", 1);
		surahName = getIntent().getStringExtra("SurahName");
		String searchWord = getIntent().getStringExtra("WORD");
		ArrayList<String> suraTranslation = new ArrayList<String>();
		suraTranslation = getIntent().getStringArrayListExtra("TRANSLATIONLIST");
		// int[] surahNoArr = {7, 13, 16, 17, 19, 22, 22, 25, 27, 32, 41, 53, 84, 96};

		List<Integer> listClone = new ArrayList<Integer>();
		for (int i = 0; i < suraTranslation.size(); i++)
		{
			Boolean found = Arrays.asList(suraTranslation.get(i).split(" ")).contains(searchWord);
			if(found)
				listClone.add(i);
		}
		Integer[] ayahNoArr = new Integer[listClone.size()];
		ayahNoArr = listClone.toArray(ayahNoArr);

		for (int pos = 0; pos < ayahNoArr.length; pos++)
		{
			id = pos + 1;

			// surahNo = surahNoArr[pos];
			ayahNo = ayahNoArr[pos] + 1;

			BookmarksListModel model = new BookmarksListModel(id, surahName, surahNo, ayahNo);
			bookmarksList.add(model);
		}

		customAdapter = new BookmarksListAdapter(BookmarksActivity.this, bookmarksList);
		bookmarksListView.setAdapter(customAdapter);
	}

	private void initializeWordSearchListFromQuran() {
		resrcTimeArray = this.getResources().getIdentifier("surah_names", "array", this.getPackageName());

		if(resrcTimeArray > 0)
		{

			engNamesData = getResources().getStringArray(resrcTimeArray);
		}
		String searchWord = getIntent().getStringExtra("WORD");
		tvHeader.setText("Bookmarks");

		DBManager dbObj = new DBManager(this);
		dbObj.open();

		String surahName, translation;
		int id, surahNo, ayahNo;

		Cursor c = dbObj.getFullQuranTranslation();

		if(c.moveToFirst())
		{
			do
			{
				id = c.getInt(c.getColumnIndex(Constants.FLD_ID));
				surahNo = c.getInt(c.getColumnIndex(Constants.FLD_SURAH_NO));
				// getting name of sura
				surahName = engNamesData[surahNo - 1];
				translation = c.getString(c.getColumnIndex(Constants.FLD_TRANSLATION));
				ayahNo = c.getInt(c.getColumnIndex(Constants.FLD_AYAH_NO));

				Boolean found = Arrays.asList(translation.split(" ")).contains(searchWord);
				if(found)
				{
					if(surahNo == 9 || surahNo == 1)
						ayahNo = ayahNo + 1;

					BookmarksListModel model = new BookmarksListModel(id, surahName, surahNo, ayahNo, translation);
					bookmarksList.add(model);
				}

			}
			while (c.moveToNext());

			customAdapter = new BookmarksListAdapter(BookmarksActivity.this, bookmarksList);
			bookmarksListView.setAdapter(customAdapter);
		}
		else
		{
			showToast("Word doesnot exists");
		}

		c.close();
		dbObj.close();
	}

	public void initializeBookmarksList() {
		tvHeader.setText("Bookmarks");

		DBManager dbObj = new DBManager(this);
		dbObj.open();

		String surahName;
		int id, surahNo, ayahNo;

		Cursor c = dbObj.getAllBookmarks();

		if(c.moveToFirst())
		{
			do
			{
				id = c.getInt(c.getColumnIndex(Constants.FLD_ID));
				surahName = c.getString(c.getColumnIndex(Constants.FLD_SURAH_NAME));
				surahNo = c.getInt(c.getColumnIndex(Constants.FLD_SURAH_NO));

				ayahNo = c.getInt(c.getColumnIndex(Constants.FLD_AYAH_NO));
				if(surahNo == 9 || surahNo == 1)
					ayahNo = ayahNo + 1;

				BookmarksListModel model = new BookmarksListModel(id, surahName, surahNo, ayahNo);
				bookmarksList.add(model);

			}
			while (c.moveToNext());

			customAdapter = new BookmarksListAdapter(BookmarksActivity.this, bookmarksList);
			bookmarksListView.setAdapter(customAdapter);
		}
		else
		{

			//showToast("No Bookmarks Added");
			tvEmptyBookMark.setVisibility(View.VISIBLE);
			deleteItems.setVisibility(View.GONE);
		}

		c.close();
		dbObj.close();
	}
	public void initRabanaDuasList()
	{
		tvHeader.setText("Rabana Duas");
		String surahName;
		int id, surahNo, ayahNo;
		String[] surahNamesArr = { "Al-Baqarah", "Al-Baqarah", "Al-Baqarah", "Al-Baqarah","Al-Baqarah", "Al-Baqarah", "Al-Baqarah", "Aal-e-Imran", "Aal-e-Imran", "Aal-e-Imran", "Aal-e-Imran", "Aal-e-Imran", "Aal-e-Imran","Aal-e-Imran", "Aal-e-Imran","Aal-e-Imran","Aal-e-Imran","Al-Ma'idah","Al-Ma'idah","Al-A'raf", "Al-A'raf","Al-A'raf","Al-A'raf","Yunus","Yunus","Ibrahim","Ibrahim","Ibrahim",
				"Al-Kahf","Ta Ha","Al-Mu'minun","Al-Furqan","Al-Furqan","Al-Furqan","Fatir","Al-A'raf","Al-Anfal","Al-Hashr","Al-Hashr","Al-Mumtahanah","Al-Mumtahanah","At-Tahrim"};
		int[] surahNoArr = { 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3,3,3,5,5,7,7,7,7,10,10,14,14,14,18,20,23,25,25,25,35,7,8,59,59,60,60,66};
		int[] ayahNoArr = { 127, 128, 201, 250, 286, 286, 286, 8, 9, 16, 53, 147, 191, 192, 193,193,194,83,114,23,47,89,126,85,86,38,40,41,10,45,109,65,66,74,34,7,8,10,10,4,5,8 };
		for (int pos = 0; pos < surahNoArr.length; pos++)
		{
			id = pos + 1;
			surahName = surahNamesArr[pos];
			surahNo = surahNoArr[pos];
			ayahNo = ayahNoArr[pos];

			BookmarksListModel model = new BookmarksListModel(id, surahName, surahNo, ayahNo);
			bookmarksList.add(model);
		}

		customAdapter = new BookmarksListAdapter(BookmarksActivity.this, bookmarksList);
		bookmarksListView.setAdapter(customAdapter);

	}

	public void initializeSajdahsList() {
		tvHeader.setText(R.string.txt_sajdahs);
		String surahName;
		int id, surahNo, ayahNo;

		String[] surahNamesArr = { "Al-A'raf", "Ar-Ra'd", "An-Nahl", "Bani Isra'il", "Maryam", "Al-Hajj", "Al-Hajj (As Shafee - Optional)", "Al-Furqan", "An-Naml", "As-Sajdah", "Sad (Hanafi)", "Ha Meem Al-Sajdah", "An-Najm", "Al-Inshiqaq", "Al-'Alaq" };
		int[] surahNoArr = { 7, 13, 16, 17, 19, 22, 22, 25, 27, 32, 38, 41, 53, 84, 96 };
		int[] ayahNoArr = { 206, 15, 50, 109, 58, 18, 77, 60, 26, 15, 24, 38, 62, 21, 19 };

		for (int pos = 0; pos < surahNoArr.length; pos++)
		{
			id = pos + 1;
			surahName = surahNamesArr[pos];
			surahNo = surahNoArr[pos];
			ayahNo = ayahNoArr[pos];

			BookmarksListModel model = new BookmarksListModel(id, surahName, surahNo, ayahNo);
			bookmarksList.add(model);
		}

		customAdapter = new BookmarksListAdapter(BookmarksActivity.this, bookmarksList);
		bookmarksListView.setAdapter(customAdapter);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(from.equals("BMARK"))
		{
			longClick = true;
			Intent end_actvty = new Intent();
			setResult(RESULT_CANCELED, end_actvty);
		}



	}

	public void showToast(String msg) {
		Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		longClick = false;
		if(!mGlobal.isPurchase)
		{
			startAdsCall();
		}
		else
		{
			adImage.setVisibility(View.GONE);
			adview.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(!mGlobal.isPurchase)
		{
			stopAdsCall();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(!mGlobal.isPurchase)
		{
			destroyAds();
		}
	}

	public void DelBookmarks() {
		if(delItemList.size() != 0)
		{
			Collections.sort(delItemList);
			DBManager dbObj = new DBManager(this);
			dbObj.open();
			for (int i = delItemList.size() - 1; i >= 0; i--)
			{
				int id = delItemList.get(i);
				dbObj.deleteOneBookmark(id);
//				bookmarksList.remove((int) delItemList.get(i));
			}
			delItemList.clear();
			if(bookmarksList.size()==0) //code added
			{
				tvEmptyBookMark.setVisibility(View.VISIBLE);
				deleteItems.setVisibility(View.GONE);
			}
		}
	}

	private boolean isNetworkConnected() {
		ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netInfo = mgr.getActiveNetworkInfo();

		return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
	}

	private Runnable sendUpdatesAdsToUI = new Runnable() {
		public void run() {
			Log.v(LOG_TAG, "Recall");
			updateUIAds();
		}
	};

	private final void updateUIAds() {
		if(isNetworkConnected())
		{
			AdRequest adRequest = new AdRequest.Builder().build();
			adview.loadAd(adRequest);
		}
		else
		{
			timerValue = networkRefreshTime;
			adsHandler.removeCallbacks(sendUpdatesAdsToUI);
			adsHandler.postDelayed(sendUpdatesAdsToUI, timerValue);
		}
	}

	public void startAdsCall() {
		Log.i(LOG_TAG, "Starts");
		if(isNetworkConnected())
		{
			this.adview.setVisibility(View.VISIBLE);
		}
		else
		{
			this.adview.setVisibility(View.GONE);
		}

		adview.resume();
		adsHandler.removeCallbacks(sendUpdatesAdsToUI);
		adsHandler.postDelayed(sendUpdatesAdsToUI, 0);
	}

	public void stopAdsCall() {
		Log.e(LOG_TAG, "Ends");
		adsHandler.removeCallbacks(sendUpdatesAdsToUI);
		adview.pause();
	}

	public void destroyAds() {
		Log.e(LOG_TAG, "Destroy");
		adview.destroy();
		adview = null;
	}

	private void setAdsListener() {
		adview.setAdListener(new AdListener() {
			@Override
			public void onAdClosed() {
				Log.d(LOG_TAG, "onAdClosed");
			}

			@Override
			public void onAdFailedToLoad(int error) {
				String message = "onAdFailedToLoad: " + getErrorReason(error);
				Log.d(LOG_TAG, message);
				adview.setVisibility(View.GONE);
			}

			@Override
			public void onAdLeftApplication() {
				Log.d(LOG_TAG, "onAdLeftApplication");
			}

			@Override
			public void onAdOpened() {
				Log.d(LOG_TAG, "onAdOpened");
			}

			@Override
			public void onAdLoaded() {
				Log.d(LOG_TAG, "onAdLoaded");
				adview.setVisibility(View.VISIBLE);

			}
		});
	}

	private String getErrorReason(int errorCode) {
		String errorReason = "";
		switch (errorCode)
		{
			case AdRequest.ERROR_CODE_INTERNAL_ERROR:
				errorReason = "Internal error";
				break;
			case AdRequest.ERROR_CODE_INVALID_REQUEST:
				errorReason = "Invalid request";
				break;
			case AdRequest.ERROR_CODE_NETWORK_ERROR:
				errorReason = "Network Error";
				break;
			case AdRequest.ERROR_CODE_NO_FILL:
				errorReason = "No fill";
				break;
		}
		return errorReason;
	}
}
