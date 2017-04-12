package com.QuranReading.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.QuranReading.listeners.UnzipListener;
import com.QuranReading.urduquran.Constants;

import android.os.AsyncTask;
import android.util.Log;

public class UnZipUtil extends AsyncTask<String, Void, Boolean> 
{
	int reciter;
	boolean status = false;
	UnzipListener listener;
	
	public void setReciter(int reciter)
	{
		this.reciter = reciter;
	}
	
	public void setListener(UnzipListener listener)
	{
		this.listener = listener;
	}
	
	@Override
	protected Boolean doInBackground(String... params) 
	{
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
	protected void onPostExecute(Boolean result) 
	{
		listener.unzipStatus(status, reciter);
	}
	
	public void unzip() 
	{
		String _zipFile;

		if(reciter == 1)
			_zipFile = Constants.rootPath.getAbsolutePath() + "/temp_" + Constants.QuranFile1;
		else
			_zipFile = Constants.rootPath.getAbsolutePath() + "/temp_" + Constants.QuranFile2;
		
		try 
		{
			FileInputStream fin = new FileInputStream(_zipFile);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;

			while ((ze = zin.getNextEntry()) != null) 
			{
				Log.v("Decompress", "Unzipping " + ze.getName());

				File tempFile = new File(Constants.rootPath.getAbsolutePath(), ze.getName().toString());

				if (!tempFile.exists()) 
				{
					FileOutputStream fout = new FileOutputStream(Constants.rootPath.getAbsolutePath() + "/"
							+ ze.getName());
					
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
			 
			if (tempFile.exists()) 
			{
				tempFile.delete();
			}
		} 
		catch (Exception e) 
		{
			Log.e("Decompress", "unzip " + e.toString());
			status = false;
		}

	}
}
