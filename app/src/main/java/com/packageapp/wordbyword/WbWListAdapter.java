package com.packageapp.wordbyword;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.ArabicText.Helper.ArabicUtilities;
import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;
import com.packageapp.wordbyword.models.DataModel;

import java.util.ArrayList;


public class WbWListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DataModel> dataList;
    LayoutInflater inflator;
    GlobalClass globalObject;
    SpannableString WordtoSpan;

    LinearLayout linear;
    LinearLayout testLayout;
    public int LISTITEMLIMIT = 3;
    int pos , wordTag;
    int p = 0;

    Intent intent;
    IntentFilter filter;

    public WbWListAdapter(Context context1, ArrayList<DataModel> dataList1)
    {
        this.context = context1;
        this.dataList = dataList1;
        this.inflator = LayoutInflater.from(this.context);
        globalObject = (GlobalClass)context1.getApplicationContext();
    }

    @Override
    public int getCount()
    {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        DataModel surah = dataList.get(position);
        pos = position;

        String[] splitedArr = surah.getArabicText().toString().split("\\ ");

        viewHolder = new ViewHolder();
        convertView = inflator.inflate(R.layout.word_surah_row_view, null);
        testLayout = (LinearLayout) convertView.findViewById(R.id.wbw_ayah_row1);

        viewHolder.ayahNumber = (TextView) convertView.findViewById(R.id.text_aya_num);
        viewHolder.ayaNumLayout = (LinearLayout) convertView.findViewById(R.id.layout_ayah_no);

        if (position == 0)
        {
            viewHolder.ayaNumLayout.setVisibility(View.GONE);
        }
        else
        {
            viewHolder.ayaNumLayout.setVisibility(View.VISIBLE);
            viewHolder.ayahNumber.setText(String.valueOf(position));
        }

        viewHolder.ayahNumber.setText("" + position);
        wordDivider(splitedArr);

        return convertView;
    }

    @SuppressLint("NewApi")
    private void wordDivider(String[] splitingArr)
    {
        int length = splitingArr.length;
        int limit = 0;
        int cont = 0;
        int cont1 = 1;
        for (int j = length; j > 0; j = j - LISTITEMLIMIT)
        {
            linear = new LinearLayout(context);
            linear.setOrientation(LinearLayout.HORIZONTAL);
            int id=100+1;
            linear.setId(id);
            linear.setRotationY(180);
            linear.setRotationX(360);

            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            limit = (cont1) * LISTITEMLIMIT;

            cont1++;
            for (int i = cont; i < limit; i++)
            {
                if (i < splitingArr.length)
                {
                    String s = ArabicUtilities.reshapeSentence(splitingArr[i])+ "  ";
                    TextView tv = new TextView(context);
                    tv.setId(i);

                    tv.setPadding(0, globalObject.ayahPadding, 0, 0);
                    String wordPos = String.valueOf(i);

                    //globalObject.ayahPos = pos;
                    //globalObject.wordPos = i;
                    wordPos = wordPos + pos;

                    int ii = Integer.valueOf(wordPos);

                    tv.setTag(ii);
                    tv.setRotationY(180);
                    tv.setTypeface(globalObject.faceArabic2);
                    tv.setTextSize(globalObject.font_size_arabic);

                    if (globalObject.wordPos == i     //WBWFragment.currentIndex
                            && globalObject.ayahPosWordbWord== pos)               //WBWFragment.ayahPos
                    {
                        tv.setTextColor(Color.parseColor("#EE0000"));
                        tv.setText(s);

                    }
                    else
                    {
                        tv.setTextColor(Color.BLACK);
                        tv.setText(s);

                    }

                    tv.setOnClickListener(myTextViewListner);

                    if (i != 0)
                    {
                        params.addRule(RelativeLayout.CENTER_VERTICAL, i - 1);
                    }
                    cont++;
                    linear.addView(tv, params);
                }
            }
            testLayout.setGravity(Gravity.RIGHT);
            testLayout.addView(linear);
        }

    }

    OnClickListener myTextViewListner = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

            int i = (Integer) v.getTag();

            String val = String.valueOf(i);

            String[] tagPos = val.split("");

            p = 0;
            int tag = 0;
            if (tagPos.length < 3) {
                p = Integer.valueOf(tagPos[1]);
			/*	globalObject.ayahPos = Integer.valueOf(tagPos[0]);
				globalObject.wordPos = Integer.valueOf(tagPos[1]);*/
            }
            else
            {
                tag = Integer.valueOf(tagPos[1]);
                p = Integer.valueOf(tagPos[2]);

                //globalObject.wordPos = Integer.valueOf(tagPos[1]);
            }
            wordTag = tag;
            intent = new Intent();
            intent.setAction("wordTapBroadcast");
			/*intent.putExtra("wordPosition", tag);
			intent.putExtra("AyahPosition", p);*/
            context.sendBroadcast(intent);

            globalObject.ayahPosWordbWord = p;
            globalObject.wordPos = tag;
			/*WBWFragment.currentIndex = tag;*/


            notifyDataSetChanged();
        }
    };




    private class ViewHolder
    {
        TextView ayahArabicText, ayahNumber;
        LinearLayout ayaNumLayout;

    }
}
