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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.webuyforyou.R;
import com.webuyforyou.adapter.HomeListAdapter;
import com.webuyforyou.alarm.AlarmManagerUtil;
import com.webuyforyou.controller.ActivityController;
import com.webuyforyou.controller.Session;
import com.webuyforyou.listener.DataCallbacks;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Constants;
import com.webuyforyou.widget.HeaderListView;

public class MainActivity extends BaseActivity implements DataCallbacks {
	private static final int SETTINGS_RESULT = 111;
	private static final String TAG = MainActivity.class.getSimpleName();
	private HeaderListView listView;
	private ArrayList<String> Name = new ArrayList<String>();
	private ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
	private LayoutInflater li;
	private View promptsView;
	private Map<String, List<BirthdayDataModel>> map;

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
			handleAddEvent();
			break;
		case R.id.action_about:
			handleAboutButtonSubmit();
			break;

		default:
			break;
		}

		return false;
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

		alertDialogBuilder.setTitle("Add Birthdays");
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final EditText UserInput = (EditText) promptsView
				.findViewById(R.id.username);
		final EditText OccasionInput = (EditText) promptsView
				.findViewById(R.id.occasion);
		final EditText DateInput = (EditText) promptsView
				.findViewById(R.id.dob);

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						// edit text
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == SETTINGS_RESULT) {
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