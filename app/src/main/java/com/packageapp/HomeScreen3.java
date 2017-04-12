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
 * Created by cyber on 11/28/2016.
 */

public class HomeScreen3 extends Fragment {



    public HomeScreen3() {
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home_screen_3, container, false);
        setTypeFace(v);
        return v;
    }
    private void setTypeFace(View v) {
        GlobalClass mGlobal= (GlobalClass) getActivity().getApplicationContext();
        TextView tv13;
        tv13= (TextView) v.findViewById(R.id.tv13);
        tv13.setTypeface(mGlobal.robotoLight);


    }
}
