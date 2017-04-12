package com.QuranReading.urduquran;

import java.io.File;

import android.os.Environment;

public class Constants {

	public static final String BANNER_IMAGE_URL = "market://details?id=com.quranreading.muslim.kids";
	public static final String URL_REFFERER = "&referrer=utm_source%3DUrduQuran";
	public static final String FLD_ID = "_id";
	public static final String FLD_SURAH_NAME = "surah_name";
	public static final String FLD_SURAH_NO = "surah_no";
	public static final String FLD_AYAH_NO = "ayah_no";
	public static final String FLD_TRANSLATION = "translation";
	public static final String FLD_DOWNLOAD_ID = "download_id";
	public static final String FLD_TEMP_NAME = "temp_name";

	public static final String TBL_BOOKMARKS = "tbl_bookmarks";
	public static final String TBL_DOWNLOADS = "tbl_downloads";
	public static final String TBL_TRANSLATION = "engTranslationSaheeh";

	public static final String CHK_SUCCESSFUL = "chk_successful";
	public static final String CHK_FAILED = "chk_failed";
	public static final String CHK_RUNNING = "chk_running";
	public static final String CHK_PAUSED = "chk_paused";
	public static final String CHK_PENDING = "chk_pending";

	public static final String DB_NAME = "quran_now_db_new";

	/*
	 * public static final String ARABIC_FONT_1 = "trado.ttf"; public static final String ARABIC_FONT_2 = "AnjaliOldLipi.ttf";
	 */
	// public static final String ARABIC_FONT_1 = "trado.ttf";
	/* public static final String ARABIC_FONT_3 = "noorehira.ttf"; */

	// public static final String ARABIC_FONT = "_PDMS_IslamicFont_Android_shipped.ttf";
	public static final String ARABIC_FONT = "XBZarIndoPak.ttf";
	public static final String ARABIC_FONT1 = "PDMS_Saleem_ACQuranFont_shipped.ttf";
	public static final String ARABIC_FONT2 = "trado.ttf";
	public static final String ARABIC_FONT3 = "XBZarIndoPak.ttf";
	public static final String ARABIC_FONT4 = "noorehira.ttf";
	public static final String HEADING_FONT = "gabriola.ttf";
	public static final String CONTENT_FONT_1 = "hero.otf";
	public static final String CONTENT_FONT_3 = "helvetica_regular.ttf";
	public static final String CONTENT_FONT_4 = "futuraL.ttf";

	public static final int fontSize_A_small[] = { 28, 30, 32, 34, 36, 38 };
	public static final int fontSize_E_small[] = { 16, 18, 20, 22, 24, 26 };

	public static final int fontSize_A_med[] = { 40, 45, 55, 58, 61, 64 };
	public static final int fontSize_E_med[] = { 28, 30, 35, 43, 46, 49 };

	public static final int fontSize_A_large[] = { 44, 50, 56, 60, 64, 68 };
	public static final int fontSize_E_large[] = { 22, 26, 32, 36, 40, 44 };

	public static String extMp3 = ".mp3";
	public static String QuranFile1 = "quran_translation_basit.zip";
	public static String QuranFile2 = "quran_a.zip";
	public static final int font_size_arabic=30;
	public static String rootFolderName = "QuranNow/";
	//public static final String folderName = "QuranNow";
	// public static String audioUrl = "http://daily-edition.net/quran_pak/";
	public static File rootPath = new File(Environment.getExternalStorageDirectory(), rootFolderName);

	public static final String BroadcastActionDownload = "downloading_broadcast";
	public static final String BroadcastActionComplete = "complete_broadcastt";
	public static final String BroadcastActionNotification = "daily_surah";
}
