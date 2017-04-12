package com.QuranReading.model;

import android.content.Context;

/**
 * Created by Aamir Riaz on 2/28/2017.
 */

public class JuzModel {

    private int JuzNo,SurahNo,AyahNO;



    public JuzModel( int juzNo, int SurahNo, int AyahNo)
    {
        this.JuzNo=juzNo;
        this.SurahNo=SurahNo;
        this.AyahNO=AyahNo;
    }

    public int getJuzNo() {
        return JuzNo;
    }

    public int getSurahNo() {
        return SurahNo;
    }

    public int getAyahNO() {
        return AyahNO;
    }

}
