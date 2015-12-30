package com.mad.cynosure;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ParseOperations {

	private Context context;
	private String message;
	private String userId;
	private String currentUserId;
	private int type;
	private ParseFile file;
	private Handler handler;
	private String conversationId;
	private String name;
	private boolean newConversation;

	public ParseOperations(Context context, String message, String userId,
			String conversationId, String name, int type, ParseFile file,
			Handler handler, boolean newConversation) {
		this.context = context;
		this.message = message;
		this.userId = userId;
		this.conversationId = conversationId;
		this.name = name;
		this.type = type;
		this.file = file;
		this.handler = handler;
		this.newConversation = newConversation;
		currentUserId = ParseUser.getCurrentUser().getObjectId();
	}

	public void setUpParticipants() {

	}

	public ParseOperations(Context context, String userId) {
		this.context = context;
		this.userId = userId;
	}

	public void sendMesssage() {
		if (type == 1 && message.isEmpty()) {
			return;
		}
		Log.d("cynosureMsg", "send message - " + newConversation);
		if (newConversation) {
			updateConversation(userId, ParseUser.getCurrentUser().getUsername());
		} else {
			updateMessage();
		}
	}

	public void updateMessage() {

		Log.d("cynosureMsg", "update message, about to send....");

		ParseObject messageObj = ParseObject.create("Message");
		messageObj.put(Constants.USER_ID, ParseUser.getCurrentUser()
				.getObjectId());
		messageObj.put(Constants.MESSAGE_CONVERSATION_ID, conversationId);
		messageObj.put(Constants.MESSAGE_MESSAGE_BODY, message);
		messageObj.put(Constants.MESSAGE_TYPE, String.valueOf(type));
		if (file == null) {
			byte[] data = new byte[] {};
			file = new ParseFile(data);
		}
		messageObj.put("file", file);
		messageObj.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					Constants.displayToast(context, "Message sent");
					handler.obtainMessage(1).sendToTarget();
				} else {
					Log.d("cynosureMsg", "e - " + e);
					handler.obtainMessage(2).sendToTarget();
				}
			}
		});
	}

	public void updateConversation(final String id, String convName) {
		ParseObject convObj = ParseObject.create("Conversation");
		convObj.put(Constants.CONVERSATION_ID, conversationId);
		convObj.put(Constants.CONVERSATION_NAME, convName);
		convObj.put(Constants.CONVERSATION_PARTICIPANTS, id);

		convObj.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					if (currentUserId.equals(id)) {
						updateMessage();
					} else {
						updateConversation(currentUserId, name);
					}
				} else {
					Log.d("cynosureMsg", "e - " + e);
					handler.obtainMessage(2).sendToTarget();
				}
			}
		});

	}
}
