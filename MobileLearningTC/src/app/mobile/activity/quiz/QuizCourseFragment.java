package app.mobile.activity.quiz;

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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;

public class QuizCourseFragment extends SherlockListFragment {
	
	String courseid, courseFullname, userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	RSSFeed rssFeed = null;
	
	private ProgressDialog progressDialog;
	private TextView tvFeedTitle, tvFeedDescription;
	
	public QuizCourseFragment newInstance(String courseid) {
		QuizCourseFragment fragment = new QuizCourseFragment();
		fragment.courseid = courseid;

		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Get session info
		session = new SessionManager(getActivity());
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		View v = inflater.inflate(R.layout.list_info, container, false);
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		
		// Get session info
		session = new SessionManager(getActivity());
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// Progress dialog
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading information...");
		progressDialog.setIndeterminate(true);

		new getCourseQuizTask().execute();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
	
	private class getCourseQuizTask extends AsyncTask<Void, Void, RSSFeed>{
		@Override
		protected RSSFeed doInBackground(Void... params) {
			return getCourseQuiz();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(RSSFeed result) {
			if (rssFeed != null) {
				tvFeedTitle = (TextView) getView().findViewById(R.id.tvFeedTitle);
				tvFeedDescription = (TextView) getView().findViewById(R.id.tvFeedDescription);

				if (rssFeed.getLink() != null) {
					tvFeedTitle.setText(rssFeed.getTitle());
					tvFeedDescription.setText("Week " + rssFeed.getDescription());
				}
				else
					tvFeedTitle.setText(rssFeed.getTitle());

				TwoLineListAdapter adapter = new TwoLineListAdapter(getActivity());

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

	private RSSFeed getCourseQuiz() {
		String parameters = "userid=" + userid + "&courseid=" + courseid;

		try {
			serviceConnection = new ServiceConnection("/courseQuiz.php?" + parameters);
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
