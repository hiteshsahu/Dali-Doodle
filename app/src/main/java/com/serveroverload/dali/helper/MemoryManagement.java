package com.serveroverload.dali.helper;

import android.app.ActivityManager;
import android.content.Context;


public class MemoryManagement {

        /**
         * Gets available memory to load image 
        */
	public static float free(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int memoryClass = am.getMemoryClass() - 24;
		if (memoryClass < 1) memoryClass = 1;
		return (float) memoryClass;
	}
	
}
