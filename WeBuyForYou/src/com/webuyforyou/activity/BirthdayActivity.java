package com.webuyforyou.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.adapter.CustomListAdapter;
import com.webuyforyou.controller.Session;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.listener.DataCallbacks;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Utility;

public class BirthdayActivity extends BaseActivity implements DataCallbacks {

	private ListView listView;
	private TextView mHintTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_birthday_layout);
		isShowActionBarHomeButton(true);

		listView = (ListView) findViewById(R.id.birthday_list);
		mHintTextView = (TextView) findViewById(R.id.birthday_textview);

		ReadCalendarEventsUtil calendarEventsUtil = new ReadCalendarEventsUtil(
				this, this);

		// read data from DB
		String daysSetting = DBHelper.getInstance().getBithDaysFrequency();
		int days = 0;
		if (!TextUtils.isEmpty(daysSetting)) {
			days = Integer.parseInt(daysSetting);
		}
		calendarEventsUtil.readCalendarEvents(Utility.getSelectionArgs(days));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void processSuccess(List<BirthdayDataModel> birthdayDataModels) {
		if (isFinishing()) {
			return;
		}
		List<BirthdayDataModel> favoriteBirthdayDataModel = new ArrayList<BirthdayDataModel>();
		if (birthdayDataModels != null && birthdayDataModels.size() > 0) {
			for (BirthdayDataModel birthdayDataModel : birthdayDataModels) {
				if (birthdayDataModel.isFavorite()) {
					favoriteBirthdayDataModel.add(birthdayDataModel);
				}
			}
			if (favoriteBirthdayDataModel != null
					&& favoriteBirthdayDataModel.size() > 0) {
				LinkedHashMap<String, List<BirthdayDataModel>> map = (LinkedHashMap<String, List<BirthdayDataModel>>) Session
						.getInstance().getSortedCalendarEventData(
								favoriteBirthdayDataModel);
				CustomListAdapter birthDayListAdapter = new CustomListAdapter(
						this,
						(LinkedHashMap<String, List<BirthdayDataModel>>) map);
				listView.setAdapter(birthDayListAdapter);
				listView.setVisibility(View.VISIBLE);
				mHintTextView.setVisibility(View.GONE);
			} else {
				listView.setVisibility(View.GONE);
				mHintTextView.setVisibility(View.VISIBLE);
			}
		} else {
			listView.setVisibility(View.GONE);
			mHintTextView.setVisibility(View.VISIBLE);
		}
	}
}
