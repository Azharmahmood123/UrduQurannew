package com.packageapp.quranvocabulary.generalhelpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

@SuppressLint("SimpleDateFormat")
public class DBManager {
	private final Context context;

	private static final String DATABASE_NAME = "vocabulary.db";
    private static final String TBL_CATEGORIES = "tbl_categories";
    private static final String TBL_WORDS = "tbl_commonwords";
	private static final int DATABASE_VERSION = 1;

	// /////////////////////// HELPER CLASS TO CREATE DATABASE // //////////////////////

    private static final Charset UTF_8 = Charset.forName("UTF-8");
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBManager(Context ctx) {
		this.context = ctx;
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
            if(newVersion>oldVersion) {

            }
		}
	}

	// /////////////////////////// Opens Database ////////////////////////////

	public DBManager open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	// //////////////////////////// FUNCTIONS ////////////////////////////////

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */

	public void createDataBase() throws IOException {
		// QiblaDirectionPref qiblaPref = new QiblaDirectionPref(context);
		boolean dbExist = checkDataBase();
		//int version = qiblaPref.chkDbVersion();

		// By calling this method and empty database will be created into
		// the default system path
		// of your application so we are gonna be able to overwrite that
		// database with our database.
		DBHelper.getReadableDatabase();

		try {
//            if (!dbExist) {
                copyDataBase();
//            }

			//qiblaPref.setDbVersion(DATABASE_VERSION + 1);
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try {
			String myPath = context.getDatabasePath(DATABASE_NAME).getPath()
					.toString();
			;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}

		if (checkDB != null) {
			checkDB.close();
		}

		return checkDB != null ? true : false;
	}

	public void copyDataBase() throws IOException {
		String DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath()
				.toString();

		// Open your local db as the input stream
		InputStream myInput = context.getAssets().open(DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

    ///////////////
    public Cursor getCategories() {
        Cursor value = null;
        try {
            open();
            String query = "Select * from tbl_categories";
            value = db.rawQuery(query, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public Cursor getCatDetail(int catId) {
        Cursor value = null;
        try {
            open();
            String query = "Select * from tbl_commonwords where Category = "+catId;
            value = db.rawQuery(query, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    public Cursor getCatNames(String str)
    {
        Cursor value = null;
        try {
            open();
            value = db.rawQuery(str, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public Cursor getAllIds() {
        Cursor value = null;
        try {
            open();
            String query = "Select ID from tbl_commonwords";
            value = db.rawQuery(query, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public Cursor getQuizWords(int ids[]) {
        Cursor value = null;
        try {
            open();
            String query = "Select * from tbl_commonwords where ID = "+ids[0]+
                    " OR ID = "+ids[1]+
                    " OR ID = "+ids[2]+
                    " OR ID = "+ids[3]+
                    " OR ID = "+ids[4]+
                    " OR ID = "+ids[5]+
                    " OR ID = "+ids[6]+
                    " OR ID = "+ids[7]+
                    " OR ID = "+ids[8]+
                    " OR ID = "+ids[9]+
                    " OR ID = "+ids[10]+
                    " OR ID = "+ids[11]+
                    " OR ID = "+ids[12]+
                    " OR ID = "+ids[13]+
                    " OR ID = "+ids[14];
            value = db.rawQuery(query, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    ////////////////////// Search in English ////////////////////////////////
    /*public Cursor getEngMean(String word) {
        String query = "SELECT * FROM eng, other WHERE eng.serial = other.serial AND eng.word = '"+word.toUpperCase()+"'";
        Cursor value = db.rawQuery(query, null);
//                Cursor value =  db.query(false, TBL_OTHER, new String[] { FLD_SERIAL, FLD_WORD }, FLD_WORD+" LIKE '"+word+"%'", null, null, null, null, null);
        return value;
    }

    public Cursor getUrduMean(String word) {
        String query = "SELECT * FROM eng, other WHERE eng.serial = other.serial AND other.word = '"+word+"'";
        Cursor queryResult = db.rawQuery(query, null);
        return queryResult;
    }

    public Cursor getSerialDetail(int serial) {
        db = DBHelper.getReadableDatabase();
        String query = "SELECT * FROM eng, other WHERE eng.serial = other.serial AND eng.serial = "+serial;
        Cursor value = db.rawQuery(query, null);
        return value;
    }

    public Cursor getAllUrduData() {
        db = DBHelper.getReadableDatabase();
        String query = "SELECT serial, word FROM other";
        Cursor value = db.rawQuery(query, null);
        return value;
    }

    public Cursor getAllEngData() {
        db = DBHelper.getReadableDatabase();
        String query = "select serial, word from eng";
        Cursor value = db.rawQuery(query, null);
        return value;
    }

    public Cursor getUrduLike(String word) {
        word = word+"%";
        db = DBHelper.getReadableDatabase();
        String query = "select serial, word from other where word LIKE '"+word+"' AND word NOT LIKE ' "+word+"' GROUP BY word ORDER BY LENGTH(word)";
        Cursor value = db.rawQuery(query, null);
        return value;
    }

    public Cursor getEngLike(String word) {
        word = word+"%";
        db = DBHelper.getReadableDatabase();
//        String query = "select serial, word FROM eng WHERE word LIKE '"+word+"' AND word NOT LIKE ' "+word+"' GROUP BY word";
        String query = "select serial, word from eng where word LIKE '"+word+"' AND word NOT LIKE ' "+word+"' ORDER BY LENGTH(word)";
        Cursor value = db.rawQuery(query, null);
        return value;
    }

    public Cursor getUrduWordDetail(String word) {
        db = DBHelper.getReadableDatabase();
        String query = "select * from eng, other where eng.serial = other.serial AND other.word LIKE '"+word+"'";
        Cursor value = db.rawQuery(query, null);
        return value;
    }*/


}
