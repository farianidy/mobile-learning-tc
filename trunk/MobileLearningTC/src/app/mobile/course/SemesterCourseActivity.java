package app.mobile.course;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.util.connection.ServiceConnection;
import app.util.gui.TwoLineListAdapter;
import app.util.gui.TwoLineListItem;
import app.util.xml.RSSFeed;
import app.util.xml.RSSHandler;
import app.util.xml.RSSItem;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListActivity;;

public class SemesterCourseActivity extends SherlockListActivity {

	String semesterId, semesterName, userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	RSSFeed rssFeed = null;
	
	private ProgressDialog progressDialog;
	private TextView feedTitle, feedDescription;
	
	getCourseTask serviceTask = null;

	public static Intent newInstance(Activity activity, String semesterId, String semesterName) {
		Intent intent = new Intent(activity, SemesterCourseActivity.class);
		intent.putExtra("semesterId", semesterId);
		intent.putExtra("semsterName", semesterName);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getExtras() != null) {
			semesterId = getIntent().getExtras().getString("semesterId");
			semesterName = getIntent().getExtras().getString("semsterName");
		}

		setContentView(R.layout.list_info);
		setTitle(semesterName);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get session info
		session = new SessionManager(this);
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// Progress dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading information...");
		progressDialog.setIndeterminate(true);
		
		serviceTask = new getCourseTask();
		serviceTask.execute();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
	
	private class getCourseTask extends AsyncTask<Void, Void, RSSFeed> {
		@Override
		protected RSSFeed doInBackground(Void... params) {			
			return getCourse();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(RSSFeed rssFeed) {			
			if (rssFeed != null) {
				feedTitle = (TextView) findViewById(R.id.feedTitle);
				feedDescription = (TextView) findViewById(R.id.feedDescription);

				feedTitle.setText(rssFeed.getTitle());
				feedDescription.setText(rssFeed.getDescription());
				
				TwoLineListAdapter adapter = new TwoLineListAdapter(getApplicationContext());
				
				List<RSSItem> rssList = rssFeed.getList();
				for (int i = 0; i < rssList.size(); i++) {
				    RSSItem item = rssList.get(i);
				    adapter.add(new TwoLineListItem(item.getTitle(), item.getDescription()));
				}
				setListAdapter(adapter);
			}

			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
		}
	}

	private RSSFeed getCourse() {
		String parameters = "semesterid=" + semesterId + "&userid=" + userid;

		try {
			serviceConnection = new ServiceConnection("/categoryCourse.php?" + parameters);
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
