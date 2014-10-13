package com.webuyforyou.activity;

import java.util.LinkedHashMap;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.adapter.CustomListAdapter;
import com.webuyforyou.controller.Session;
import com.webuyforyou.listener.DataCallbacks;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.preference.SharedKeyPreference;
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

		// read data from preference
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String daysSetting = sharedPrefs.getString(
				SharedKeyPreference.PREF_KEY_REMAINDER_BIRTHDAY_INTERVAL, null);
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void processSuccess(List<BirthdayDataModel> birthdayDataModels) {
		if (isFinishing()) {
			return;
		}
		if (birthdayDataModels != null && birthdayDataModels.size() > 0) {
			LinkedHashMap<String, List<BirthdayDataModel>> map = (LinkedHashMap<String, List<BirthdayDataModel>>) Session
					.getInstance().getSortedCalendarEventData(
							birthdayDataModels);
			CustomListAdapter birthDayListAdapter = new CustomListAdapter(this,
					(LinkedHashMap<String, List<BirthdayDataModel>>) map);
			listView.setAdapter(birthDayListAdapter);
			listView.setVisibility(View.VISIBLE);
			mHintTextView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.GONE);
			mHintTextView.setVisibility(View.VISIBLE);
		}
	}
}
