package com.webuyforyou.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.webuyforyou.R;
import com.webuyforyou.util.Constants;

public class WebViewActivity extends BaseActivity {

	private static final String TAG = WebViewActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		String url = getIntent().getStringExtra(Constants.WEB_VIEW_URL);
		WebView webView = (WebView) findViewById(R.id.webView);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebviewClientController());
		webView.loadUrl(url);
		isShowActionBarHomeButton(true);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class WebviewClientController extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			// show progress bar
			showProgressLoader();
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			// hide progress bar
			hideProgressLoader();
		}

	}

	/**
	 * show progress loader
	 */
	public void showProgressLoader() {
		// showProgressDialog(this);
	}

	/**
	 * dismiss progress loader..
	 */
	public void hideProgressLoader() {
		// dismissProgressDialog();
	}

}
