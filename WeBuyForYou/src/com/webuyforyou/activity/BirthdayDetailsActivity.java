package com.webuyforyou.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.model.BirthdayDataModel;
import com.webuyforyou.util.Constants;
import com.webuyforyou.util.Utility;

public class BirthdayDetailsActivity extends BaseActivity {

	private BirthdayDataModel mBirthdayDataModel;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthday_details);
		isShowActionBarHomeButton(true);

		// read data from bundle
		mBirthdayDataModel = (BirthdayDataModel) getIntent()
				.getSerializableExtra(Constants.KEY_BUNDLE_BIRTHDAY_MODEL);

		initializeView();
	}

	/**
	 * initialize a layout view
	 */
	private void initializeView() {
		TextView titleTextView = (TextView) findViewById(R.id.title_textview);
		TextView dateTextView = (TextView) findViewById(R.id.date_textview);
		TextView descriptionTextView = (TextView) findViewById(R.id.description_textview);

		titleTextView.setText(mBirthdayDataModel.getTitle());

		String date = mBirthdayDataModel.getStartDate();
		if (!TextUtils.isEmpty(date)) {
			String dateString = Utility.getDate(Long.parseLong(date), null,
					Utility.CONVERT_DATE_FORMAT);
			dateTextView.setText(dateString);
		}
		descriptionTextView.setText(mBirthdayDataModel.getDescription());

		findViewById(R.id.website_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						handleWebsiteButtonSubmit();
					}
				});
	}

	protected void handleWebsiteButtonSubmit() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(Constants.ABOUT_URL));
		startActivity(browserIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
