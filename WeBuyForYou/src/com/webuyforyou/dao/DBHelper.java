package com.webuyforyou.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.preference.SharedKeyPreference;
import com.webuyforyou.preference.SharedPreferencesUtil;
import com.webuyforyou.util.Constants;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "com.webuyforu";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME_REMAINDER_SETTINGS = "remainder";
	public static final String COLUMN_NAME_BDAYS = "birthday_days";
	public static final String COLUMN_NAME_DAYS_FREQUENCY = "days_frequency";
	public static final String COLUMN_NAME_TIME_FREQUENCY = "time_frequency";

	public static final String TABLE_NAME_REMAINDER_SETTINGS_SAVED = "remainder_saved";
	public static final String COLUMN_NAME_SETTING_FIRST_TIME = "first_time";
	private static final String TAG = DBHelper.class.getSimpleName();
	private SQLiteDatabase mSqLiteDatabase;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mSqLiteDatabase = getWritableDatabase();
	}

	private static DBHelper helper;

	public static DBHelper getInstance() {
		if (helper == null) {
			synchronized (DBHelper.class) {
				if (helper == null) {
					helper = new DBHelper(BaseApplication.getApplication());
				}
			}
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String statement = "CREATE TABLE " + TABLE_NAME_REMAINDER_SETTINGS
				+ "(" + COLUMN_NAME_BDAYS + " VARCHAR,"
				+ COLUMN_NAME_DAYS_FREQUENCY + " VARCHAR,"
				+ COLUMN_NAME_TIME_FREQUENCY + " VARCHAR)";
		String saveStatement = "CREATE TABLE "
				+ TABLE_NAME_REMAINDER_SETTINGS_SAVED + "("
				+ COLUMN_NAME_SETTING_FIRST_TIME + " VARCHAR)";
		db.execSQL(statement);
		db.execSQL(saveStatement);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	/**
	 * delete database
	 * 
	 */
	public void deleteDatabase() {
		try {
			BaseApplication.getApplication().deleteDatabase(DATABASE_NAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Convenience method for inserting a row into the database.
	 * 
	 * @param table
	 *            the table to insert the row into.
	 * @param nullColumnHack
	 *            optional; may be null. SQL doesn't allow inserting a
	 *            completely empty row without naming at least one column name.
	 *            If your provided values is empty, no column names are known
	 *            and an empty row can't be inserted. If not set to null, the
	 *            nullColumnHack parameter provides the name of nullable column
	 *            name to explicitly insert a NULL into in the case where your
	 *            values is empty.
	 * @param values
	 *            this map contains the initial column values for the row. The
	 *            keys should be the column names and the values the column
	 *            values.
	 * @return the row ID of the newly inserted row, or -1 if an error occurred.
	 */
	public long insert(String table, String nullColumnHack, ContentValues values) {
		return getWritableDatabase().insert(table, null, values);
	}

	/**
	 * Convenience method for updating rows in the database.
	 * 
	 * @param table
	 *            the table to update in.
	 * @param values
	 *            a map from column names to new column values. null is a valid
	 *            value that will be translated to NULL.
	 * @param whereClause
	 *            the optional WHERE clause to apply when updating. Passing null
	 *            will update all rows.
	 * @param whereArgs
	 * @return the number of rows affected
	 */
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		return getWritableDatabase().update(table, values, whereClause,
				whereArgs);
	}

	public Cursor rawQuery(String sql, String[] selectionArgs) {
		return getReadableDatabase().rawQuery(sql, selectionArgs);
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * 
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		return getWritableDatabase().delete(table, whereClause, whereArgs);
	}

	/**
	 * Query the given table, returning a Cursor over the result set.
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor query(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		Cursor cursor = getReadableDatabase().query(table, columns, selection,
				selectionArgs, groupBy, having, orderBy);
		return cursor;
	}

	/**
	 * get a user setting saved in value..
	 * 
	 * @return
	 */
	public boolean isUserSettingSaved() {
		String isSettingSaved = getColumnData(COLUMN_NAME_SETTING_FIRST_TIME,
				TABLE_NAME_REMAINDER_SETTINGS_SAVED);
		if (!TextUtils.isEmpty(isSettingSaved)) {
			if (isSettingSaved.equalsIgnoreCase("true")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * set user login value
	 * 
	 * @param isSettingSaved
	 */
	public void setUserSettingSaved(boolean isSettingSaved) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_NAME_SETTING_FIRST_TIME, "" + isSettingSaved);
		int count = getRowCount(TABLE_NAME_REMAINDER_SETTINGS_SAVED);
		if (count == 0) {
			insert(TABLE_NAME_REMAINDER_SETTINGS_SAVED, null, values);
		} else {
			update(TABLE_NAME_REMAINDER_SETTINGS_SAVED, values, null, null);
		}
	}

	/**
	 * get column data
	 * 
	 * @return
	 */
	public int getRowCount(String tableName) {
		String[] projection = new String[] { "COUNT(*)" };

		Cursor cursor = getReadableDatabase().query(tableName, projection,
				null, null, null, null, null);
		int count = 0;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			count = cursor.getInt(0);
		}
		cursor.close();
		if (Constants.DEBUG) {
			Log.d(TAG, "  count : " + count);
		}

		return count;
	}

	/**
	 * getting a particular column data
	 * 
	 * @param columnName
	 * @return
	 */
	public String getColumnData(String columnName, String tableName) {
		String sqlQuery = "SELECT " + columnName + " FROM " + tableName;
		mSqLiteDatabase = getReadableDatabase();
		Cursor cursor = mSqLiteDatabase.rawQuery(sqlQuery, null);
		String response = null;
		while (cursor.moveToNext()) {
			int columnIndex = cursor.getColumnIndex(columnName);
			response = cursor.getString(columnIndex);
			if (Constants.DEBUG) {
				Log.d(TAG, "columnIndex: " + columnIndex);
				Log.d(TAG, "response: " + response);
			}
		}
		return response;

	}

	/**
	 * 
	 * 
	 * @param value
	 */
	public void setDaysFrequency(String value) {
		if (!TextUtils.isEmpty(value)) {
			value = value.replaceAll("days", "").toString().trim();
			value = value.replaceAll("day", "").toString().trim();
			if (value.contains("week")) {
				value = "" + 7;
			}

			ContentValues contentValues = new ContentValues();
			contentValues.put(DBHelper.COLUMN_NAME_DAYS_FREQUENCY, value);

			int count = getRowCount(TABLE_NAME_REMAINDER_SETTINGS);
			if (count == 0) {
				insert(TABLE_NAME_REMAINDER_SETTINGS, null, contentValues);
			} else {
				update(DBHelper.TABLE_NAME_REMAINDER_SETTINGS, contentValues,
						null, null);
			}
		}
	}

	/**
	 * 
	 * @param value
	 */
	public void setBirthDaysFrequency(String value) {
		if (!TextUtils.isEmpty(value)) {
			value = value.replaceAll("days", "").toString().trim();
			ContentValues contentValues = new ContentValues();
			contentValues.put(DBHelper.COLUMN_NAME_BDAYS, value);

			int count = getRowCount(TABLE_NAME_REMAINDER_SETTINGS);
			if (count == 0) {
				insert(TABLE_NAME_REMAINDER_SETTINGS, null, contentValues);
			} else {
				update(DBHelper.TABLE_NAME_REMAINDER_SETTINGS, contentValues,
						null, null);
			}
		}
	}

	/**
	 * 
	 * @param hour
	 * @param minute
	 */
	public void setTimeFrequency(int hour, int minute) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(COLUMN_NAME_TIME_FREQUENCY, "" + hour + ":" + minute);
		update(TABLE_NAME_REMAINDER_SETTINGS, contentValues, null, null);
		SharedPreferencesUtil.getInstance().setIntValue(
				SharedKeyPreference.PREF_KEY_REMAINDER_TIME_HOUR, hour);
		SharedPreferencesUtil.getInstance().setIntValue(
				SharedKeyPreference.PREF_KEY_REMAINDER_TIME_MINUTE, minute);
	}

	/**
	 * 
	 * @return
	 */
	public String getDaysFrequency() {
		String days = getColumnData(COLUMN_NAME_DAYS_FREQUENCY,
				TABLE_NAME_REMAINDER_SETTINGS);
		if (Constants.DEBUG) {
			Log.d(TAG, "getDaysFrequency: " + days);
		}
		return days;
	}

	/**
	 * 
	 * @return
	 */
	public String getBithDaysFrequency() {
		String days = getColumnData(COLUMN_NAME_BDAYS,
				TABLE_NAME_REMAINDER_SETTINGS);
		if (Constants.DEBUG) {
			Log.d(TAG, "getBithDaysFrequency: " + days);
		}
		return days;
	}

	/**
	 * 
	 * @return
	 */
	public String getTimeFrequency() {
		String days = getColumnData(COLUMN_NAME_TIME_FREQUENCY,
				TABLE_NAME_REMAINDER_SETTINGS);
		if (Constants.DEBUG) {
			Log.d(TAG, "getTimeFrequency: " + days);
		}
		return days;
	}

}
