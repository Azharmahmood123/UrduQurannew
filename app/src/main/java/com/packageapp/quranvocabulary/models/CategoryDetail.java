package com.packageapp.quranvocabulary.models;

/**
 * Created by cyber on 1/20/2016.
 */
public class CategoryDetail {

    private int id;
    private int catId;
    private String engWord;
    private String urduWord;
    private String arabicWord;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getEngWord() {
        return engWord;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }

    public String getUrduWord() {
        return urduWord;
    }

    public void setUrduWord(String urduWord) {
        this.urduWord = urduWord;
    }

    public String getArabicWord() {
        return arabicWord;
    }

    public void setArabicWord(String arabicWord) {
        this.arabicWord = arabicWord;
    }
}
