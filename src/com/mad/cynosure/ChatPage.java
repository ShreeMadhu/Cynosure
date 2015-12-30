package com.mad.cynosure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ChatPage extends AppCompatActivity {

	private ParseUser chosenParseUser;

	private ImageView sendMsg;
	private ImageView sticker;

	private String contactName;

	private EditText msg;

	private ListView msgList;

	private ArrayList<com.mad.cynosure.Message> msgHistory;

	private Handler sendMessageHandler;
	private Handler receiveMessageHanlder;

	private ChatAdapter adapter;
	private Context context;

	private String conversationId;
	private String userId;

	private boolean newConversation;

	private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		dialog = new CustomProgressDialog(ChatPage.this);
		context = this;

		conversationId = getIntent().getStringExtra(Constants.CONVERSATION_ID);
		contactName = getIntent().getStringExtra(Constants.CONVERSATION_NAME);
		userId = getIntent().getStringExtra(Constants.USER_ID);

		if (conversationId == null) {
			newConversation = true;
			chosenParseUser = CynosureApplication.getChosenParseUser();
			contactName = chosenParseUser.getUsername();
			userId = chosenParseUser.getObjectId();
			conversationId = ParseUser.getCurrentUser().getObjectId() + "-"
					+ userId;
		}

		CustomToolBar.applyCustomToolBar(this);
		CustomToolBar.removeLeftImage();
		CustomToolBar.removeRightImage();
		CustomToolBar.setTitle(contactName);

		sendMsg = (ImageView) findViewById(R.id.sendMsg);
		msg = (EditText) findViewById(R.id.msg);
		msgList = (ListView) findViewById(R.id.msgList);

		receiveMessageHanlder = new Handler();
		receiveMessageHanlder.postDelayed(runnable, 100);

		sendMessageHandler = new Handler() {

			@Override
			public void handleMessage(Message handlerMsg) {
				if (handlerMsg.what == 2) {
					dialog.dismissDialog();
					Constants.displayToast(ChatPage.this,
							"Check your network !");
				} else if (handlerMsg.what == 1) {
					dialog.dismissDialog();
					if (newConversation) {
						newConversation = false;
					}
				}
			}
		};

		sendMsg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.showDialog();
				String message = msg.getText().toString();
				msg.setText("");
				new ParseOperations(ChatPage.this, message, userId,
						conversationId, contactName,
						Constants.MESSAGE_TYPE_TEXT, null, sendMessageHandler,
						newConversation).sendMesssage();
			}
		});

		sticker = (ImageView) findViewById(R.id.sticker);
		sticker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChatPage.this, StickerPage.class);
				intent.putExtra("userId", userId);
				intent.putExtra(Constants.CONVERSATION_ID, conversationId);
				intent.putExtra(Constants.NEW_CONVERSATION, newConversation);
				intent.putExtra(Constants.CONVERSATION_NAME, contactName);
				startActivity(intent);
			}
		});
	}

	public void createChat(List<com.mad.cynosure.Message> messageList) {
		int previousMsgHistorySize = 0;
		if (msgHistory == null) {
			msgHistory = new ArrayList<com.mad.cynosure.Message>();
		} else {
			previousMsgHistorySize = msgHistory.size();
			msgHistory.clear();
		}
		for (int i = 0; i < messageList.size(); i++) {
			msgHistory.add(messageList.get(i));
			Log.d("Cynosure", "message - " + messageList.get(i).getMessage());
		}
		if (adapter == null) {
			adapter = new ChatAdapter(context, msgHistory);
			msgList.setAdapter(adapter);
			msgList.setSelection(adapter.getCount() - 1);
		} else if (msgHistory.size() > previousMsgHistorySize) {
			adapter.updateMsgHistory(msgHistory);
			msgList.invalidate();
			adapter.notifyDataSetChanged();
			msgList.setSelection(adapter.getCount() - 1);
		}
	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			refreshMessages();
			receiveMessageHanlder.postDelayed(this, 100);
		}
	};

	private void refreshMessages() {
		if (!newConversation) {
			receiveMessage();
		}
	}

	public void receiveMessage() {
		ParseQuery<com.mad.cynosure.Message> query = ParseQuery
				.getQuery(com.mad.cynosure.Message.class);

		query.whereEqualTo(Constants.CONVERSATION_ID, conversationId);
		query.findInBackground(new FindCallback<com.mad.cynosure.Message>() {
			public void done(List<com.mad.cynosure.Message> messageList,
					com.parse.ParseException e) {
				if (e == null) {
					createChat(messageList);
				} else {
					Log.d("Cynosure", "pe - " + e.toString());
				}
			}
		});
	}

	@Override
	protected void onResume() {
		if (receiveMessageHanlder != null) {
			receiveMessageHanlder.postDelayed(runnable, 100);
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		receiveMessageHanlder.removeCallbacks(runnable);
		super.onPause();
	}

	@Override
	protected void onStop() {
		receiveMessageHanlder.removeCallbacks(runnable);
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		adapter = null;
		receiveMessageHanlder.removeCallbacks(runnable);
		super.onDestroy();
	}
}