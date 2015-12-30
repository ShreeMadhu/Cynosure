package com.mad.cynosure;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class CynosureApplication extends Application {

	private static ArrayList<ParseUser> parseUserList;
	private static ParseUser chosenParseUser;

	private static ArrayList<com.mad.cynosure.Conversation> chatList;

	@Override
	public void onCreate() {
		super.onCreate();
		Constants.setTypeFace(this);
		Parse.enableLocalDatastore(this);
		ParseObject.registerSubclass(Message.class);
		ParseObject.registerSubclass(Conversation.class);
		Parse.initialize(this, Constants.PARSE_APPLICATION_ID,
				Constants.PARSE_CLIENT_KEY);
	}

	public static void setUpUserList(List<ParseUser> userList) {
		if (parseUserList == null) {
			parseUserList = new ArrayList<ParseUser>();
		} else {
			parseUserList.clear();
		}
		for (int i = 0; i < userList.size(); i++) {
			parseUserList.add(userList.get(i));
			Log.d("Cynosure",
					"userList - "
							+ userList.get(i).getUsername()
							+ "  "
							+ userList.get(i).getString(
									Constants.PARSE_USER_PHONE_KEY));
		}
	}

	public static void setUpChatList(List<com.mad.cynosure.Conversation> chat) {
		if (chatList == null) {
			chatList = new ArrayList<com.mad.cynosure.Conversation>();
		} else {
			chatList.clear();
		}
		for (int i = 0; i < chat.size(); i++) {
			chatList.add(chat.get(i));
		}
	}

	public static ArrayList<ParseUser> getUserList() {
		return parseUserList;
	}

	public static ArrayList<com.mad.cynosure.Conversation> getChatList() {
		return chatList;
	}

	public static void setChosenParseUser(int position) {
		chosenParseUser = parseUserList.get(position);
	}

	public static ParseUser getChosenParseUser() {
		return chosenParseUser;
	}
}
