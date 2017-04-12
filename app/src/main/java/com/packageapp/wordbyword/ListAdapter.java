package com.packageapp.wordbyword;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ArabicText.Helper.ArabicUtilities;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;


public class ListAdapter extends BaseAdapter {
    private Context context;
    private String[] titleList, titleListArabic;
    private LayoutInflater inflator;
    private Boolean fromKalmaList;
    private GlobalClass globalClass;

    public ListAdapter(Context context1, String[] dataList1, String[] titleListArabic1, Boolean fromDuaList, Boolean fromKalma) {
        this.context = context1;
        this.titleList = dataList1;
        this.titleListArabic = titleListArabic1;
        this.inflator = LayoutInflater.from(this.context);
        globalClass = ((GlobalClass) context.getApplicationContext());
        this.fromKalmaList = fromKalma;
    }

    @Override
    public int getCount() {
        return titleList.length;
    }

    @Override
    public Object getItem(int position) {
        return titleList[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflator.inflate(R.layout.sura_list_row_view, null);
            viewHolder.titleEng = (TextView) convertView.findViewById(R.id.suraTitleEnglish);
            viewHolder.titleArabic = (TextView) convertView.findViewById(R.id.suraTitleArabic);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (fromKalmaList)
            viewHolder.titleEng.setText(titleList[position]);
        else
            viewHolder.titleEng.setText("Surah " + titleList[position]);

       viewHolder.titleArabic.setText((titleListArabic[position]));

        viewHolder.titleEng.setTypeface(globalClass.robotoLight);
        viewHolder.titleArabic.setTypeface(globalClass.faceArabic1);


        return convertView;
    }

    private class ViewHolder {
        TextView titleEng, titleArabic;
    }

}
