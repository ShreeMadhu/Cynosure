package com.mad.cynosure;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class CynosureUsers extends AppCompatActivity {

	private boolean errorOccurred;
	private ArrayList<ParseUser> parseUserList;
	private TextView noUserTv;
	private ListView userList;

	private UserListAdapter adapter;

	private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cynosure_users);

		CustomToolBar.applyCustomToolBar(this);
		CustomToolBar.removeLeftImage();
		CustomToolBar.removeRightImage();
		CustomToolBar.setTitle(Constants.TOOL_BAR_CONTACTS);

		noUserTv = (TextView) findViewById(R.id.noContentTv);
		userList = (ListView) findViewById(R.id.contentList);

		userList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CynosureApplication.setChosenParseUser(position);
				ParseUser user = CynosureApplication.getChosenParseUser();
				String userName = user.getUsername();
				String userId = user.getObjectId();
				checkForConversation(userId, userName);
			}
		});

		fetchContacts();
	}

	public void displayContacts() {
		if (errorOccurred) {
			noUserTv.setVisibility(View.VISIBLE);
		} else if (parseUserList == null || parseUserList.size() == 0) {
			noUserTv.setVisibility(View.VISIBLE);
		} else {
			adapter = new UserListAdapter(this);
			userList.setAdapter(adapter);
		}
	}

	public void fetchContacts() {

		dialog = new CustomProgressDialog(CynosureUsers.this);
		dialog.showDialog();

		String currentUserId = ParseUser.getCurrentUser().getObjectId();
		ParseQuery<ParseUser> query = ParseUser.getQuery();

		query.whereNotEqualTo("objectId", currentUserId);
		query.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> userList,
					com.parse.ParseException e) {
				if (e == null) {
					CynosureApplication.setUpUserList(userList);
					parseUserList = CynosureApplication.getUserList();
					dialog.dismissDialog();
				} else {
					Log.d("Cynosure", "pe - " + e.toString());
					errorOccurred = true;
					dialog.dismissDialog();
					Toast.makeText(getApplicationContext(),
							"Error loading user list", Toast.LENGTH_LONG)
							.show();
				}
				displayContacts();
			}
		});

	}

	public void checkForConversation(String userId, String name) {

		dialog = new CustomProgressDialog(CynosureUsers.this);
		dialog.showDialog();

		String myIdAsParticipant = ParseUser.getCurrentUser().getObjectId();

		ParseQuery<com.mad.cynosure.Conversation> query = ParseQuery
				.getQuery(com.mad.cynosure.Conversation.class);
		query.whereEqualTo(Constants.CONVERSATION_PARTICIPANTS,
				myIdAsParticipant);
		query.whereEqualTo(Constants.CONVERSATION_NAME, name);

		query.findInBackground(new FindCallback<com.mad.cynosure.Conversation>() {
			public void done(List<com.mad.cynosure.Conversation> messageList,
					com.parse.ParseException e) {
				if (e == null) {
					if (messageList.size() > 0) {

						String conversationId = messageList.get(0)
								.getConversationId();
						String conversationName = messageList.get(0).getName();
						String userId = messageList.get(0).getParticipants();
						Intent intent = new Intent(CynosureUsers.this,
								ChatPage.class);

						intent.putExtra(Constants.CONVERSATION_ID,
								conversationId);
						intent.putExtra(Constants.CONVERSATION_NAME,
								conversationName);
						intent.putExtra(Constants.USER_ID, userId);
						startActivity(intent);
						dialog.dismissDialog();
						finish();
					} else {
						Intent intent = new Intent(CynosureUsers.this,
								ChatPage.class);
						startActivity(intent);
						finish();
					}
				} else {
					Log.d("Cynosure", "pe - " + e.toString());
					errorOccurred = true;
					dialog.dismissDialog();
					Constants.displayToast(getApplicationContext(),
							"Error loading chat history");
				}
			}
		});
	}
}
