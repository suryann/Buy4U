package com.webuyforyou.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public enum ActivityController {

	INSTANCE;

	/**
	 * launch activity..
	 * 
	 * @param context
	 * @param cls
	 * @param bundle
	 */
	public void launchActivity(Context context, Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(context, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
}
