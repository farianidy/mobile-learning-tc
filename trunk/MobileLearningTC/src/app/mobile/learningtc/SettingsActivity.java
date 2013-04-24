package app.mobile.learningtc;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.Bundle;

public class SettingsActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
