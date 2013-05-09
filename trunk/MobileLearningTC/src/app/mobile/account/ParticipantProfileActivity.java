package app.mobile.account;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.util.connection.ServiceConnection;
import app.util.xml.RSSFeed;
import app.util.xml.RSSHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.actionbarsherlock.app.SherlockActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class ParticipantProfileActivity extends SherlockActivity {
	
	String participantId, participantFullname, userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	RSSFeed rssFeed = null;
	
	private ProgressDialog progressDialog;
//	private ImageView ivUser;
//	private TextView tvUsername, tvName, tvCountry, tvCity, tvEmailAddress, tvFirstAccess, tvLastAccess;
	
	public static Intent newInstance(Activity activity, String participantId, String participantFullname) {
		Intent intent = new Intent(activity, ParticipantProfileActivity.class);
		intent.putExtra("participantId", participantId);
		intent.putExtra("participantFullname", participantFullname);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent().getExtras() != null) {
			participantId = getIntent().getExtras().getString("participantId");
			participantFullname = getIntent().getExtras().getString("participantFullname");
		}
		
		setContentView(R.layout.activity_participant_profile);
		setTitle(participantFullname);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get session info
		session = new SessionManager(this);
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading information...");
		progressDialog.setIndeterminate(true);

		new getParticipantProfileTask().execute();
	}
	
	private class getParticipantProfileTask extends AsyncTask<Void, Void, RSSFeed> {
		@Override
		protected RSSFeed doInBackground(Void... params) {			
			return getParticipantProfile();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(RSSFeed rssFeed) {		
			if (rssFeed != null) {
//				ivUser = (ImageView) findViewById(R.id.ivUser);
//				tvName = (TextView) findViewById(R.id.tvName);
//				tvUsername = (TextView) findViewById(R.id.tvUsername);
//				tvCountry = (TextView) findViewById(R.id.tvCountry);
//				tvCity = (TextView) findViewById(R.id.tvCity);
//				tvEmailAddress = (TextView) findViewById(R.id.tvEmailAddress);
//				tvFirstAccess = (TextView) findViewById(R.id.tvFirstAccess);
//				tvLastAccess = (TextView)findViewById(R.id.tvLastAccess);
				
				// TODO: get result to display
//				tvName.setText(rssFeed.getItem(0).getLink());
//				tvUsername.setText(tvUsername.getText() + ": " + rssFeed.getItem(0).getTitle());
//				tvCountry.setText(tvCountry.getText() + ": ");
//				tvCity.setText(tvCity.getText() + ": ");
//				tvEmailAddress.setText(tvEmailAddress.getText() + ": ");
//				tvFirstAccess.setText(tvFirstAccess.getText() + ": ");
//				tvLastAccess.setText(tvLastAccess.getText() + ": ");
				
				// TODO: If user picture not empty
//				ivUser.setImageResource(R.drawable.user_empty);
			}

			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
		}
	}

	private RSSFeed getParticipantProfile() {
		String parameter = "participantid=" + participantId;

		try {
			serviceConnection = new ServiceConnection("/assignmentDetail.php?" + parameter);
			serviceUrl = serviceConnection.getUrlServiceServer();
			URL url = new URL(serviceUrl);
			Log.d("Server URL", serviceUrl);

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			XMLReader xmlReader = parser.getXMLReader();

			RSSHandler rssHandler = new RSSHandler();
			xmlReader.setContentHandler(rssHandler);
			xmlReader.parse(new InputSource(url.openStream()));
			rssFeed = rssHandler.getFeed();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		}
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (SAXException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return rssFeed;
	}
}
