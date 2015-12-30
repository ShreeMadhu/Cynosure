package com.mad.cynosure;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomDialogBox {

	private Context context;
	private Dialog customDialog;
	private TextView customDialogTitle;
	private TextView customDialogMessage;

	private Button positiveButton;
	private Button negButton;

	private EditText editText;

	public CustomDialogBox(Context context) {
		this.context = context;
	}

	public void showDialog(String title, String message) {
		customDialog = new Dialog(context);
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		customDialog.show();
		customDialog.setCancelable(false);
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.setContentView(R.layout.custom_dialog);

		customDialogTitle = (TextView) customDialog
				.findViewById(R.id.dialogHeading);
		customDialogTitle.setTypeface(Constants.regular);

		customDialogMessage = (TextView) customDialog
				.findViewById(R.id.dialogMessage);
		customDialogTitle.setTypeface(Constants.light);

		positiveButton = (Button) customDialog.findViewById(R.id.positive);
		positiveButton.setTypeface(Constants.medium);

		negButton = (Button) customDialog.findViewById(R.id.negative);
		negButton.setTypeface(Constants.medium);

		editText = (EditText) customDialog.findViewById(R.id.editText);
		editText.setTypeface(Constants.medium);

		customDialogTitle.setText(title);
		customDialogMessage.setText(message);

		customDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
	}

	public void setPositiveOnClickListener(OnClickListener listener) {
		positiveButton.setOnClickListener(listener);
	}

	public void setNegativeOnClickListener(OnClickListener listener) {
		negButton.setOnClickListener(listener);
	}

	public void dismissDialog() {
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
		}
	}

	public String getMeEnteredText() {
		String text = "";
		if (editText != null) {
			String temp = editText.getText().toString().trim();
			text = temp;
		}
		return text;
	}

}
