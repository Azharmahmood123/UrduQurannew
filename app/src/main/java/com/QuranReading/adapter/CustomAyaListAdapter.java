package com.QuranReading.adapter;

import java.util.List;

import com.ArabicText.Helper.ArabicUtilities;
import com.QuranReading.model.SurahModel;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.RubanaDuasActivity;
import com.QuranReading.urduquran.SurahActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomAyaListAdapter extends BaseAdapter {
	private Context mContext;
	private List<SurahModel> surahList;
	private String arabicNumberJuz[]={ "١", "٢", "٣", "٤", "٥", "٦", "٧", "٨", "٩", "١٠", "١١", "١٢", "١٣", "١٤", "١٥", "١٦", "١٧", "١٨", "١٩", "٢٠", "٢١", "٢٢", "٢٣", "٢٤", "٢٥", "٢٦", "٢٧", "٢٨", "٢٩", "٣٠"};
	private String strJuzName="جزء: ";
	private int ayahPos=-1;

	public CustomAyaListAdapter(Context context, List<SurahModel> surahList) {
		this.mContext = context;
		this.surahList = surahList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return surahList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return surahList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView bookmarkLine;
		TextView tvAyahNo;
		TextView tvArabic;
		TextView tvTransliteration;
		TextView tvTranslation;
		TextView tvTafseer;
		LinearLayout ayahRow;
		LinearLayout ayahNo;
		LinearLayout juzRow;
		TextView tvJuzEng,tvJuzAr,tvJuzUr;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		int id = surahList.get(position).getBookMarkId();
		String arabic = surahList.get(position).getArabicAyah();
		String arabicAyah = ArabicUtilities.reshapeSentence(arabic);
		String translation = surahList.get(position).getTranslation();
		String tafseer=surahList.get(position).getTafseer();
		String transliteration = surahList.get(position).getTransliteration();

		ViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.ayah_row, null);

			holder = new ViewHolder();

			holder.bookmarkLine = (ImageView) convertView.findViewById(R.id.img_bookmark);
			holder.tvAyahNo = (TextView) convertView.findViewById(R.id.tv_ayah_no);
			holder.tvArabic = (TextView) convertView.findViewById(R.id.tv_ayah);
			holder.tvTranslation = (TextView) convertView.findViewById(R.id.text_translation);
			holder.tvTafseer= (TextView) convertView.findViewById(R.id.text_tafseer);
			holder.tvTransliteration = (TextView) convertView.findViewById(R.id.text_transliteration);
			holder.ayahNo = (LinearLayout) convertView.findViewById(R.id.layout_ayah_no);
			holder.ayahRow = (LinearLayout) convertView.findViewById(R.id.ayah_row);
			holder.juzRow=(LinearLayout)convertView.findViewById(R.id.juzRow);
			holder.tvJuzEng= (TextView) convertView.findViewById(R.id.juzEnglish);
			holder.tvJuzAr= (TextView) convertView.findViewById(R.id.juzArabic);
			holder.tvJuzUr= (TextView) convertView.findViewById(R.id.juzUrdu);
			holder.tvArabic.setTypeface(((GlobalClass) mContext.getApplicationContext()).faceArabic);
			holder.tvJuzAr.setTypeface(((GlobalClass) mContext.getApplicationContext()).faceArabic);
			holder.tvJuzEng.setTypeface(((GlobalClass) mContext.getApplicationContext()).robotoLight);
			holder.tvArabic.setPadding(10, ((GlobalClass) mContext.getApplicationContext()).ayahPadding, 10, 0);

			// holder.tvArabic.setTextColor(Color.parseColor(((GlobalClass) mContext.getApplicationContext()).fontColor));
			holder.tvArabic.setTextColor(Color.parseColor("#000000"));

			holder.tvTranslation.setTypeface(((GlobalClass) mContext.getApplicationContext()).robotoRegular);
			holder.tvTafseer.setTypeface(((GlobalClass) mContext.getApplicationContext()).robotoRegular);
			holder.tvTransliteration.setTypeface(((GlobalClass) mContext.getApplicationContext()).robotoRegular, Typeface.ITALIC);

			holder.tvTranslation.setTextColor(Color.parseColor("#000000"));
			holder.tvTafseer.setTextColor(Color.parseColor("#000000"));
			holder.tvTransliteration.setTextColor(Color.parseColor("#000000"));

			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		holder.tvArabic.setTextSize(((GlobalClass) mContext.getApplicationContext()).font_size_arabic);
		holder.tvTranslation.setTextSize(((GlobalClass) mContext.getApplicationContext()).font_size_eng);
		holder.tvTafseer.setTextSize(((GlobalClass) mContext.getApplicationContext()).font_size_eng);
		holder.tvTransliteration.setTextSize(((GlobalClass) mContext.getApplicationContext()).font_size_eng);

		if(((GlobalClass) mContext.getApplicationContext()).chkSurahTaubah)
		{
			// holder.tvAyahNo.setTextColor(Color.parseColor(((GlobalClass) mContext.getApplicationContext()).fontColor));
			holder.ayahNo.setVisibility(View.VISIBLE);
			holder.tvAyahNo.setText(String.valueOf(position + 1));
		}
		else
		{ //change 0 to 1 if errors occurs
			if(surahList.get(1).getArabicAyah().equals(mContext.getString(R.string.fatiha_text)))
			{
				holder.ayahNo.setVisibility(View.VISIBLE);
				holder.tvAyahNo.setText(String.valueOf(position + 1));
			}
			else
			{
				if(position == 0)
				{
					holder.ayahNo.setVisibility(View.GONE);
				}
				else
				{
					holder.ayahNo.setVisibility(View.VISIBLE);
					// holder.tvAyahNo.setTextColor(Color.parseColor(((GlobalClass) mContext.getApplicationContext()).fontColor));
					holder.tvAyahNo.setText(String.valueOf(position));
				}
			}
		}

		holder.tvArabic.setText(arabicAyah);
		holder.tvTranslation.setText(translation);
		holder.tvTafseer.setText(tafseer);
		holder.tvTransliteration.setText(Html.fromHtml(transliteration));
		if(((GlobalClass) mContext.getApplicationContext()).chkTafseer){
			holder.tvTafseer.setVisibility(View.VISIBLE);
			if(tafseer.isEmpty()){
				holder.tvTafseer.setVisibility(View.GONE);

			}

		}
		else{
			holder.tvTafseer.setVisibility(View.GONE);

		}


		if(id == -1)
		{
			holder.bookmarkLine.setVisibility(View.GONE);
		}
		else
		{
			holder.bookmarkLine.setVisibility(View.VISIBLE);
		}

		if(((GlobalClass) mContext.getApplicationContext()).translationLanguage == 0)
		{
			holder.tvTranslation.setVisibility(View.GONE);
		}
		else
		{
			holder.tvTranslation.setVisibility(View.VISIBLE);
		}

		if(!((GlobalClass) mContext.getApplicationContext()).hideTransliteration)
		{
			holder.tvTransliteration.setVisibility(View.GONE);
		}
		else
		{
			holder.tvTransliteration.setVisibility(View.VISIBLE);
		}

		// holder.ayahRow.setBackgroundColor(Color.parseColor(((GlobalClass) mContext.getApplicationContext()).bgcolor));
		holder.juzRow.setVisibility(View.GONE);
		if(SurahActivity.isJuzFound){
			if(SurahActivity.isSecondSurah){//two positions
				if(position==142) {

					holder.juzRow.setVisibility(View.VISIBLE);
					holder.tvJuzEng.setText(SurahActivity.juzNamesList.get(1).getJuzEnglish());
					holder.tvJuzAr.setText(ArabicUtilities.reshapeSentence(SurahActivity.juzNamesList.get(1).getJuzArabic()));
					holder.tvJuzUr.setText(strJuzName + arabicNumberJuz[1]);
				}
				else if(position==253){
					holder.juzRow.setVisibility(View.VISIBLE);
					holder.tvJuzEng.setText(SurahActivity.juzNamesList.get(2).getJuzEnglish());
					holder.tvJuzAr.setText(ArabicUtilities.reshapeSentence(SurahActivity.juzNamesList.get(2).getJuzArabic()));
					holder.tvJuzUr.setText(strJuzName + arabicNumberJuz[2]);
				}
			}
			else if(SurahActivity.isFourthSurah){ //two positions
				if(position==24) {

					holder.juzRow.setVisibility(View.VISIBLE);
					holder.tvJuzEng.setText(SurahActivity.juzNamesList.get(4).getJuzEnglish());
					holder.tvJuzAr.setText(ArabicUtilities.reshapeSentence(SurahActivity.juzNamesList.get(4).getJuzArabic()));
					holder.tvJuzUr.setText(strJuzName + arabicNumberJuz[4]);
				}
				if(position==148){
					holder.juzRow.setVisibility(View.VISIBLE);
					holder.tvJuzEng.setText(SurahActivity.juzNamesList.get(5).getJuzEnglish());
					holder.tvJuzAr.setText(ArabicUtilities.reshapeSentence(SurahActivity.juzNamesList.get(5).getJuzArabic()));
					holder.tvJuzUr.setText(strJuzName + arabicNumberJuz[5]);
				}

			} else{
				 ayahPos=SurahActivity.juzDataList.get(SurahActivity.juzPosition).getAyahNO();
				if(position==ayahPos){
					holder.juzRow.setVisibility(View.VISIBLE);
					holder.tvJuzEng.setText(SurahActivity.juzNamesList.get(SurahActivity.juzPosition).getJuzEnglish());
					holder.tvJuzAr.setText(ArabicUtilities.reshapeSentence(SurahActivity.juzNamesList.get(SurahActivity.juzPosition).getJuzArabic()));
					holder.tvJuzUr.setText(strJuzName+arabicNumberJuz[SurahActivity.juzPosition]);
				}
			}

			}

		holder.ayahRow.setBackgroundColor(Color.parseColor("#FFFFFF"));

		if(position == ((GlobalClass) mContext.getApplicationContext()).ayahPos || position== SurahActivity.secondAyaSelected)
		{
			holder.ayahRow.setBackgroundResource(R.drawable.selection_color);
		}


		return convertView;
	}

}