package com.QuranReading.removeads;

import java.nio.charset.Charset;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.SurahActivity;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.QuranReading.util.IabHelper;
import com.QuranReading.util.IabResult;
import com.QuranReading.util.Inventory;
import com.QuranReading.util.Purchase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.packageapp.HomeSplashActivity;

public class
RemoveAdsActivity extends Activity {

	AdView adview;
	GoogleAdsClass googleAds;
	LinearLayout premiumContainer;

	TextView tvPremuim[] = new TextView[8];
	// TextView tvHeading, tvFeature, tvFeature1, tvFeature2, tvFeature3, tvFeature4, tvFeature5, tvUpgrade, tvNoThanks;

	Context context = this;

	static final String TAG = "Qibla Connect";
	boolean isUpgrade = false, inappbuillingsetup = false, inPurchase = false;
	static final String SKU_UNLOCK = "sku_unlock";
	static final int RC_REQUEST = 10001;

	IabHelper mHelper;
	boolean isExitCall = false;
	private long stopClicking=0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_premium);

		isExitCall = getIntent().getBooleanExtra("EXITFROMAPP", false);

		initViews();
		initializeAds();

		if(isExitCall)
		{
			tvPremuim[7].setText(getString(R.string.exit));
		}

		String base64EncodedPublicKey = getValue();

		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if(!result.isSuccess())
				{
					alert("Problem setting up in-app billing");
					return;
				}

				if(mHelper == null)
					return;

				Log.d(TAG, "Setup successful. Querying inventory.");
				inappbuillingsetup = true;
				if(inappbuillingsetup)
					mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});

		/*
		 * if(isExitCall) { showTwoButtonDialog(getResources().getString(R.string.exit)); } else { showTwoButtonDialog(getResources().getString(R.string.cancel)); }
		 */
	}

	private void initializeAds() {

		adview = (AdView) findViewById(R.id.adView);
		adview.setVisibility(View.GONE);

		googleAds = new GoogleAdsClass(this, adview, null);
	}

	private void initViews() {

		premiumContainer = (LinearLayout) findViewById(R.id.premium_container);
		// premiumContainer.setVisibility(View.GONE);

		tvPremuim[0] = (TextView) findViewById(R.id.tv_app_name);
		tvPremuim[1] = (TextView) findViewById(R.id.tv_premium_1);
		tvPremuim[2] = (TextView) findViewById(R.id.tv_premium_2);
		tvPremuim[3] = (TextView) findViewById(R.id.tv_premium_3);
		tvPremuim[4] = (TextView) findViewById(R.id.tv_premium_4);
		tvPremuim[5] = (TextView) findViewById(R.id.tv_premium_5);
		tvPremuim[6] = (TextView) findViewById(R.id.tv_premium_upgrade);
		tvPremuim[7] = (TextView) findViewById(R.id.tv_no_thanks);

		for (int i = 0; i < tvPremuim.length; i++)
		{
			tvPremuim[i].setTypeface(((GlobalClass) getApplication()).robotoRegular);
		}

	}

	private void sendAnalyticsData() {

		AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Remove Ads", "Ads Removed");
	}

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if(mHelper == null)
				return;

			// Is it a failure?
			if(result.isFailure())
			{
				// alert("Failed to query inventory");
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check the developer payload to see if it's correct! See verifyDeveloperPayload().
			 */

			Purchase unlockPurchase = inventory.getPurchase(SKU_UNLOCK);
			isUpgrade = (unlockPurchase != null && verifyDeveloperPayload(unlockPurchase));
			Log.d(TAG, "Kalmas " + (isUpgrade ? "Locked" : "Unlocked"));

			if(isUpgrade)
			{
				showToast(context.getResources().getString(R.string.toast_already_purchased));
				savePurchase();

				// alert("Kalmas Unlocked");
			}
			/*
			 * else { alert("Kalmas Locked"); }
			 */
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
		if(mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if(!mHelper.handleActivityResult(requestCode, resultCode, data))
		{
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		}
		else
		{
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}


	}

	/** Verifies the developer payload of a purchase. */
	@SuppressWarnings("unused")
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase and verifying it here might seem like a good approach, but this will fail in the case where the user purchases an item on one device and then uses your app on a different device, because on the other device you will not
		 * have access to the random string you originally generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different between them, so that one user's purchase can't be replayed to another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app wasn't the one who initiated the purchase flow (so that items purchased by the user on one device work on other devices owned by the user).
		 * 
		 * Using your own server to store and verify developer payloads across app installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

			// if we were disposed of in the meantime, quit.
			if(mHelper == null)
			{
				inPurchase = false;
				finish();
				return;
			}

			if(result.isFailure())
			{
				inPurchase = false;
				// alert("Error purchasing");
				finish();
				return;
			}
			if(!verifyDeveloperPayload(purchase))
			{
				inPurchase = false;
				// alert("Error purchasing. Authenticity verification failed.");
				finish();
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if(purchase.getSku().equals(SKU_UNLOCK))
			{
				Log.d(TAG, "Purchased.");
				isUpgrade = true;

				sendAnalyticsData();
				showToast(context.getResources().getString(R.string.toast_purchase_successful));
				savePurchase();
				finish();
			}

			inPurchase = false;

			finish();
		}
	};

	/*
	 * // Called when consumption is complete IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() { public void onConsumeFinished(Purchase purchase, IabResult result) { Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " +
	 * result);
	 * 
	 * // if we were disposed of in the meantime, quit. if (mHelper == null) { inPurchase = false; progressBar.setVisibility(View.GONE); btnTransprnt.setVisibility(View.GONE); return; }
	 * 
	 * 
	 * 
	 * // We know this is the "gas" sku because it's the only one we consume, // so we don't check which sku was consumed. If you have more than one // sku, you probably should check... if (result.isSuccess()) { // successfully consumed, so we apply the effects of the item in our // game world's
	 * logic, which in our case means filling the gas tank a bit Log.d(TAG, "Consumption successful. Provisioning."); //saveData(); alert("Purchase Successful"); } else { complain("Error while consuming: " + result); }
	 * 
	 * inPurchase = false; progressBar.setVisibility(View.GONE); btnTransprnt.setVisibility(View.GONE); Log.d(TAG, "End consumption flow."); } };
	 */

	/*
	 * void complain(String message) { Log.e(TAG, "****Error: " + message); //alert("Error: " + message); }
	 */

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	public void showToast(String msg) {
		Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public void onSave(View view) {
		if(!inPurchase)
		{
			if(isUpgrade)
			{
				alert("No need! You're subscribed");
				return;
			}

			if(inappbuillingsetup || mHelper != null)
			{
				inPurchase = true;

				String payload = "";

				mHelper.launchPurchaseFlow(this, SKU_UNLOCK, RC_REQUEST, mPurchaseFinishedListener, payload);
			}
			else
			{
				alert("In-app builling not setup");
			}
		}
	}

	public void onRemoveAddsClick(View v) {
		if(SystemClock.elapsedRealtime()-stopClicking<1000)
		{
			return;
		}
		stopClicking=SystemClock.elapsedRealtime();


		if(!inPurchase)
		{
			if(isUpgrade)
			{
				alert("No need! You're subscribed");
				return;
			}

			if(inappbuillingsetup || mHelper != null)
			{
				if(!mHelper.isAsyncInProgress()) { //block added by amir, in IabHelper as well


					inPurchase = true;

					String payload = "";

					mHelper.launchPurchaseFlow(this, SKU_UNLOCK, RC_REQUEST, mPurchaseFinishedListener, payload);

				}
			}
			else
			{
				alert("In-app builling not setup");
			}
		}
	}

	public void savePurchase() {

		((GlobalClass) getApplication()).isPurchase = true;
		((GlobalClass) getApplication()).purchasePref.setPurchased(true);

		Intent end_actvty = new Intent();
		setResult(RESULT_OK, end_actvty);
		finish();
	}

	public void onCancel(View view) {

		if(isExitCall)
		{
			HomeSplashActivity.Finish();
			//super.finish();
		}

		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!inPurchase)
			super.onBackPressed();
	}

	/*
	 * public void showTwoButtonDialog(String negativeText) { AlertDialog.Builder builder = new AlertDialog.Builder(context);
	 * builder.setTitle(getResources().getString(R.string.remove_ads_heading)).setMessage(getResources().getString(R.string.remove_ads_msg)).setPositiveButton(getString(R.string.remove), new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { // User clicked OK
	 * button onRemoveAddsClick(null); } }).setNegativeButton(negativeText, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { // User cancelled the dialog onCancel(null); } }).setOnKeyListener(new OnKeyListener() {
	 * 
	 * @Override public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
	 * 
	 * if(keyCode == KeyEvent.KEYCODE_BACK) { finish(); dialog.dismiss(); } return true; } }).setOnCancelListener(new OnCancelListener() {
	 * 
	 * @Override public void onCancel(DialogInterface dialog) { // TODO Auto-generated method stub
	 * 
	 * finish(); } });
	 * 
	 * builder.show(); }
	 */

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if(((GlobalClass) getApplication()).isPurchase)
		{
			adview.setVisibility(View.GONE);
		}
		else
		{
			googleAds.startAdsCall();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if(!((GlobalClass) getApplication()).isPurchase)
		{
			googleAds.stopAdsCall();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "Destroying helper.");

		if(mHelper != null)
		{
			mHelper.dispose();
			mHelper = null;
		}

		if(!((GlobalClass) getApplication()).isPurchase)
		{

			googleAds.destroyAds();
		}
	}

	private class GoogleAdsClass {

		private AdView adview;
		private Context context;
		private static final String LOG_TAG = "Ads";
		private final Handler adsHandler = new Handler();
		private int timerValue = 3000, networkRefreshTime = 10000;

		public GoogleAdsClass(Context context, AdView adview, ImageView adImage) {
			super();
			this.context = context;
			this.adview = adview;
			if(!((GlobalClass) context.getApplicationContext()).isPurchase)
			{
				if(isNetworkConnected())
				{
					premiumContainer.setVisibility(View.VISIBLE);
				}
				else
				{
					this.adview.setVisibility(View.GONE);
				}
				setAdsListener();
			}

		}

		public void startAdsCall() {
			Log.i(LOG_TAG, "Starts");

			adview.resume();
			adsHandler.removeCallbacks(sendUpdatesToUI);
			adsHandler.postDelayed(sendUpdatesToUI, 0);
		}

		public void stopAdsCall() {
			Log.e(LOG_TAG, "Ends");
			adsHandler.removeCallbacks(sendUpdatesToUI);
			adview.pause();
		}

		public void destroyAds() {
			Log.e(LOG_TAG, "Destroy");
			adview.destroy();
			adview = null;
		}

		private final void updateUIAds() {
			if(isNetworkConnected())
			{
				AdRequest adRequest = new AdRequest.Builder().build();
				adview.loadAd(adRequest);
			}
			else
			{
				timerValue = networkRefreshTime;
				adview.setVisibility(View.GONE);
				adsHandler.removeCallbacks(sendUpdatesToUI);
				adsHandler.postDelayed(sendUpdatesToUI, timerValue);
			}
		}

		private void setAdsListener() {
			adview.setAdListener(new AdListener() {
				@Override
				public void onAdClosed() {
					Log.d(LOG_TAG, "onAdClosed");
				}

				@Override
				public void onAdFailedToLoad(int error) {

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
					premiumContainer.setVisibility(View.GONE);
					adview.setVisibility(View.VISIBLE);
				}
			});
		}

		private Runnable sendUpdatesToUI = new Runnable() {
			public void run() {
				Log.v(LOG_TAG, "Recall");
				updateUIAds();
			}
		};

		public boolean isNetworkConnected() {
			ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo netInfo = mgr.getActiveNetworkInfo();

			return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
		}
	}

	public String getValue() {
		String keyValue = "";

		try
		{
			String key = "NinSolIslamicKey";

			// String s = getString(R.string.inapp_purchase_key);
			// byte[] ciphertext = encrypt(key, s);

			byte[] theByteArray = { 29, -39, -49, 44, 58, 28, -121, -70, 94, 70, -3, 34, -50, -14, 29, -95, -15, 125, 90, -58, 16, -18, -17, -118, 16, 83, 99, 7, -21, 59, 57, -101, 90, -39, -53, 27, -1, 90, 98, -28, 101, -7, 73, 3, 92, 23, 73, -101, 110, 68, -111, -98, 83, -97, -8, 59, -69, -96,
					-109, -50, 115, 86, -51, -103, -90, -119, -74, -39, 42, 20, -37, -72, 101, -21, 95, 68, -117, 45, 44, 30, -105, -88, -70, 16, 85, -112, 42, 55, -102, -58, 22, -120, 6, -22, -104, 47, 122, 10, 61, -63, 107, -29, -42, 75, -81, 67, 15, -48, -94, -29, 71, -119, 126, -112, 41, -93,
					-1, -45, 76, 109, 118, -127, -11, -87, 120, 100, 46, 22, 61, -122, 106, 104, -67, 51, -125, 25, -124, 83, 52, 81, -106, -36, 17, -35, -62, -85, -4, 40, -91, -125, 4, 3, 69, -41, -91, 5, 15, -12, -39, -47, 120, 76, 107, -5, 8, -95, -5, -74, -108, 83, -2, 49, -63, -68, -58, 50,
					-13, -25, -107, -89, 85, 22, 16, 54, 1, -20, -35, -66, -128, -3, 115, -115, 86, -46, 116, 36, 54, 12, -42, 2, -46, 95, -56, -15, -49, -74, -85, 88, 110, 107, -126, -41, -5, 64, -59, 126, 48, -1, 107, -24, 72, -99, -116, 99, 112, 82, 121, 69, 3, 16, -113, 104, -7, -118, -12, 68,
					-58, -39, -120, -82, -65, 111, -36, 84, 105, -60, -23, 117, 124, -117, 47, 105, 3, -4, 77, 2, -87, 8, -85, 99, -18, 122, 72, -71, -125, 69, -101, -25, -33, 71, 78, -127, 50, 47, 59, 112, 4, 95, 3, 112, 113, -39, 67, 39, -40, -51, -13, -79, -92, 64, -79, -83, -90, -105, 43, 124,
					-89, -97, -122, -31, 23, -90, -127, -82, -26, -38, 19, -128, 32, 105, 115, 20, 91, 63, 77, -113, 17, 44, 64, 79, -60, 12, 56, -68, -81, 67, -66, -28, -25, -1, -105, -4, 26, -55, -48, -26, 81, 47, -96, -88, -124, -35, -123, -109, -56, -106, 74, -53, 20, -44, 88, -56, -53, -90,
					-76, -25, -13, 22, -66, 0, 56, -93, -103, -62, 108, -109, -45, 73, -92, -124, 51, 28, 35, -40, -124, 2, 51, -7, -110, 7, -40, 108, 39, -71, 22, 10, -13, -105, 42, -118, 60, -104, -37, -76, -45, 28, -46, -73, 77, -40 };

			byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
			if(raw.length != 16)
			{
				return keyValue;
			}

			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
			byte[] original = cipher.doFinal(theByteArray);
			keyValue = new String(original, Charset.forName("US-ASCII"));
			return keyValue;
		}
		catch (GeneralSecurityException e)
		{
			e.printStackTrace();
			return keyValue;
		}
	}

	// private byte[] encrypt(String key, String value)
	// throws GeneralSecurityException {
	// byte[] raw = key.getBytes(Charset.forName("US-ASCII"));
	//
	// if (raw.length != 16) {
	// return null;
	// }
	//
	// SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	// Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	// cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(
	// new byte[16]));
	// return cipher.doFinal(value.getBytes(Charset.forName("US-ASCII")));
	// }

}
