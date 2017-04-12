package com.packageapp.wordbyword;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.QuranReading.urduquran.GlobalClass;

public class DetailPageAdapter extends FragmentPagerAdapter
{
    private String[] surahTitleList, ayahList, ayahTranslationList, ayahTransliterationList;
    Context ctx;
    GlobalClass globalObject;

    public DetailPageAdapter(FragmentManager supportFragmentManager,String[] dataList1, int surapos, Context ct)
    {
        super(supportFragmentManager);
        this.ctx = ct;
        this.surahTitleList = dataList1;
    }

    @Override
    public Fragment getItem(int arg0)
    {
        globalObject= (GlobalClass)ctx.getApplicationContext();
        String strTemp=surahTitleList[arg0].replace("-", "_");
        String pakage=String.valueOf(ctx.getPackageName());
        int ayahListId = ctx.getResources().getIdentifier(strTemp,"array", ctx.getPackageName());

        ayahList = ctx.getResources().getStringArray(ayahListId);

        int ayahListTranslationId = ctx.getResources().getIdentifier(surahTitleList[arg0].replace("-", "_")+"_translation", "array", ctx.getPackageName());
        ayahTranslationList = ctx.getResources().getStringArray(ayahListTranslationId);

        int ayahListTransliterationId = ctx.getResources().getIdentifier(surahTitleList[arg0].replace("-", "_")+"_transliteration", "array", ctx.getPackageName());
        ayahTransliterationList = ctx.getResources().getStringArray(ayahListTransliterationId);


        SurahFragment myFragment = new SurahFragment();
        Bundle args = new Bundle();
        args.putStringArray("ayasArray", ayahList);
        args.putStringArray("translationArray", ayahTranslationList);
        args.putStringArray("transliterationArray", ayahTransliterationList);
        args.putInt("surahPos", arg0);
        myFragment.setArguments(args);
        return myFragment;


    }


    @Override
    public int getCount() {
        return surahTitleList.length;
    }

}
