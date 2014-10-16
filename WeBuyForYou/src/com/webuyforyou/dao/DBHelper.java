package com.webuyforyou.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Constants;

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "com.webuyforu";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME_REMAINDER_SETTINGS = "remainder";
	public static final String COLUMN_NAME_BDAYS = "birthday_days";
	public static final String COLUMN_NAME_DAYS_FREQUENCY = "days_frequency";
	public static final String COLUMN_NAME_TIME_FREQUENCY_HOUR = "hour";
	public static final String COLUMN_NAME_TIME_FREQUENCY_MINUTE = "minute";

	public static final String TABLE_NAME_REMAINDER_SETTINGS_SAVED = "remainder_saved";
	public static final String COLUMN_NAME_SETTING_FIRST_TIME = "first_time";
	private static final String TAG = DBHelper.class.getSimpleName();
	private static final String COLUMN_NAME_EVENT = "value";
	private static final String TABLE_NAME_FAVORITE_ITEM = "favorite";
	private static final String COLUMN_NAME_ID = "id";

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
				+ COLUMN_NAME_TIME_FREQUENCY_HOUR + " VARCHAR,"
				+ COLUMN_NAME_TIME_FREQUENCY_MINUTE + " VARCHAR)";
		String saveStatement = "CREATE TABLE "
				+ TABLE_NAME_REMAINDER_SETTINGS_SAVED + "("
				+ COLUMN_NAME_SETTING_FIRST_TIME + " VARCHAR)";
		String favoriteEventStatement = "CREATE TABLE "
				+ TABLE_NAME_FAVORITE_ITEM + "(" + COLUMN_NAME_ID
				+ " VARCHAR(200) NOT NULL unique, " + COLUMN_NAME_EVENT
				+ " BLOB NOT NULL)";
		db.execSQL(statement);
		db.execSQL(saveStatement);
		db.execSQL(favoriteEventStatement);
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
		contentValues.put(COLUMN_NAME_TIME_FREQUENCY_HOUR, "" + hour);
		contentValues.put(COLUMN_NAME_TIME_FREQUENCY_MINUTE, "" + minute);
		update(TABLE_NAME_REMAINDER_SETTINGS, contentValues, null, null);
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
	public String getTimeFrequency(String columnName) {
		String time = getColumnData(columnName, TABLE_NAME_REMAINDER_SETTINGS);
		if (Constants.DEBUG) {
			Log.d(TAG, "getTimeFrequency: " + time);
		}
		return time;
	}

	/**
	 * 
	 * @param tablename
	 * @param id
	 * @param columnname
	 * @return
	 */
	public byte[] findByte(String tablename, String id, String columnname) {
		final String sql = "select * from " + tablename + " where id='" + id
				+ "'";
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = getReadableDatabase();
			cursor = db.rawQuery(sql, null);
			while (cursor.moveToNext()) {
				byte[] jsonByte = cursor.getBlob(cursor
						.getColumnIndex(columnname));
				return jsonByte;
			}
		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * set favorite events
	 * 
	 * @param value
	 */
	public void setFavoriteEvent(BirthdayDataModel birthdayDataModel) {
		byte[] bytes = null;
		try {
			bytes = Serializer.serialize(birthdayDataModel);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ContentValues contentValues = new ContentValues();
		contentValues.put(DBHelper.COLUMN_NAME_EVENT, bytes);

		byte[] data = findByte(TABLE_NAME_FAVORITE_ITEM,
				birthdayDataModel.getEventId(), COLUMN_NAME_EVENT);
		if (data == null) {
			contentValues.put(COLUMN_NAME_ID, birthdayDataModel.getEventId());
			insert(TABLE_NAME_FAVORITE_ITEM, null, contentValues);
		} else {
			update(TABLE_NAME_FAVORITE_ITEM, contentValues, "id=?",
					new String[] { birthdayDataModel.getEventId() });
		}

	}

	/**
	 * remove favorite items data
	 * 
	 * @param id
	 */
	public void removeFavorites(String id) {
		delete(TABLE_NAME_FAVORITE_ITEM, "id=?", new String[] { id });
	}

	/**
	 * get all favorites item
	 * 
	 * @return
	 */
	public List<BirthdayDataModel> getFavoritesItemData() {
		List<BirthdayDataModel> favoriteItemList = new ArrayList<BirthdayDataModel>();
		String sqlQuery = "SELECT * " + " FROM " + TABLE_NAME_FAVORITE_ITEM;
		Cursor cursor = null;
		try {
			cursor = rawQuery(sqlQuery, null);
			if (cursor.moveToFirst()) {
				do {
					byte[] bytes = cursor.getBlob(cursor
							.getColumnIndex(COLUMN_NAME_EVENT));
					BirthdayDataModel section = (BirthdayDataModel) Serializer
							.deserialize(bytes);
					favoriteItemList.add(section);
				} while (cursor.moveToNext());
			}

		} catch (final Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		return favoriteItemList;
	}

}
