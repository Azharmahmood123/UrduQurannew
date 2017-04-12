package com.packageapp.tajweedquran;

import android.content.Context;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;



public class DetailPager extends FragmentStatePagerAdapter {
    int id;
    Context mContext;
    View viewLayout;
   Fragment fragment;
    Global mGlobal;
    int pageSize;


    public DetailPager(FragmentManager fm, int Id, Context context, int pageCount) {
        super(fm);
        this.mContext = context;
        this.id = Id;
        pageSize=pageCount;

    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("mPosition", position);
        
        if(pageSize==10)
        {
        	fragment=new Components_Of_Tajweed();
            fragment.setArguments(bundle);                 

        }
        else
        {
        	fragment = new Rules_Of_Tajweed();
            fragment.setArguments(bundle);
        }
/*switch (position) {
            case 2:
                // set Fragmentclass Arguments
            	 fragment=new Components_Of_Tajweed();
                 fragment.setArguments(bundle);
                fragment = new CommonFragment();
                fragment.setArguments(bundle);
               break;


            case 3:
                // set Fragmentclass Arguments
            	fragment = new Rules_Of_Tajweed();
                fragment.setArguments(bundle);
                fragment = new CommonFragment();
                fragment.setArguments(bundle);
                break;
*//*
            case 2:
                fragment=new Components_Of_Tajweed();
                fragment.setArguments(bundle);
                break;*//*
            case 12:
                // this position is to be set later
                // set Fragmentclass Arguments
                fragment = new Rules_Of_Tajweed();
                fragment.setArguments(bundle);
                break;
            default:
                
        }*/

       return  fragment;


    }
    @Override
    public int getItemPosition(Object object) {

        if (id >= 0) {
            return id;
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public int getCount() {
        return pageSize ;
    }

}

