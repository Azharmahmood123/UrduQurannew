package com.packageapp;


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
public class HomeScreen2 extends Fragment {


    public HomeScreen2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_home_screen2, container, false);
       setTypeFace(v);
        return v;
    }
    private void setTypeFace(View v) {
        GlobalClass mGlobal= (GlobalClass) getActivity().getApplicationContext();
        TextView tv7,tv8,tv9,tv10,tv11,tv12;
        tv7= (TextView) v.findViewById(R.id.tv7);
        tv8= (TextView) v.findViewById(R.id.tv8);
        tv9= (TextView) v.findViewById(R.id.tv9);
        tv10= (TextView) v.findViewById(R.id.tv10);
        tv11= (TextView) v.findViewById(R.id.tv11);
        tv12= (TextView) v.findViewById(R.id.tv12);


        tv7.setTypeface(mGlobal.robotoLight);
        tv8.setTypeface(mGlobal.robotoLight);
        tv9.setTypeface(mGlobal.robotoLight);
        tv10.setTypeface(mGlobal.robotoLight);
        tv11.setTypeface(mGlobal.robotoLight);
        tv12.setTypeface(mGlobal.robotoLight);

    }


}
