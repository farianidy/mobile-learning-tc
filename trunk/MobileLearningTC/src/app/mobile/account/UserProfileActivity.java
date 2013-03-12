package app.mobile.account;

import app.mobile.learningtc.R;
import app.tool.config.MoodleConnection;
import app.tool.config.ServiceConnection;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import android.os.Bundle;
//import android.os.StrictMode;
//import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
//import android.widget.Toast;

public class UserProfileActivity extends Activity {
	
	int userid;
	String username, token, moodleUrl, serviceUrl;
	ServiceConnection serviceConnection;
	MoodleConnection moodleConnection = new MoodleConnection();
	
	private TextView tvUsername;
	//private TextView tvName, tvCountry, tvCity, tvEmailAddress, tvFirstAccess, tvLastAccess;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_profile);
		
		Intent param = getIntent();
		username = param.getStringExtra("username");
		
//		tvName = (TextView) findViewById(R.id.tvName);
		tvUsername = (TextView) findViewById(R.id.tvUsername);
//		tvCountry = (TextView) findViewById(R.id.tvCountry);
//		tvCity = (TextView) findViewById(R.id.tvCity);
//		tvEmailAddress = (TextView) findViewById(R.id.tvEmailAddress);
//		tvFirstAccess = (TextView) findViewById(R.id.tvFirstAccess);
//		tvLastAccess = (TextView) findViewById(R.id.tvLastAccess);
		
		tvUsername.setText(tvUsername.getText() + ": " + username);
		
		userid = moodleConnection.getIdByUsername(username);
		
		try {
			getUserById(userid);
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		tvName.setText();
		
//		try {
//			getToken();
//		} catch (ProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_profile, menu);
		return true;
	}
	
	protected void getUserById(int id) throws ProtocolException, IOException {
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
	}
}
