package com.packageapp.quranvocabulary.generalhelpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by cyber on 12/8/2015.
 */
public class SharedPref {

    private Context mContext;
    private final String LANG_PREF = "lang_pref";
    private final String NOTIFY_PREF = "notification_pref";
    private final String DETAIL_PREF = "detail_pref";

    public SharedPref(Context mContext) {
        this.mContext = mContext;
    }

  }
