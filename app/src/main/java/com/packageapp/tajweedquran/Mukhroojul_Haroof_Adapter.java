package com.packageapp.tajweedquran;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;


public class Mukhroojul_Haroof_Adapter extends BaseAdapter {
	ViewHolder holder;
	Context mContext;
	GlobalClass mGlobal;
	public static MediaPlayer mp;
	boolean checkRunning = false;
	ImageView temporayImage;
	SettingsSharedPref prefs;

	public Mukhroojul_Haroof_Adapter(Context c) {
		this.mContext = c;
		mGlobal = ((GlobalClass) mContext.getApplicationContext());
		prefs = new SettingsSharedPref(mContext);



		// mp=new MediaPlayer();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if(prefs.getLanguageTajweed()==0) //urdu language
			{
				convertView = inflater.inflate(R.layout.mukhrooj_row_item_urdu, null);
			}
			else
			{
				convertView = inflater.inflate(R.layout.mukhrooj_row_item, null);
			}

			holder.txtWords = (TextView) convertView
					.findViewById(R.id.txt_mukhroojWord);
			holder.txtMeanings = (TextView) convertView
					.findViewById(R.id.txt_mukhroojMeaning);
			holder.imgSpeaker = (ImageView) convertView
					.findViewById(R.id.imgMukhrooj);
			holder.linMukhraj = (LinearLayout) convertView
					.findViewById(R.id.linearMukhraj);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();// get the tag
		}
		holder.txtWords.setText(mGlobal.mukhroojHaroof.get(position).toString().trim()
				);
		holder.txtMeanings.setText(mGlobal.mukhroojMeanings.get(position)
				.toString().trim());
		holder.imgSpeaker.setImageResource(R.drawable.speaker);
		holder.txtWords.setTypeface(mGlobal.robotoRegular);
		holder.txtMeanings.setTypeface(mGlobal.robotoRegular);

		if (prefs.getMukhrujPos() == position) {
			holder.linMukhraj.setBackgroundColor(mContext.getResources()
					.getColor(R.color.selection_color));
		} else {
			holder.linMukhraj.setBackgroundColor(mContext.getResources()
					.getColor(R.color.white));

		}

		if (CommonFragment.onComplete) {
			holder.imgSpeaker.setImageResource(R.drawable.speaker);
			//notifyDataSetChanged();
		}

		else {
			if (position == CommonFragment.locPos) {

				holder.imgSpeaker.setImageResource(R.drawable.speaker_hover);
				notifyDataSetChanged();
				holder.linMukhraj.setBackgroundColor(mContext.getResources()
						.getColor(R.color.selection_color));
			} else {
				holder.imgSpeaker.setImageResource(R.drawable.speaker);
				holder.linMukhraj.setBackgroundColor(mContext.getResources()
						.getColor(R.color.white));
			}
		}

		return convertView;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public int getCount() {
		return mGlobal.mukhroojHaroof.size();
	}

	public class ViewHolder {
		TextView txtWords, txtMeanings;
		ImageView imgSpeaker;
		LinearLayout linMukhraj;
	}

}
