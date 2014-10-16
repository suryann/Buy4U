package com.webuyforyou.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Utility;

public class FavoriteListAdapter extends BaseAdapter {

	private List<BirthdayDataModel> mBirthdayDataModels = new ArrayList<BirthdayDataModel>();
	private Context _context;

	public static class ViewHolder {
		public ImageView imageView;
		public TextView dateTextView;
		public TextView nameTextView;
		public TextView sectionHeaderTextView;
		public TextView textView;
		public ImageView starImageView;
	}

	public FavoriteListAdapter(Context context,
			List<BirthdayDataModel> birthdayDataModels) {
		this._context = context;
		this.mBirthdayDataModels = birthdayDataModels;
	}

	@Override
	public int getCount() {
		return mBirthdayDataModels.size();
	}

	@Override
	public Object getItem(int position) {
		return mBirthdayDataModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.list_single, parent, false);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.name);
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.dateofbirth);
			holder.imageView = (ImageView) convertView.findViewById(R.id.img);
			holder.starImageView = (ImageView) convertView
					.findViewById(R.id.star_imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BirthdayDataModel birthdayDataModel = (BirthdayDataModel) getItem(position);
		if (birthdayDataModel != null) {
			holder.nameTextView.setText(birthdayDataModel.getTitle());
			String startDate = birthdayDataModel.getStartDate();
			if (!TextUtils.isEmpty(startDate)) {
				long milliseconds = Long.parseLong(startDate);
				holder.dateTextView.setText(Utility.getDate(milliseconds,
						birthdayDataModel.getTimezone()));
			}
			if (birthdayDataModel.isFavorite()) {
				holder.starImageView
						.setImageResource(R.drawable.ic_stat_star_yellow);
			} else {
				holder.starImageView.setImageResource(R.drawable.ic_stat_star);
			}
		}
		//
		holder.starImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				handleStarButtonClickEvent(position);
			}
		});
		return convertView;
	}

	/**
	 * 
	 * @param section
	 * @param row
	 */
	protected void handleStarButtonClickEvent(int pos) {
		BirthdayDataModel birthdayDataModel = (BirthdayDataModel) getItem(pos);
		if (birthdayDataModel != null) {
			if (!birthdayDataModel.isFavorite()) {
				birthdayDataModel.setFavorite(true);
				DBHelper.getInstance().setFavoriteEvent(birthdayDataModel);
			} else {
				birthdayDataModel.setFavorite(false);
				DBHelper.getInstance().removeFavorites(
						birthdayDataModel.getEventId());
			}
		}
		notifyDataSetChanged();
	}
}
