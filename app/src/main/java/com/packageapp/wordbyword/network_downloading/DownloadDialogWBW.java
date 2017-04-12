package com.packageapp.wordbyword.network_downloading;


import android.app.Activity;
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
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;


import java.io.File;


public class DownloadDialogWBW extends Activity implements UnzipListenerWBW {

    // Button btnSurah, btnQuran, btnCancel, btnoky, btnback;
    @Override
    public void unzipStatus(boolean status) {

        if (!status) {

            if(!EnoughMemory(Double.parseDouble(getString(R.string.WBW_audioSize)))) {
                showMemoryLowDialog();
            }
            AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Failed");
        }
    }

    private void showMemoryLowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage("You have insufficient storage, 30Mb is required. Please free some space and Download again.");

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
    DownloadingWBWPref mDownloadingNamesPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transperant_layout);
        mDownloadingNamesPref = new DownloadingWBWPref(this);

        IntentFilter surahDownloadComplete = new IntentFilter(ServiceDownloadWBW.ACTION_WBW_AUDIO_DOWNLOAD_COMPLETED);
        registerReceiver(downloadCompleteWBW, surahDownloadComplete);

        if (checkDownloadStatus()) {
            if (msg != null) {
                showDownloadProcessingDialog();
            } else {
                msg = getString(R.string.download_body);
                showDownloadDialog();
            }
        }

        //sendAnalyticsData();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(downloadCompleteWBW);
    }

    /*private void sendAnalyticsData() {
        AnalyticSingaltonClass.getInstance(this).sendScreenAnalytics("Download Dialog Screen");
    }*/

    public boolean checkDownloadStatus() {

        msg = null;
        if (!checkDownloadManagerState()) {
            return false;
        }

        String refId = mDownloadingNamesPref.getReferenceId();

        if (!refId.equals("")) {
            String status = FileUtilsWBW.checkDownloadStatus(this, Long.parseLong(refId.trim()));
            if (status == FileUtilsWBW.CHK_SUCCESSFUL) {
                final Toast toast=Toast.makeText(this, R.string.already_downloaded, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

                UnZipUtilWBW mUnZipUtil = new UnZipUtilWBW();
                mUnZipUtil.setListener(this);
                mUnZipUtil.execute();
               // msg = "";
                msg=null;
                return true;
            } else if (status == FileUtilsWBW.CHK_FAILED) {
                mDownloadingNamesPref.clearStoredData();
            } else if (status == FileUtilsWBW.CHK_PAUSED) {
                msg = "";
            } else if (status == FileUtilsWBW.CHK_RUNNING) {
                msg = "";
            } else if (status == FileUtilsWBW.CHK_PENDING) {
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

        String _zipFile = Constants.rootPathWBW.getAbsolutePath() + "/" + Constants.WBW_TEMP;

        File tempFile = new File(_zipFile);

        if(tempFile.exists())
        {
            tempFile.delete();
        }

        FileUtilsWBW.deleteTempFiles(Constants.rootPathWBW.getAbsolutePath());

        if (!checkDownloadManagerState()) {
            alertMessage();
            return;
        }

        if (isNetworkConnected()) {
            Intent serviceIntent = new Intent(DownloadDialogWBW.this, ServiceDownloadWBW.class);
            startService(serviceIntent);
        } else {
           final  Toast toast=Toast.makeText(DownloadDialogWBW.this, R.string.no_network_connection, Toast.LENGTH_LONG);
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


                if(EnoughMemory(Double.parseDouble(getString(R.string.WBW_audioSize))))
                {
                    AnalyticSingaltonClass.getInstance(DownloadDialogWBW.this).sendEventAnalytics("Downloads","WBW_Downloaded");
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

    private BroadcastReceiver downloadCompleteWBW = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                DownloadDialogWBW.this.finish();
            }
        }
    };

}
