package com.packageapp.quranvocabulary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.R;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;
import com.packageapp.quranvocabulary.models.Categories;

import java.util.ArrayList;

/**
 * Created by cyber on 1/20/2016.
 */
public class IndexAdapter extends ArrayAdapter {

    private ArrayList<Categories> arrayList;
    private Context context;
    private SettingsSharedPref sharedPref;

    public IndexAdapter(Context context, int resource, ArrayList<Categories> list) {
        super(context, resource);
        this.context = context;
        this.arrayList = list;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_layout,parent,false);

        sharedPref = new SettingsSharedPref(context);
        TextView layoutId = (TextView) rootView.findViewById(R.id.index_layout_id);
        TextView layoutText = (TextView) rootView.findViewById(R.id.index_layout_text);
        TextView layoutIdUrdu = (TextView) rootView.findViewById(R.id.index_layout_urdu_id);
        TextView layoutTextUrdu = (TextView) rootView.findViewById(R.id.index_layout_urdu_text);
        if (sharedPref.getLanguageQuranVocabulary() != 0) {
            layoutId.setVisibility(View.VISIBLE);
            layoutText.setVisibility(View.VISIBLE);
            layoutIdUrdu.setVisibility(View.GONE);
            layoutTextUrdu.setVisibility(View.GONE);
            layoutId.setText(String.valueOf(arrayList.get(position).getId()));
            layoutText.setText(String.valueOf(arrayList.get(position).getEngCat()));
        }else {
            layoutId.setVisibility(View.GONE);
            layoutText.setVisibility(View.GONE);
            layoutIdUrdu.setVisibility(View.VISIBLE);
            layoutTextUrdu.setVisibility(View.VISIBLE);
            layoutIdUrdu.setText(String.valueOf(arrayList.get(position).getId()));
            layoutTextUrdu.setText(String.valueOf(arrayList.get(position).getUrduCat()));
        }

        return rootView;
    }
}
