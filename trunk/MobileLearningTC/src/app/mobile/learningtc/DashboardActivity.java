package app.mobile.learningtc;

import java.util.HashMap;

import app.mobile.account.UserProfileActivity;
import app.mobile.authentication.AlertDialogManager;
import app.mobile.authentication.SessionManager;
import app.mobile.calendar.CalendarActivity;
import app.mobile.course.CourseActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends Activity {
	
	String fullname, userid, username;
	SessionManager session;
	AlertDialogManager alert = new AlertDialogManager();
	
	private TextView tvLogout, tvUserProfile;
	private ImageButton ibCourse, ibCalendar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		session = new SessionManager(this);
		
		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), 
				Toast.LENGTH_LONG).show();
		
		session.checkLogin();
		
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		username = user.get(SessionManager.KEY_USERNAME);
		
		Intent param = getIntent();
//		username = param.getStringExtra("username");
		fullname = param.getStringExtra("fullname");
        
        tvLogout = (TextView) findViewById(R.id.tvLogout);
        tvUserProfile = (TextView) findViewById(R.id.tvUserProfile);
        ibCourse = (ImageButton) findViewById(R.id.ibCourse);
        ibCalendar = (ImageButton) findViewById(R.id.ibCalendar);
        
        tvLogout.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				session.logoutUser();
			}
		});
        
        Resources res = getResources();
        String textUser = String.format(res.getString(R.string.link_user_profile), fullname, username);
        CharSequence styledTextUser = Html.fromHtml(textUser);
        tvUserProfile.setText(styledTextUser);
        
        tvUserProfile.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent userProfilePage = new Intent(DashboardActivity.this, UserProfileActivity.class);
				userProfilePage.putExtra("username", username);
				startActivity(userProfilePage);
			}
		});
        
        ibCourse.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent coursePage = new Intent(DashboardActivity.this, CourseActivity.class);
				coursePage.putExtra("username", username);
				startActivity(coursePage);
			}
		});
        
        ibCalendar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent calendarPage = new Intent(DashboardActivity.this, CalendarActivity.class);
				calendarPage.putExtra("username", username);
				startActivity(calendarPage);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dashboard, menu);
		return true;
	}
}
