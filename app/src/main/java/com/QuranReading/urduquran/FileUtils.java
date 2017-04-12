package com.QuranReading.urduquran;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.QuranReading.helper.DBManager;
import com.QuranReading.sharedPreference.SettingsSharedPref;

public class FileUtils {
	public static final String CHK_SUCCESSFUL = "chk_successful";
	public static final String CHK_FAILED = "chk_failed";
	public static final String CHK_RUNNING = "chk_running";
	public static final String CHK_PAUSED = "chk_paused";
	public static final String CHK_PENDING = "chk_pending";

	@SuppressWarnings("unused")
	public static HashMap<String, Boolean> chkDownloadStatus(Context ctx, Cursor cursor, String tempName, int referenceId, int surahPos) {
		HashMap<String, Boolean> chkStatus = new HashMap<String, Boolean>();
		boolean chkInProgress = false;
		boolean chkFailed = false, chkPaused = false, chkPending = false, chkRunning = false, chkSuccessful = false;

		// column for status
		int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
		int status = cursor.getInt(columnIndex);
		// column for reason code if the download failed or paused
		int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
		int reason = cursor.getInt(columnReason);
		/*
		 * //get the download filename int filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME); String filename = cursor.getString(filenameIndex);
		 */

		String statusText = "";
		String reasonText = "";

		switch (status) {
		case DownloadManager.STATUS_FAILED:
			statusText = "STATUS_FAILED";
			chkFailed = true;

			switch (reason) {
			case DownloadManager.ERROR_CANNOT_RESUME:
				reasonText = "ERROR_CANNOT_RESUME";
				break;
			case DownloadManager.ERROR_DEVICE_NOT_FOUND:
				reasonText = "ERROR_DEVICE_NOT_FOUND";
				break;
			case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
				reasonText = "ERROR_FILE_ALREADY_EXISTS";
				break;
			case DownloadManager.ERROR_FILE_ERROR:
				reasonText = "ERROR_FILE_ERROR";
				break;
			case DownloadManager.ERROR_HTTP_DATA_ERROR:
				reasonText = "ERROR_HTTP_DATA_ERROR";
				break;
			case DownloadManager.ERROR_INSUFFICIENT_SPACE:
				reasonText = "ERROR_INSUFFICIENT_SPACE";
				break;
			case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
				reasonText = "ERROR_TOO_MANY_REDIRECTS";
				break;
			case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
				reasonText = "ERROR_UNHANDLED_HTTP_CODE";
				break;
			case DownloadManager.ERROR_UNKNOWN:
				reasonText = "ERROR_UNKNOWN";
				break;
			}
			break;

		case DownloadManager.STATUS_PAUSED:
			statusText = "STATUS_PAUSED";
			chkPaused = true;
			switch (reason) {
			case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
				reasonText = "PAUSED_QUEUED_FOR_WIFI";
				break;
			case DownloadManager.PAUSED_UNKNOWN:
				reasonText = "PAUSED_UNKNOWN";
				break;
			case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
				reasonText = "PAUSED_WAITING_FOR_NETWORK";
				break;
			case DownloadManager.PAUSED_WAITING_TO_RETRY:
				reasonText = "PAUSED_WAITING_TO_RETRY";
				break;
			}
			break;

		case DownloadManager.STATUS_PENDING:
			chkPending = true;
			statusText = "STATUS_PENDING";
			break;

		case DownloadManager.STATUS_RUNNING:
			chkRunning = true;
			statusText = "STATUS_RUNNING";
			break;

		case DownloadManager.STATUS_SUCCESSFUL:
			chkSuccessful = true;
			statusText = "STATUS_SUCCESSFUL";
			reasonText = "Filename:\n";// + filename;
			break;
		}

		if (chkFailed)
		{
			try
			{
				ServiceClass.downloadManager.remove(referenceId);
			}
			catch (NullPointerException e)
			{
				e.printStackTrace();
			}

			deleteAudioFile(tempName);
			deleteFromDB(ctx, DBManager.FLD_DOWNLOAD_ID, referenceId);

		}
		else if (chkSuccessful)
		{
			checkOneAudioFile(ctx, tempName, surahPos);
		}
		else
		{
			chkInProgress = true;
		}

		chkStatus.put(DBManager.CHK_FAILED, chkFailed);
		chkStatus.put(DBManager.CHK_SUCCESSFUL, chkSuccessful);
		chkStatus.put(DBManager.CHK_RUNNING, chkInProgress);

		Log.e("On Status Check", statusText + "::::" + reasonText);

		return chkStatus;
	}

	public static boolean checkOneAudioFile(Context ctx, String tempName, int pos) {
		double actualSize = 0, fileSize;
		File tempFile;
		int resrcSizesArray = 0;
		boolean chk = false;
		String[] sizesArray = {};
		SettingsSharedPref settngPref = new SettingsSharedPref(ctx);
		HashMap<String, Integer> fontSettngs = settngPref.getFontSettings();
		int reciter = fontSettngs.get(SettingsSharedPref.RECITER);
		if (reciter == 0)
		{
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_basit", "array", ctx.getPackageName());
		}

		else if (reciter == 1)
		{
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_alfasay", "array", ctx.getPackageName());
		}

		else if (reciter == 2)
		{
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_sudais", "array", ctx.getPackageName());
		}

		if (resrcSizesArray > 0)
		{
			sizesArray = ctx.getResources().getStringArray(resrcSizesArray);
			actualSize = Double.parseDouble(sizesArray[pos - 1]);
		}

		tempFile = new File(Constants.rootPath.getAbsolutePath(), tempName);

		if (tempFile.exists())
		{
			fileSize = tempFile.length();

			if (fileSize == actualSize)
			{
				String originalName = tempName.substring(5, tempName.length());
				renameAudioFile(tempName, originalName);
				deleteFromDB(ctx, DBManager.FLD_SURAH_NO, pos);
				chk = true;
			}
			else if (fileSize < actualSize)
			{
				deleteAudioFile(tempName);
				deleteFromDB(ctx, DBManager.FLD_SURAH_NO, pos);
				chk = false;
			}
		}
		else
		{
			chk = false;
			deleteFromDB(ctx, DBManager.FLD_SURAH_NO, pos);
		}

		return chk;
	}

	public static void checkCompleteAudioFile(Context ctx, int reciter) {
		String[] namesArray = {};
		String[] sizesArray = {};
		int resrcSizesArray = 0, resrcNamesArray = 0;
		if (reciter == 0)
		{
			resrcNamesArray = ctx.getResources().getIdentifier("surah_temp_names_basit", "array", ctx.getPackageName());
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_basit", "array", ctx.getPackageName());
		}
		else if (reciter == 1)
		{
			resrcNamesArray = ctx.getResources().getIdentifier("surah_temp_names_alfasay", "array", ctx.getPackageName());
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_alfasay", "array", ctx.getPackageName());
		}
		else if (reciter == 2)
		{
			resrcNamesArray = ctx.getResources().getIdentifier("surah_temp_names_sudais", "array", ctx.getPackageName());
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_sudais", "array", ctx.getPackageName());
		}

		if (resrcNamesArray > 0)
		{
			namesArray = ctx.getResources().getStringArray(resrcNamesArray);
		}

		if (resrcSizesArray > 0)
		{
			sizesArray = ctx.getResources().getStringArray(resrcSizesArray);
		}

		File dir = new File(Constants.rootPath.getAbsolutePath());

		File[] directoryListing = dir.listFiles();

		if (directoryListing != null)
		{
			for (File child : directoryListing)
			{
				String fileName = child.getName();

				if (fileName.contains("temp"))
				{
					double bytes = child.length();

					for (int pos = 0; pos < 114; pos++)
					{
						if (fileName.equals(namesArray[pos]))
						{
							double actualSize = Double.parseDouble(sizesArray[pos]);

							if (bytes == actualSize)
							{
								String originalName = fileName.substring(5, fileName.length());
								renameAudioFile(fileName, originalName);
								deleteFromDB(ctx, DBManager.FLD_SURAH_NO, (pos + 1));
							}
						}
					}
				}
			}
		}
	}

	public static boolean checkAudioFileSize(Context ctx, String name, int pos, int reciter) {
		double actualSize = 0, fileSize;
		File audioFile;
		int resrcSizesArray = 0;
		boolean chk = false;

		String[] sizesArray = {};
		if (reciter == 0)
		{
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_basit", "array", ctx.getPackageName());
		}
		else if (reciter == 1)
		{
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_alfasay", "array", ctx.getPackageName());
		}
		else if (reciter == 2)
		{
			resrcSizesArray = ctx.getResources().getIdentifier("surah_sizes_sudais", "array", ctx.getPackageName());
		}

		if (resrcSizesArray > 0)
		{
			sizesArray = ctx.getResources().getStringArray(resrcSizesArray);
			actualSize = Double.parseDouble(sizesArray[pos - 1]);
		}

		audioFile = new File(Constants.rootPath.getAbsolutePath(), name);

		if (audioFile.exists())
		{
			fileSize = audioFile.length();

			if (fileSize == actualSize)
			{
				chk = true;
			}
			else
			{
				chk = false;
				deleteAudioFile(name);
				deleteFromDB(ctx, DBManager.FLD_SURAH_NO, pos);
			}
		}
		else
		{
			chk = false;
			deleteFromDB(ctx, DBManager.FLD_SURAH_NO, pos);
		}

		return chk;
	}

	public static void renameAudioFile(String tempName, String originalName) {
		File tempFile = new File(Constants.rootPath.getAbsolutePath(), tempName);
		File originalFile = new File(Constants.rootPath.getAbsolutePath(), originalName);

		if (tempFile.exists())
		{
			tempFile.renameTo(originalFile);

			/*
			 * if (tempFile.renameTo(originalFile)) { ((GlobalClass)getApplication()).showToast("Rename Successfully"); } else { ((GlobalClass) getApplication()).showToast("Not Rename"); }
			 */

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

	public static void deleteFromDB(Context ctx, String column, int pos) {
		DBManager dbObj = new DBManager(ctx);
		dbObj.open();
		dbObj.deleteOneDownload(column, pos);
		dbObj.close();
	}

	public static void deleteTempFiles() {
		File dir = new File(Constants.rootPath.getAbsolutePath());

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

	// For Check Size of Audio
	// Place Audio files in root folder and run this method to test
	public static void test(Context ctx) {
		ArrayList<String> dataList = new ArrayList<String>();
		ArrayList<String> dataList2 = new ArrayList<String>();

		File dir = new File(Constants.rootPath.getAbsolutePath());

		File[] directoryListing = dir.listFiles();

		if (directoryListing != null)
		{
			for (File child : directoryListing)
			{
				String fileName = child.getName();
				double bytes = child.length();

				dataList.add(fileName + " --- " + String.valueOf(bytes));
				dataList2.add("<item>" + String.valueOf(bytes) + "</item>");
			}
		}

		Log.e("Test", "End");
	}
}
