package com.packageapp._13linequran.network;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;

import com.QuranReading.urduquran.Constants;

import java.io.File;

/**
 * Created by Aamir Riaz on 12/7/2016.
 */

public class DownloadUtils13Line {
    public static final String CHK_SUCCESSFUL = "chk_successful";
    public static final String CHK_FAILED = "chk_failed";
    public static final String CHK_RUNNING = "chk_running";
    public static final String CHK_PAUSED = "chk_paused";
    public static final String CHK_PENDING = "chk_pending";
    public static  String rootFolder13Line = "QuranNow/_13LineQuran/";
    public static  String _13Line_ZIP_TEMP = "temp_images13Line.zip";
    public static final File rootPath13Line = new File(Environment.getExternalStorageDirectory(), rootFolder13Line);

    public static String checkDownloadStatus(Context ctx, long id) {


        String state = "";

        DownloadManager downloadManager = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);
        ;

        // TODO Auto-generated method stub
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if(cursor.moveToFirst())
        {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);

            if(status == DownloadManager.STATUS_SUCCESSFUL)
            {
                state = CHK_SUCCESSFUL;
            }
            else if(status == DownloadManager.STATUS_FAILED)
            {
                state = CHK_FAILED;
            }
            else if(status == DownloadManager.STATUS_PAUSED)
            {
                state = CHK_PAUSED;
            }
            else if(status == DownloadManager.STATUS_RUNNING)
            {
                state = CHK_RUNNING;
            }
            else if(status == DownloadManager.STATUS_PENDING)
            {
                state = CHK_PENDING;
            }

        }

        return state;
    }
    public static void deleteTempFiles(File dir) {
        //  File dir = new File(Constants.rootPath.getAbsolutePath());

        File[] directoryListing = dir.listFiles();

        if (directoryListing != null)
        {
            for (File child : directoryListing)
            {
                String fileName = child.getName();

                if (fileName.contains("temp"))
                {
                    deleteAudioFile(fileName);
                }
            }
        }
    }
    public static void deleteAudioFile(String fileName) {
        File tempFile = new File(Constants.rootPath.getAbsolutePath(), fileName);

        if (tempFile.exists())
        {
            tempFile.delete();

			/*
			 * if(tempFile.delete()) { ((GlobalClass) getApplication()).showToast("File Deleted"); } else { ((GlobalClass) getApplication()).showToast("File Not Deleted"); }
			 */
        }
    }
    public static void deleteTempFiles(String name) {
        File dir = new File(name);

        File[] directoryListing = dir.listFiles();

        if(directoryListing != null)
        {
            for (File child : directoryListing)
            {
                String fileName = child.getName();

                if(fileName.contains("temp"))
                {
                    deleteAudioFile(fileName);
                }
            }
        }
    }

}
