package app.mobile.course;

import app.mobile.learningtc.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CourseWeeklyDetailActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_weekly_detail);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course_weekly_detail, menu);
		return true;
	}

}
