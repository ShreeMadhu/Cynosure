package com.mad.cynosure;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.TextView;

public class CustomProgressDialog {

	private Context context;
	private ProgressDialog customDialog;

	public CustomProgressDialog(Context context) {
		this.context = context;
	}

	public void showDialog() {
		customDialog = new ProgressDialog(context);
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customDialog.show();
		customDialog.setCancelable(false);
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.setContentView(R.layout.custom_progress_dialog);
		customDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	public void dismissDialog() {
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
		}
	}
}
