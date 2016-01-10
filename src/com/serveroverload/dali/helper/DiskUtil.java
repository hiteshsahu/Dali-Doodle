package com.serveroverload.dali.helper;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;
import android.util.Log;

public class DiskUtil {

	private final static String TAG = DiskUtil.class.getSimpleName();

	private static ArrayList<String> imageURL = new ArrayList<String>();

	public static ArrayList<String> getListOfDoodles(boolean forced) {

		if (!forced && !imageURL.isEmpty()) {
			return imageURL;
		} else {
			imageURL.clear();
			
			return fetchAllDoodles();
		}

	}

	private static ArrayList<String> fetchAllDoodles() {

		File folder = new File(getDoodleDirectory());

		File[] listOfFiles = folder.listFiles();

		if (null != listOfFiles && listOfFiles.length != 0) {

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {

					imageURL.add(listOfFiles[i].getAbsolutePath());

					Log.d(TAG, "File " + listOfFiles[i].getName());

				} else if (listOfFiles[i].isDirectory()) {

					Log.d(TAG, "Directory " + listOfFiles[i].getName());
				}
			}
		}

		return imageURL;
	}

	private static String getDoodleDirectory() {

		return createDirIfNotExists().getAbsolutePath();
	}

	private static File createDirIfNotExists() {

		File imageDirectory = new File(
				Environment.getExternalStorageDirectory(),
				AppContants.DOODLE_DIRECTORY);

		if (!imageDirectory.exists()) {

			Log.i(TAG, "Creating Image folder");

			if (!imageDirectory.mkdirs()) {

				Log.e(TAG, "Problem creating Image folder");
			}
		}
		return imageDirectory;
	}

}
