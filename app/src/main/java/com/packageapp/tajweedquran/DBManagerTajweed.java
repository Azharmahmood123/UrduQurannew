package com.packageapp.tajweedquran;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.GlobalClass;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManagerTajweed {
	private final Context context;
	GlobalClass mGlobal;
	String columName = "datatext";


	// /////////////////////////// Column Names //////////////////////////////

	// ///////////////////////// Dtabase And Table Name
	// ///////////////////////////

	private static final String DATABASE_NAME = "dbFvrt";

	// /////////////////////////// Table Create Queries
	// ////////////////////////////

	private static final int DATABASE_VERSION = 1;

	// /////////////////////// HELPER CLASS TO CREATE DATABASE
	// //////////////////////

	private DatabaseHelper DBHelper;
	public static SQLiteDatabase db;
	private Cursor cursor;

	public DBManagerTajweed(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		mGlobal = ((GlobalClass) context.getApplicationContext());
	}

	public Object getCursor(String queryText) {
		cursor = db.rawQuery(queryText, null);
		return cursor;

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

	// /////////////////////////// Opens Database ////////////////////////////

	public DBManagerTajweed open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	// //////////////////////////// FUNCTIONS ////////////////////////////////

	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 */

	public void createDataBase() throws IOException {
		SettingsSharedPref dbVersionPref = new SettingsSharedPref(context);
		boolean dbExist = checkDataBase();
		int version = dbVersionPref.chkDbVersion();

		if(dbExist)
		{
			if(version <= DATABASE_VERSION)
			{
				copyDataBase();
				dbVersionPref.setDbVersion(DATABASE_VERSION + 1);
			}
			else if(DATABASE_VERSION > version)
			{
				dbVersionPref.setDbVersion(DATABASE_VERSION + 1);
			}
		}
		else
		{
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			DBHelper.getReadableDatabase();

			try
			{
				copyDataBase();
				dbVersionPref.setDbVersion(DATABASE_VERSION + 1);
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

			// checkDB = SQLiteDatabase.openDatabase(myPath, null,
			// SQLiteDatabase.NO_LOCALIZED_COLLATORS |
			// SQLiteDatabase.OPEN_READWRITE);
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
		InputStream myInput = context.getAssets().open(DATABASE_NAME);

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

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void getData(String queryText) {
		try
		{
			SettingsSharedPref pref=new SettingsSharedPref(context);
			if(pref.getLanguageTajweed()==0)//urdu language

			{
				columName="urdu_datatext";
			}
			else {
				columName="datatext";

			}

			cursor = db.rawQuery(queryText, null);
			if(cursor != null)
			{
				cursor.moveToFirst();
				do
				{
					mGlobal.dataList.add(cursor.getString(cursor.getColumnIndex(columName)));// Chapter subHeading

				}
				while (cursor.moveToNext());
			}
			cursor.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		/*
		 * finally { db.close(); }
		 */
	}
}
