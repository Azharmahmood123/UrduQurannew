package com.packageapp._13linequran;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.QuranReading.urduquran.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentImages extends Fragment {
    ImageView imgQuranPage;
    Bundle bundle;
    int tempId;
    int randomTemp;


    public FragmentImages() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView;
       rootView =inflater.inflate(R.layout.fragment_fragment_images, container, false);
        bundle=getArguments();
        if(bundle.containsKey("mPosition"))
        {
            tempId=bundle.getInt("mPosition");
            randomTemp=tempId;
            //randomTemp+=1;

        }
        imgQuranPage= (ImageView) rootView.findViewById(R.id.imgQuranPage);
        if(MainActivity13LineQuran.pagesSize==50)
        {
            imgQuranPage.setImageResource(Integer.parseInt(MainActivity13LineQuran.listimagesQuran.get(randomTemp)));
        }
        else {
            if (randomTemp > 799) {

                imgQuranPage.setImageResource(Integer.parseInt(MainActivity13LineQuran.listimagesQuran.get(randomTemp)));
            } else {
                imgQuranPage.setImageDrawable(Drawable.createFromPath(MainActivity13LineQuran.listimagesQuran.get(randomTemp).toString()));
            }
        }


        return  rootView;
    }

}
