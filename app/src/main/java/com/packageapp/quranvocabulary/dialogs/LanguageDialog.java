/*
package com.packageapp.quranvocabulary.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.quranvocabulary.MainActivity;

import com.packageapp.quranvocabulary.generalhelpers.SharedPref;

*/
/**
 * Created by cyber on 1/28/2016.
 *//*

public class LanguageDialog {

    private Context context;
    private SharedPref sharedPref;//quran vocabulary
    private SettingsSharedPref prefs;

    private int value;
    private LanguageInterface languageInterface;
    Activity mActivity;
    AnalyticSingaltonClass mAnalyticSingaltonClass;
    boolean isTajweed=false;
    public LanguageDialog(Context context, LanguageInterface langInterface) {
        this.context = context;
        mActivity = (AppCompatActivity) context;
    }

    public void showDialog( boolean isTajweed) {
        this.isTajweed=isTajweed;
        if(isTajweed)
        {
            prefs = new SettingsSharedPref(context);
        }
        else
        {
            //quran vocabulary preferences
            sharedPref = new SharedPref(context);

        }

        mAnalyticSingaltonClass = AnalyticSingaltonClass.getInstance(context);
        value = sharedPref.getLanguage();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set Language")
                .setSingleChoiceItems(R.array.eng_urdu, value, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        value = which;
                        if (value == 0) {

                            mAnalyticSingaltonClass.sendEventAnalytics("Language Dialogue", "English Mode On");
                        } else {
                            mAnalyticSingaltonClass.sendEventAnalytics("Language Dialogue", "Urdu Mode On");
                        }
                    }
                })
                .setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.txt_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    sharedPref.setLanguage(value);
                                    languageInterface.setLanguage();
                                } catch (Exception e)

                                {

                                }
                       */
/* if ((fragmentManager.getBackStackEntryCount() > 1))
                        {
                            fragmentManager.popBackStack();
                        }*//*



                            }
                        }

                )
                .

                        create()

                .

                        show();
    }
}
*/
