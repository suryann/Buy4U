package com.webuyforyou.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.R;

/**
 * Utility class for android notification..
 * 
 * 
 * @author Kannappan
 * 
 */
public class NotificationManagerUtil {

	private static final String TAG = NotificationManagerUtil.class
			.getSimpleName();
	private static NotificationManagerUtil notificationManagerUtil = null;
	private static Context mContext;

	private NotificationManagerUtil(Context context) {
		mContext = context;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static NotificationManagerUtil getInstance() {
		if (notificationManagerUtil == null) {
			notificationManagerUtil = new NotificationManagerUtil(
					BaseApplication.getApplication());
		}
		return notificationManagerUtil;
	}

	/**
	 * 
	 * @return
	 */
	private NotificationManager getNotificationManager() {
		NotificationManager notificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		return notificationManager;
	}

	/**
	 * 
	 * create a simple notification message.
	 * 
	 * @param context
	 * @param gcmMessageId
	 * @param gcmMessage
	 * @param intent
	 */
	public void createNotifications(Context context, int gcmMessageId,
			String message, PendingIntent pendingIntent) {
		setVibration();

		// TODO add a notification sound file
		// Uri soundUri = Uri.parse("android.resource://"
		// + mContext.getPackageName() + "/" + R.raw.long_tone);

		NotificationManager notificationManager = getNotificationManager();

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentText(message)
				.setOngoing(false)
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_LIGHTS)
				.setLights(0xff00ff00, 300, 1000)
				.setVibrate(new long[] { 100, 200, 100, 500 })
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(message));

		// mBuilder.setSound(soundUri);
		// Puts the PendingIntent into the notification builder
		mBuilder.setContentIntent(pendingIntent);

		notificationManager.notify(gcmMessageId, mBuilder.build());
	}

	/**
	 * add a vibration
	 * 
	 */
	private void setVibration() {
		try {
			Vibrator vibrator = (Vibrator) mContext
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(5000);
		} catch (Exception e) {
			Log.d(TAG, "Error in vibration :" + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	/**
	 * cancel notification using unique id
	 * 
	 * @param id
	 */
	public void cancelNotifications(int id) {
		try {
			NotificationManager notificationManager = getNotificationManager();
			if (notificationManager != null) {
				notificationManager.cancel(id);
			}
		} catch (Exception e) {
			Log.d(TAG, "error in cancelActionNotifications " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * cancel all notifications
	 */
	public void cancelAllNotification() {
		NotificationManager notificationManager = getNotificationManager();
		if (notificationManager != null) {
			notificationManager.cancelAll();
		}
	}

}
