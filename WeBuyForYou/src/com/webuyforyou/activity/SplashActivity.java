package com.webuyforyou.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.webuyforyou.R;
import com.webuyforyou.controller.ActivityController;
import com.webuyforyou.dao.DBHelper;
import com.webuyforyou.util.Utility;

/**
 * 
 * @author Kannappan
 * 
 */
public class SplashActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_layout);

		ImageView imageView = (ImageView) findViewById(R.id.image_view);

		Bitmap bitmap = Utility.decodeSampledBitmapFromResource(getResources(),
				R.drawable.ic_logo_giftbox, 300, 300);

		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (DBHelper.getInstance().isUserSettingSaved()) {
					launchHomeScreen();
				} else {
					launchRemainderScreen();
				}
			}
		}, 1000);
	}

	/**
	 * launch remainder screen
	 */
	private void launchRemainderScreen() {
		ActivityController.INSTANCE.launchActivity(this,
				SettingsActivity.class, null);
		finish();
	}

	/**
	 * launch home screen
	 */
	private void launchHomeScreen() {
		ActivityController.INSTANCE.launchActivity(this, MainActivity.class,
				null);
		finish();
	}

}
