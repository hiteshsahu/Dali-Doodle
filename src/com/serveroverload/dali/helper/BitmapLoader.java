package com.serveroverload.dali.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class BitmapLoader {

	public Bitmap load(Context context, int[] holderDimension, String image_url) throws Exception {
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		bitmapOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(image_url, bitmapOptions);

		int inSampleSize = 1;

		final int outWidth = bitmapOptions.outWidth;
		final int outHeight = bitmapOptions.outHeight;

		final int holderWidth = holderDimension[0];
		final int holderHeight = holderDimension[1];

		// Calculation inSampleSize
		if (outHeight > holderHeight || outWidth > holderWidth) {
			final int halfWidth = outWidth / 2;
			final int halfHeight = outHeight / 2;

			while ((halfHeight / inSampleSize) > holderHeight && (halfWidth / inSampleSize) > holderWidth) {
				inSampleSize *= 2;
			}
		}

		bitmapOptions.inSampleSize = inSampleSize;

		// Decoding bitmap
		bitmapOptions.inJustDecodeBounds = false;
		return BitmapProcessing.modifyOrientation(BitmapFactory.decodeFile(image_url, bitmapOptions), image_url);
	}
	
	public Bitmap load(Context context, int[] holderDimension, Uri image_uri) throws Exception {
		String image_url = UriToUrl.get(context, image_uri);
		if (image_url != null) {
			return load(context, holderDimension, image_url);
		}
		return null;
	}
	
	public Bitmap load(Context context, String image_url) throws Exception {
		BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
		
		bitmapOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(image_url, bitmapOptions);
		
		final float imageSize = (float) bitmapOptions.outWidth * (float) bitmapOptions.outHeight * 4.0f / 1024.0f / 1024.0f; // MB
		
		bitmapOptions.inSampleSize = (int) Math.pow(2, Math.floor(imageSize / MemoryManagement.free(context)));
		bitmapOptions.inJustDecodeBounds = false;
		
		return BitmapProcessing.modifyOrientation(BitmapFactory.decodeFile(image_url, bitmapOptions), image_url);
	}

}
