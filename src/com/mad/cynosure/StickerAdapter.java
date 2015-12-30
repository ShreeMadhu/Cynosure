package com.mad.cynosure;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class StickerAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Integer> imageList;
	private LayoutInflater inflater;

	public StickerAdapter(Context context, ArrayList<Integer> imageList) {
		this.context = context;
		this.imageList = imageList;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
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
			convertView = inflater.inflate(R.layout.sticker_grid_adapter, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.stickerImage);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int imageId = imageList.get(curPosition);
		holder.image.setBackgroundResource(imageId);
		return convertView;
	}

	private class ViewHolder {
		private ImageView image;
	}

}
