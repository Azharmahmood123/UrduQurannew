package com.packageapp._13linequran.network;

import android.os.AsyncTask;
import android.util.Log;

import com.packageapp.tajweedquran.networkdownloading.DownloadingZipUtil;
import com.packageapp.tajweedquran.networkdownloading.UnzipListenerTajweed;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Aamir Riaz on 12/7/2016.
 */

public class UnZipUtil13Line extends AsyncTask<String, Void, Boolean> {
    int reciter;
    boolean status = false;
    UnzipListener13Line listener;

    public void setReciter(int reciter) {
        this.reciter = reciter;
    }

    public void setListener(UnzipListener13Line listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try
        {
            unzip();

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if(listener != null)
        {
            listener.unzipStatus(status);
        }
    }

    private void unzip() {
        String _zipFile = DownloadUtils13Line.rootPath13Line.getAbsolutePath() + "/" + DownloadUtils13Line._13Line_ZIP_TEMP;

        try
        {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null)
            {
                Log.v("Decompress", "Unzipping " + ze.getName());

                String name = ze.getName().toString();

                File tempFile = new File(DownloadUtils13Line.rootPath13Line.getAbsolutePath(), name);

                if(!tempFile.exists())
                {
                    FileOutputStream fout = new FileOutputStream(DownloadUtils13Line.rootPath13Line.getAbsolutePath() + "/" + name);

                    byte[] buffer = new byte[8192];
                    int len;

                    while ((len = zin.read(buffer)) != -1)
                    {
                        fout.write(buffer, 0, len);
                    }

                    fout.close();
                }

                zin.closeEntry();
            }

            zin.close();

            status = true;

            File tempFile = new File(_zipFile);

            if(tempFile.exists())
            {
                tempFile.delete();
            }
        }
        catch (Exception e)
        {
            File tempFile = new File(_zipFile);
            if(tempFile.exists())
            {
                tempFile.delete();
            }
            status = false;
        }
    }
}


