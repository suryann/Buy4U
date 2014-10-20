package com.webuyforyou.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.webuyforyou.R;

public class ImportBirthdayActivity extends BaseActivity {

	private UiLifecycleHelper lifecycleHelper;
	private static final List<String> PERMISSIONS = new ArrayList<String>() {
		{
			add("user_friends");
			add("public_profile");
			add("user_birthday");
		}
	};
	private static final int PICK_FRIENDS_ACTIVITY = 100;
	protected static final String TAG = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_birthday_layout);

		findViewById(R.id.facebook_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						handleImportFacebook();
					}
				});

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		lifecycleHelper = new UiLifecycleHelper(this,
				new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						onSessionStateChanged(session, state, exception);
					}
				});
		lifecycleHelper.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {
		super.onResume();
		lifecycleHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// Session.getActiveSession().onActivityResult(this, requestCode,
		// resultCode, data);
		lifecycleHelper.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	public void onPause() {
		super.onPause();
		lifecycleHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		lifecycleHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		lifecycleHelper.onSaveInstanceState(outState);
	}

	// public void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // switch (requestCode) {
	// // case PICK_FRIENDS_ACTIVITY:
	// // displaySelectedFriends(resultCode);
	// // break;
	// // default:
	// // Session.getActiveSession().onActivityResult(this, requestCode,
	// // resultCode, data);
	// // break;
	// // }
	// uiHelper.onActivityResult(requestCode, resultCode, data);
	// }

	private void handleImportFacebook() {
		Session session = Session.getActiveSession();
		if (!session.isOpened() && !session.isClosed()) {
			session.openForRead(new Session.OpenRequest(this).setPermissions(
					PERMISSIONS).setCallback(statusCallback));
		} else {
			Session.openActiveSession(this, true, PERMISSIONS,
					new Session.StatusCallback() {
						@Override
						public void call(Session session, SessionState state,
								Exception exception) {
							onSessionStateChanged(session, state, exception);
						}
					});
		}

	}

	Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChanged(session, state, exception);
		}
	};

	protected void onSessionStateChanged(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			// session.requestNewReadPermissions(new NewPermissionsRequest(
			// ImportBirthdayActivity.this, getMissingPermissions(session)));
			// Session.NewPermissionsRequest newPermissionsRequest = new
			// Session.NewPermissionsRequest(
			// this, Arrays.asList("publish_actions"));
			// session.requestNewPublishPermissions(newPermissionsRequest);

			Intent intent = new Intent(this, PickFriendsActivity.class);
			PickFriendsActivity.populateParameters(intent, null, true, true);
			startActivity(intent);

			// ActivityController.INSTANCE.launchActivity(this,
			// PickFriendsActivity.class, null);
			handleApiCall(session);
			Log.i(TAG, "Logged in...");
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");

			try {
				PackageInfo info = getPackageManager().getPackageInfo(
						"com.webuyforyou", PackageManager.GET_SIGNATURES);
				for (Signature signature : info.signatures) {
					MessageDigest md = MessageDigest.getInstance("SHA");
					md.update(signature.toByteArray());
					Log.d("KeyHash:",
							Base64.encodeToString(md.digest(), Base64.DEFAULT));
				}
			} catch (NameNotFoundException e) {

			} catch (NoSuchAlgorithmException e) {

			}
		}
		handleApiCall(session);
	}

	private void doBatchRequest() {
		String[] requestIds = { "me", "4" };

		RequestBatch requestBatch = new RequestBatch();
		for (final String requestId : requestIds) {
			requestBatch.add(new Request(Session.getActiveSession(), requestId,
					null, null, new Request.Callback() {
						public void onCompleted(Response response) {
							GraphObject graphObject = response.getGraphObject();
							// String s = textViewResults.getText().toString();
							// if (graphObject != null) {
							// if (graphObject.getProperty("id") != null) {
							// s = s + String.format("%s: %s\n",
							// graphObject.getProperty("id"),
							// graphObject.getProperty("name"));
							// }
							// }
						}
					}));
		}
		requestBatch.executeAsync();
	}

	private void handleApiCall(Session session) {
		/* make the API call */
		new Request(Session.getActiveSession(), "/me/friends", null,
				HttpMethod.GET, new Request.Callback() {
					public void onCompleted(Response response) {
						/* handle the result */
						String string = response.getRawResponse();
						System.out.println("Response: " + string);
					}
				}).executeAsync();
	}

	private List<String> getMissingPermissions(Session session) {
		List<String> missingPerms = new ArrayList<String>(PERMISSIONS);
		if (session != null && session.getPermissions() != null) {
			for (String requestedPerm : PERMISSIONS) {
				if (session.getPermissions().contains(requestedPerm)) {
					missingPerms.remove(requestedPerm);
				}
			}
		}
		return missingPerms;
	}

	// Request.GraphUserCallback graphUserCallback = new GraphUserCallback() {
	// @Override
	// public void onCompleted(GraphUser user, Response response) {
	// if (user != null) {
	// try {
	// String socialEmail = (String) user.getProperty("email");
	// if (socialEmail != null) {
	// String firstName = user.getFirstName();
	// String lastName = user.getLastName();
	// if (Constants.DEBUG) {
	// Log.d(TAG,
	// socialEmail + "," + firstName != null ? firstName
	// : "" + "," + lastName != null ? lastName
	// : "");
	// }
	// }
	// } catch (Exception e) {
	// if (Constants.DEBUG) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
	// }
	// };

}
