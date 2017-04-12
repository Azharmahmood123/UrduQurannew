package com.QuranReading.model;

public class RubnaDuaModel {
    private int bookMarkId = -1;
    private String surahName = "";
    private String translation = "";
    private int surahNo = -1;
    private String AyahNo = "";

    public RubnaDuaModel(int bookMarkId, String surahName, int surahNo, String AyahNo) {
        this.bookMarkId = bookMarkId;
        this.surahName = surahName;
        this.surahNo = surahNo;
        this.AyahNo = AyahNo;
    }

    //************************* For WordSearch **************************//
    public RubnaDuaModel(int bookMarkId, String surahName, int surahNo,
                         String AyahNo, String translation) {
        this.bookMarkId = bookMarkId;
        this.surahName = surahName;
        this.surahNo = surahNo;
        this.AyahNo = AyahNo;
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getBookMarkId() {
        return bookMarkId;
    }

    public void setBookMarkId(int bookMarkId) {
        this.bookMarkId = bookMarkId;
    }

    public String getsurahName() {
        return surahName;
    }

    public void setsurahName(String surahName) {
        this.surahName = surahName;
    }

    public int getsurahNo() {
        return surahNo;
    }

    public void setsurahNo(int surahNo) {
        this.surahNo = surahNo;
    }

    public String getAyahNo() {
        return AyahNo;
    }

    public void setAyahNo(String AyahNo) {
        this.AyahNo = AyahNo;
    }
}
