package com.packageapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.packageapp.HomeScreen1;
import com.packageapp.HomeScreen2;
import com.packageapp.HomeScreen3;

/**
 * Created by Aamir Riaz on 11/11/2016.
 */

public class HomeScreenAdapter extends FragmentStatePagerAdapter {
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
    public HomeScreenAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        /*Bundle bundle = new Bundle();
        bundle.putInt("mPosition", position);
         fragment.setArguments(bundle);*/
        // set Fragmentclass Arguments
        if (position == 0) {
            HomeScreen1 fragment = new HomeScreen1();
            return fragment;
        } else if (position == 1) {
            HomeScreen2 fragment = new HomeScreen2();
            return fragment;
        } else
        {
            HomeScreen3 fragment = new HomeScreen3();
            return fragment;
        }


        ///return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
