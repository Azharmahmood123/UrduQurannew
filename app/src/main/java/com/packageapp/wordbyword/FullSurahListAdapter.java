package com.packageapp.wordbyword;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ArabicText.Helper.ArabicUtilities;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.packageapp.wordbyword.models.DataModel;

import java.util.ArrayList;


public class FullSurahListAdapter extends BaseAdapter
{
	Context context;
	private ArrayList<DataModel> dataList = new ArrayList<DataModel>();
	private LayoutInflater inflator;
	private GlobalClass globalObject;
	private SpannableString WordtoSpan;
	private SettingsSharedPref preferences;
	
	public FullSurahListAdapter(Context context1, ArrayList<DataModel> dataList1) 
	{
		this.context = context1;
		this.dataList = dataList1;
		this.inflator = LayoutInflater.from(this.context);
		this.globalObject = (GlobalClass)context.getApplicationContext();
		preferences = new SettingsSharedPref(context);
		
	}
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		
		String arabic = dataList.get(position).getArabicText();
		//String arabicAyah = ArabicUtilities.reshapeSentence(arabic);
		String translation = dataList.get(position).getTranslationText();
		String transliteration = dataList.get(position).gettransliterationText();

		
		if(convertView==null)
		{
			viewHolder = new ViewHolder();
			convertView = inflator.inflate(R.layout.full_surah_row_view, null);
			viewHolder.ayahArabicText = (TextView) convertView.findViewById(R.id.text_ayah);
			viewHolder.ayahTranslation = (TextView) convertView.findViewById(R.id.text_translation);
			viewHolder.ayahTransliteration = (TextView) convertView.findViewById(R.id.text_transliteration);
			viewHolder.ayahNumber = (TextView) convertView.findViewById(R.id.text_aya_num);
			viewHolder.ayahNumLayout = (LinearLayout)convertView.findViewById(R.id.layout_ayah_no);
			
		    // Applying Text Sizes
					viewHolder.ayahArabicText.setTextSize(globalObject.font_size_arabic);
					viewHolder.ayahTranslation.setTextSize(globalObject.font_size_eng);
					viewHolder.ayahTransliteration.setTextSize(globalObject.font_size_eng);
					viewHolder.ayahArabicText.setTypeface(globalObject.faceArabic1);
					
					viewHolder.ayahArabicText.setPadding(0,globalObject.ayahPadding,
							0, 0);
					
					viewHolder.ayahTranslation.setTypeface(globalObject.robotoLight);
					viewHolder.ayahTransliteration.setTypeface(globalObject.robotoLight);
			
			convertView.setTag(viewHolder);
			
		}
		else
		{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		
//		viewHolder.ayahArabicText.setText(ArabicUtilities.reshapeSentence(arabic.replace("-", "")));
		//viewHolder.ayahNumber.setText("" + position);
		viewHolder.ayahTranslation.setText(translation.replace("-", ""));
		viewHolder.ayahTransliteration.setText(transliteration);
		
		if (position == globalObject.ayahPosFullSura)
			convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		else
			convertView.setBackgroundColor(context.getResources().getColor(R.color.background_color));
		
		
		if (position == 0)
		{
			//viewHolder.ayahNumber.setVisibility(View.GONE);
			viewHolder.ayahNumLayout.setVisibility(View.GONE);
		} 
		else 
		{
			//viewHolder.ayahNumber.setVisibility(View.VISIBLE);
			viewHolder.ayahNumLayout.setVisibility(View.VISIBLE);
			viewHolder.ayahNumber.setText(String.valueOf(position));
		}
		
/*		HashMap<String, Integer> settings = preferences.getSettings();

		int faceArabic = settings.get(SharedPreference.FACEARABIC);*/
		
		if (globalObject.ayahPosFullSura == position) 
		{
			String ayahVerse = dataList.get(position).getArabicText();
			String word= "";

			    ayahVerse = ArabicUtilities.reshapeSentence(ayahVerse);
			    word = ArabicUtilities.reshapeSentence(SurahFragment.highlightedWordForFullSura);
			
			
			if (ayahVerse.contains(word)) {
				int startIndex = 0, length;
				WordtoSpan = new SpannableString(ayahVerse);
				//WordtoSpan = new SpannableString(ArabicUtilities.reshapeSentence(ayahVerse));
				
				length = word.length();
				startIndex = ayahVerse.indexOf(word, SurahFragment.lastIndex);

				SurahFragment.lastIndex = startIndex;
				
				try {
					WordtoSpan.setSpan( new ForegroundColorSpan(Color.parseColor("#EE0000")), startIndex, startIndex + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} catch (Exception e) {

					e.printStackTrace();
				}
				viewHolder.ayahArabicText.setText(WordtoSpan);
			}
		}	
		else
		{
			viewHolder.ayahArabicText.setText(ArabicUtilities.reshapeSentence(arabic));
		}
			/*
			//translation And transliteration Settings	
				if (preferences.getTranslation()) {

					viewHolder.ayahTranslation.setVisibility(View.VISIBLE);
				} else {
					viewHolder.ayahTranslation.setVisibility(View.GONE);
				}
				if (preferences.getTransliteration()) {

					viewHolder.ayahTransliteration.setVisibility(View.VISIBLE);
				} else {
					viewHolder.ayahTransliteration.setVisibility(View.GONE);
				}
				*/
				//Applying Font
		

		return convertView;
	}
	
	private class ViewHolder
	{
		TextView ayahArabicText, ayahNumber,ayahTranslation,ayahTransliteration;
		LinearLayout ayahNumLayout;
	}
	
}
