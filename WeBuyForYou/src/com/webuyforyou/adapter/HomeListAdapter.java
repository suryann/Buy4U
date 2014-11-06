package com.webuyforyou.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.activity.BirthdayDetailsActivity;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Constants;
import com.webuyforyou.util.Utility;
import com.webuyforyou.widget.SectionAdapter;

public class HomeListAdapter extends SectionAdapter {

	private static final String TAG = HomeListAdapter.class.getSimpleName();
	private Context _context;
	private List<String> mListDataHeader = new ArrayList<String>();
	private LinkedHashMap<String, List<BirthdayDataModel>> mListDataChild = new LinkedHashMap<String, List<BirthdayDataModel>>();

	public HomeListAdapter(Context context,
			LinkedHashMap<String, List<BirthdayDataModel>> childKeyItems) {
		this._context = context;
		for (String keys : childKeyItems.keySet()) {
			mListDataHeader.add(keys);
			List<BirthdayDataModel> dataModels = childKeyItems.get(keys);
			mListDataChild.put(keys, dataModels);
		}
	}

	@Override
	public int numberOfSections() {
		return mListDataHeader.size();
	}

	@Override
	public int numberOfRows(int section) {
		if (section == -1) {
			return 0;
		}
		if (mListDataChild == null) {
			return 0;
		}
		return mListDataChild.get(mListDataHeader.get(section)).size();
	}

	@Override
	public View getRowView(final int section, final int row, View convertView,
			ViewGroup parent) {
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
			// holder.starImageView = (ImageView) convertView
			// .findViewById(R.id.star_imageview);
			holder.checkboxView = (CheckBox) convertView
					.findViewById(R.id.checkbox_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		BirthdayDataModel birthdayDataModel = (BirthdayDataModel) getRowItem(
				section, row);
		if (birthdayDataModel != null) {
			holder.nameTextView.setText(birthdayDataModel.getTitle());
			String startDate = birthdayDataModel.getStartDate();
			if (!TextUtils.isEmpty(startDate)) {
				long milliseconds = Long.parseLong(startDate);
				holder.dateTextView.setText(Utility.getDate(milliseconds,
						birthdayDataModel.getTimezone()));
			}
			if (birthdayDataModel.isFavorite()) {
				holder.checkboxView.setChecked(true);
				// holder.starImageView
				// .setImageResource(R.drawable.ic_stat_star_yellow);
			} else {
				holder.checkboxView.setChecked(false);
				// holder.starImageView.setImageResource(R.drawable.ic_stat_star);
			}
		}
		
		holder.checkboxView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				buttonView.setChecked(isChecked);
				handleStarButtonClickEvent(section, row);
			}
		});
		//
		// holder.starImageView.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// handleStarButtonClickEvent(section, row);
		// }
		// });
		return convertView;
	}

	/**
	 * 
	 * @param section
	 * @param row
	 */
	protected void handleStarButtonClickEvent(int section, int row) {
		BirthdayDataModel birthdayDataModel = (BirthdayDataModel) getRowItem(
				section, row);
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

	@Override
	public View getSectionHeaderView(int groupPosition, View convertView,
			ViewGroup parent) {
		String headerTitle = (String) getSectionHeaderItem(groupPosition);

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) _context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(
					R.layout.list_birthday_setcion_header, parent, false);
			holder.sectionHeaderTextView = (TextView) convertView
					.findViewById(R.id.section_header_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (!TextUtils.isEmpty(headerTitle)) {
			holder.sectionHeaderTextView.setText(headerTitle);
		}

		return convertView;

	}

	@Override
	public Object getSectionHeaderItem(int section) {
		if (mListDataHeader == null) {
			return null;
		}
		return mListDataHeader.get(section);
	}

	@Override
	public Object getRowItem(final int section, final int row) {
		if (Constants.DEBUG) {
			Log.d(TAG, "Section:" + section + ",Row:" + row);
		}
		final String key = mListDataHeader.get(section);
		if (!TextUtils.isEmpty(key)) {
			final List<BirthdayDataModel> birthdayDataModels = mListDataChild
					.get(key);
			if (birthdayDataModels != null && birthdayDataModels.size() > 0
					&& row <= birthdayDataModels.size()) {
				return birthdayDataModels.get(row);
			}
		}
		return null;
	}

	@Override
	public boolean hasSectionHeaderView(int section) {
		return true;
	}

	public static class ViewHolder {
		// public ImageView starImageView;
		public ImageView imageView;
		public TextView dateTextView;
		public TextView nameTextView;
		public TextView sectionHeaderTextView;
		public TextView textView;
		public CheckBox checkboxView;
	}

	@Override
	public void onRowItemClick(AdapterView<?> parent, View view, int section,
			int row, long id) {
		super.onRowItemClick(parent, view, section, row, id);
		BirthdayDataModel rowDataModels = (BirthdayDataModel) getRowItem(
				section, row);
		if (rowDataModels != null) {
			Intent intent = new Intent(_context, BirthdayDetailsActivity.class);
			intent.putExtra(Constants.KEY_BUNDLE_BIRTHDAY_MODEL, rowDataModels);
			_context.startActivity(intent);
		}
	}

}
