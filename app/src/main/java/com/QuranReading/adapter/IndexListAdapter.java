package com.QuranReading.adapter;

import java.util.List;

import com.ArabicText.Helper.ArabicUtilities;
import com.QuranReading.model.IndexListModel;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IndexListAdapter extends BaseAdapter {
	private Context mContext;
	private List<IndexListModel> dataArray;
	private GlobalClass mGlobal;
	private boolean isSurah=false;

	public IndexListAdapter(Context context, List<IndexListModel> dataArray,boolean isSurah) {
		this.mContext = context;
		this.dataArray = dataArray;
		mGlobal = ((GlobalClass) mContext.getApplicationContext());
		this.isSurah=isSurah;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataArray.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView tvEngSurahName;
		TextView tvIndexNo;
		TextView tvArabicSurahName;
		TextView tvSurahSize;
		TextView tvPlaceOfRevelation;
		RelativeLayout ayahRow;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.index_row, parent, false);
			holder = new ViewHolder();

			holder.tvEngSurahName = (TextView) convertView.findViewById(R.id.tv_index);
			holder.tvIndexNo = (TextView) convertView.findViewById(R.id.tv_index_no);
			holder.tvArabicSurahName = (TextView) convertView.findViewById(R.id.arabic_name);
			holder.tvSurahSize = (TextView) convertView.findViewById(R.id.verses);
			holder.tvPlaceOfRevelation = (TextView) convertView.findViewById(R.id.maki_madni);
			holder.ayahRow = (RelativeLayout) convertView.findViewById(R.id.index_row);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		if(isSurah) {


			// Setting fonts
			holder.tvArabicSurahName.setTypeface(mGlobal.faceArabic);
			holder.tvPlaceOfRevelation.setTypeface(mGlobal.faceContent3);
			holder.tvEngSurahName.setTypeface(mGlobal.faceContent1);
			holder.tvSurahSize.setTypeface(mGlobal.faceContent3);
			// Setting textSizes

			// Setting Text
			String arabic = ArabicUtilities.reshapeSentence(dataArray.get(position).getArabicSurahName());
			holder.tvIndexNo.setText(String.valueOf(dataArray.get(position).getSurahNo()));
			holder.tvEngSurahName.setText(dataArray.get(position).getEngSurahName());
			holder.tvArabicSurahName.setText(arabic);
			holder.tvSurahSize.setText("Verses : " + dataArray.get(position).getSurahSize() + ", ");
			holder.tvPlaceOfRevelation.setText(dataArray.get(position).getPlaceOfRevelation());
			holder.ayahRow.setBackgroundResource(R.drawable.index_row_bg);
			holder.tvSurahSize.setVisibility(View.VISIBLE);
			holder.tvPlaceOfRevelation.setVisibility(View.VISIBLE);
			holder.tvPlaceOfRevelation.setVisibility(View.VISIBLE);
		}
		else{  // for juz
			// Setting fonts
			holder.tvArabicSurahName.setTypeface(mGlobal.faceArabic);
			holder.tvPlaceOfRevelation.setTypeface(mGlobal.faceContent3);
			holder.tvEngSurahName.setTypeface(mGlobal.faceContent1);
			holder.tvSurahSize.setTypeface(mGlobal.faceContent3);

			String arabic = ArabicUtilities.reshapeSentence(dataArray.get(position).getJuzArabic());
			holder.tvIndexNo.setText(String.valueOf(dataArray.get(position).getIndexNo()));
			holder.tvEngSurahName.setText(dataArray.get(position).getJuzEnglish());
			holder.tvArabicSurahName.setText((arabic));
			holder.tvSurahSize.setVisibility(View.GONE);
			holder.tvPlaceOfRevelation.setVisibility(View.GONE);
			holder.tvPlaceOfRevelation.setVisibility(View.GONE);
			//holder.tvSurahSize.setText("Verses : " + dataArray.get(position).getSurahSize() + ", ");
			//holder.tvPlaceOfRevelation.setText(dataArray.get(position).getPlaceOfRevelation());

		}
		holder.ayahRow.setBackgroundResource(R.drawable.index_row_bg);

		if(position == mGlobal.selectedIndex)
		{
			holder.ayahRow.setBackgroundColor(Color.parseColor("#e4e4e4"));
		}

		return convertView;
	}
}
