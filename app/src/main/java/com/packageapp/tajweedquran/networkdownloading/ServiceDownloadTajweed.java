package com.packageapp.tajweedquran.networkdownloading;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.QuranReading.sharedPreference.SettingsSharedPref;
import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;
import com.packageapp.tajweedquran.ConstantsTajweedQuran;

import java.util.Date;
import java.util.TimeZone;


public class ServiceDownloadTajweed extends Service implements UnzipListenerTajweed {


    DownloadingTajweedPref mDownloadingDuasPref;
    private long downloadReference;
    public DownloadManager downloadManager;
    SettingsSharedPref settingPrefs;

    @Override
    public void onCreate() {
        super.onCreate();
        settingPrefs=new SettingsSharedPref(this);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mDownloadingDuasPref = new DownloadingTajweedPref(this);
        IntentFilter surahDownloadFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, surahDownloadFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service", "onStartCommand");

        try {
            if (intent != null) {
                extractValues(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    public void extractValues(Intent intent) {

        downloadDuas();
    }

    private void downloadDuas() {
        //String url = ;

        if (!ConstantsTajweedQuran.rootPathTajweed.exists()) {
            ConstantsTajweedQuran.rootPathTajweed.mkdirs();
        }

        String url = getUrl();

        Uri Download_Uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

        // Restrict the types of networks over which this download may proceed.
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        // Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);

        // Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading . . . ");

        // Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription(getString(R.string.downloading));

        // Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(ConstantsTajweedQuran.rootFolderTajweed, DownloadingZipUtil.TAJWEED_ZIP_TEMP);

        // Enqueue a new download and same the referenceId
        try {
            downloadReference = downloadManager.enqueue(request);
            String refId = downloadReference + "";

            mDownloadingDuasPref.setReferenceId(refId);

           // AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Started");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {

        String url;
        String urlAsia = "https://storage.googleapis.com/quran_urdu_as/tajweed/AudiosTajweed.zip";
        String urlUS = "https://storage.googleapis.com/quran_urdu_us/tajweed/AudiosTajweed.zip";
        String urlEU = "https://storage.googleapis.com/quran_urdu_eu/tajweed/AudiosTajweed.zip";

        TimeZone tz = TimeZone.getDefault();
        long timeNow = new Date().getTime();
        double timezone = (double) ((tz.getOffset(timeNow) / 1000) / 60) / 60;
        if (timezone >= 4.0 && timezone <= 13.0) // Asia
        {
            url = urlAsia;
        } else if (timezone >= -13.0 && timezone <= -4.0) // US
        {
            url = urlUS;
        } else if (timezone >= -3.5 && timezone <= 3.5) // EU
        {
            url = urlEU;
        } else {
            url = urlEU;
        }

        return url;
    }

    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // check if the broadcast message is for our Enqueued download
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (checkDownloadStatus(referenceId)) {
                UnZipUtilTajweed mUnZipUtil = new UnZipUtilTajweed();
                mUnZipUtil.setListener(ServiceDownloadTajweed.this);
                mUnZipUtil.execute();
            }
        }
    };

    @Override
    public void onDestroy() {

        String id = mDownloadingDuasPref.getReferenceId();


        if (!id.equals("")) {
            downloadManager.remove(Long.parseLong(id));
            mDownloadingDuasPref.clearStoredData();
        }
    }

    private boolean checkDownloadStatus(long id) {

        boolean isSuccessful = false;

        // TODO Auto-generated method stub
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                isSuccessful = true;
            }
        }

        return isSuccessful;
    }

    @Override
    public void unzipStatus(boolean status) {
        // TODO Auto-generated method stub

        String id = mDownloadingDuasPref.getReferenceId();

        if (!id.equals("")) {
            downloadManager.remove(Long.parseLong(id));
            mDownloadingDuasPref.clearStoredData();
        }

        if (status) {
            Intent broadCastIntent=new Intent(DownloadingZipUtil.downloadComplete);
            sendBroadcast(broadCastIntent);

            AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Completed");
        } else {
            if (!EnoughMemory(Double.parseDouble(getString(R.string.TajweedQuran_audioSize)))) {
                showMemoryLowDialog();
            }
            AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Failed");
        }

        stopSelf();
        if (settingPrefs.isAppActive()) {
           final Toast toast= Toast.makeText(this, "Tajweed Quran audio files " + " " + getString(R.string.downloadCompleted), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }
    private void showMemoryLowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage("You have insufficient storage, 20Mb is required. Please Try again.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean EnoughMemory(double fileSize) {

        double sizeRequired;
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

}