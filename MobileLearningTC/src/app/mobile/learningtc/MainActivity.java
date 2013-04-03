package app.mobile.learningtc;

import app.mobile.authentication.SessionManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;

public class MainActivity extends SlidingFragmentActivity {

	String userid, username, fullname;
	SessionManager session;

	private Fragment content;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set view
		setContentView(R.layout.activity_main);

		// Get session info
		session = new SessionManager(getApplicationContext());
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		username = user.get(SessionManager.KEY_USERNAME);
		fullname = user.get(SessionManager.KEY_FULLNAME);

		// Set content frame contains menu frame
		setBehindContentView(R.layout.menu_frame);
		getSlidingMenu().setSlidingEnabled(true);
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Set above view fragment
		if (savedInstanceState != null)
			content = getSupportFragmentManager().getFragment(savedInstanceState, "content");

		if (content == null)
			content = new HomeFragment();

		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, 
				content).commit();

		// Set behind view fragment
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, 
				new MainMenuFragment()).commit();

		// Customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindScrollScale(0.25f);
		sm.setFadeDegree(0.25f);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "content", content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_session, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_logout:
				session.logoutUser();
				return true;
			case R.id.action_settings:
				Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void switchContent(final Fragment fragment) {
		content = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);
	}
}