package com.QuranReading.urduquran;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class DisclaimerDialog extends Activity 
{
	TextView tvHeader, tvMsg;
	Button btnUpgrade, btnCancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	//	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	//			WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.disclaimer_dialog);

		
		btnCancel = (Button) findViewById(R.id.btnCancel);
		tvHeader = (TextView) findViewById(R.id.tv_header);
		tvMsg = (TextView) findViewById(R.id.tv_msg);	

		tvHeader.setTypeface(((GlobalClass) getApplication()).faceHeading);
		tvMsg.setTypeface(((GlobalClass) getApplication()).faceContent1);
		btnCancel.setTypeface(((GlobalClass) getApplication()).faceContent1);
	}


	public void onCancelClick(View view) 
	{
		finish();
	}



	
}
