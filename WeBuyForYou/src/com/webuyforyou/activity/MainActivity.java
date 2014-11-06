package com.webuyforyou.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.adapter.HomeListAdapter;
import com.webuyforyou.alarm.AlarmManagerUtil;
import com.webuyforyou.controller.ActivityController;
import com.webuyforyou.controller.Session;
import com.webuyforyou.listener.DataCallbacks;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Constants;
import com.webuyforyou.util.Utility;
import com.webuyforyou.widget.HeaderListView;

public class MainActivity extends BaseActivity implements DataCallbacks {
	private static final int SETTINGS_RESULT = 111;
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int REQUEST_CODE = 10;
	private HeaderListView listView;
	private ArrayList<String> Name = new ArrayList<String>();
	private ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
	private LayoutInflater li;
	private View promptsView;
	private Map<String, List<BirthdayDataModel>> map;
	protected String mEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// set alarm
		setAlarm();
		// initialize a layout view
		listView = (HeaderListView) findViewById(R.id.list);

		findViewById(R.id.import_image_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						ActivityController.INSTANCE.launchActivity(
								MainActivity.this,
								ImportBirthdayActivity.class, null);
					}
				});

		// read calendar events
		ReadCalendarEventsUtil calendarEventsUtil = new ReadCalendarEventsUtil(
				this, this);
		calendarEventsUtil.readCalendarEvents(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// set empty animation
		overridePendingTransition(0, 0);
	}

	/**
	 * set alarm every day..
	 */
	private void setAlarm() {
		AlarmManagerUtil alarmManagerUtil = new AlarmManagerUtil(this);
		if (!alarmManagerUtil.checkAlarmExist()) {
			alarmManagerUtil.setAlarm();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_home, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			ActivityController.INSTANCE.launchActivity(MainActivity.this,
					SettingsActivity.class, null);
			break;
		case R.id.action_add:
			Intent intent = new Intent(Intent.ACTION_EDIT);
			intent.setType("vnd.android.cursor.item/event");
			startActivityForResult(intent, REQUEST_CODE);
			// handleAddEvent();
			break;
		case R.id.action_about:
			handleAboutButtonSubmit();
			break;
		case R.id.action_favorite:
			handleFavoriteButtonSubmit();
			break;

		default:
			break;
		}

		return false;
	}

	/**
	 * Launch a favorite activity
	 */
	private void handleFavoriteButtonSubmit() {
		ActivityController.INSTANCE.launchActivity(MainActivity.this,
				FavoritesActivity.class, null);
	}

	/**
	 * 
	 */
	private void handleAboutButtonSubmit() {
		Bundle bundle = new Bundle();
		bundle.putString(Constants.WEB_VIEW_URL, Constants.ABOUT_URL);
		ActivityController.INSTANCE.launchActivity(MainActivity.this,
				WebViewActivity.class, bundle);
	}

	/**
	 * 
	 */
	private void handleAddEvent() {
		li = LayoutInflater.from(getApplicationContext());
		promptsView = li.inflate(R.layout.dialogdate, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("Add new event");
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText nameEditText = (EditText) promptsView
				.findViewById(R.id.name_edittext);
		final TextView dateTextView = (TextView) promptsView
				.findViewById(R.id.dob);
		final Spinner occasionEditText = (Spinner) promptsView
				.findViewById(R.id.occasion);
		occasionEditText
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						mEvent = (String) occasionEditText
								.getItemAtPosition(position);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						// edit text
						String title = nameEditText.getText().toString();
						String eventOccassion = mEvent;
						String date = null;

						if (validateData(title, eventOccassion, date)) {
							handleAddEventSubmit(title, eventOccassion, date);
						}
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	protected boolean validateData(String title, String eventOccassion,
			String date) {
		String message = null;
		boolean isValid = true;
		if (TextUtils.isEmpty(title)) {
			isValid = false;
			message = "Title should not be empty";
		}
		if (TextUtils.isEmpty(eventOccassion)) {
			isValid = false;
		}
		if (TextUtils.isEmpty(date)) {
			// FIXME set false later
			isValid = true;
			message = "Date should not be empty";
		}
		if (!isValid) {
			showToastMessage(this, message);
		}
		return isValid;
	}

	/**
	 * @param date
	 * @param eventOccassion
	 * @param title
	 * 
	 */
	protected void handleAddEventSubmit(String title, String eventOccassion,
			String date) {
		Utility.addCalendarEvent(this, title);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE) {
			// read calendar events
			ReadCalendarEventsUtil calendarEventsUtil = new ReadCalendarEventsUtil(
					this, this);
			calendarEventsUtil.readCalendarEvents(null);
		}
	}

	@Override
	public void processSuccess(List<BirthdayDataModel> birthdayDataModels) {
		if (isFinishing()) {
			return;
		}
		if (birthdayDataModels != null && birthdayDataModels.size() > 0) {
			map = (LinkedHashMap<String, List<BirthdayDataModel>>) Session
					.getInstance().getSortedCalendarEventData(
							birthdayDataModels);
			HomeListAdapter birthDayListAdapter = new HomeListAdapter(this,
					(LinkedHashMap<String, List<BirthdayDataModel>>) map);
			listView.setAdapter(birthDayListAdapter);
		}
	}

}