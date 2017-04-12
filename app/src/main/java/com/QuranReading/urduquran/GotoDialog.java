package com.QuranReading.urduquran;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;

import java.util.Timer;
import java.util.TimerTask;

public class GotoDialog extends AppCompatActivity {
	int minLimit, maxLimit;
	// EditText edGoto;
	int suraNo;
	public static boolean isGoToDialogVisible=false;
	private static Activity activity;
	AlertDialog alertGoto;
	private  EditText input;
	boolean isKeyBordHide=false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.goto_dialog);

		maxLimit = getIntent().getIntExtra("LIMIT", 0);
		activity = this;
		suraNo = getIntent().getIntExtra("SURAHNO", 10);
		//commented During Qa amir
	/*	if (suraNo == 1)
		{
			maxLimit += 1;
		}
*/
		openGoTo();
		sendAnalyticsData();

		// //////////////////////////Registering Notification Receiver
		// ///////////////////

		IntentFilter dailySurah = new IntentFilter(Constants.BroadcastActionNotification);
		registerReceiver(dailySurahNotification, dailySurah);
	}

	private void sendAnalyticsData() {
		AnalyticSingaltonClass.getInstance(this).sendScreenAnalytics("Goto Screen");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isGoToDialogVisible = true;
		if(isKeyBordHide) {
			showSoftKeyboard(input);
			isKeyBordHide=false;
		}

	}
	public void showSoftKeyboard(View view) {
		view.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

				if(m != null){
					// m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				}
			}

		}, 200);
		/*InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);*/
		//inputMethodManager.showSoftInput(view,0);
	}

	public static void Finish() {
		activity.finish();
	}

	public void onOkClick(int index) {

		Intent end_actvty = new Intent();

		end_actvty.putExtra("INDEX", index);
		setResult(RESULT_OK, end_actvty);
		// used for saving last read in surah activity
		finish();
	}

	public void onCancelClick() {
		Intent end_actvty = new Intent();
		setResult(RESULT_CANCELED, end_actvty);
		// used for saving last read in surah activity
		((GlobalClass) getApplicationContext()).saveonpause = true;
		finish();
	}

	public void showToast(String msg) {
		Toast toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	// public void hideSoftKeyboard() {
	// InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
	// inputMethodManager.hideSoftInputFromWindow(edGoto.getWindowToken(), 0);
	// }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		// hideSoftKeyboard();
		super.onPause();
		isKeyBordHide=true;
		hideSoftKeyboard();
	}
	public void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}

	private BroadcastReceiver dailySurahNotification = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			finish();
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(dailySurahNotification);

	}
	public void openGoTo() {

		// maxLimit = totalVerses[surahNumber - 1];

		// For Surah Taubah
		//Commented original amir
		/*if (suraNo == 9 || suraNo == 1)
		{
			minLimit = 1;
		}*/
		//Added by Amir new
		if (suraNo == 9 )
		{
			minLimit = 1;
		}
		else
		{
			minLimit = 0;
		}

		final String versesCount = "" + maxLimit;
		String hint = getResources().getString(R.string.please_enter_value) + " " + minLimit + "-" + maxLimit;

		 input = new EditText(this);
		input.setFocusable(true);
		input.setFocusableInTouchMode(true);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(versesCount.length());
		input.setFilters(FilterArray);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		input.setHint(hint);
		//input.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

		alertGoto = new AlertDialog.Builder(this).setCancelable(false).setTitle(R.string.txt_goto).setMessage(R.string.txt_ayahno).setView(input).setPositiveButton(getString(R.string.txt_ok), null).setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
				onCancelClick();
			}
		}).create();

		alertGoto.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {

				Button b = alertGoto.getButton(AlertDialog.BUTTON_POSITIVE);
				b.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {

						String value = input.getText().toString().trim();

						if (value.length() > 0 && value.length() <= versesCount.length())
						{
							int temp = Integer.parseInt(value);

							if (temp < minLimit || temp > maxLimit)
							{
								input.setText("");
								showShortToast(getString(R.string.please_enter_value) +" "+ minLimit + "-" + versesCount, 1000, Gravity.CENTER);
							}
							else
							{
								if(temp==maxLimit){
									SurahActivity.isLastAya=true;
								}

								//amir commented original
								/*if (suraNo == 9 || suraNo == 1)
								// if (suraNo == 9)
								{
									temp = temp - 1;
								}*/
								//this is new
								if (suraNo == 9 )
								// if (suraNo == 9)
								{
									temp = temp - 1;
								}
								/*
								 * chkSelection(temp); ayahListView.setSelection(temp);
								 */
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
								alertGoto.dismiss();
								onOkClick(temp);
							}
						}
						else
						{
							input.setText("");

							showShortToast(getString(R.string.please_enter_value) + " "+ minLimit + "-" + versesCount, 1000, Gravity.CENTER);
						}
					}
				});
			}
		});

		alertGoto.show();
		input.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	private void showShortToast(String message, int milliesTime, int gravity) {

		if (getString(R.string.device).equals("large"))
		{
			final Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.setGravity(gravity, 0, 0);
			toast.show();
		}
		else
		{
			final Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			toast.setGravity(gravity, 0, 0);
			toast.show();

			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					toast.cancel();
				}
			}, milliesTime);
		}
	}

}
