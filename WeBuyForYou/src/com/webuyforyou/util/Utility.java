package com.webuyforyou.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.R;

public class Utility {
	private static final String ASIA_CALCUTTA = "Asia/Calcutta";
	public static final String DEFAULT_DATE_FORMATTER = "dd-MM-yyyy  hh:mm a";
	private static final String UTC_TIMEZONE_ID = "GMT/UTC";
	public static final String CONVERT_DATE_FORMAT = "EEEE, MMM dd";

	private static final String TAG = Utility.class.getSimpleName();
	private static String[] monthsFullName;

	/**
	 * this method will format long time value to given time zone with MMMMM d
	 * EEEEEEEEE,hh:mm a
	 * 
	 * @param timeZone
	 * @param date
	 * @param incomingDateFormat
	 * @return
	 */
	public static String getDate(long milliSeconds, String timeZone,
			String dateFormat) {
		SimpleDateFormat incomingFormatter = new SimpleDateFormat(
				DEFAULT_DATE_FORMATTER, Locale.US);
		incomingFormatter.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE_ID));

		if (TextUtils.isEmpty(dateFormat)) {
			dateFormat = DEFAULT_DATE_FORMATTER;
		}
		SimpleDateFormat outputFormat = new SimpleDateFormat(dateFormat,
				Locale.US);
		outputFormat.setTimeZone(TimeZone.getTimeZone(ASIA_CALCUTTA));

		String response = "";
		try {
			Date date = new Date(milliSeconds);
			String defaultFormattedDate = incomingFormatter.format(date
					.getTime());
			Date dateOutput = incomingFormatter.parse(defaultFormattedDate);
			response = outputFormat.format(dateOutput);
		} catch (Exception e) {
			if (Constants.DEBUG) {
				Log.d(TAG, "Error in convert date format:"
						+ e.getMessage().toString());
			}
		}
		if (Constants.DEBUG) {
			Log.d(TAG, "convertToLocalTime : " + response);
		}
		return response;
	}

	private static void initMonths() {
		if (monthsFullName == null) {
			monthsFullName = BaseApplication.getApplication().getResources()
					.getStringArray(R.array.months_full_name);
		}
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static String getMonth(String dateTime) {
		initMonths();
		SimpleDateFormat outputFormat = new SimpleDateFormat(
				DEFAULT_DATE_FORMATTER, Locale.US);
		outputFormat.setTimeZone(TimeZone.getTimeZone(ASIA_CALCUTTA));

		Date date = convertToDate(dateTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int monthCount = calendar.get(Calendar.MONTH);

		return monthsFullName[monthCount];
	}

	/**
	 * 
	 * @param dateTime
	 * @return
	 */
	public static Date convertToDate(String dateTime) {
		if (TextUtils.isEmpty(dateTime)) {
			return null;
		}
		SimpleDateFormat outputFormat = new SimpleDateFormat(
				DEFAULT_DATE_FORMATTER, Locale.US);
		outputFormat.setTimeZone(TimeZone.getTimeZone(ASIA_CALCUTTA));

		try {
			Date defaultFormattedDate = outputFormat.parse(dateTime);
			return defaultFormattedDate;
		} catch (Exception e) {
			if (Constants.DEBUG) {
				Log.d(TAG, "Error in convert date format:"
						+ e.getMessage().toString());
			}
		}
		return null;
	}

	/**
	 * 
	 */
	public static String getSelectionArgs(int days) {
		if (days == 0) {
			days = 10;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		return "((dtstart >= " + calendar.getTimeInMillis()
				+ ") AND (dtend <= " + nextDaysDate(days) + "))";
	}

	/**
	 * 
	 * @param noOfDays
	 * @return
	 */
	private static long nextDaysDate(int noOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.DATE, noOfDays);
		return calendar.getTimeInMillis();
	}

	/**
	 * 
	 * @param milliseconds
	 * @param timezone
	 * @return
	 */
	public static String getDate(long milliseconds, String timezone) {
		return getDate(milliseconds, timezone, null);
	}

	public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
