package com.packageapp.tajweedquran;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.QuranReading.urduquran.Constants;
import com.QuranReading.urduquran.FileUtils;
import com.QuranReading.urduquran.SurahActivity;

import java.io.File;

/**
 * Created by Aamir Riaz on 11/22/2016.
 */

public class ConstantsTajweedQuran {

    public static final String CHK_PENDING = "chk_pending";
    public static final String rootFolderTajweed = "QuranNow/TajweedQuran/";
    public static final File rootPathTajweed = new File(Environment.getExternalStorageDirectory(), rootFolderTajweed);







   /* try
    {
        if(!Constants.rootPath.exists())
        {
            Constants.rootPath.mkdirs();
        }

        File myFile = new File(Constants.rootPath.getAbsolutePath(), audioFile);

        Uri audioUri = Uri.parse(myFile.getPath());

        if(myFile.exists())
        {
            boolean chkSize = FileUtils.checkAudioFileSize(SurahActivity.this, audioFile, suraPosition, reciter);

            if(chkSize)
                mp = MediaPlayer.create(SurahActivity.this, audioUri);
        }
    }
    catch (Exception e)
    {
        Log.e("File", e.toString());
    }
*/
}
