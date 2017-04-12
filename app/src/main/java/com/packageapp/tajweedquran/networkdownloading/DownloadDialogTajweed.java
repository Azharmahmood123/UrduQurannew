package com.packageapp.tajweedquran.networkdownloading;


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
import com.packageapp.tajweedquran.ConstantsTajweedQuran;

import java.io.File;


public class DownloadDialogTajweed extends AppCompatActivity implements UnzipListenerTajweed {

    // Button btnSurah, btnQuran, btnCancel, btnoky, btnback;
    @Override
    public void unzipStatus(boolean status) {

        if (!status) {

            if(!EnoughMemory(Double.parseDouble(getString(R.string.TajweedQuran_audioSize)))) {
                showMemoryLowDialog();
            }
           // AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Failed");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    private BroadcastReceiver TajweedDownloadReciever =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null)
            {
                DownloadDialogTajweed.this.finish();

            }

        }
    };





    private void showMemoryLowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage("You have insufficient storage, 20Mb is required. Please free some space and Download again.");

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
    DownloadingTajweedPref mDownloadingNamesPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transperant_layout);
        mDownloadingNamesPref = new DownloadingTajweedPref(this);
        IntentFilter intentFilter=new IntentFilter(DownloadingZipUtil.downloadComplete);
        registerReceiver(TajweedDownloadReciever, intentFilter);
        if (checkDownloadStatus()) {

                    if (msg != null) {
                        if (!msg.equals("1")) {//this block was added
                            showDownloadProcessingDialog();
                        }
                    } else {
                        msg = getString(R.string.downloadTajweed);
                        showDownloadDialog();
                    }
                }

        //sendAnalyticsData();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(TajweedDownloadReciever);
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
               final Toast toast= Toast.makeText(this, R.string.already_downloaded, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

                UnZipUtilTajweed mUnZipUtil = new UnZipUtilTajweed();
                mUnZipUtil.setListener(this);
                mUnZipUtil.execute();
                msg = "1";//msg="" if error occur remove this line
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

        String _zipFile = ConstantsTajweedQuran.rootPathTajweed.getAbsolutePath() + "/" + DownloadingZipUtil.TAJWEED_ZIP_TEMP;

        File tempFile = new File(_zipFile);

        if(tempFile.exists())
        {
            tempFile.delete();
        }

        DownloadingZipUtil.deleteTempFiles(ConstantsTajweedQuran.rootPathTajweed.getAbsolutePath());

        if (!checkDownloadManagerState()) {
            alertMessage();
            return;
        }

        if (isNetworkConnected()) {
            Intent serviceIntent = new Intent(DownloadDialogTajweed.this, ServiceDownloadTajweed.class);
            startService(serviceIntent);
        } else {
            final  Toast toast=Toast.makeText(DownloadDialogTajweed.this, R.string.no_network_connection, Toast.LENGTH_LONG);
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


                if(EnoughMemory(Double.parseDouble(getString(R.string.TajweedQuran_audioSize))))
                {
                    AnalyticSingaltonClass.getInstance(DownloadDialogTajweed.this).sendEventAnalytics("Downloads","Tajweed_Downloaded");
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

}



