package app.mobile.activity.assignment;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.util.connection.ServiceConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

public class AssignmentDetailActivity extends SherlockActivity {
	
	String assignmentId, assignmentName, userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	
	private ProgressDialog progressDialog;
	private TextView tvDesc, tvAvaDate, tvDueDate, tvTurnedDate, tvStatus, 
		tvGrade, tvFeedback;
	private Button bSubmit;
	
	public static Intent newInstance(Activity activity, String assignmentId, String assignmentName) {
		Intent intent = new Intent(activity, AssignmentDetailActivity.class);
		intent.putExtra("assignmentId", assignmentId);
		intent.putExtra("assignmentName", assignmentName);
		return intent;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent().getExtras() != null) {
			assignmentId = getIntent().getExtras().getString("assignmentId");
			assignmentName = getIntent().getExtras().getString("assignmentName");
		}
		
		setContentView(R.layout.activity_assignment_detail);
		setTitle(assignmentName);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get session info
		session = new SessionManager(this);
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		
		bSubmit = (Button) findViewById(R.id.bDownload);
		bSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(AssignmentDetailActivity.this, AssignmentSubmitActivity.class);
				startActivity(i);
			}
		});
		
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading information...");
		progressDialog.setIndeterminate(true);
		
		new getAssignmentDetailTask().execute();
	}
	
	private class getAssignmentDetailTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			return getAssignmentDetailJSON();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(String content) {
			if (!content.equals(null)) {
				try {
					JSONArray jsonArray = new JSONArray(content);
					String desc = "", avaDate = "", dueDate = "", turnedDate = "", 
							status = "", grade = "", feedback = "";
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						
						String _desc = jsonObj.getString("intro");
						String _avaDate = jsonObj.getString("fromdate");
						String _dueDate = jsonObj.getString("duedate");
						String _turnedDate = jsonObj.getString("turneddate");
						String _status = "";
						String _grade = "";
						String _feedback = "";
						
						desc += _desc;
						avaDate += _avaDate;
						dueDate += _dueDate;
						turnedDate += _turnedDate;
						status += _status;
						grade += _grade;
						feedback += _feedback;
					}
					
					tvDesc = (TextView) findViewById(R.id.tvDescContent);
					tvAvaDate = (TextView) findViewById(R.id.tvTypeContent);
					tvDueDate = (TextView) findViewById(R.id.tvDateContent);
					tvTurnedDate = (TextView) findViewById(R.id.tvComponentContent);
					tvStatus = (TextView) findViewById(R.id.tvAuthorContent);
					tvGrade = (TextView) findViewById(R.id.tvLicenseContent);
					tvFeedback = (TextView) findViewById(R.id.tvFeedbackContent);
					
					tvDesc.setText(Html.fromHtml(desc));
					tvAvaDate.setText(avaDate);
					tvDueDate.setText(dueDate);
					tvTurnedDate.setText(turnedDate);
					tvStatus.setText(status);
					tvGrade.setText(grade);
					tvFeedback.setText(feedback);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
		}
	}

	private String getAssignmentDetailJSON() {
		String parameter = "assignmentid=" + assignmentId;
		String content = null, line = null;
		
		serviceConnection = new ServiceConnection("/assignmentDetail.php?" + parameter);
		serviceUrl = serviceConnection.getUrlServiceServer();
		Log.d("Server URL", serviceUrl);
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(serviceUrl);
		
		// Parameter
		ArrayList<NameValuePair> param = new ArrayList<NameValuePair>(); {
			try {
				// Add parameter
				httpPost.setEntity(new UrlEncodedFormEntity(param));
				
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				
				// Read content
				InputStream is = httpEntity.getContent();
				BufferedReader read = new BufferedReader(new InputStreamReader(is));
				
				content = "";
				line = "";
				
				while ((line = read.readLine()) != null) {
					content += line;
				}
				
				Log.d("Content", content);
			}
			catch (ClientProtocolException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return content;
	}
}
