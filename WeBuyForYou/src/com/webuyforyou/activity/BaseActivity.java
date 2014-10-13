package com.webuyforyou.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.webuyforyou.R;

public class BaseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// opening transition animations
		overridePendingTransition(R.anim.activity_open_translate,
				R.anim.activity_close_scale);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// closing transition animations
		overridePendingTransition(R.anim.activity_open_scale,
				R.anim.activity_close_translate);
	}

	/**
	 * Show actionbar and other properties..
	 * 
	 * @param isShow
	 */
	protected void isShowActionBarHomeButton(boolean isShow) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(isShow);
		actionBar.setDisplayHomeAsUpEnabled(isShow);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * show toast messages...
	 * 
	 * @param context
	 * @param message
	 */
	public void showToastMessage(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
}
