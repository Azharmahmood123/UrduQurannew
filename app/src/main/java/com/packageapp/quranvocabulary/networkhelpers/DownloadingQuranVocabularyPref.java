package com.packageapp.quranvocabulary.networkhelpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aamir Riaz on 12/8/2016.
 */

public class DownloadingQuranVocabularyPref {
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    SharedPreferences pref;
    private static final String PREF_NAME = "QuranVocabularyDownloadPref";

    public static String REFERENCE_ID = "reference_id";

    public DownloadingQuranVocabularyPref(Context context) {
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
