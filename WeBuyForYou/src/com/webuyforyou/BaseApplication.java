package com.webuyforyou;

import android.app.Application;

import com.webuyforyou.preference.SharedPreferencesUtil;

public class BaseApplication extends Application {

	private static final String TAG = BaseApplication.class.getSimpleName();
	private static BaseApplication application = null;

	@Override
	public void onCreate() {
		super.onCreate();
		// initializea a app instance
		init();
		// initialize a sharedPreference
		SharedPreferencesUtil.init(this);
	}

	/**
	 * create a application instance
	 * 
	 */
	private void init() {
		if (application == null) {
			application = BaseApplication.this;
		}
	}

	/**
	 * get Application context from Application class
	 * 
	 * @return
	 */
	public static BaseApplication getApplication() {
		return application;
	}
}
