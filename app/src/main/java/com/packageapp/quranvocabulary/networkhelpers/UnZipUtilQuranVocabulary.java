package com.packageapp.quranvocabulary.networkhelpers;

import android.os.AsyncTask;
import android.util.Log;

import com.packageapp._13linequran.network.DownloadUtils13Line;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Aamir Riaz on 12/8/2016.
 */

public class UnZipUtilQuranVocabulary extends AsyncTask<String, Void, Boolean> {
    int reciter;
    boolean status = false;
    UnzipListenerQuranVocabulary listener;

    public void setReciter(int reciter) {
        this.reciter = reciter;
    }

    public void setListener(UnzipListenerQuranVocabulary listener) {
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
        String _zipFile = DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath() + "/" + DownloadUtilsQuranVocabulary.QuranVocabulary_ZIP_TEMP;
            int filesCounter=0;
        try
        {
            FileInputStream fin = new FileInputStream(_zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;

            while ((ze = zin.getNextEntry()) != null)
            {
                Log.v("Decompress", "Unzipping " + ze.getName());

                String name = ze.getName().toString();

                File tempFile = new File(DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath(), name);

                if(!tempFile.exists())
                {
                    FileOutputStream fout = new FileOutputStream(DownloadUtilsQuranVocabulary.rootPathQuranVocabulary.getAbsolutePath() + "/" + name);

                    byte[] buffer = new byte[8192];
                    int len;

                    while ((len = zin.read(buffer)) != -1)
                    {
                        fout.write(buffer, 0, len);
                    }
                    filesCounter+=1;

                    fout.close();
                }

                zin.closeEntry();
            }

            zin.close();
            if(filesCounter==1440)
            {
                status = true;
            }
            else {

                status=false;
            }



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


