package com.mad.cynosure;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<com.mad.cynosure.Conversation> chatList;
	private LayoutInflater inflater;

	private String name;
	private String firstLetter;

	public ChatListAdapter(Context context) {
		this.context = context;
		this.chatList = CynosureApplication.getChatList();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateChatList(
			ArrayList<com.mad.cynosure.Conversation> updatedChatList) {
		chatList = updatedChatList;
	}

	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public Conversation getItem(int position) {
		Conversation conversation = chatList.get(position);
		return conversation;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final int curPosition = position;
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.user_list_adapter, null);
			holder = new ViewHolder();

			holder.userFirstLetter = (TextView) convertView
					.findViewById(R.id.letter);
			holder.userFirstLetter.setTypeface(Constants.regular);

			holder.userName = (TextView) convertView.findViewById(R.id.name);
			holder.userName.setTypeface(Constants.light);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Conversation chatDetails = getItem(curPosition);
		name = chatDetails.getName();
		if (name != null && name.length() > 0) {
			firstLetter = name.substring(0, 1);
		} else {
			name = "Unkown";
			firstLetter = "U";
		}

		holder.userFirstLetter.setText(firstLetter.toUpperCase());
		holder.userName.setText(name);

		return convertView;
	}

	private class ViewHolder {
		private TextView userFirstLetter;
		private TextView userName;
	}

}
