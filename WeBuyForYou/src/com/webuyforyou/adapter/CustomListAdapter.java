package com.webuyforyou.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Utility;

public class CustomListAdapter extends BaseAdapter {

	private List<BirthdayDataModel> birthdayDataModels = new ArrayList<BirthdayDataModel>();
	private Context _context;

	public static class ViewHolder {
		public ImageView imageView;
		public TextView dateTextView;
		public TextView nameTextView;
		public TextView sectionHeaderTextView;
		public TextView textView;
	}

	public CustomListAdapter(Context context,
			LinkedHashMap<String, List<BirthdayDataModel>> map) {
		this._context = context;
		for (String keys : map.keySet()) {
			List<BirthdayDataModel> dataModels = map.get(keys);
			for (BirthdayDataModel birthdayDataModel : dataModels) {
				birthdayDataModels.add(birthdayDataModel);
			}
		}

	}

	@Override
	public int getCount() {
		return birthdayDataModels.size();
	}

	@Override
	public Object getItem(int position) {
		return birthdayDataModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.list_row_birthday, null);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.name);
			holder.dateTextView = (TextView) convertView
					.findViewById(R.id.dateofbirth);
			holder.imageView = (ImageView) convertView.findViewById(R.id.img);
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
			// holder.nameTextView.setText(birthdayDataModel.getTitle());
		}
		return convertView;
	}
}