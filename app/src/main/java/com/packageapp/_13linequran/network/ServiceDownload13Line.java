package com.packageapp._13linequran.network;

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
import android.widget.Toast;

import com.QuranReading.urduquran.R;
import com.QuranReading.urduquran.ads.AnalyticSingaltonClass;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Aamir Riaz on 12/7/2016.
 */

public class ServiceDownload13Line extends Service implements UnzipListener13Line {
    public static final String ACTION_NAMES_DOWNLOAD_COMPLETED = "action.images.completed";

    Downloading13LinePref mDownloadingDuasPref;
    private long downloadReference;
    public DownloadManager downloadManager;

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        mDownloadingDuasPref = new Downloading13LinePref(this);
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

        if (!DownloadUtils13Line.rootPath13Line.exists()) {
            DownloadUtils13Line.rootPath13Line.mkdirs();
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
        request.setDestinationInExternalPublicDir(DownloadUtils13Line.rootFolder13Line, DownloadUtils13Line._13Line_ZIP_TEMP);

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
        String  urlAsia = "https://storage.googleapis.com/quran_urdu_as/quran-pages/images13line.zip";
        String  urlUS = "https://storage.googleapis.com/quran_urdu_us/quran-pages/images13line.zip";
        String  urlEU = "https://storage.googleapis.com/quran_urdu_eu/quran-pages/images13line.zip";

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
                UnZipUtil13Line mUnZipUtil = new UnZipUtil13Line();
                mUnZipUtil.setListener(ServiceDownload13Line.this);
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
            Intent intent = new Intent(ACTION_NAMES_DOWNLOAD_COMPLETED);
            sendBroadcast(intent);

            AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Completed");
        } else {
            if(!EnoughMemory(Double.parseDouble("3.8E8")))
            {
                showMemoryLowDialog();
            }
            AnalyticSingaltonClass.getInstance(this).sendEventAnalytics("Downloading", "Download Failed");
        }

        stopSelf();
        Toast.makeText(this, "13 Line Quran Files " + " " + getString(R.string.downloadCompleted), Toast.LENGTH_SHORT).show();
    }

    private void showMemoryLowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.download);
        builder.setMessage("You have insufficient storage, 190Mb is required. Please Try again.");

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

