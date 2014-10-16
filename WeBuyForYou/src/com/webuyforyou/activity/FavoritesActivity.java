package com.webuyforyou.activity;

import java.util.List;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.webuyforyou.R;
import com.webuyforyou.adapter.FavoriteListAdapter;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.model.BirthdayDataModel;

public class FavoritesActivity extends BaseActivity {
	private static final String TAG = FavoritesActivity.class.getSimpleName();
	private ListView listView;
	private TextView mHintTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_birthday_layout);
		isShowActionBarHomeButton(true);

		listView = (ListView) findViewById(R.id.birthday_list);
		mHintTextView = (TextView) findViewById(R.id.birthday_textview);
		mHintTextView.setText(R.string.you_don_t_have_any_favorites_events_);

		//
		getFavoriteItems();
	}

	/**
	 * get All favorites items
	 * 
	 */
	private void getFavoriteItems() {
		List<BirthdayDataModel> birthdayDataModels = DBHelper.getInstance()
				.getFavoritesItemData();
		if (birthdayDataModels != null && birthdayDataModels.size() > 0) {
			FavoriteListAdapter favoriteListAdapter = new FavoriteListAdapter(
					this, birthdayDataModels);
			listView.setAdapter(favoriteListAdapter);
			listView.setVisibility(View.VISIBLE);
			mHintTextView.setVisibility(View.GONE);
		} else {
			listView.setVisibility(View.GONE);
			mHintTextView.setVisibility(View.VISIBLE);
		}
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
