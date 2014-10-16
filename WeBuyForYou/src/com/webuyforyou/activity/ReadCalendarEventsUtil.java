package com.webuyforyou.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.listener.DataCallbacks;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Constants;

public class ReadCalendarEventsUtil {
	private static final String WEDDING = "wedding";
	private static final String ANNIVERSARY = "anniversary";
	private static final String BIRTHDAY = "birthday";
	private static final String TAG = ReadCalendarEventsUtil.class
			.getSimpleName();
	private List<BirthdayDataModel> birthdayDataModels = new ArrayList<BirthdayDataModel>();
	private Context mContext;
	private DataCallbacks mCallbacks;

	public ReadCalendarEventsUtil(Context context, DataCallbacks callbacks) {
		this.mContext = context;
		this.mCallbacks = callbacks;
	}

	/**
	 * read calendar events
	 * 
	 * @param selection
	 * @return
	 */
	public void readCalendarEvents(String selection) {
		ForeGroundAsynsTask asynsTask = new ForeGroundAsynsTask();
		asynsTask.execute(selection);
	}

	class ForeGroundAsynsTask extends
			AsyncTask<String, Void, List<BirthdayDataModel>> {

		@Override
		protected List<BirthdayDataModel> doInBackground(String... params) {
			Cursor cursor = getQueryEvents(params[0]);
			return handleCursorDataResponse(cursor);
		}

		@Override
		protected void onPostExecute(List<BirthdayDataModel> result) {
			if (result != null) {
				mCallbacks.processSuccess(result);
			}
		}

	}

	/**
	 * 
	 * @param selection
	 * @return
	 */
	private Cursor getQueryEvents(String selection) {
		Cursor cursor = mContext.getContentResolver().query(
				Uri.parse("content://com.android.calendar/events"),
				new String[] { "calendar_id", "title", "description",
						"dtstart", "dtend", "eventLocation", "eventTimezone",
						"_id" }, selection, null, null);
		return cursor;
	}

	/**
	 * handle query response data
	 * 
	 * @param cursor
	 * @return
	 */
	private List<BirthdayDataModel> handleCursorDataResponse(Cursor cursor) {

		birthdayDataModels.clear();
		if (cursor != null) {
			cursor.moveToFirst();
			// fetching calendars name
			String CNames[] = new String[cursor.getCount()];
			String eventName;
			for (int i = 0; i < CNames.length; i++) {
				BirthdayDataModel birthdayDataModel = new BirthdayDataModel();
				// Title
				if (cursor.getString(1) != null) {
					if (Constants.DEBUG) {
						Log.d(TAG, "Name: " + cursor.getString(1)
								+ ";Event Id:" + cursor.getString(7));
					}
					// event id
					birthdayDataModel.setEventId(cursor.getString(7));
					// favorite
					birthdayDataModel.setFavorite(isFavorite(cursor
							.getString(7)));
					// event name
					eventName = cursor.getString(1);
					if (eventName.toLowerCase()
							.contains(BIRTHDAY.toLowerCase())
							|| eventName.toLowerCase().contains(
									ANNIVERSARY.toLowerCase())
							|| eventName.toLowerCase().contains(
									WEDDING.toLowerCase())) {
						birthdayDataModel.setTitle(cursor.getString(1));
						// start date
						if (cursor.getString(3) != null) {
							if (Constants.DEBUG) {
								Log.d(TAG, "STDate: " + cursor.getString(3));
							}
							birthdayDataModel.setStartDate(cursor.getString(3));
						}
						// end date
						if (cursor.getString(4) != null) {
							if (Constants.DEBUG) {
								Log.d(TAG, "End date: " + cursor.getString(4));
							}
							birthdayDataModel.setEndDate(cursor.getString(4));
						}

						// description
						if (Constants.DEBUG) {
							Log.d(TAG, "descriptions: " + cursor.getString(2));
						}

						birthdayDataModel.setDescription(cursor.getString(2));

						// time zone
						if (Constants.DEBUG) {
							Log.d(TAG, "TimeZone: " + cursor.getString(6));
						}
						birthdayDataModel.setTimezone(cursor.getString(6));

						// add to list
						birthdayDataModels.add(birthdayDataModel);
					}
				}
				CNames[i] = cursor.getString(1);
				cursor.moveToNext();
			}

			// Collections.sort(startDates);

			cursor.close();
		} else {
			Toast.makeText(BaseApplication.getApplication(),
					"Calendar don have entries", Toast.LENGTH_LONG).show();
		}
		return birthdayDataModels;
	}

	private boolean isFavorite(String eventId) {
		boolean isFavorite = false;
		List<BirthdayDataModel> birthdayDataModels = DBHelper.getInstance()
				.getFavoritesItemData();
		if (birthdayDataModels != null && birthdayDataModels.size() > 0) {
			for (BirthdayDataModel birthdayDataModel : birthdayDataModels) {
				if (eventId.equalsIgnoreCase(birthdayDataModel.getEventId())) {
					isFavorite = true;
					break;
				}
			}
		}
		return isFavorite;
	}

}
