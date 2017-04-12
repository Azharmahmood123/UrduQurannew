package com.packageapp.quranvocabulary.networkhelpers;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.tajweedquran.networkdownloading.DownloadingZipUtil;

import java.io.File;

/**
 * Created by Aamir Riaz on 12/8/2016.
 */

public class DownloadDialogQuranVocabulary extends AppCompatActivity implements UnzipListenerQuranVocabulary {

    // Button btnSurah, btnQuran, btnCancel, btnoky, btnback;
    AnalyticSingaltonClass mAnalyticSingaltonClass;
    @Override
    public void unzipStatus(boolean status) {

        if (!status) {

            if(!EnoughMemory(Double.parseDouble(getString(R.string.Quranvocabulary_audioSize)))) {
                showMemoryLowDialog();
            }
            // AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Failed");
        }
    }

    private void showMemoryLowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage("You have insufficient storage, 100Mb is required. Please free some space and Download again.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    // LinearLayout btnLayout;
    // TextView tvBody, header;
    String packageName = "com.android.providers.downloads";
    double sizeRequired;

    String msg = null;
    DownloadingQuranVocabularyPref mDownloadingNamesPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transperant_layout);
        mDownloadingNamesPref = new DownloadingQuranVocabularyPref(this);
        mAnalyticSingaltonClass=AnalyticSingaltonClass.getInstance(this);

        IntentFilter surahDownloadComplete = new IntentFilter(ServiceDownloadQuranVocabulary.ACTION_QURANVOCABULARY_AUDIO_DOWNLOAD_COMPLETED);
        registerReceiver(downloadCompleteQuranVoacabulary, surahDownloadComplete);

        if (checkDownloadStatus()) {
            if (msg != null) {
                showDownloadProcessingDialog();
            } else {
                msg = getString(R.string.downloadQuranVocabulary);
                showDownloadDialog();
            }
        }

        //sendAnalyticsData();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(downloadCompleteQuranVoacabulary);
    }

    private void sendAnalyticsData() {
        AnalyticSingaltonClass.getInstance(this).sendScreenAnalytics("Download Dialog Screen");
    }

    public boolean checkDownloadStatus() {

        msg = null;
        if (!checkDownloadManagerState()) {
            return false;
        }

        String refId = mDownloadingNamesPref.getReferenceId();

        if (!refId.equals("")) {
            String status = DownloadingZipUtil.checkDownloadStatus(this, Long.parseLong(refId.trim()));
            if (status == DownloadingZipUtil.CHK_SUCCESSFUL) {
                final Toast toast=Toast.makeText(this, R.string.already_downloaded, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();


                UnZipUtilQuranVocabulary mUnZipUtil = new UnZipUtilQuranVocabulary();
                mUnZipUtil.setListener(this);
                mUnZipUtil.execute();
                msg = "";
                return true;
            } else if (status == DownloadingZipUtil.CHK_FAILED) {
                mDownloadingNamesPref.clearStoredData();
            } else if (status == DownloadingZipUtil.CHK_PAUSED) {
                msg = "";
            } else if (status == DownloadingZipUtil.CHK_RUNNING) {
                msg = "";
            } else if (status == DownloadingZipUtil.CHK_PENDING) {
                msg = "";
            }
        }

        return true;
    }

    public boolean checkDownloadManagerState() {
        boolean status = false;

        int state = this.getPackageManager().getApplicationEnabledSetting(packageName);

        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER || state == 4) {
            status = false;
        } else {
            status = true;
        }

        return status;
    }

    public void onDownloadSurah() {

        String _zipFile = DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath() + "/" + DownloadUtilsQuranVocabulary.QuranVocabulary_ZIP_TEMP;

        File tempFile = new File(_zipFile);

        if(tempFile.exists())
        {
            tempFile.delete();
        }

        DownloadUtilsQuranVocabulary.deleteTempFiles(DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath());

        if (!checkDownloadManagerState()) {
            alertMessage();
            return;
        }

        if (isNetworkConnected()) {
            Intent serviceIntent = new Intent(DownloadDialogQuranVocabulary.this, ServiceDownloadQuranVocabulary.class);
            startService(serviceIntent);
        } else {
            final Toast toast=Toast.makeText(DownloadDialogQuranVocabulary.this, R.string.no_network_connection, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

        finish();
    }

    private void alertMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert);
        builder.setMessage("Download Manager Disabled");

        builder.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    // Open the specific App Info page:
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + packageName));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // e.printStackTrace();
                    // Open the generic Apps page:
                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                    startActivity(intent);
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void onCancel(View view) {
        finish();
    }

    private void showDownloadProcessingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage(R.string.downloading_in_progress);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showDownloadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage(msg);

        builder.setPositiveButton(getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                if(EnoughMemory(Double.parseDouble(getString(R.string.Quranvocabulary_audioSize))))
                {
                    mAnalyticSingaltonClass.sendEventAnalytics("Downloads","Vocab_Downlaoded");
                    onDownloadSurah();

                }
                else
                {
                    showMemoryLowDialog();
                }
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.index_btn1), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean EnoughMemory(double fileSize) {
        StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
        stat.restat(Environment.getDataDirectory().getPath());
        double bytesAvailable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
            bytesAvailable = (double) stat.getAvailableBytes();
        else
            bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        if (bytesAvailable >= fileSize)
        {
            return true;
        }
        else
        {
            sizeRequired = fileSize - bytesAvailable;
            return false;
        }
    }

    public boolean isNetworkConnected() {
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = mgr.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnected() && netInfo.isAvailable());
    }

    private BroadcastReceiver downloadCompleteQuranVoacabulary = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                DownloadDialogQuranVocabulary.this.finish();
            }
        }
    };

}






