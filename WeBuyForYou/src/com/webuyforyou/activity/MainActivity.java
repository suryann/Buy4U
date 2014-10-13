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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.webuyforyou.R;
import com.webuyforyou.adapter.HomeListAdapter;
import com.webuyforyou.alarm.AlarmManagerUtil;
import com.webuyforyou.controller.Session;
import com.webuyforyou.listener.DataCallbacks;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Constants;
import com.webuyforyou.widget.HeaderListView;

public class MainActivity extends BaseActivity implements DataCallbacks {
	private static final int SETTINGS_RESULT = 111;
	private static final String TAG = MainActivity.class.getSimpleName();
	private HeaderListView listView;
	private String[] SelectText = { "Save", "Settings" };

	private ArrayList<String> Name = new ArrayList<String>();
	private ArrayList<Bitmap> Images = new ArrayList<Bitmap>();
	private LayoutInflater li;
	private View promptsView;
	private Map<String, List<BirthdayDataModel>> map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setAlarm();

		listView = (HeaderListView) findViewById(R.id.list);

		// listView.setOnChildClickListener(new OnChildClickListener() {
		//
		// @Override
		// public boolean onChildClick(ExpandableListView parent, View v,
		// int groupPosition, int childPosition, long id) {
		// startActivity(new Intent(getApplicationContext(),
		// BirthdayActivity.class));
		// return false;
		// }
		// });

		findViewById(R.id.import_image_btn).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showSettingsDialog();
					}
				});

		ReadCalendarEventsUtil calendarEventsUtil = new ReadCalendarEventsUtil(
				this, this);
		calendarEventsUtil.readCalendarEvents(null);
	}

	@Override
	protected void onPause() {
		super.onPause();
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
		// Used to put dark icons on light action bar
		menu.add(SelectText[0]).setTitle("Add")
				.setIcon(R.drawable.ic_action_content_new)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(SelectText[1])
				.setTitle("Settings")
				.setIcon(R.drawable.ic_action_action_settings)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// If this callback does not handle the item click,
		// onPerformDefaultAction
		// of the ActionProvider is invoked. Hence, the provider encapsulates
		// the
		// complete functionality of the menu item.
		if (item.getTitle().equals("Settings")) {
			Intent i = new Intent(MainActivity.this, Preference.class);
			startActivityForResult(i, SETTINGS_RESULT);
		} else if (item.getTitle().equals("Add")) {
			li = LayoutInflater.from(getApplicationContext());
			promptsView = li.inflate(R.layout.dialogdate, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

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
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// get user input and set it to result
									// edit text
								}
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});

			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();

			// show it
			alertDialog.show();
		} else
			Toast.makeText(this, "Handling in onOptionsItemSelected avoided",
					Toast.LENGTH_SHORT).show();

		return false;
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

	/**
	 * show settings dialog view.
	 * 
	 */
	private void showSettingsDialog() {
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		View view = layoutInflater.inflate(R.layout.settings_layout, null);

		Spinner timeSpinner = (Spinner) view.findViewById(R.id.time_spinner);
		Spinner daysSpinner = (Spinner) view.findViewById(R.id.day_spinner);
		Spinner bithdaySpinner = (Spinner) view
				.findViewById(R.id.birthday_spinner);
		timeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String value = (String) parent.getItemAtPosition(position);
				if (Constants.DEBUG) {
					Log.d(TAG, value);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		daysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String value = (String) parent.getItemAtPosition(position);
				if (Constants.DEBUG) {
					Log.d(TAG, value);
				}
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
					Log.d(TAG, value);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Remainders");
		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(view);

		// set dialog message
		alertDialogBuilder.setCancelable(false).setPositiveButton("OK", null)
				.setNegativeButton("Cancel", null);

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}