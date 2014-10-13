package com.webuyforyou.alarm;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;

import com.webuyforyou.activity.BirthdayActivity;
import com.webuyforyou.notification.NotificationManagerUtil;

public class AlarmService extends IntentService {

	private static final String TAG = AlarmService.class.getSimpleName();

	public AlarmService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		createNotification();
	}

	private void createNotification() {
		// Creates an Intent for the Activity
		Intent notifyIntent = new Intent(this, BirthdayActivity.class);
		// Sets the Activity to start in a new, empty task
		notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// Creates the PendingIntent
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
				(int) System.currentTimeMillis(), notifyIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationManagerUtil notificationManagerUtil = NotificationManagerUtil
				.getInstance();
		notificationManagerUtil.createNotifications(this, 1, "Message",
				pendingIntent);
	}

}
