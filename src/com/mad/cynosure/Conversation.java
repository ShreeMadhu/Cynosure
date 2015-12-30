package com.mad.cynosure;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Conversation")
public class Conversation extends ParseObject {

	public void setConversationId(String conversationId) {
		put(Constants.CONVERSATION_ID, conversationId);
	}

	public void setName(String name) {
		put(Constants.CONVERSATION_NAME, name);
	}

	public void setParticipants(String participants) {
		put(Constants.CONVERSATION_PARTICIPANTS, participants);
	}

	public void setCreatedAt(String createdAt) {
		put(Constants.CONVERSATION_CREATED_AT, createdAt);
	}

	public String getName() {
		return getString(Constants.CONVERSATION_NAME);
	}

	public String getParticipants() {
		return getString(Constants.CONVERSATION_PARTICIPANTS);
	}

	public String getConversationId() {
		return getString(Constants.CONVERSATION_ID);
	}

	public Date getCreatedAt() {
		return getCreatedAt();
	}

}
