package com.webuyforyou.linkedin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import com.webuyforyou.R;
import com.webuyforyou.activity.BaseActivity;
import com.webuyforyou.adapter.LinkedinListAdapter;
import com.webuyforyou.model.LinkedinUserDataModel;
import com.webuyforyou.preference.SharedKeyPreference;
import com.webuyforyou.preference.SharedPreferencesUtil;
import com.webuyforyou.util.Constants;

/**
 * 
 * @author Kannappan
 * 
 */
public class LinkedInListActivity extends BaseActivity {

	public static final String TAG = LinkedInListActivity.class.getSimpleName();
	private Context mContext = null;
	private ListView mLinkedInList = null;
	private LinkedinListAdapter mLinkedInListAdapter = null;
	private ProgressDialog mProgressDialog;
	private String LINKED_IN_CONNECTION_API = "https://api.linkedin.com/v1/people/~/connections:(headline,first-name,last-name,pictureUrl,siteStandardProfileRequest)";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.linkedin_list);

		// show home action bar home up button
		isShowActionBarHomeButton(true);

		mContext = this;

		// List view
		mLinkedInList = (ListView) findViewById(R.id.linkedInList);

		// Start async task to get connections
		LinkedInConnectionDetailsTask connectionDetailsTask = new LinkedInConnectionDetailsTask();
		connectionDetailsTask.execute();

	}

	// Async task to get linkedin connections
	private class LinkedInConnectionDetailsTask extends
			AsyncTask<Void, Void, List<LinkedinUserDataModel>> {

		@Override
		protected void onPreExecute() {
			showProgressLoader();
		}

		@Override
		protected List<LinkedinUserDataModel> doInBackground(Void... arg) {
			List<LinkedinUserDataModel> dataModels = new ArrayList<LinkedinUserDataModel>();

			String urlStr = LINKED_IN_CONNECTION_API;
			urlStr += "?format=json";

			SharedPreferencesUtil preferencesUtil = SharedPreferencesUtil
					.getInstance();

			String access_token = preferencesUtil.getStringValue(
					SharedKeyPreference.PREF_KEY_LINKEDIN_ACCESS_TOKEN, null);
			String access_secret = preferencesUtil.getStringValue(
					SharedKeyPreference.PREF_KEY_LINKEDIN_ACCESS_SECRET, null);

			if (access_token != null && access_secret != null) {
				OAuthService linkedInService = new ServiceBuilder()
						.provider(LinkedInApi.class)
						.apiKey(LinkedInOAuthActivity.APIKEY)
						.apiSecret(LinkedInOAuthActivity.APISECRET)
						.callback(LinkedInOAuthActivity.CALLBACK).build();

				OAuthRequest request = new OAuthRequest(Verb.GET, urlStr);

				Token t = new Token(access_token, access_secret);
				linkedInService.signRequest(t, request);

				Response response = null;
				try {
					response = request.send();
					if (response.isSuccessful()) {
						String result = response.getBody();
						try {
							JSONObject baseJsonObject = new JSONObject(result);
							JSONArray jsonArray = baseJsonObject
									.getJSONArray("values");
							for (int i = 0; i < jsonArray.length(); i++) {
								LinkedinUserDataModel userDataModel = new LinkedinUserDataModel();
								JSONObject jsonObject = jsonArray
										.getJSONObject(i);
								String firstName = jsonObject
										.getString("firstName");
								String lastName = jsonObject
										.getString("lastName");
								String picUrl = null;
								if (jsonObject.has("pictureUrl")) {
									picUrl = jsonObject.getString("pictureUrl");
								}

								String publicUrl = null;
								if (jsonObject
										.has("siteStandardProfileRequest")) {
									publicUrl = jsonObject.getJSONObject(
											"siteStandardProfileRequest")
											.getString("url");

									if (i == 0) {
										String birthday = readPublicUrl(publicUrl);
										if (Constants.DEBUG) { 
											Log.d(TAG, "Read web page: "
													+ birthday);
										}
										if (!TextUtils.isEmpty(birthday)) {

										}
									}
									// set data to data model
									userDataModel.setFirstName(firstName);
									userDataModel.setLastName(lastName);
									userDataModel.setImageUrl(picUrl);
									userDataModel.setPublicUrl(publicUrl);
									dataModels.add(userDataModel);
								}

							}

						} catch (JSONException e1) {
							e1.printStackTrace();
						}
					} else
						return null;
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return null;
			}
			return dataModels;
		}

		@Override
		protected void onPostExecute(List<LinkedinUserDataModel> result) {
			if (result != null && result.size() > 0) {
				mLinkedInListAdapter = new LinkedinListAdapter(mContext, result);
				mLinkedInList.setAdapter(mLinkedInListAdapter);
			}
			hideProgressLoader();
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

	private String readPublicUrl(String url) {
		String response = null;
		try {
			URL data = new URL(url);
			HttpURLConnection con = (HttpURLConnection) data.openConnection();
			con.setRequestProperty("Content-Type", "text/html");
			InputStream inputStream = con.getInputStream();

			 /* Read webpage coontent */
			 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			 /* Read line by line */
			 String inputLine;
			 while ((inputLine = in.readLine()) != null) {
			 System.out.println(inputLine);
			 }

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			int ch = -1;
			while ((ch = inputStream.read()) != -1) {
				os.write(ch);
			}

			byte[] byteData = os.toByteArray();
			response = new String(byteData);
			/* close BufferedReader */
			inputStream.close();
			/* close HttpURLConnection */
			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public String getHomeTitle(String urlStr) throws Exception {
		// TODO Auto-generated method stub
		String homeTitle = "";

		HtmlCleaner htmlCleaner = new HtmlCleaner();
		CleanerProperties props = htmlCleaner.getProperties();
		props.setAllowHtmlInsideAttributes(false);
		props.setAllowMultiWordAttributes(true);
		props.setRecognizeUnicodeChars(true);
		props.setOmitComments(true);

		URL url = new URL(urlStr);
		TagNode root = htmlCleaner.clean(url);

		String XPATH_HOME = "//div[@class='']/h1";
		Object[] homeTitleNode = root.evaluateXPath(XPATH_HOME);
		if (homeTitleNode.length > 0) {
			TagNode resultNode = (TagNode) homeTitleNode[0];
			homeTitle = resultNode.getText().toString();
		}

		return homeTitle;
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
