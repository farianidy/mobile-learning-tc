package app.tool.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

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
