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


public class DuaQanootWbWListAdapter extends BaseAdapter {
    Context context;
    ArrayList<DataModel> dataList;
    LayoutInflater inflator;
    GlobalClass globalObject;
    SpannableString WordtoSpan;

    LinearLayout linear;
    LinearLayout testLayout;
    public int LISTITEMLIMIT = 3;
    private int pos , wordTag;
    private int p = 0;

    Intent intent;
    IntentFilter filter;

    public DuaQanootWbWListAdapter(Context context1, ArrayList<DataModel> dataList1)
    {
        this.context = context1;
        this.dataList = dataList1;
        this.inflator = LayoutInflater.from(this.context);
        globalObject = (GlobalClass)context1.getApplicationContext();
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

        DataModel surah = dataList.get(position);
        pos = position;
        String[]  splitedArr = surah.getArabicText().toString().split("\\ -");
        convertView = inflator.inflate(R.layout.word_row_view, null);
        //viewHolder.ayahArabicText = (TextView) convertView.findViewById(R.id.text_ayah);
        testLayout = (LinearLayout) convertView.findViewById(R.id.wbw_ayah_row);
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
            linear.setId(1);
            linear.setRotationY(180);
            linear.setRotationX(360);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);

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
                    wordPos = wordPos + pos;
                    int ii = Integer.valueOf(wordPos);
                    tv.setTag(ii);
                    tv.setRotationY(180);
                    tv.setTypeface(globalObject.faceArabic);
                    tv.setTextSize(globalObject.font_size_arabic);

                    if (WordByWord.wordPosQanoot == i     //WBWFragment.currentIndex
                            && WordByWord.ayahPosWordbWordQanoot== pos)               //WBWFragment.ayahPos
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
            }
            else
            {
                tag = Integer.valueOf(tagPos[1]);
                p = Integer.valueOf(tagPos[2]);

            }
            wordTag = tag;
            intent = new Intent();
            intent.setAction("duaWordTapBroadcast");
            context.sendBroadcast(intent);
            WordByWord.ayahPosWordbWordQanoot = p;
            WordByWord.wordPosQanoot = tag;
            notifyDataSetChanged();
        }
    };
}
