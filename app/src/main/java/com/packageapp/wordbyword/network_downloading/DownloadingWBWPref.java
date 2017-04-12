package com.packageapp.wordbyword.network_downloading;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DownloadingWBWPref {

	Editor editor;
	Context _context;
	int PRIVATE_MODE = 0;
	SharedPreferences pref;
	private static final String PREF_NAME = "WBWDownloadPref";

	public static String REFERENCE_ID = "reference_id";

	public DownloadingWBWPref(Context context) {
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
