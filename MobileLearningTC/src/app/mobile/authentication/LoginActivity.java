package app.mobile.authentication;

import app.mobile.learningtc.AboutActivity;
import app.mobile.learningtc.DashboardActivity;
import app.mobile.learningtc.R;
import app.tool.config.ServiceConnection;
//import app.tool.xml.RSSFeed;
//import app.tool.xml.RSSHandler;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.parsers.SAXParser;
//import javax.xml.parsers.SAXParserFactory;
//
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//import org.xml.sax.XMLReader;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	String serviceUrl;
	ServiceConnection serviceConnection;
	SessionManager session;
	AlertDialogManager alert = new AlertDialogManager();

	private Button bLogin;
	private EditText etUsername, etPassword;
	private TextView tvAbout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		session = new SessionManager(getApplicationContext());

		bLogin = (Button) findViewById(R.id.bLogin);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		tvAbout = (TextView) findViewById(R.id.tvAboutLink);

		bLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();

				if (username.trim().length() > 0 && password.trim().length() > 0) {
					// LOGIN
					String reply = tryLogin(username, password);
					
					if (reply != null) {
						Log.d("AfterLoginCheck", "Here");
						Log.d("Full Name", reply);
						
						Intent dashboardPage = new Intent(LoginActivity.this, DashboardActivity.class);
						dashboardPage.putExtra("username", username);
						dashboardPage.putExtra("fullname", reply);
						startActivity(dashboardPage);
					}
					
					// LOGIN XML
//					RSSFeed loginFeed = tryLogin(username, password);
//					
//					if (loginFeed != null) {
//						String userid = loginFeed.getCategory();
//						String fullname = loginFeed.getDescription();					
//	
//						Log.d("AfterLoginXMLCheck", "Here");
//						Log.d("User Id", userid);
//						Log.d("Full Name", fullname);
//	
//						if (!(userid.equals("0")) && !(fullname.equals(""))) {
//							session.createLoginSession(userid, username);
//							
//							Intent dashboardPage = new Intent(LoginActivity.this, DashboardActivity.class);
//							dashboardPage.putExtra("fullname", fullname);
//							startActivity(dashboardPage);
//						}
//						else
//							alert.showAlertDialog(LoginActivity.this, "Login Failed", "Username or Password is incorrect", false);
//					}
					else
						alert.showAlertDialog(LoginActivity.this, "Login Failed", "Not found", false);
				}
				else
					alert.showAlertDialog(LoginActivity.this, "Login Failed", "Please enter username and password", false);
			}
		});

		tvAbout.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Intent aboutPage = new Intent(LoginActivity.this, AboutActivity.class);
				startActivity(aboutPage);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@SuppressLint("NewApi")
	protected String tryLogin(String username, String password) {
		Log.d("LoginCheck", "Here");
		
		String response = null;
		String parameters = "username=" + username + "&password=" + password;
		Log.d("Parameters", parameters);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {
			serviceConnection = new ServiceConnection("/login.php?" + parameters);
			serviceUrl = serviceConnection.getUrlServiceServer();
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
			request.writeBytes(parameters);
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
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			int duration = 10;
			Toast.makeText(this, e.toString(), duration).show();
		}
		
		return response;
	}
	
//	protected RSSFeed tryLogin(String username, String password) {
//		Log.d("LoginXMLCheck", "Here");
//
//		RSSFeed rssFeed = null;
//		String parameters = "username=" + username + "&password=" + password;
//
//		if (android.os.Build.VERSION.SDK_INT > 9) {
//			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//			StrictMode.setThreadPolicy(policy);
//		}
//
//		try {
//			serviceConnection = new ServiceConnection("/login.php?" + parameters);
//			serviceUrl = serviceConnection.getUrlServiceServer();
//			URL url = new URL(serviceUrl);
//			Log.d("Server URL", serviceUrl);
//			
//			SAXParserFactory factory = SAXParserFactory.newInstance();
//			SAXParser parser = factory.newSAXParser();
//			XMLReader xmlReader = parser.getXMLReader();
//			
//			RSSHandler rssHandler = new RSSHandler();
//			xmlReader.setContentHandler(rssHandler);
//			xmlReader.parse(new InputSource(url.openStream()));
//			rssFeed = rssHandler.getFeed();
//		}
//		catch (MalformedURLException e) {
//			e.printStackTrace();
//		}
//		catch(ParserConfigurationException e) {
//			e.printStackTrace();
//		}
//		catch (SAXException e) {
//			e.printStackTrace();
//		}
//		catch(IOException e) {
//			int duration = 10;
//			Toast.makeText(this, e.toString(), duration).show();
//		}
//		
//		if (rssFeed != null)
//			return rssFeed;
//		else
//			return null;
//	}
	
	
}
