package app.mobile.learningtc;

import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.widget.Toast;
import app.mobile.authentication.SessionManager;

public class SplashScreenActivity extends Activity {
	
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set view
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		
		final int welcomeScreenDisplay = 1000;
		
		session = new SessionManager(getApplicationContext());

		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), 
				Toast.LENGTH_SHORT).show();

		// Thread to show splash up to splash time
		Thread welcomeThread = new Thread() {
			int wait = 0;

			@Override
			public void run() {
				try {
					super.run();
					/**
					 * use while to get the splash time. Use sleep() to increase
					 * the wait variable for every 100L.
					 */
					while (wait < welcomeScreenDisplay) {
						sleep(100);
						wait += 100;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					session.checkLogin();
					
					finish();
				}
			}
		};

		welcomeThread.start();
	}
}
