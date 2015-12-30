package com.mad.cynosure;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

	public void setMessage(String message) {
		put(Constants.MESSAGE_MESSAGE_BODY, message);
	}

	public void setType(int type) {
		put(Constants.MESSAGE_TYPE, type);
	}

	public void setUserId(String userId) {
		put(Constants.MESSAGE_USER_ID, userId);
	}

	public void setConversationId(String conversationId) {
		put(Constants.MESSAGE_CONVERSATION_ID, conversationId);
	}

	public void setFile(ParseFile file) {
		put(Constants.MESSAGE_FILE, file);
	}

	public void setCreatedAt(String createdAt) {
		put(Constants.MESSAGE_CREATED_AT, createdAt);
	}

	public String getMessage() {
		return getString(Constants.MESSAGE_MESSAGE_BODY);
	}

	public int getType() {
		return getInt(Constants.MESSAGE_TYPE);
	}

	public String getUserId() {
		return getString(Constants.MESSAGE_USER_ID);
	}

	public String getConversationId() {
		return getString(Constants.MESSAGE_CONVERSATION_ID);
	}

	public ParseFile getFile() {
		return getParseFile(Constants.MESSAGE_FILE);
	}

	public Date getCreatedAt() {
		return getCreatedAt();
	}

}
