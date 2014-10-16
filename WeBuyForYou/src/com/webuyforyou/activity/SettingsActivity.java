package com.webuyforyou.activity;

import java.util.Calendar;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.webuyforyou.R;
import com.webuyforyou.alarm.AlarmManagerUtil;
import com.webuyforyou.controller.ActivityController;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.preference.SharedPreferencesUtil;
import com.webuyforyou.util.Constants;

public class SettingsActivity extends BaseActivity implements OnClickListener {

	private static final String DAYS = "days";
	private static final String TAG = SettingsActivity.class.getSimpleName();
	private TextView mTimeSpinnerTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_layout);
		if (DBHelper.getInstance().isUserSettingSaved()) {
			isShowActionBarHomeButton(true);
		} else {
			isShowActionBarHomeButton(false);
		}
		initializeView();
	}

	/**
	 * initialize a layout view
	 * 
	 */
	private void initializeView() {
		mTimeSpinnerTextView = (TextView) findViewById(R.id.time_spinner);
		Spinner daysSpinner = (Spinner) findViewById(R.id.day_spinner);
		Spinner bithdaySpinner = (Spinner) findViewById(R.id.birthday_spinner);
		mTimeSpinnerTextView.setOnClickListener(this);

		if (Constants.DEBUG) {
			Log.d(TAG, "getBithDaysFrequency: "
					+ DBHelper.getInstance().getBithDaysFrequency());
			Log.d(TAG, "getDaysFrequency: "
					+ DBHelper.getInstance().getDaysFrequency());

		}

		// hour
		String hourString = DBHelper.getInstance().getTimeFrequency(
				DBHelper.COLUMN_NAME_TIME_FREQUENCY_HOUR);
		int hour = 0;
		if (!TextUtils.isEmpty(hourString)) {
			hour = Integer.parseInt(hourString);
		}

		// minute
		String minuteString = DBHelper.getInstance().getTimeFrequency(
				DBHelper.COLUMN_NAME_TIME_FREQUENCY_MINUTE);
		int minute = 0;
		if (!TextUtils.isEmpty(minuteString)) {
			minute = Integer.parseInt(minuteString);
		}
		// set time
		setTime(hour <= 0 ? 8 : hour, minute);

		// remainder daySpinner
		String daysFreq = DBHelper.getInstance().getDaysFrequency();
		if (!TextUtils.isEmpty(daysFreq)) {
			String[] daySpinnerStrings = getResources().getStringArray(
					R.array.remainder_days_list_preference);
			for (int i = 0; i < daySpinnerStrings.length; i++) {
				if (daySpinnerStrings[i].contains(daysFreq)) {
					daysSpinner.setSelection(i);
					break;
				} else if (daysFreq.contains("7")) {
					daysSpinner.setSelection(daySpinnerStrings.length - 1);
				}
			}
		} else {
			daysSpinner.setSelection(0);
		}

		// remainder bithdaySpinner
		String birthdayFreq = DBHelper.getInstance().getBithDaysFrequency();
		if (!TextUtils.isEmpty(birthdayFreq)) {
			String[] birthdaySpinnerStrings = getResources().getStringArray(
					R.array.birthday_list_preference);
			for (int i = 0; i < birthdaySpinnerStrings.length; i++) {
				if (birthdaySpinnerStrings[i].contains(birthdayFreq)) {
					bithdaySpinner.setSelection(i);
					break;
				}
			}
		} else {
			bithdaySpinner.setSelection(0);
		}

		daysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String value = (String) parent.getItemAtPosition(position);
				if (Constants.DEBUG) {
					Log.d(TAG, "Position: " + value);
				}
				DBHelper.getInstance().setDaysFrequency(value);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		bithdaySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String value = (String) parent.getItemAtPosition(position);
				if (Constants.DEBUG) {
					Log.d(TAG, "Position: " + value);
				}
				DBHelper.getInstance().setBirthDaysFrequency(value);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// button
		findViewById(R.id.cancel_button).setOnClickListener(this);
		findViewById(R.id.save_button).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.cancel_button:
			handleCancelButtonSubmit();
			break;
		case R.id.save_button:
			handleSaveButtonSubmit();
			break;
		case R.id.time_spinner:
			showTimePickerDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * handle save button submit
	 */
	private void handleSaveButtonSubmit() {
		if (DBHelper.getInstance().isUserSettingSaved()) {
			// cancel alarm and set it again
			AlarmManagerUtil alarmManagerUtil = new AlarmManagerUtil(this);
			alarmManagerUtil.cancelAlarm();
			alarmManagerUtil.setAlarm();
			// finish activity
			finish();
		} else {
			DBHelper.getInstance().setUserSettingSaved(true);
			launchHomeScreen();
		}
	}

	/**
	 * 
	 */
	private void handleCancelButtonSubmit() {
		if (DBHelper.getInstance().isUserSettingSaved()) {
			finish();
		} else {
			launchHomeScreen();
		}
	}

	/**
	 * launch remainder screen
	 */
	private void launchHomeScreen() {
		ActivityController.INSTANCE.launchActivity(this, MainActivity.class,
				null);
		finish();
	}

	/**
	 * show time picker dialog
	 * 
	 */
	private void showTimePickerDialog() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		TimePickerDialog timePickerDialog = new TimePickerDialog(this,
				timePickerListener, hour, minute, false);
		timePickerDialog.show();

	}

	/**
	 * Time picker listener
	 */
	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hour, int minute) {
			setTime(hour, minute);
		}
	};

	private void setTime(int hourOfDay, int minute) {

		if (Constants.DEBUG) {
			Log.d(TAG, "Hour: " + hourOfDay + ";Minutes: " + minute);
		}

		DBHelper.getInstance().setTimeFrequency(hourOfDay, minute);

		int _hour = 0;
		String am_pm_String;
		String hour;
		if (hourOfDay >= 12) {
			if (hourOfDay == 12) {
				hour = "" + 12;
			} else {
				_hour = hourOfDay - 12;
				hour = String.valueOf(_hour);
				if (hour.length() == 1) {
					hour = "0" + hour;
				}
			}

			am_pm_String = "PM";
		} else {
			hour = String.valueOf(hourOfDay);
			if (hour.length() == 1) {
				hour = "0" + hour;
			}
			am_pm_String = "AM";
		}
		String min = String.valueOf(minute);
		if (min.length() == 1) {
			min = "0" + min;
		}
		mTimeSpinnerTextView.setText(hour + " : " + min + " " + am_pm_String);
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

}
