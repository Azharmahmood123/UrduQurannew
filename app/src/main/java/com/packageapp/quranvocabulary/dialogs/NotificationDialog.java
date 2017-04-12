/*
package com.packageapp.quranvocabulary.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.QuranReading.urduquran.R;
import com.packageapp.quranvocabulary.generalhelpers.SharedPref;

*/
/**
 * Created by cyber on 1/28/2016.
 *//*

public class NotificationDialog {

    private Context context;
    private SharedPref sharedPref;
    private int value;

    public NotificationDialog(Context context) {
        this.context = context;
    }

    public void showDialog() {
        sharedPref = new SharedPref(context);
        value = sharedPref.getNotification();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Set Notification")
                .setSingleChoiceItems(R.array.on_off, value, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        value = which;
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
                        sharedPref.setNotification(value);
                    }
                })
                .create()
                .show();
    }
}
*/
