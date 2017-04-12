package com.QuranReading.model;

public class SurahModel 
{
	private int bookMarkId = -1;
	private String arabicAyah = "";
	private String translation = "";
	private String transliteration = "";
	private String tafseer="";



	public SurahModel (int bookMarkId, String arabicAyah, String translation, String transliteration, String tafseer)
	{
		this.bookMarkId = bookMarkId;
		this.arabicAyah = arabicAyah;
		this.translation = translation;
		this.transliteration = transliteration;
		this.tafseer=tafseer;
	}
	public String getTafseer() {
		return tafseer;
	}

	public void setTafseer(String tafseer) {
		this.tafseer = tafseer;
	}

	public int getBookMarkId() {
		return bookMarkId;
	}

	public void setBookMarkId(int bookMarkId) {
		this.bookMarkId = bookMarkId;
	}

	public String getArabicAyah() {
		return arabicAyah;
	}

	public void setArabicAyah(String arabicAyah) {
		this.arabicAyah = arabicAyah;
	}

	public String getTranslation() {
		return translation;
	}

	public void setTranslation(String translation) {
		this.translation = translation;
	}

	public String getTransliteration() {
		return transliteration;
	}

	public void setTransliteration(String transliteration) {
		this.transliteration = transliteration;
	}
}
