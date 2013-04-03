package app.mobile.authentication;

import app.mobile.learningtc.MainActivity;
import app.mobile.learningtc.R;
import app.util.connection.ServiceConnection;
import app.util.xml.RSSFeed;
import app.util.xml.RSSHandler;
import app.util.xml.RSSItem;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	String passwordSaltMain = ">=6W{18=sVhepsu%f+QcE0q}4ep";
	
	String username, password, passwordHash, serviceUrl;
	ServiceConnection serviceConnection;
	SessionManager session;
	
	userLoginTask authTask = null;

	private Button bLogin;
	private EditText etUsername, etPassword;
	private TextView tvLoginStatusMessage, tvForgetPassword;
	private View vLoginForm, vLoginStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		session = new SessionManager(getApplicationContext());
		
		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), 
				Toast.LENGTH_SHORT).show();
		
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);		
		bLogin = (Button) findViewById(R.id.bLogin);
		vLoginForm = (View) findViewById(R.id.login_form);
		vLoginStatus = (View) findViewById(R.id.login_status);
		tvLoginStatusMessage = (TextView) findViewById(R.id.login_status_message);
		tvForgetPassword = (TextView) findViewById(R.id.tvForgetPassword);
		
		tvForgetPassword.setMovementMethod(LinkMovementMethod.getInstance());
		
		etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == R.id.login || actionId == EditorInfo.IME_NULL) {
					attemptLogin();
					return true;
				}
				return false;
			}
		});

		bLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				attemptLogin();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_no_session, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_about:
				showDialog(0);
				break;
			case R.id.action_credits:
				showDialog(1);
				break;
		}
		
		return true;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog = new Dialog(this);
		
		switch (id) {
			case 0:
				dialog.setContentView(R.layout.dialog_about);
				dialog.setTitle("About");
				break;
			case 1:
				dialog.setContentView(R.layout.dialog_credits);
				dialog.setTitle("Credits");
				break;
		}
		
		return dialog;
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			moveTaskToBack(true);
//			return true;
//		}
//		
//		return super.onKeyDown(keyCode, event);
//	}
	
	public void attemptLogin() {
		if (authTask != null)
			return;
		
		etUsername.setError(null);
		etPassword.setError(null);
		
		username = etUsername.getText().toString();
		password = etPassword.getText().toString();
		
		boolean cancel = false;
		View focusView = null;
		
		if (TextUtils.isEmpty(password)) {
			etPassword.setError(getString(R.string.error_field_required));
			focusView = etPassword;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(username)) {
			etUsername.setError(getString(R.string.error_field_required));
			focusView = etUsername;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		}
		else {
			tvLoginStatusMessage.setText(R.string.login_progress_logging_in);
			showProgress(true);
			authTask = new userLoginTask();
			authTask.execute((Void) null);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			vLoginStatus.setVisibility(View.VISIBLE);
			vLoginStatus.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							vLoginStatus.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			vLoginForm.setVisibility(View.VISIBLE);
			vLoginForm.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							vLoginForm.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		}
		else {
			vLoginStatus.setVisibility(show ? View.VISIBLE : View.GONE);
			vLoginForm.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	private class userLoginTask extends AsyncTask<Void, Void, RSSItem> {
		@Override
		protected RSSItem doInBackground(Void... params) {
			Log.d("LoginXMLCheck", "Here");

			RSSFeed rssFeed = null;
			String parameter = "username=" + username;
			
			try {
				Thread.sleep(2000);
				serviceConnection = new ServiceConnection("/loginXML.php?" + parameter);
				serviceUrl = serviceConnection.getUrlServiceServer();
				URL url = new URL(serviceUrl);
				Log.d("Server URL", serviceUrl);
				
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser parser = factory.newSAXParser();
				XMLReader xmlReader = parser.getXMLReader();
				
				RSSHandler rssHandler = new RSSHandler();
				xmlReader.setContentHandler(rssHandler);
				xmlReader.parse(new InputSource(url.openStream()));
				rssFeed = rssHandler.getFeed();
				
				return rssFeed.getItem(0);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch(ParserConfigurationException e) {
				e.printStackTrace();
			}
			catch (SAXException e) {
				e.printStackTrace();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		protected void onPostExecute(RSSItem userDetail) {
			authTask = null;
			showProgress(false);
			
			String userid = userDetail.getTitle();
			String fullname = userDetail.getLink();					

			Log.d("AfterLoginXMLCheck", "Here");
			Log.d("User Id", userid);
			Log.d("Password", userDetail.getPubdate());
			Log.d("Full Name", fullname);
			
			passwordHash = md5(password + passwordSaltMain);
			
			if (userid.equals("0")) {
				etUsername.setError(getString(R.string.error_incorrect_username));
				etUsername.requestFocus();
			}
			else {
				if (userDetail.getPubdate().equals(passwordHash)) {
					session.createLoginSession(userid, username, fullname);
					
					Intent i = new Intent(getApplicationContext(), MainActivity.class);
					startActivity(i);
					finish();
				}
				else {
					etPassword.setError(getString(R.string.error_incorrect_password));
					etPassword.requestFocus();
				}
			}
		}

		@Override
		protected void onCancelled() {
			authTask = null;
			showProgress(false);
		}
	}
	
	public static String md5(String input) {
	    String result = input;
	    
	    if (input == null)
	    	return null;
	    
	    try {
	        MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
	        md.update(input.getBytes());
	        BigInteger hash = new BigInteger(1, md.digest());
	        result = hash.toString(16);
	        
	        while (result.length() < 32) { //40 for SHA-1
	            result = "0" + result;
	        }
	    }
	    catch (NoSuchAlgorithmException e) {
	    	e.printStackTrace();
	    }
	    
	    return result;
	}
}
