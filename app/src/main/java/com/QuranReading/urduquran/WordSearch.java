package com.QuranReading.urduquran;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WordSearch extends Activity{

private EditText wordSearch;
private String word;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wordsearch_dialog);
		wordSearch = (EditText) findViewById(R.id.et_search);
		
		
		////////////////////////////Registering Notification Receiver ///////////////////
				
		IntentFilter dailySurah = new IntentFilter(Constants.BroadcastActionNotification);
		registerReceiver(dailySurahNotification, dailySurah);
    }
    
	
	@SuppressLint("ShowToast")
	public void onEntireQuranSearch(View v)
	{
		word= wordSearch.getText().toString();
		if(!word.equals("") || !word.equals(null))
		{
			Intent end_actvty = new Intent();
			end_actvty.putExtra("WORD", word);
			end_actvty.putExtra("FROM", "quran");
			setResult(RESULT_OK, end_actvty);
			finish();
		}
		else
		{
			Toast.makeText(this, "Please Enter Word", 0).show();
		}
	}
	
	@SuppressLint("ShowToast")
	public void onSurahSearch(View v)
	{
		word= wordSearch.getText().toString();
		if(!word.equals("") || !word.equals(null))
		{
			Intent end_actvty = new Intent();
			end_actvty.putExtra("WORD", word);
			end_actvty.putExtra("FROM", "sura");
			setResult(RESULT_OK, end_actvty);
			finish();
		}
		else
		{
			Toast.makeText(this, "Please Enter Word", 0).show();
		}
	}
	
	@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			unregisterReceiver(dailySurahNotification);
		}

		private BroadcastReceiver dailySurahNotification = new BroadcastReceiver()
		{
			@Override
			public void onReceive(Context context, Intent intent) 
			{
				finish();
			}
		};
}
