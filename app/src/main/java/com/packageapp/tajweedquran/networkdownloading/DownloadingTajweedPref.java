package com.packageapp.tajweedquran.networkdownloading;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DownloadingTajweedPref {

	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	SharedPreferences pref;
	private static final String PREF_NAME = "DuaDownloadPref";

	public static String REFERENCE_ID = "reference_id";
	public static String REFERENCE_ID_13LINE = "reference_id_13line";

	public DownloadingTajweedPref(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setReferenceId(String refId) {
		editor.putString(REFERENCE_ID, refId);
		editor.commit();
	}

	public String getReferenceId() {
		return pref.getString(REFERENCE_ID, "");
		// return true;
	}


	public void clearStoredData() {
		editor.clear();
		editor.commit();
	}
}
