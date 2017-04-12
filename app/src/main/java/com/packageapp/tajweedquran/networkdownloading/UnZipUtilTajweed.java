package com.packageapp.tajweedquran.networkdownloading;

import android.os.AsyncTask;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnZipUtilTajweed extends AsyncTask<String, Void, Boolean> {
	int reciter;
	boolean status = false;
	UnzipListenerTajweed listener;

	public void setReciter(int reciter) {
		this.reciter = reciter;
	}

	public void setListener(UnzipListenerTajweed listener) {
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
		String _zipFile = DownloadingZipUtil.rootPathTajweed.getAbsolutePath() + "/" + DownloadingZipUtil.TAJWEED_ZIP_TEMP;

		try
		{
			FileInputStream fin = new FileInputStream(_zipFile);
			ZipInputStream zin = new ZipInputStream(fin);
			ZipEntry ze = null;

			while ((ze = zin.getNextEntry()) != null)
			{
				Log.v("Decompress", "Unzipping " + ze.getName());

				String name = ze.getName().toString();

				File tempFile = new File(DownloadingZipUtil.rootPathTajweed.getAbsolutePath(), name);

				if(!tempFile.exists())
				{
					FileOutputStream fout = new FileOutputStream(DownloadingZipUtil.rootPathTajweed.getAbsolutePath() + "/" + name);

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
