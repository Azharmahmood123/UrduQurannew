package com.packageapp.quranvocabulary.models;

import java.util.ArrayList;

/**
 * Created by cyber on 1/26/2016.
 */
public class QuizModel {

    private int wordId;
    private int categoryId;
    private String urduMeaning;
    private String engMeaning;
    private String arabicWord;

    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public String getUrduMeaning() {
        return urduMeaning;
    }

    public void setUrduMeaning(String urduMeaning) {
        this.urduMeaning = urduMeaning;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getEngMeaning() {
        return engMeaning;
    }

    public void setEngMeaning(String engMeaning) {
        this.engMeaning = engMeaning;
    }

    public String getArabicWord() {
        return arabicWord;
    }

    public void setArabicWord(String arabicWord) {
        this.arabicWord = arabicWord;
    }
}
