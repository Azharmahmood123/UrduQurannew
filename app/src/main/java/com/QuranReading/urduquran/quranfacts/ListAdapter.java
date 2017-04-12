package com.QuranReading.urduquran.quranfacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;

/**
 * Created by Aamir Riaz on 12/20/2016.
 */

public class ListAdapter extends BaseAdapter {
    String[] listdata;
    Context context;
    LayoutInflater layoutInflater;
    public ListAdapter(Context context,String[] data){
        this.context=context;
        this.listdata=data;
        layoutInflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return listdata.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder{
        TextView tvName;
        TextView tvIndexNo;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        ViewHolder viewHolder;
        if (view==null)
        {   view=layoutInflater.inflate(R.layout.bookmarks_row,null);
            viewHolder=new ViewHolder();
            viewHolder.tvName=(TextView)view.findViewById(R.id.tv_index);
            viewHolder.tvIndexNo=(TextView)view.findViewById(R.id.tv_index_no);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)  view.getTag();
        }

        viewHolder.tvIndexNo.setTypeface(((GlobalClass) context.getApplicationContext()).faceContent1);
        viewHolder.tvName.setTypeface(((GlobalClass) context.getApplicationContext()).faceContent1);

        viewHolder.tvIndexNo.setText(String.valueOf(i + 1));
        viewHolder.tvName.setText(listdata[i]);
        return view;
    }
}

