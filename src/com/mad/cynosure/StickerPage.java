package com.mad.cynosure;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

public class StickerPage extends Activity {

	private GridView stickerGrid;
	private ArrayList<Integer> imageList;

	private String userId;
	private String conversationId;
	private boolean newConversation;
	private String name;

	private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_sticker);

		userId = getIntent().getStringExtra(Constants.USER_ID);
		conversationId = getIntent().getStringExtra(Constants.CONVERSATION_ID);
		newConversation = getIntent().getBooleanExtra(
				Constants.NEW_CONVERSATION, false);
		name = getIntent().getStringExtra(Constants.CONVERSATION_NAME);

		setUpImages();
		stickerGrid = (GridView) findViewById(R.id.stickerGrid);
		dialog = new CustomProgressDialog(this);

		StickerAdapter stickerAdapter = new StickerAdapter(this, imageList);
		stickerGrid.setAdapter(stickerAdapter);

		stickerGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int tempPos = position;
				final CustomDialogBox captionDialog = new CustomDialogBox(
						StickerPage.this);
				captionDialog.showDialog("Send Sticker",
						"Would you like to enter any caption?");
				captionDialog
						.setNegativeOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								captionDialog.dismissDialog();
							}
						});
				captionDialog
						.setPositiveOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								String caption = captionDialog
										.getMeEnteredText();
								uploadAndSendImage(tempPos, caption);
							}
						});
			}
		});

		super.onCreate(savedInstanceState);
	}

	public void uploadAndSendImage(int position, final String message) {
		dialog.showDialog();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				imageList.get(position));

		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byte[] image = stream.toByteArray();

		String currentDate = Constants.getDateTime();
		currentDate = currentDate.replaceAll(" ", "");
		final ParseFile file = new ParseFile(userId + currentDate + ".png",
				image);
		file.saveInBackground();

		file.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {
					new ParseOperations(StickerPage.this, message, userId,
							conversationId, name, Constants.MESSAGE_TYPE_IMAGE,
							file, sendMessageHandler, newConversation)
							.sendMesssage();
				} else {
					dialog.dismissDialog();
					Log.d("cynosureMsg", "e - " + e);
					Constants.displayToast(StickerPage.this,
							"A problem occurred. Check your network !");
				}
			}
		});
	}

	private Handler sendMessageHandler = new Handler() {

		@Override
		public void handleMessage(Message handlerMsg) {
			if (handlerMsg.what == 2) {
				dialog.dismissDialog();
				Constants
						.displayToast(StickerPage.this, "Check your network !");
			} else if (handlerMsg.what == 1) {
				dialog.dismissDialog();
				finish();
			}
		}
	};

	public void setUpImages() {
		imageList = new ArrayList<Integer>();
		imageList.add(this.getResources().getIdentifier("smile", "drawable",
				this.getPackageName()));
		imageList.add(this.getResources().getIdentifier("you_are_right",
				"drawable", this.getPackageName()));
	}
}
