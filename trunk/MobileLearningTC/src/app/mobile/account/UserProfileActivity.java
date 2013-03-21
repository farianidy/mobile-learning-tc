package app.mobile.account;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.tool.config.MoodleConnection;
import app.tool.config.ServiceConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class UserProfileActivity extends Activity {
	
	String userid, username, fullname, token, moodleUrl, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	MoodleConnection moodleConnection = new MoodleConnection();
	
	getUserDetailById userDetailTask = null;
	
	ProgressDialog progressUserDetail;
	private TextView tvUsername, tvName, tvCountry, tvCity, tvEmailAddress, tvFirstAccess, tvLastAccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		session = new SessionManager(this);
		
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		username = user.get(SessionManager.KEY_USERNAME);
		fullname = user.get(SessionManager.KEY_FULLNAME);
		
		Log.d("userid", userid);
		
		tvName = (TextView) findViewById(R.id.tvName);
		tvUsername = (TextView) findViewById(R.id.tvUsername);
		tvCountry = (TextView) findViewById(R.id.tvCountry);
		tvCity = (TextView) findViewById(R.id.tvCity);
		tvEmailAddress = (TextView) findViewById(R.id.tvEmailAddress);
		tvFirstAccess = (TextView) findViewById(R.id.tvFirstAccess);
		tvLastAccess = (TextView) findViewById(R.id.tvLastAccess);
		
		tvName.setText(fullname);
		tvUsername.setText(tvUsername.getText() + ": " + username);
		tvCountry.setText(tvCountry.getText() + ": ");
		tvCity.setText(tvCity.getText() + ": ");
		tvEmailAddress.setText(tvEmailAddress.getText() + ": ");
		tvFirstAccess.setText(tvFirstAccess.getText() + ": ");
		tvLastAccess.setText(tvLastAccess.getText() + ": ");
		
		progressUserDetail = new ProgressDialog(UserProfileActivity.this);
		progressUserDetail.setMessage("Loading user information...");
		progressUserDetail.setIndeterminate(true);
		
		userDetailTask = new getUserDetailById();
		userDetailTask.execute(userid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}
	
	private class getUserDetailById extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			return getUserDetailsByIdWSRest(params[0]);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressUserDetail.show();
		}
		
		@Override
		protected void onPostExecute(String result) {
			userDetailTask = null;
			
			// TODO: get result to display
			
			progressUserDetail.dismiss();
		}
		
		@Override
		protected void onCancelled() {
			userDetailTask = null;
			progressUserDetail.dismiss();
		}
	}
	
	protected String getUserDetailsByIdWSRest(String id) {
		moodleUrl = moodleConnection.getUrlMoodleServer();
		token = moodleConnection.getToken();
		
		// REST RETURNED VALUES FORMAT
		String restFormat = "xml";
		if (restFormat.equals("json"))
			restFormat = "&moodlewsrestformat" + restFormat;
		else
			restFormat = "";
		
		// PARAMETERS
		String functionName = "moodle_user_get_users_by_id";
		String parameter = "userids[0]=" + id;
		
		// REST CALL
		
		// Send Request
		String serverUrl = moodleUrl + "/webservice/rest/server.php" + "?wstoken=" + token +
				"&wsfunction=" + functionName;
		Log.d("server url", serverUrl);
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Language", "en-US");
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			
			DataOutputStream request = new DataOutputStream(connection.getOutputStream());
			request.writeBytes(parameter);
			request.flush();
			request.close();
			
			// Get Response
			InputStream is = connection.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuilder sb = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append('\r');
			}
			reader.close();
			
			System.out.println(sb.toString());
			return sb.toString();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (ProtocolException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
