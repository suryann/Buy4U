package com.webuyforyou.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.webuyforyou.R;
import com.webuyforyou.model.LinkedinUserDataModel;

public class LinkedinListAdapter extends BaseAdapter {

	private Context mContext = null;
	private List<LinkedinUserDataModel> mDataModel = new ArrayList<LinkedinUserDataModel>();

	static class ViewHolder {
		public TextView nameTextView;
		public TextView dateTextView;
		public ImageView imageView;
		public CheckBox checkboxView;
	}

	public LinkedinListAdapter(Context context,
			List<LinkedinUserDataModel> values) {
		this.mContext = context;
		this.mDataModel = values;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.list_single, parent, false);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.name);
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.dateofbirth);
			holder.imageView = (ImageView) convertView.findViewById(R.id.img);
			// holder.starImageView = (ImageView) convertView
			// .findViewById(R.id.star_imageview);
			holder.checkboxView = (CheckBox) convertView
					.findViewById(R.id.checkbox_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// hide check box
		holder.checkboxView.setVisibility(View.GONE);

		LinkedinUserDataModel userDataModel = (LinkedinUserDataModel) getItem(position);
		if (userDataModel != null) {
			holder.nameTextView.setText(userDataModel.getFirstName() + " "
					+ userDataModel.getLastName());

			if (!TextUtils.isEmpty(userDataModel.getImageUrl())) {
				Picasso.with(mContext).load(userDataModel.getImageUrl())
						.into(holder.imageView);
			} else {
				holder.imageView.setImageResource(R.drawable.ic_launcher);
			}
		}

		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDataModel.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDataModel.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
}