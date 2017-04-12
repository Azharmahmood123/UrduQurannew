package com.QuranReading.sharedPreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PurchasePreferences {
	Editor editor;

	Context _context;
	int PRIVATE_MODE = 0;
	SharedPreferences pref;
	private static final String PREF_NAME = "BillingPref";

	public static final String PURCHASED = "purchased";

	public static final String DATABASE_COPY = "data_copy";
	public static final String DBVERSION = "db_version";

	public PurchasePreferences(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setPurchased(boolean isPurchased) {
		editor.putBoolean(PURCHASED, isPurchased);
		editor.commit();
	}

	public boolean getPurchased() {
		return pref.getBoolean(PURCHASED, false);
		// return true;
	}

	public void setAdCount(int count) {
		editor.putInt("ADCOUNT", count);
		editor.commit();
	}

	public int getAdCount() {
		return pref.getInt("ADCOUNT", 0);
	}

	public void setDbVersion(int v) {
		editor.putInt(DBVERSION, v);
		editor.commit();
	}

	public int chkDbVersion() {
		return pref.getInt(DBVERSION, 2);
	}

	public void setDatabaseCopied(boolean isDbCopied) {
		editor.putBoolean(DATABASE_COPY, isDbCopied);
		editor.commit();
	}

	public boolean isDatabaseCopied() {
		return pref.getBoolean(DATABASE_COPY, false);
	}

	public void clearStoredData() {
		editor.clear();
		editor.commit();
	}
}