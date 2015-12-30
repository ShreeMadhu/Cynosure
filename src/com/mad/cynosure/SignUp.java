package com.mad.cynosure;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends AppCompatActivity {

	private TextView userNameTv;
	private TextView phoneNumberTv;

	private Button signUp;

	private String userName;
	private String phoneNumber;

	private CustomProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			setContentView(R.layout.activity_sign_up);

			CustomToolBar.applyCustomToolBar(this);
			CustomToolBar.removeLeftImage();
			CustomToolBar.removeRightImage();
			CustomToolBar.setTitle(Constants.TOOL_BAR_SIGN_UP);

			userNameTv = (TextView) findViewById(R.id.userName);
			userNameTv.setTypeface(Constants.light);

			phoneNumberTv = (TextView) findViewById(R.id.phoneNumber);
			phoneNumberTv.setTypeface(Constants.light);

			((TextView) findViewById(R.id.pageHeading))
					.setTypeface(Constants.light);

			signUp = (Button) findViewById(R.id.signUp);
			signUp.setTypeface(Constants.medium);

			signUp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					boolean credentialsApproved = validateUserCredentials();
					if (credentialsApproved) {
						progressDialog = new CustomProgressDialog(SignUp.this);
						progressDialog.showDialog();
						checkForUserName();
					}
				}
			});
		} else {
			moveToNextPage();
		}
	}

	public void createUser() {
		ParseUser user = new ParseUser();
		user.setUsername(userName);
		user.setPassword(Constants.PARSE_USER_PWD);
		user.put(Constants.PARSE_USER_PHONE_KEY, phoneNumber);

		user.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException pe) {
				progressDialog.dismissDialog();
				if (pe == null) {
					moveToNextPage();
				} else {
					Log.d("Cynosure", "parse exception - " + pe.toString());
					Constants
							.displayToast(SignUp.this,
									"Oops, Signing up failed. Please make sure the network is on");
				}
			}
		});
	}

	public void checkForUserName() {
		ParseQuery<ParseUser> query = ParseUser.getQuery();

		query.whereEqualTo("username", userName);
		query.findInBackground(new FindCallback<ParseUser>() {
			public void done(List<ParseUser> userList,
					com.parse.ParseException e) {
				if (e == null) {
					if (userList.size() > 0) {
						progressDialog.dismissDialog();
						Constants
								.displayToast(SignUp.this,
										"User name exists. Please provide a different user name");
					} else {
						createUser();
					}
				} else {
					Log.d("Cynosure", "pe - " + e.toString());
					progressDialog.dismissDialog();
					Constants.displayToast(SignUp.this,
							"An error occurred. Please try again later.");
				}
			}
		});
	}

	public boolean validateUserCredentials() {
		boolean credentialsStatus = false;
		userName = userNameTv.getText().toString().trim().toLowerCase();
		phoneNumber = phoneNumberTv.getText().toString().trim();

		if (userName.isEmpty() || phoneNumber.isEmpty()) {
			Constants.displayToast(this,
					"Please provide the required credentials...");
		} else {
			if (phoneNumber.length() == 10
					&& android.util.Patterns.PHONE.matcher(phoneNumber)
							.matches()) {
				credentialsStatus = true;
			} else {
				Constants.displayToast(this,
						"Please enter valid phone number...");
			}
		}
		return credentialsStatus;
	}

	public void moveToNextPage() {
		Intent intent = new Intent(SignUp.this, ConversationPage.class);
		startActivity(intent);
		finish();
	}
}
