package app.mobile.account;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.util.connection.MoodleConnection;
import app.util.connection.ServiceConnection;

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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class UserProfileFragment extends SherlockFragment {
	
	String userid, username, fullname, token, moodleUrl, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	MoodleConnection moodleConnection = new MoodleConnection();
	
	private ProgressDialog progressDialog;
	private ImageView ivUser;
	private TextView tvUsername, tvName, tvCountry, tvCity, tvEmailAddress, tvFirstAccess, tvLastAccess;
	
	getUserDetailById serviceTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set view
		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.title_fragment_userprofile);
		
		// Get session info
		session = new SessionManager(getActivity());
		
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		username = user.get(SessionManager.KEY_USERNAME);
		fullname = user.get(SessionManager.KEY_FULLNAME);
		
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading information...");
		progressDialog.setIndeterminate(true);
		
		serviceTask = new getUserDetailById();
		serviceTask.execute(userid);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View v = inflater.inflate(R.layout.fragment_userprofile, container, false);
		return v;
	}
	
	private class getUserDetailById extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
			return getUserDetailsByIdWSRest(params[0]);
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}
		
		@Override
		protected void onPostExecute(String result) {
			serviceTask = null;
			
			ivUser = (ImageView) getView().findViewById(R.id.ivUser);
			tvName = (TextView) getView().findViewById(R.id.tvName);
			tvUsername = (TextView) getView().findViewById(R.id.tvUsername);
			tvCountry = (TextView) getView().findViewById(R.id.tvCountry);
			tvCity = (TextView) getView().findViewById(R.id.tvCity);
			tvEmailAddress = (TextView) getView().findViewById(R.id.tvEmailAddress);
			tvFirstAccess = (TextView) getView().findViewById(R.id.tvFirstAccess);
			tvLastAccess = (TextView) getView().findViewById(R.id.tvLastAccess);
			
			// TODO: get result to display
			tvName.setText(fullname);
			tvUsername.setText(tvUsername.getText() + ": " + username);
			tvCountry.setText(tvCountry.getText() + ": ");
			tvCity.setText(tvCity.getText() + ": ");
			tvEmailAddress.setText(tvEmailAddress.getText() + ": ");
			tvFirstAccess.setText(tvFirstAccess.getText() + ": ");
			tvLastAccess.setText(tvLastAccess.getText() + ": ");
			
			// TODO: If user picture not empty
			ivUser.setImageResource(R.drawable.user_empty);
			
			progressDialog.dismiss();
		}
		
		@Override
		protected void onCancelled() {
			serviceTask = null;
			progressDialog.dismiss();
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
		
		// Send request
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
			
			// Get response
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
