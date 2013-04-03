package app.mobile.learningtc;

import java.util.HashMap;

import app.mobile.authentication.SessionManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends Activity {
	
	String userid, username, fullname;
	SessionManager session;
	
	private TextView tvUserProfile;
	private ImageButton ibCourse, ibCalendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		session = new SessionManager(getApplicationContext());
		
		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), 
				Toast.LENGTH_SHORT).show();
		
		session.checkLogin();
		
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		username = user.get(SessionManager.KEY_USERNAME);
		fullname = user.get(SessionManager.KEY_FULLNAME);
        
        tvUserProfile = (TextView) findViewById(R.id.tvUserProfile);
        ibCourse = (ImageButton) findViewById(R.id.ibCourse);
        ibCalendar = (ImageButton) findViewById(R.id.ibCalendar);
        
        Resources res = getResources();
        String textUser = String.format(res.getString(R.string.link_user_profile), fullname, username);
        CharSequence styledTextUser = Html.fromHtml(textUser);
        tvUserProfile.setText(styledTextUser);
        
        tvUserProfile.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			}
		});
        
        ibCourse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			}
		});
        
        ibCalendar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_session, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_logout:
				session.logoutUser();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
