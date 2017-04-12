package com.QuranReading.model;

public class IndexListModel 
{
	private int surahNo;
	private int surahSize;
	private String engSurahName;
	private String arabicSurahName;
	private String placeOfRevelation;
	//Juzz data for Index list
	private String juzEnglish,juzArabic;
	private int indexNo;

	public IndexListModel (int surahNo, String engName, String arabicName, String revealedPlace, int surahSizes)
	{
		this.surahNo = surahNo;
		this.engSurahName = engName;
		this.arabicSurahName = arabicName;
		this.placeOfRevelation =revealedPlace;
		this.surahSize = surahSizes;
		
		
	}
	//for initializing data of juz in index list Surah Activity
	public IndexListModel(int index, String JuzEnglish, String JuzArabic){ //for juzz click in index list
		this.juzEnglish=JuzEnglish;
		this.juzArabic=JuzArabic;
		this.indexNo=index;

	}
	public int getIndexNo(){
		return indexNo;
	}
	public String getJuzEnglish(){
		return juzEnglish;
	}
	public String getJuzArabic(){
		return juzArabic;
	}


	public int getSurahSize() {
		return surahSize;
	}

	public void setSurahSize(int surahSize) {
		this.surahSize = surahSize;
	}

	public String getEngSurahName() {
		return engSurahName;
	}

	public void setEngSurahName(String engSurahName) {
		this.engSurahName = engSurahName;
	}

	public String getArabicSurahName() {
		return arabicSurahName;
	}

	public void setArabicSurahName(String arabicSurahName) {
		this.arabicSurahName = arabicSurahName;
	}

	public String getPlaceOfRevelation() {
		return placeOfRevelation;
	}

	public void setPlaceOfRevelation(String placeOfRevelation) {
		this.placeOfRevelation = placeOfRevelation;
	}

	public int getSurahNo() {
		return surahNo;
	}

	public void setSurahNo(int surahNo) {
		this.surahNo = surahNo;
	}

}
