package com.serveroverload.dali.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.serveroverload.dali.R;

public class LoadingDialog extends Dialog {
	
	private Animation loading_animation;
	private ImageView loading_img;

	public LoadingDialog(Context context) {
		super(context);
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
		this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_loading);
		loading_img = (ImageView) findViewById(R.id.loading_img);
	}
	
	@Override
	public void show() {
		loading_animation = AnimationUtils.loadAnimation(this.getContext(), R.anim.loading);
		loading_img.startAnimation(loading_animation);
		super.show();
	}
	
	@Override
	public void dismiss() {
		loading_img.clearAnimation();
		loading_animation = null;
		
		super.dismiss();
	}
	
	@Override
	public void onBackPressed() {}

}
