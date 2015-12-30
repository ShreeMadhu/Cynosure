package com.mad.cynosure;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

public class UserListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ParseUser> userList;
	private LayoutInflater inflater;

	private String name;
	private String firstLetter;

	public UserListAdapter(Context context) {
		this.context = context;
		this.userList = CynosureApplication.getUserList();
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateUserList(ArrayList<ParseUser> updatedUserList) {
		userList = updatedUserList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userList.size();
	}

	@Override
	public ParseUser getItem(int position) {
		ParseUser parseUser = userList.get(position);
		return parseUser;
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

		ParseUser userDetails = getItem(curPosition);
		name = userDetails.getUsername();
		firstLetter = name.substring(0, 1);

		holder.userFirstLetter.setText(firstLetter.toUpperCase());
		holder.userName.setText(name);

		return convertView;
	}

	private class ViewHolder {
		private TextView userFirstLetter;
		private TextView userName;
	}

}
