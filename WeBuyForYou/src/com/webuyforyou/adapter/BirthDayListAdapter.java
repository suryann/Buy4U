//package com.webuyforyou.adapter;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseExpandableListAdapter;
//import android.widget.ExpandableListView;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.webuyforyou.R;
//import com.webuyforyou.model.BirthdayDataModel;
//import com.webuyforyou.util.Utility;
//
//public class BirthDayListAdapter extends BaseExpandableListAdapter {
//
//	private static final String TAG = ExpandableListAdapter.class
//			.getSimpleName();
//	private Context _context;
//	private List<String> mListDataHeader = new ArrayList<String>();
//	// child data in format of header title, child title
//	private Map<String, List<BirthdayDataModel>> mListDataChild;
//
//	public BirthDayListAdapter(Context context,
//			LinkedHashMap<String, List<BirthdayDataModel>> map) {
//		this._context = context;
//		for (String keys : map.keySet()) {
//			mListDataHeader.add(keys);
//			List<BirthdayDataModel> dataModels = map.get(keys);
//			for (BirthdayDataModel birthdayDataModel : dataModels) {
//				Log.d(TAG, "Title: " + birthdayDataModel.getTitle());
//			}
//
//		}
//		this.mListDataChild = map;
//
//	}
//
//	@Override
//	public Object getChild(int groupPosition, int childPosititon) {
//		if (mListDataHeader != null) {
//			String key = mListDataHeader.get(groupPosition);
//			List<BirthdayDataModel> birthdayDataModels = mListDataChild
//					.get(key);
//			if (birthdayDataModels != null && birthdayDataModels.size() > 0) {
//				return birthdayDataModels.get(childPosititon);
//			}
//		}
//		return null;
//	}
//
//	@Override
//	public long getChildId(int groupPosition, int childPosition) {
//		return childPosition;
//	}
//
//	@Override
//	public View getChildView(int groupPosition, final int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//
//		ViewHolder holder = null;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			LayoutInflater inflater = (LayoutInflater) _context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//			convertView = inflater.inflate(R.layout.list_row_birthday, null);
//			holder.nameTextView = (TextView) convertView
//					.findViewById(R.id.name);
//			holder.dateTextView = (TextView) convertView
//					.findViewById(R.id.dateofbirth);
//			holder.imageView = (ImageView) convertView.findViewById(R.id.img);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		if (getChild(groupPosition, childPosition) != null) {
//			BirthdayDataModel birthdayDataModel = (BirthdayDataModel) getChild(
//					groupPosition, childPosition);
//			if (birthdayDataModel != null) {
//				holder.nameTextView.setText(birthdayDataModel.getTitle());
//				String startDate = birthdayDataModel.getStartDate();
//				if (!TextUtils.isEmpty(startDate)) {
//					long milliseconds = Long.parseLong(startDate);
//					holder.dateTextView.setText(Utility.getDate(milliseconds,
//							birthdayDataModel.getTimezone()));
//				}
//				// holder.nameTextView.setText(birthdayDataModel.getTitle());
//			}
//		}
//		return convertView;
//	}
//
//	@Override
//	public int getChildrenCount(int groupPosition) {
//		return mListDataChild.get(mListDataHeader.get(groupPosition)).size();
//	}
//
//	@Override
//	public Object getGroup(int groupPosition) {
//		return mListDataHeader.get(groupPosition);
//	}
//
//	@Override
//	public int getGroupCount() {
//		return mListDataHeader.size();
//	}
//
//	@Override
//	public long getGroupId(int groupPosition) {
//		return groupPosition;
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//		String headerTitle = (String) getGroup(groupPosition);
//
//		ExpandableListView expandableListView = (ExpandableListView) parent;
//		expandableListView.expandGroup(groupPosition);
//		ViewHolder holder = null;
//		if (convertView == null) {
//			holder = new ViewHolder();
//			LayoutInflater inflater = (LayoutInflater) _context
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			convertView = inflater.inflate(
//					R.layout.list_birthday_setcion_header, null);
//			holder.sectionHeaderTextView = (TextView) convertView
//					.findViewById(R.id.section_header_textview);
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//
//		holder.sectionHeaderTextView.setText(headerTitle);
//
//		return convertView;
//	}
//
//	@Override
//	public boolean hasStableIds() {
//		return false;
//	}
//
//	@Override
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		return true;
//	}
//
//	public static class ViewHolder {
//		public ImageView imageView;
//		public TextView dateTextView;
//		public TextView nameTextView;
//		public TextView sectionHeaderTextView;
//		public TextView textView;
//	}
// }