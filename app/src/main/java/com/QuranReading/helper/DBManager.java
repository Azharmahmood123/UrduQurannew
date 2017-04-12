package com.QuranReading.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.QuranReading.sharedPreference.PurchasePreferences;
import com.QuranReading.urduquran.R;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class DBManager {

	public static final String TABLE_URLS = "Urls";
	public static final String FLD_ASIA = "asia";
	public static final String FLD_EU = "eu";
	public static final String FLD_US = "us";
	public static final String FLD_RECITER = "reciter";

	public static final String RECITER_ALAFSAY = "alafasy";
	public static final String RECITER_BASIT = "basit";
	public static final String RECITER_SUDAIS = "sudais";

	private final Context context;
	///////////////////////////// Column Names //////////////////////////////

	public static final String FLD_ID = "_id";
	public static final String FLD_SURAH_NAME = "surah_name";
	public static final String FLD_SURAH_NO = "surah_no";
	public static final String FLD_AYAH_NO = "ayah_no";
	public static final String FLD_TRANSLATION = "translation";
	public static final String FLD_DOWNLOAD_ID = "download_id";
	public static final String FLD_TEMP_NAME = "temp_name";

	public static final String TBL_BOOKMARKS = "tbl_bookmarks";
	public static final String TBL_DOWNLOADS = "tbl_downloads";
	public static final String TBL_ENG_TRANSLATION = "engTranslationSaheeh";

	public static final String DATABASE_NAME = "quran_now_db_new";
	public static final String CHK_SUCCESSFUL = "chk_successful";
	public static final String CHK_FAILED = "chk_failed";
	public static final String CHK_RUNNING = "chk_running";
	public static final String CHK_PAUSED = "chk_paused";
	public static final String CHK_PENDING = "chk_pending";

	///////////////////////////// Table Create Queries ////////////////////////////
   // old version was 3 amir you changed it dont panic
	private static final int DATABASE_VERSION = 4;

	/*
	 * private static final String CREATE_TBLLOCATION =
	 * 
	 * "create table " + TBL_LOCATION + "(" + FLD_ID + " integer not null, " + FLD_NAME + " text not null, " + FLD_ALARM + " integer not null);";
	 */

	///////////////////////// HELPER CLASS TO CREATE DATABASE //////////////////////

	private DatabaseHelper DBHelper;
	PurchasePreferences prefs;
	private SQLiteDatabase db;

	public DBManager(Context ctx) {
		this.context = ctx;
		prefs = new PurchasePreferences(context);
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}

	///////////////////////////// Opens Database ////////////////////////////

	public DBManager open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	///////////////////////// Insert Into Bookmark Table //////////////////////

	public long addBookmark(String surahName, int surahNo, int ayahNo) {
		ContentValues newValues = new ContentValues();
		newValues.put(FLD_SURAH_NAME, surahName);
		newValues.put(FLD_SURAH_NO, surahNo);
		newValues.put(FLD_AYAH_NO, ayahNo);

		return db.insert(TBL_BOOKMARKS, null, newValues);
	}

	////////////////////// Delete From Bookmark Table //////////////////////////

	public boolean deleteOneBookmark(long id) {
		return db.delete(TBL_BOOKMARKS, FLD_ID + "=" + id, null) > 0;
	}

	public boolean deleteAllBookmarks() {
		return db.delete(TBL_BOOKMARKS, null, null) > 0;
	}

	////////////////////// Get Records From Bookmark Table //////////////////////

	public Cursor getAllBookmarks() {
		return db.query(TBL_BOOKMARKS, new String[] { FLD_ID, FLD_SURAH_NAME, FLD_SURAH_NO, FLD_AYAH_NO }, null, null, null, null, FLD_SURAH_NO + "," + FLD_AYAH_NO);
	}

	public Cursor getOneBookmark(long surahNo) throws SQLException {
		return db.query(TBL_BOOKMARKS, new String[] { FLD_ID, FLD_SURAH_NO, FLD_AYAH_NO }, FLD_SURAH_NO + "=" + surahNo, null, null, null, FLD_AYAH_NO, null);

	}

	///////////////////// Update Into Bookmark Table /////////////////////////

	public boolean updateBookmark(long rowId, String surahName, int surahNo, int ayahNo) {
		ContentValues newValues = new ContentValues();
		newValues.put(FLD_SURAH_NAME, surahName);
		newValues.put(FLD_SURAH_NO, surahNo);
		newValues.put(FLD_AYAH_NO, ayahNo);

		return db.update(TBL_BOOKMARKS, newValues, FLD_ID + "=" + rowId, null) > 0;
	}

	///////////////////////// Get Translation for word search from whole Quran ///////

	public Cursor getFullQuranTranslation() {
		// return db.query(TBL_ENG_TRANSLATION, new String[] {FLD_ID, FLD_TRANSLATION, FLD_SURAH_NO, FLD_AYAH_NO}, null, null, null, null, null);
		return db.query(TBL_ENG_TRANSLATION, null, null, null, null, null, FLD_SURAH_NO + "," + FLD_AYAH_NO);
	}

	///////////////////////// Insert Into Download Table //////////////////////

	public long addDownload(int d_Id, int s_No, String s_name, String t_name) {
		ContentValues newValues = new ContentValues();
		newValues.put(FLD_DOWNLOAD_ID, d_Id);
		newValues.put(FLD_SURAH_NO, s_No);
		newValues.put(FLD_SURAH_NAME, s_name);
		newValues.put(FLD_TEMP_NAME, t_name);

		return db.insert(TBL_DOWNLOADS, null, newValues);
	}

	////////////////////// Delete From Download Table //////////////////////////

	public boolean deleteOneDownload(String column, long id) {
		return db.delete(TBL_DOWNLOADS, column + "=" + id, null) > 0;
	}

	public boolean deleteAllDownloads() {
		return db.delete(TBL_DOWNLOADS, null, null) > 0;
	}

	////////////////////// Get Records From Download Table //////////////////////

	public Cursor getAllDownloads() {
		return db.query(TBL_DOWNLOADS, new String[] { FLD_ID, FLD_DOWNLOAD_ID, FLD_SURAH_NO, FLD_SURAH_NAME, FLD_TEMP_NAME }, null, null, null, null, null);
	}

	public Cursor getOneDownload(long d_id) throws SQLException {
		return db.query(TBL_BOOKMARKS, new String[] { FLD_ID, FLD_DOWNLOAD_ID, FLD_SURAH_NO, FLD_SURAH_NAME, FLD_TEMP_NAME }, FLD_DOWNLOAD_ID + "=" + d_id, null, null, null, null, null);

	}

	///////////////////// Update Into Download Table /////////////////////////

	public boolean updateDownload(long rowId, int d_Id, int s_No, String s_name, String t_name) {
		ContentValues newValues = new ContentValues();
		newValues.put(FLD_DOWNLOAD_ID, d_Id);
		newValues.put(FLD_SURAH_NO, s_No);
		newValues.put(FLD_SURAH_NAME, s_name);
		newValues.put(FLD_TEMP_NAME, t_name);

		return db.update(TBL_DOWNLOADS, newValues, FLD_ID + "=" + rowId, null) > 0;
	}

	///////////////////// Get Info /////////////////////////
	public String getUrl(String area, String reciter) throws SQLException {

		String url = "";

		Cursor c = db.query(TABLE_URLS, new String[] { area }, FLD_RECITER + "='" + reciter + "' COLLATE NOCASE ", null, null, null, null, null);
		if(c != null && c.moveToFirst())
		{
			url = c.getString(0);
			c.close();
		}

		return url;
	}
	////////////////////////////// FUNCTIONS ////////////////////////////////

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 */

	public void createDataBase() throws IOException {
		boolean dbExist = checkDataBase();
		int version = prefs.chkDbVersion();

		if(dbExist)
		{
			if(version <= DATABASE_VERSION)
			{
				copyDataBase();
				prefs.setDbVersion(DATABASE_VERSION + 1);
			}
			else if(DATABASE_VERSION > version)
			{
				prefs.setDbVersion(DATABASE_VERSION + 1);
			}
		}
		else
		{
			// By calling this method and empty database will be created into the default system path
			// of your application so we are gonna be able to overwrite that database with our database.
			DBHelper.getReadableDatabase();

			try
			{
				copyDataBase();
				prefs.setDbVersion(DATABASE_VERSION + 1);
			}
			catch (IOException e)
			{
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try
		{
			String myPath = context.getDatabasePath(DATABASE_NAME).getPath().toString();
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

			// checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.OPEN_READWRITE);
		}
		catch (SQLiteException e)
		{
			// database does't exist yet.
		}

		if(checkDB != null)
		{
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public void copyDataBase() throws IOException {
		String DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath().toString();

		// Open your local db as the input stream
		String filePath = setDatabase();
		if(filePath != null)
		{
			File d = new File(filePath);
			InputStream myInput = new FileInputStream(d);

			// Path to the just created empty db
			String outFileName = DB_PATH;

			// Open the empty db as the output stream
			OutputStream myOutput = new FileOutputStream(outFileName);

			// transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer)) > 0)
			{
				myOutput.write(buffer, 0, length);
			}
			prefs.setDatabaseCopied(true);

			// Close the streams
			myOutput.flush();
			myOutput.close();
			myInput.close();
		}
		deleteFiles();
	}

	private String setDatabase() {
		String name = context.getString(R.string.admob_id);
		String path = null;
		String filePath = context.getFilesDir().toString() + "/myfiles/quran_now_db_new";
		try
		{
			AssetManager am = context.getAssets();
			InputStream inputStream = am.open("quran_now_db_new.zip");
			File src = createFileFromInputStream(inputStream);

			ZipFile zipFile = new ZipFile(src);
			if(zipFile.isEncrypted())
			{
				zipFile.setPassword(name);
			}
			String dest = new String(context.getFilesDir().toString() + "/myfiles");
			zipFile.extractAll(dest);
			path = filePath;
		}
		catch (IOException e)
		{
			path = null;
			deleteFiles();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ZipException e)
		{
			path = null;
			deleteFiles();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return path;
	}

	private File createFileFromInputStream(InputStream inputStream) {

		File myDoc = new File(context.getFilesDir().toString() + "/quran_now_db_new.zip");

		try
		{
			OutputStream outputStream = new FileOutputStream(myDoc);
			byte buffer[] = new byte[1024];
			int length = 0;

			while ((length = inputStream.read(buffer)) > 0)
			{
				outputStream.write(buffer, 0, length);
			}

			outputStream.close();
			inputStream.close();

			return myDoc;
		}
		catch (IOException e)
		{
			if(myDoc.exists())
			{
				myDoc.delete();
			}
			e.printStackTrace();
		}

		return null;
	}

	private void deleteFiles() {
		// TODO Auto-generated method stub
		String filePath = context.getFilesDir().toString() + "/myfiles/quran_now_db_new";
		String folderPath = context.getFilesDir().toString() + "/myfiles";
		String zipPath = context.getFilesDir().toString() + "/quran_now_db_new.zip";

		File file = new File(filePath);
		File folder = new File(folderPath);
		File zip = new File(zipPath);

		if(file != null)
		{
			if(file.exists())
			{
				file.delete();
			}
		}

		if(folder != null)
		{

			if(folder.exists())
			{
				folder.delete();
			}
		}

		if(zip != null)
		{
			if(zip.exists())
			{
				zip.delete();
			}
		}
	}
}
