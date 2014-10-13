package com.webuyforyou.alarm;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.preference.SharedKeyPreference;
import com.webuyforyou.util.Constants;

/**
 * 
 * Alarm Manager Utility
 * 
 * @author Kannappan
 * 
 */
public class AlarmManagerUtil {

	private static final String TAG = AlarmManagerUtil.class.getSimpleName();

	private static final long INTERVAL_DAY = AlarmManager.INTERVAL_DAY;

	private static final String ALARM_SERVICE_ACTION = "alarm_service";
	private static final int ALARM_REQUEST_ID = 1000;
	private Context mContext;

	public AlarmManagerUtil(Context context) {
		this.mContext = context;
	}

	/**
	 * get a alarmManager instance
	 * 
	 * @return
	 */
	private AlarmManager getAlarmManager() {
		AlarmManager alarmManager = (AlarmManager) BaseApplication
				.getApplication().getSystemService(Context.ALARM_SERVICE);
		return alarmManager;
	}

	/**
	 * set the alarm to alarm manager
	 * 
	 */
	public void setAlarm() {
		AlarmManager alarmManager = getAlarmManager();
		if (alarmManager != null) {
			if (Constants.DEBUG) {
				Log.d(TAG, "Set alarm");
			}
			// default values 1 day and 8 am
			int remainderDays = 1;
			int remainderTime = 8;

			String remainderDaysString = getPreferenceData(SharedKeyPreference.PREF_KEY_REMAINDER_DAY);
			String remainderTimeString = getPreferenceData(SharedKeyPreference.PREF_KEY_REMAINDER_TIME);

			if (!TextUtils.isEmpty(remainderDaysString)) {
				remainderDays = Integer.parseInt(remainderDaysString);
			}
			if (!TextUtils.isEmpty(remainderTimeString)) {
				remainderTime = Integer.parseInt(remainderTimeString);
			}

			// set hour in calendar
			Calendar updateTime = Calendar.getInstance();
			updateTime.setTimeZone(TimeZone.getDefault());
			updateTime.set(Calendar.HOUR_OF_DAY, remainderTime);

			// set alarm
			PendingIntent pendingIntent = getPendingIntent();
			alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), remainderDays
							* AlarmManager.INTERVAL_DAY, pendingIntent);
		}
	}

	private String getPreferenceData(String key) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		String value = sharedPrefs.getString(key, null);
		return value;
	}

	/**
	 * get pending intent for location service
	 * 
	 * @return
	 */
	private PendingIntent getPendingIntent() {
		Intent intent = new Intent(mContext, AlarmService.class);
		intent.setAction(ALARM_SERVICE_ACTION);
		PendingIntent pendingIntent = PendingIntent.getService(mContext,
				ALARM_REQUEST_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		return pendingIntent;
	}

	/**
	 * cancel alarm from alarm manager..
	 * 
	 */
	public void cancelAlarm() {
		AlarmManager alarmManager = getAlarmManager();
		if (alarmManager != null) {
			alarmManager.cancel(getPendingIntent());
			if (Constants.DEBUG) {
				Log.d(TAG, "cancel alarm");
			}
		}
		alarmManager = null;
	}

	/**
	 * check whether the location update alarm is set or not
	 * 
	 * @return
	 */
	public boolean checkAlarmExist() {
		boolean isTrue = false;
		Intent intent = new Intent(mContext, AlarmService.class);
		intent.setAction(ALARM_SERVICE_ACTION);
		PendingIntent pendingIntent = PendingIntent.getService(mContext,
				ALARM_REQUEST_ID, intent, PendingIntent.FLAG_NO_CREATE);

		if (pendingIntent != null) {
			isTrue = true;
		}
		if (Constants.DEBUG) {
			Log.d(TAG, "Check alarm exist: " + isTrue);
		}
		return isTrue;
	}
}
