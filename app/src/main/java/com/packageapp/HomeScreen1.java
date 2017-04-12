package com.packageapp;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.QuranReading.urduquran.GlobalClass;
import com.QuranReading.urduquran.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreen1 extends Fragment {



    public HomeScreen1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home_screen1, container, false);
        setTypeFace(v);

        return v;
    }
private void setTypeFace(View v) {
    GlobalClass mGlobal= (GlobalClass) getActivity().getApplicationContext();
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    tv1= (TextView) v.findViewById(R.id.tv1);
    tv2= (TextView) v.findViewById(R.id.tv2);
    tv3= (TextView) v.findViewById(R.id.tv3);
    tv4= (TextView) v.findViewById(R.id.tv4);
    tv5= (TextView) v.findViewById(R.id.tv5);
    tv6= (TextView) v.findViewById(R.id.tv6);
    tv1.setTypeface(mGlobal.robotoLight);
    tv2.setTypeface(mGlobal.robotoLight);
    tv3.setTypeface(mGlobal.robotoLight);
    tv4.setTypeface(mGlobal.robotoLight);
    tv5.setTypeface(mGlobal.robotoLight);
    tv6.setTypeface(mGlobal.robotoLight);
}


}
