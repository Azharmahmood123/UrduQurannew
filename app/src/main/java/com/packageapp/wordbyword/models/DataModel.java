package com.packageapp.wordbyword.models;

/**
 * Created by Aamir Riaz on 12/20/2016.
 */

public class DataModel {
    public String arabicText;
    public String translationText;
    public String transliterationText;


    public void setTexts(String arabic, String translation,String transliteration) {
        arabicText = arabic;
        translationText = translation;
        transliterationText = transliteration;

    }

    public String getArabicText() {
        return arabicText;
    }

    public String getTranslationText() {
        return translationText;
    }

    public String gettransliterationText() {
        return transliterationText;
    }



}