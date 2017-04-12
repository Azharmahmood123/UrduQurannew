package com.QuranReading.urduquran;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.HomeSplashActivity;

public class RateUsDialog extends AppCompatActivity {
	boolean exitFromApp = false;
	String Cancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Auto-generated method stub
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		///setContentView(R.layout.rate_us);

		exitFromApp = getIntent().getBooleanExtra("EXITFROMAPP", false);
		showRateus();

		sendAnalyticsData();

	}

	private void sendAnalyticsData() {
		AnalyticSingaltonClass.getInstance(this).sendScreenAnalytics("RateUs Screen");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	/*
	 * public void openRateUs(View view) { this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.getPackageName()))); finish(); }
	 */
	private void showRateus() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.rate_us_header);
		builder.setMessage(R.string.rate_us_content);
		if (exitFromApp)
		{
			Cancel = getResources().getString(R.string.txt_exit);
			// btnCancel.setText(getResources().getString(R.string.txt_exit));
		}
		else
		{
			Cancel = getResources().getString(R.string.index_btn1);
		}

		builder.setPositiveButton((R.string.index_btn2), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// Write your code here to execute after dialog closed
				RateUsDialog.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + RateUsDialog.this.getPackageName())));
				finish();

			}
		});
		builder.setNegativeButton(Cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (exitFromApp)
				{
					HomeSplashActivity.Finish();
					RateUsDialog.this.finish();
				}
				else
				{
					finish();
				}
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				finish();
				// TODO Auto-generated method stub

			}
		});
		AlertDialog dialog = builder.show();
		/*TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
		messageText.setGravity(Gravity.CENTER);
		dialog.show();*/

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.tick);

		// Setting OK Button

	}
}
