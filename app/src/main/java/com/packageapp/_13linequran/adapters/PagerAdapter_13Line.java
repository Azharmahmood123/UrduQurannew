package com.packageapp._13linequran.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.packageapp.HomeScreen1;
import com.packageapp.HomeScreen2;
import com.packageapp._13linequran.FragmentImages;
import com.packageapp._13linequran.MainActivity13LineQuran;

/**
 * Created by Aamir Riaz on 11/25/2016.
 */

public class PagerAdapter_13Line extends FragmentStatePagerAdapter {
    Fragment fragment;
    public PagerAdapter_13Line(FragmentManager fm)
    {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

       fragment=new FragmentImages();
        Bundle bundle=new Bundle();
        bundle.putInt("mPosition", position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        Log.i("PageSizes","Page size is ="+MainActivity13LineQuran.pagesSize);
        return MainActivity13LineQuran.pagesSize;
    }
}
