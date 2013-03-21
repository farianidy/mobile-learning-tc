package app.mobile.calendar;

import java.util.HashMap;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CalendarActivity extends Activity {
	
	String username;
	SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		session = new SessionManager(this);
		
		HashMap<String, String> user = session.getUserDetails();
		username = user.get(SessionManager.KEY_USERNAME);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar, menu);
		return true;
	}
}
