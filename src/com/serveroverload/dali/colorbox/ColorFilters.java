package com.serveroverload.dali.colorbox;
//package com.android.graphics;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.ColorFilter;
//import android.graphics.ColorMatrixColorFilter;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffColorFilter;
//import android.widget.ImageView;
//
//import com.example.casnasas.R;
//
//public class ColorFilters {
//	
//	
//	 public void setColorFilter(ImageView iv ,
//			 
//			 float redValue ,  float greenValue , float blueValue ){
//	     
//	       /*
//	        * 5x4 matrix for transforming the color+alpha components of a Bitmap. 
//	        * The matrix is stored in a single array, and its treated as follows: 
//	        * [  a, b, c, d, e, 
//	        *   f, g, h, i, j, 
//	        *   k, l, m, n, o, 
//	        *   p, q, r, s, t ] 
//	        * 
//	        * When applied to a color [r, g, b, a], the resulting color is computed 
//	        * as (after clamping) 
//	        * R' = a*R + b*G + c*B + d*A + e; 
//	        * G' = f*R + g*G + h*B + i*A + j; 
//	        * B' = k*R + l*G + m*B + n*A + o; 
//	        * A' = p*R + q*G + r*B + s*A + t;
//	        */
//	     
////	     
////	     float redValue = ((float)redBar.getProgress())/255;
////	     float greenValue = ((float)greenBar.getProgress())/255;
////	     float blueValue = ((float)blueBar.getProgress())/255;
//	     
//	     float[] colorMatrix = { 
//	       redValue, 0, 0, 0, 0,  //red
//	       0, greenValue, 0, 0, 0, //green
//	       0, 0, blueValue, 0, 0,  //blue
//	       0, 0, 0, 1, 0    //alpha    
//	     };
//	     
//	     ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
//	     
//	     iv.setColorFilter(colorFilter);
//
//	    }
//	 
//	 
//	private void applyPorterDuff(Canvas canvas ,Bitmap bitmap) {
//
//		int srcColor = 0xFF00FF00;
//		PorterDuff.Mode mode = PorterDuff.Mode.ADD;
//
//		PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(
//				srcColor, mode);
//
//		Paint MyPaint_Normal = new Paint();
//		Paint MyPaint_PorterDuff = new Paint();
//		MyPaint_PorterDuff.setColorFilter(porterDuffColorFilter);
//
////		Bitmap myBitmap = BitmapFactory.decodeResource(getResources(),
////				R.drawable.ic_launcher);
//		//canvas.drawBitmap(myBitmap, 400, 100, MyPaint_Normal);
//		canvas.drawBitmap(bitmap, 500, 100, MyPaint_PorterDuff);
//
//	}
//	
//	
//	 private void setPorterDuffColorFilter(ImageView iv){
//	     
//	     int srcColor = Color.argb(
//	       alphaBar.getProgress(), 
//	       redBar.getProgress(), 
//	       greenBar.getProgress(), 
//	       blueBar.getProgress());
//	     
//	     PorterDuff.Mode mode = optMode[modeSpinner.getSelectedItemPosition()];
//	     
//	     PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(srcColor, mode);
//	     iv.setColorFilter(porterDuffColorFilter);
//	     
//	     colorInfo.setText(
//	       "srcColor = #" + Integer.toHexString(srcColor) +"\n" +
//	       "mode = " + String.valueOf(mode.toString()) + " of total " + String.valueOf(optMode.length) + " modes.");
//	  
//	    }
//	 
//	 
//
//
//}
