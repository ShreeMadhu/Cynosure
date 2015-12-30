package com.mad.cynosure;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class ChatAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<com.mad.cynosure.Message> msgHistory;
	private LayoutInflater inflater;

	private ViewHolder holder;

	public ChatAdapter(Context context,
			ArrayList<com.mad.cynosure.Message> msgHistory) {
		this.context = context;
		this.msgHistory = msgHistory;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void updateMsgHistory(
			ArrayList<com.mad.cynosure.Message> updatedMsgHistory) {
		msgHistory = updatedMsgHistory;
	}

	@Override
	public int getCount() {
		return msgHistory.size();
	}

	@Override
	public com.mad.cynosure.Message getItem(int position) {
		com.mad.cynosure.Message message = msgHistory.get(position);
		return message;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int curPosition = position;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.chat_list_adapter, null);
			holder = new ViewHolder();

			holder.senderLayout = (LinearLayout) convertView
					.findViewById(R.id.senderLayout);
			holder.receiverLayout = (LinearLayout) convertView
					.findViewById(R.id.receiverLayout);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		com.mad.cynosure.Message message = getItem(curPosition);
		String userId = message.getUserId();
		String currentUserId = ParseUser.getCurrentUser().getObjectId();
		LinearLayout layoutToBeSent = null;
		if (userId.equals(currentUserId)) {
			layoutToBeSent = holder.senderLayout;
			holder.receiverLayout.setVisibility(View.GONE);
		} else {
			layoutToBeSent = holder.receiverLayout;
			holder.senderLayout.setVisibility(View.GONE);
		}
		setUpMessageLayout(message, layoutToBeSent);

		return convertView;
	}

	private class ViewHolder {
		private LinearLayout senderLayout;
		private LinearLayout receiverLayout;
	}

	public void setUpMessageLayout(com.mad.cynosure.Message message,
			LinearLayout parent) {

		String msgType = message.getString("type");
		if (msgType.equals("1")) {
			String msg = message.getMessage();
			parent.setVisibility(View.VISIBLE);
			TextView msgText = giveMeTextView(msg);

			if (parent.getChildCount() > 0) {
				parent.removeAllViews();
			}
			parent.addView(msgText);
		} else if (msgType.equals("2")) {
			ParseFile file = message.getParseFile("file");
			String msg = message.getMessage();
			parent.setVisibility(View.VISIBLE);
			TextView msgText = null;
			if (msg != null && !msg.equals("")) {
				msgText = giveMeTextView(msg);
			}

			ImageView image = giveMeImageView(file);
			if (image != null) {
				if (parent.getChildCount() > 0) {
					parent.removeAllViews();
				}
				parent.addView(image);
				if (msgText != null) {
					parent.addView(msgText);
				}
			}

		}
	}

	public ImageView giveMeImageView(ParseFile file) {
		ImageView image = null;
		if (file != null) {
			byte[] bitmapdata = null;
			try {
				bitmapdata = file.getData();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			Bitmap bitmap = giveMeImage(100, bitmapdata);

			image = new ImageView(context);
			image.setImageBitmap(bitmap);
		}
		return image;
	}

	public TextView giveMeTextView(String message) {
		TextView msgText = new TextView(context);
		msgText.setTextColor(Color.parseColor("#FFFFFF"));
		msgText.setTypeface(Constants.light);
		msgText.setTextSize(15);
		msgText.setText(message);
		return msgText;
	}

	public Bitmap giveMeImage(int size, byte[] bitmapData) {
		Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0,
				bitmapData.length, options);
		int sampleSize = calculateInSampleSize(options, size, size);

		options.inSampleSize = sampleSize;
		options.inScreenDensity = DisplayMetrics.DENSITY_HIGH;
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeByteArray(bitmapData, 0,
				bitmapData.length, options);

		return bitmap;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;

		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

}
