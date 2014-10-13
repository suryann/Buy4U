package com.webuyforyou.preference;

import com.webuyforyou.BaseApplication;
import com.webuyforyou.R;

/**
 * 
 * This class contains all the preference keys..
 * 
 * @author Kannappan
 * 
 */
public final class SharedKeyPreference {

	/**
	 * Preferences keys
	 * 
	 */
	public static final String PREF_KEY_REMAINDER_DAY = BaseApplication
			.getApplication().getString(
					R.string.pref_key_remainder_days_preference);

	public static final String PREF_KEY_REMAINDER_TIME = BaseApplication
			.getApplication().getString(
					R.string.pref_key_remainder_time_preference);

	public static final String PREF_KEY_REMAINDER_BIRTHDAY_INTERVAL = BaseApplication
			.getApplication().getString(
					R.string.pref_key_remainder_birthday_interval);
}
