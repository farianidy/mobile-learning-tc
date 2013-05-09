package app.mobile.activity.assignment;

import app.mobile.learningtc.R;
import app.util.upload.UploadActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class AssignmentSubmitActivity extends Activity {
	
	private Button bSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assignment_submit);
		
		bSubmit = (Button) findViewById(R.id.bDownload);
		bSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(AssignmentSubmitActivity.this, UploadActivity.class);
				startActivity(i);
				//finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assignment_submit, menu);
		return true;
	}

}
