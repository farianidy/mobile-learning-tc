package app.mobile.course;

import app.mobile.learningtc.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class CourseTopicActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_topic);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course_topic, menu);
		return true;
	}

}
