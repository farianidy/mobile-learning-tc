package app.tool.config;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

public class MoodleConnection {
	
	//String urlMoodleServer = "http://10.0.2.2/moodle";
	String urlMoodleServer = "http://192.168.1.3/moodle";
	//String urlMoodleServer = "http://192.168.43.210/moodle";
	String adminToken = "4506e2a3f8aa465558ac6d0f31438287";
	
	public MoodleConnection() {
		
	}
	
	public String getUrlMoodleServer() {
		return urlMoodleServer;
	}
	
	public String getToken() {
		return adminToken;
	}
	
	@SuppressLint("NewApi")
	public int getIdByUsername(String username) {		
		Log.d("Try Get Id by User", "Here");
		
		String response = null;
		String parameter = "username=" + username;
		Log.d("Parameter", parameter);
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		try {
			ServiceConnection serviceConnection = new ServiceConnection("/idByUsername.php");
			String serviceUrl = serviceConnection.getUrlServiceServer();
			URL url = new URL(serviceUrl);
			Log.d("Server URL", serviceUrl);
			
			// REST CALL

			// Send Request
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
				sb.append(line + "\n");
			}

			String temp = sb.toString();
			Log.d("Temp", temp);              
			response = sb.toString();
			Log.d("Response", response);
			Log.d("SB Value", sb.toString());
			reader.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		float _id = Float.parseFloat(response);
		return (int) _id;
	}
	
	protected void getTokenFromMoodleServer() throws ProtocolException, IOException {
		String moodleUrl = getUrlMoodleServer();
		
		// REST RETURNED VALUES FORMAT
		String restFormat = "xml";
		if (restFormat.equals("json"))
			restFormat = "&moodlewsrestformat" + restFormat;
		else
			restFormat = "";
		
		// REST CALL
		
		// Send Request
		String serverUrl = moodleUrl + "/login/token.php" + "?username=admin" +
				"&password=admin1str4T0R.," + "&service=moodle_mobile_app";
		HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Language", "en-US");
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setDoInput(true);
		
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
