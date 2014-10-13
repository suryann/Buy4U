package com.webuyforyou.preference;

import java.util.Set;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 
 * Use this class to store a small kind of data into a SharedPreferences..
 * 
 * @author Kannappan
 * 
 */
public class SharedPreferencesUtil {

	private static final String TAG = SharedPreferencesUtil.class
			.getSimpleName();

	private static SharedPreferencesUtil instance;

	private SharedPreferences mSharedPrefs;
	private Editor mPrefsEditor;

	private SharedPreferencesUtil(Context context) {
		mSharedPrefs = context.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		mPrefsEditor = mSharedPrefs.edit();
	}

	/**
	 * call this method in application your class..
	 * 
	 * @param application
	 */
	public static void init(Application application) {
		if (instance == null) {
			instance = new SharedPreferencesUtil(
					application.getApplicationContext());
		}
	}

	/**
	 * get a preference instance
	 * 
	 * @return
	 */
	public static SharedPreferencesUtil getInstance() {
		if (instance == null) {
			throw new RuntimeException(
					"Must run init(Application application) before an instance can be obtained");
		}
		return instance;
	}

	/**
	 * clear all the data from preferences..
	 */
	public void clear() {
		mPrefsEditor.clear();
		mPrefsEditor.commit();
	}

	/**
	 * remove data from preferences..
	 * 
	 * @param key
	 */
	public void remove(String key) {
		mPrefsEditor.remove(key);
		mPrefsEditor.commit();
	}

	/**
	 * get string value
	 * 
	 * @param key
	 * @param defaultvalue
	 * @return
	 */
	public String getStringValue(String key, String defaultvalue) {
		return mSharedPrefs.getString(key, defaultvalue);
	}

	/**
	 * store string value
	 * 
	 * @param key
	 * @param value
	 */
	public void setStringValue(String key, String value) {
		mPrefsEditor.putString(key, value);
		mPrefsEditor.commit();
	}

	/**
	 * get integer value
	 * 
	 * @param key
	 * @param defaultvalue
	 * @return
	 */
	public int getIntValue(String key, int defaultvalue) {
		return mSharedPrefs.getInt(key, defaultvalue);
	}

	/**
	 * store integer value
	 * 
	 * @param key
	 * @param value
	 */
	public void setIntValue(String key, int value) {
		mPrefsEditor.putInt(key, value);
		mPrefsEditor.commit();
	}

	/**
	 * get boolean value
	 * 
	 * @param key
	 * @param defaultvalue
	 * @return
	 */
	public boolean getBooleanValue(String key, Boolean defaultvalue) {
		return mSharedPrefs.getBoolean(key, defaultvalue);
	}

	/**
	 * store boolean value
	 * 
	 * @param key
	 * @param value
	 */
	public void setBooleanValue(String key, boolean value) {
		mPrefsEditor.putBoolean(key, value);
		mPrefsEditor.commit();
	}

	/**
	 * store long value
	 * 
	 * @param key
	 * @param value
	 */
	public void setLongValue(String key, long value) {
		mPrefsEditor.putLong(key, value);
		mPrefsEditor.commit();
	}

	/**
	 * get Long value.
	 * 
	 * @param key
	 * @param defaultvalue
	 * @return
	 */
	public long getLongValue(String key, long defaultvalue) {
		return mSharedPrefs.getLong(key, defaultvalue);
	}

	/**
	 * This one is added in API level 11.
	 * 
	 * Set a set of String values in the preferences editor
	 * 
	 * @param key
	 * @param values
	 */
	public void putStringSet(String key, Set<String> values) {
		mPrefsEditor.putStringSet(key, values);
		mPrefsEditor.commit();
	}

	/**
	 * This one is added in API level 11.
	 * 
	 * get a set of string values..
	 * 
	 * @param key
	 * @param defaultvalue
	 * @return
	 */
	public Set<String> getStringset(String key, Set<String> defaultvalue) {
		return mSharedPrefs.getStringSet(key, defaultvalue);
	}

}
