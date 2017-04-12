package com.QuranReading.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SettingsSharedPref {
	private Editor editor;
	private Context _context;
	private int PRIVATE_MODE = 0;
	private SharedPreferences pref;
	private static final String PREF_NAME = "SettingsPref";
	public static final String DBVERSION = "dbVersion";
	public static final String DEVICE = "device";
	public static final String DEVICECHK = "deviceChk";
	public static final String LAST_READ = "lastRead";
	public static final String LAST_READ_SURAH = "lastReadSurah";
	public static final String LAST_READ_AYAH = "lastReadAyah";
	public static final String FONT_INDEX = "fontIndex";
	public static final String FONT_ARABIC = "fontArabic";
	public static final String FONT_ENGLISH = "fontEnglish";
	public static final String RECITER = "reciter";
	public static final String TRANSLATION = "translation";
	public static final String TRANSLITERATION = "transliteration";
	public static final String NOTIFICATION = "notification";
	public static final String TAFSEER = "urduTafseer";
	public static final String SURAHNO = "SurahNo";
	public static final String FIRSTTIME = "FirstTime";
	public static final String LastReadAyaNo = "AyaNo";
	public static final String RegisterReciever = "Reciever";
	public static final String TRANSLATIONLANGUAGE = "TranslationLanguage";
	public static final String APP_EXIT_DIALOG_POS = "app_exit_dialog_pos";
	public static final String USER_COUNTER = "user_counter";

	public static final String Tajweed_Notification = "tajweed_notificatn";
	public static final String Vocabulary_Notification = "vocabulary_notificatn";
	public static final String surah_Notification = "surah_notificatn";

	//WBW Data
	public static final String COUNTRY_LANGUAGE = "country";
	public static final String FACEARABIC = "faceArabic";
	public static final String FONTINDEX = "fontIndex";
	public static final String ARABICFONT = "arabicFontSize";
	public static final String ENGLISHFONT = "englishFontSize";
	public static final String FAVOURITESURAS = "favoriteSura";
	public static final String FAVOURITEDUAS = "favoriteDua";
	public static final String FAVOURITEKALMAS= "favoriteKalmas";
	public static final String FAVOURITENAMES = "favoriteNames";
	public static final String FAVOURITEDUASPOS = "favoriteDuaPos";

	public static final String TOTALSURAHS = "favoriteKalma";
	public static final String FIRSTTIMEAPPCHECK = "firstTime";
	public static final String NOTIFICATONNAMEPOS = "NotiNamePos";

	public static final String NAMESPRESENATATIONCHECK = "namesCheck";
	public static final String WBWCALIBRATIONCHECK = "wbwcheck";
	public static final String SWIPECALIBRATIONCHECK = "swipecheck";
	public static final String DISCLAIMERCHECK = "DisclaimerCheck";

	public SettingsSharedPref(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setTajweed_Notification(boolean notif) {
		editor.putBoolean(Tajweed_Notification, notif);
		editor.commit();
	}
	public void setTajweedRandomNumber(int value) {
		editor.putInt("TajweedRandomNumber", value);
		editor.commit();
	}
	public int getTajweedRandomNumber() {
		return pref.getInt("TajweedRandomNumber", 0);
	}

	public boolean getTajweed_Notification() {
		return pref.getBoolean(Tajweed_Notification, true);
	}
	public void setVocabulary_Notification_(boolean notif) {
		editor.putBoolean(Vocabulary_Notification, notif);
		editor.commit();
	}

	public boolean getVocabulary_Notification() {
		return pref.getBoolean(Vocabulary_Notification, true);
	}

	public void setSurah_Notification(boolean notif) {
		editor.putBoolean(surah_Notification, notif);
		editor.commit();
	}

	public boolean getSurah_Notification() {
		return pref.getBoolean(surah_Notification, true);
	}

	public void saveSettings(int f_index, int f_Arabic, int f_english, int reciter, int translation, boolean transliteration, boolean notification,boolean tafseer) {
		editor.putInt(FONT_INDEX, f_index);
		editor.putInt(FONT_ARABIC, f_Arabic);
		editor.putInt(FONT_ENGLISH, f_english);
		editor.putInt(RECITER, reciter);
		editor.putInt(TRANSLATION, translation);
		editor.putBoolean(TRANSLITERATION, transliteration);
		editor.putBoolean(NOTIFICATION, notification);
		editor.putBoolean(TAFSEER,tafseer);
		editor.commit();
	}

	public void setFirstTime() {
		editor.putBoolean(FIRSTTIME, false);
		editor.commit();
	}

	public boolean getFirstTime() {
		return pref.getBoolean(FIRSTTIME, true);
	}

	public void setDevice(String device) {
		editor.putString(DEVICE, device);
		editor.putBoolean(DEVICECHK, true);
		editor.commit();
	}

	public boolean getDeviceChk() {
		return pref.getBoolean(DEVICECHK, false);
	}

	public String getDevice() {
		return pref.getString(DEVICE, "small");
	}

	public int getPosForExitDialog() {
		return pref.getInt(APP_EXIT_DIALOG_POS, 0);
	}

	public void setPosForExitDialog(int pos) {
		editor.putInt(APP_EXIT_DIALOG_POS, pos);
		editor.commit();
	}

	public void setLastReadSurahNo(int index) {
		editor.putInt(LAST_READ_SURAH, index);
		editor.commit();
	}
	public void setLastReadAyahNo(int index) {
		editor.putInt(LAST_READ_AYAH, index);
		editor.commit();
	}
	public  int getLastReadSurahNo(){
		return  pref.getInt(LAST_READ_SURAH,-1);
	}
	public  int getLastReadAyahNo(){
		return  pref.getInt(LAST_READ_AYAH,-1);
	}

	public void setSurahNo(int index) {
		editor.putInt(SURAHNO, index);
		editor.commit();
	}

	public int getSurahNo() {
		return pref.getInt(SURAHNO, 0);
	}

	public HashMap<String, Boolean> getTransSettings() {
		HashMap<String, Boolean> settngs = new HashMap<String, Boolean>();
		// settngs.put(TRANSLATION, pref.getBoolean(TRANSLATION, false));
		settngs.put(TRANSLITERATION, pref.getBoolean(TRANSLITERATION, false));
		settngs.put(NOTIFICATION, pref.getBoolean(NOTIFICATION, true));
		settngs.put(TAFSEER, pref.getBoolean(TAFSEER, false));
		return settngs;
	}

	public HashMap<String, Integer> getFontSettings() {
		HashMap<String, Integer> settngs = new HashMap<String, Integer>();
		settngs.put(FONT_INDEX, pref.getInt(FONT_INDEX, 0));
		settngs.put(FONT_ARABIC, pref.getInt(FONT_ARABIC, 0));
		settngs.put(FONT_ENGLISH, pref.getInt(FONT_ENGLISH, 0));
		settngs.put(RECITER, pref.getInt(RECITER, 0));
		settngs.put(LAST_READ, pref.getInt(LAST_READ, -1));
		return settngs;
	}

	public void setReciter(int reciter) {
		editor.putInt(RECITER, reciter);
		editor.commit();
	}

	public void setAyano(int ayahPos) {
		editor.putInt(LastReadAyaNo, ayahPos);
		editor.commit();
	}

	public int getAyano() {
		return pref.getInt(LastReadAyaNo, 1);
	}

	public int getTranslationLanguage() {
		return pref.getInt(TRANSLATION, 1);
	}

	public void setUserCounter(int number) {
		editor.putInt(USER_COUNTER, number);
		editor.commit();
	}

	public int getUserCounter() {
		return pref.getInt(USER_COUNTER, 1);
	}

	public void setMukhrujPositon(int listPos) {
		editor.putInt("ListItem", listPos);
		editor.commit();
	}
	// tajweed quran

	public void setDbVersion(int v) {
		editor.putInt(DBVERSION, v);
		editor.commit();
	}

	public int chkDbVersion() {
		return pref.getInt(DBVERSION, 1);
	}

	public void calibrationValue(boolean boolValue) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("calibration", boolValue);
		editor.commit();

	}

	public boolean getCalibrationValue() {
		boolean caliValue = pref.getBoolean("calibration", true);
		return caliValue;
	}


	public int getMukhrujPos() {
		return pref.getInt("ListItem", -1);

	}
	public void setTajweedFirstTime(boolean value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("Tajweed_FirstTime", value);
		editor.commit();
	}
	public boolean isTajweedFirstTime()
	{
		return pref.getBoolean("Tajweed_FirstTime", true);

	}
	public int getLanguageTajweed() {

		return pref.getInt("isEngTajweed", 0);
	}

	public void setLanguageTajweed(int isEnglish) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("isEngTajweed", isEnglish);
		editor.commit();
	}

	/*Quran vocabulary Preferences values*/
	public int getLanguageQuranVocabulary() {

		return pref.getInt("isEng", 0);
	}

	public void setLanguageQuranVocabulary(int isEnglish) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("isEng", isEnglish);
		editor.commit();
	}
	/*public int getUserCounter() {
		return pref.getInt("userCounter", 1);
	}

	public void setUserCounter(int userCounter) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("userCounter", userCounter);
		editor.commit();
	}
*/



	public int getNotification() {
		return pref.getInt("isNotification", 0);
	}

	public void setNotification(int isActive) {

		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("isNotification", isActive);
		editor.commit();
	}

	public boolean getDetailQuranVocabulary() {

		return pref.getBoolean("isDetailActive", true);
	}

	public void setDetailQuranVocabulary(boolean isActive) {
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("isDetailActive", isActive);
		editor.commit();
	}

	public void calibrationValueQuranVocabulary(boolean boolValue)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("calibration",boolValue);
		editor.commit();

	}
	public boolean getCalibrationValueQuranVocabulary()
	{
		boolean caliValue=pref.getBoolean("calibration",true);
		return caliValue;
	}

	//Quran Facts Notification data
	public boolean getQuran_facts_Notification()
	{
		return pref.getBoolean("QuranFactsNotification",true);
	}
	public void setQuran_facts_Notification(boolean value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("QuranFactsNotification",value);
		editor.commit();
	}
	public boolean getFacts_random(){
		return  pref.getBoolean("QuranFacts",true);
	}
	public void setFacts_random(boolean value){
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean("QuranFacts",value);
		editor.commit();
	}
	//WBW Preferences

	public void setNamesPresCheck(boolean popUp)
	{
		editor.putBoolean(NAMESPRESENATATIONCHECK, popUp);
		editor.commit();
	}

	public boolean getNamesPresCheck()
	{
		return pref.getBoolean(NAMESPRESENATATIONCHECK, true);
	}

	public void setWordBWordCheck(boolean popUp)
	{
		editor.putBoolean(WBWCALIBRATIONCHECK, popUp);
		editor.commit();
	}

	public boolean getWordBWordCheck()
	{
		return pref.getBoolean(WBWCALIBRATIONCHECK, true);
	}


	public void setSwipeCheck(boolean popUp)
	{
		editor.putBoolean(SWIPECALIBRATIONCHECK, popUp);
		editor.commit();
	}

	public boolean getSwipeCheck()
	{
		return pref.getBoolean(SWIPECALIBRATIONCHECK, true);
	}


	public void setNotiNamePos(int namePOs)
	{
		editor.putInt(NOTIFICATONNAMEPOS, namePOs);
		editor.commit();
	}

	public int getNotiNamePos()
	{
		return pref.getInt(NOTIFICATONNAMEPOS, 0);
	}


	public void saveFavSuraList(String suras )
	{
		editor.putString(FAVOURITESURAS, suras);
		editor.commit();

	}

	public String getFavSurahsList()
	{
		return pref.getString(FAVOURITESURAS, "");
	}

	public void saveFavNamesList(String favNames)
	{
		editor.putString(FAVOURITENAMES, favNames);
		editor.commit();

	}

	public String getFavNamesList()
	{
		return pref.getString(FAVOURITENAMES, "");
	}


	public void saveFavDuaList(String duaNames)
	{
		editor.putString(FAVOURITEDUAS, duaNames);
		editor.commit();
	}
	public String getFavDuaList()
	{
		return pref.getString(FAVOURITEDUAS, "");
	}


	public void saveFavDuaPosList(String pos)
	{
		editor.putString(FAVOURITEDUASPOS, pos);
		editor.commit();
	}
	public String getFavDuaPosList()
	{
		return pref.getString(FAVOURITEDUASPOS, "");
	}





	public void saveFavKalmaList(String kalmaNames)
	{
		editor.putString(FAVOURITEKALMAS, kalmaNames);
		editor.commit();
	}
	public String getFavKalmaList()
	{
		return pref.getString(FAVOURITEKALMAS, "");
	}


	public void setFirstTymCheck(boolean firstTime)
	{
		editor.putBoolean(FIRSTTIMEAPPCHECK, firstTime);
		editor.commit();
	}

	public boolean getFirstTymCheck()
	{
		return pref.getBoolean(FIRSTTIMEAPPCHECK, true);
	}


	public void setTransliteration(boolean transl)
	{
		editor.putBoolean(TRANSLITERATION, transl);
		editor.commit();
	}

	public boolean getTransliteration()
	{
		return pref.getBoolean(TRANSLITERATION, false);
	}


	public void setTranslation(boolean translation)
	{
		editor.putBoolean(TRANSLATION, translation);
		editor.commit();
	}

	public boolean getTranslation()
	{
		return pref.getBoolean(TRANSLATION, true);
	}


	/*public void setNotification(boolean notification)
	{
		editor.putBoolean(NOTIFICATION, notification);
		editor.commit();
	}*/

/*	public boolean getNotification()
	{
		return pref.getBoolean(NOTIFICATION, true);
	}*/

	public void setDisclaimerCheck(boolean dialogShown)
	{
		editor.putBoolean(DISCLAIMERCHECK, dialogShown);
		editor.commit();
	}

	public boolean getDisclaimerCheck()
	{
		return pref.getBoolean(DISCLAIMERCHECK, false);
	}

	public void saveTextSettings(int faceArabic, int fontIndex, int arabicFont, int englishFont)
	{
		editor.putInt(FACEARABIC, faceArabic);
		editor.putInt(FONTINDEX, fontIndex);
		editor.putInt(ARABICFONT, arabicFont);
		editor.putInt(ENGLISHFONT, englishFont);
		editor.commit();
	}

	public HashMap<String, Integer> getSettings()
	{
		HashMap<String, Integer> settngs = new HashMap<String, Integer>();
		settngs.put(FACEARABIC, pref.getInt(FACEARABIC, 1));
		settngs.put(FONTINDEX, pref.getInt(FONTINDEX, 2));
		settngs.put(ARABICFONT, pref.getInt(ARABICFONT, 1));
		settngs.put(ENGLISHFONT, pref.getInt(ENGLISHFONT, 1));
		return settngs;
	}

	public Integer getTotalSurah()
	{
		return pref.getInt(TOTALSURAHS, 0);
	}

//pref for download checking
	public boolean isAppActive()
	{
		return  pref.getBoolean("appState",false);
	}
	public void setAppActive(boolean value)
	{
		 editor.putBoolean("appState",value);
		editor.commit();
	}
	public boolean isFirstTimeAppLaunched()
	{
		return pref.getBoolean("firsTimeLaunched",true);
	}
	public void setFirstTimeAppLaunched(boolean value)
	{
		editor.putBoolean("firsTimeLaunched",value);
		editor.commit();

	}
	public boolean isFirstTimeTranslation()
	{
		return pref.getBoolean("Text_Translation",true);
	}
	public void setFirstTimeTranslation(boolean value){
		editor.putBoolean("Text_Translation",value);
		editor.commit();
	}


}