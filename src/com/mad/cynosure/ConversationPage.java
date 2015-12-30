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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ConversationPage extends AppCompatActivity {

	private boolean errorOccurred;
	private ArrayList<com.mad.cynosure.Conversation> chatHistoryList;
	private TextView noChatTv;
	private ListView chatList;

	private ChatListAdapter adapter;

	private CustomProgressDialog dialog;

	private Button contacts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cynosure_users);

		CustomToolBar.applyCustomToolBar(this);
		CustomToolBar.removeLeftImage();
		CustomToolBar.removeRightImage();
		CustomToolBar.setTitle(Constants.TOOL_BAR_CHATS);

		noChatTv = (TextView) findViewById(R.id.noContentTv);
		noChatTv.setTypeface(Constants.light);

		chatList = (ListView) findViewById(R.id.contentList);

		contacts = (Button) findViewById(R.id.contacts);
		contacts.setTypeface(Constants.medium);

		contacts.setVisibility(View.VISIBLE);

		contacts.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConversationPage.this,
						CynosureUsers.class);
				startActivity(intent);
			}
		});

		chatList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String conversationId = chatHistoryList.get(position)
						.getConversationId();
				String conversationName = chatHistoryList.get(position)
						.getName();
				String userId = chatHistoryList.get(position).getParticipants();
				Intent intent = new Intent(ConversationPage.this,
						ChatPage.class);

				intent.putExtra(Constants.CONVERSATION_ID, conversationId);
				intent.putExtra(Constants.CONVERSATION_NAME, conversationName);
				intent.putExtra(Constants.USER_ID, userId);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		fetchConversation();
		super.onResume();
	}

	public void displayChats() {
		if (errorOccurred) {
			noChatTv.setVisibility(View.VISIBLE);
			noChatTv.setText(getResources().getString(R.string.no_chat));
		} else if (chatHistoryList == null || chatHistoryList.size() == 0) {
			noChatTv.setVisibility(View.VISIBLE);
			noChatTv.setText(getResources().getString(R.string.no_chat));
		} else {
			noChatTv.setVisibility(View.GONE);
			if (adapter == null) {
				adapter = new ChatListAdapter(this);
				chatList.setAdapter(adapter);
			} else {
				adapter.updateChatList(chatHistoryList);
				adapter.notifyDataSetChanged();
			}
		}
	}

	public void fetchConversation() {

		dialog = new CustomProgressDialog(ConversationPage.this);
		dialog.showDialog();

		ParseQuery<com.mad.cynosure.Conversation> query = ParseQuery
				.getQuery(com.mad.cynosure.Conversation.class);

		String myIdAsParticipant = ParseUser.getCurrentUser().getObjectId();

		query.whereEqualTo(Constants.CONVERSATION_PARTICIPANTS,
				myIdAsParticipant);
		query.findInBackground(new FindCallback<com.mad.cynosure.Conversation>() {
			public void done(List<com.mad.cynosure.Conversation> messageList,
					com.parse.ParseException e) {
				if (e == null) {
					CynosureApplication.setUpChatList(messageList);
					chatHistoryList = CynosureApplication.getChatList();
					dialog.dismissDialog();
				} else {
					Log.d("Cynosure", "pe - " + e.toString());
					errorOccurred = true;
					dialog.dismissDialog();
					Constants.displayToast(getApplicationContext(),
							"Error loading chat history");
				}
				displayChats();
			}
		});
	}
}
