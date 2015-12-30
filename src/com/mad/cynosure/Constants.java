package com.mad.cynosure;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Toast;

public class Constants {

	public static final String PARSE_APPLICATION_ID = "CBuFcItjzymrrHHFqBKNW36nbVrn5R99QYRpYqX9";
	public static final String PARSE_CLIENT_KEY = "ROZJ78Cg48j9716ZiSOiaeuIEkr6L17tJYpW7zFu";

	public static final String TOOL_BAR_SIGN_UP = "Sign up";
	public static final String TOOL_BAR_CONTACTS = "Contacts";
	public static final String TOOL_BAR_CHATS = "Home";

	public static final String PARSE_USER_PHONE_KEY = "Phone";
	public static final String PARSE_USER_PWD = "freeUser";

	public static final int MESSAGE_TYPE_TEXT = 1;
	public static final int MESSAGE_TYPE_IMAGE = 2;
	public static final int MESSAGE_TYPE_LOCATION = 3;

	public static final String CONVERSATION_ID = "conversationId";
	public static final String CONVERSATION_NAME = "name";
	public static final String CONVERSATION_PARTICIPANTS = "participants";
	public static final String CONVERSATION_CREATED_AT = "createdAt";

	public static final String MESSAGE_MESSAGE_BODY = "message";
	public static final String MESSAGE_TYPE = "type";
	public static final String MESSAGE_USER_ID = "userId";
	public static final String MESSAGE_CONVERSATION_ID = "conversationId";
	public static final String MESSAGE_FILE = "file";
	public static final String MESSAGE_CREATED_AT = "createdAt";

	public static final String USER_ID = "userId";
	public static final String NEW_CONVERSATION = "newConversation";

	public static Typeface medium, regular, light;

	public static void setTypeFace(Context context) {
		medium = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Medium.ttf");

		regular = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Regular.ttf");
		light = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
	}

	public static void displayToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
	}

}
