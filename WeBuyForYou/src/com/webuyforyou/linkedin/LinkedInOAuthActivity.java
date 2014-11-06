package com.webuyforyou.linkedin;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.webuyforyou.R;

public class LinkedInOAuthActivity extends Activity {

	final static String APIKEY = "75i61vqmlkxflq";
	final static String APISECRET = "GZizY0iA0Etp6OeU";
	final static String CALLBACK = "oauth://linkedin";
	private WebView mWebView = null;
	private OAuthService mService = null;
	private Token mRequestToken = null;
	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkedin_oauth);
		String scopeParams = "r_emailaddress r_contactinfo rw_nus r_network r_fullprofile";

		mService = new ServiceBuilder().provider(LinkedInApi.class)
				.apiKey(APIKEY).apiSecret(APISECRET).callback(CALLBACK)
				.scope(scopeParams).build();

		mWebView = (WebView) findViewById(R.id.linkedin_webview);
		// Start the async task
		LinkedInAuthTask task = new LinkedInAuthTask();
		task.execute();
	}

	// Async task for authentication
	private class LinkedInAuthTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			// show progress bar
			showProgressLoader();
		}

		@Override
		protected String doInBackground(Void... arg0) {

			// Temporary URL
			String authURL = "http://api.linkedin.com/";

			try {
				mRequestToken = mService.getRequestToken();
				authURL = mService.getAuthorizationUrl(mRequestToken);
			} catch (OAuthException e) {
				e.printStackTrace();
				return null;
			}

			return authURL;
		}

		@Override
		protected void onPostExecute(String authURL) {
			mWebView.setWebViewClient(new WebViewClient() {

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					super.shouldOverrideUrlLoading(view, url);

					if (url.startsWith("oauth")) {
						mWebView.setVisibility(WebView.GONE);

						final String url1 = url;
						Thread t1 = new Thread() {
							public void run() {
								Uri uri = Uri.parse(url1);

								String verifier = uri
										.getQueryParameter("oauth_verifier");
								Verifier v = new Verifier(verifier);
								Token accessToken = mService.getAccessToken(
										mRequestToken, v);

								Log.d("Response: ", "Raw response: "
										+ accessToken.getRawResponse());

								Intent intent = new Intent();
								intent.putExtra("access_token",
										accessToken.getToken());
								intent.putExtra("access_secret",
										accessToken.getSecret());
								setResult(RESULT_OK, intent);

								finish();
							}
						};
						t1.start();
					}

					return false;
				}

			});
			hideProgressLoader();
			mWebView.loadUrl(authURL);
		}

	}

	protected void showProgressLoader() {
		mProgressDialog = ProgressDialog.show(this, "loading...",
				"Please wait...");
	}

	protected void hideProgressLoader() {
		if (mProgressDialog != null)
			mProgressDialog.dismiss();
	}
}
