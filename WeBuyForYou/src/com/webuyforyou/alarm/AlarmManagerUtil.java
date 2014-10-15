package com.webuyforyou.alarm;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.preference.SharedKeyPreference;
import com.webuyforyou.preference.SharedPreferencesUtil;
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
			// default values 1 day and 8 am
			int remainderDays = 1;

			String remainderDaysString = DBHelper.getInstance()
					.getDaysFrequency();
			int hour = SharedPreferencesUtil.getInstance().getIntValue(
					SharedKeyPreference.PREF_KEY_REMAINDER_TIME_HOUR, -1);
			int minute = SharedPreferencesUtil.getInstance().getIntValue(
					SharedKeyPreference.PREF_KEY_REMAINDER_TIME_MINUTE, -1);

			if (!TextUtils.isEmpty(remainderDaysString)) {
				remainderDays = Integer.parseInt(remainderDaysString);
			}

			if (Constants.DEBUG) {
				Log.d(TAG, "Set alarm: remainderDays: " + remainderDays
						+ ";hour" + hour + ";minute: " + minute);
			}

			// set hour in calendar
			Calendar updateTime = Calendar.getInstance();
			updateTime.setTimeZone(TimeZone.getDefault());
			updateTime.set(Calendar.HOUR_OF_DAY, hour == 0 ? 8 : hour);
			updateTime.set(Calendar.MINUTE, minute == -1 ? 0 : minute);

			// set alarm
			PendingIntent pendingIntent = getPendingIntent();
			alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
					updateTime.getTimeInMillis(), remainderDays
							* AlarmManager.INTERVAL_DAY, pendingIntent);
		}
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
