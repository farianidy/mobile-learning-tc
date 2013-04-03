package app.mobile.course;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.util.connection.ServiceConnection;
import app.util.xml.RSSFeed;
import app.util.xml.RSSHandler;
import app.util.xml.RSSItem;

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

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;

public class SemesterFragment extends SherlockListFragment {

	private String[] menuItems = {
			"Course",
			"Semester"
	};

	String userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;

	private ProgressDialog progressCategory;
	private TextView feedCategoryTitle, feedCategoryDescription;

	getCategoryTask courseTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set view
		getSherlockActivity().getSupportActionBar().setTitle(null);
		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_dropdown_item_1line, menuItems);
		SpinnerAdapter spinnerAdapter = adapter;
		OnNavigationListener listener = new OnNavigationListener() {

			@Override
			public boolean onNavigationItemSelected(int itemPosition, long itemId) {
				switch (itemPosition) {
				case 1:
					break;
				case 2:
					break;
				}
				return true;
			}
		};
		getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(spinnerAdapter, listener);

		// Get session info
		session = new SessionManager(getActivity());
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// Progress dialog
		progressCategory = new ProgressDialog(getActivity());
		progressCategory.setMessage("Loading information...");
		progressCategory.setIndeterminate(true);

		// Get content from web service
		courseTask = new getCategoryTask();
		courseTask.execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_semester, container, false);
		return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//		Intent intent = new Intent(this, CourseTopicActivity.class);
		//		Bundle bundle = new Bundle();
		//		bundle.putString("courseId", rssFeed.getItem(position).getLink());
		//		bundle.putString("courseName", rssFeed.getItem(position).getTitle());
		//		intent.putExtras(bundle);
		//		startActivity(intent);
	}

	private class getCategoryTask extends AsyncTask<Void, Void, RSSFeed> {
		@Override
		protected RSSFeed doInBackground(Void... params) {
			return getCategory();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressCategory.show();
		}

		@Override
		protected void onPostExecute(RSSFeed result) {
			if (result != null) {
				feedCategoryTitle = (TextView) getView().findViewById(R.id.feedCategoryTitle);
				feedCategoryDescription = (TextView) getView().findViewById(R.id.feedCategoryDescription);

				feedCategoryTitle.setText(result.getTitle());
				feedCategoryDescription.setText(result.getDescription());

				ArrayAdapter<RSSItem> rssList = new ArrayAdapter<RSSItem>(getActivity(), 
						android.R.layout.simple_list_item_1, result.getList());
				setListAdapter(rssList);
			}

			progressCategory.dismiss();
		}

		@Override
		protected void onCancelled() {
			progressCategory.dismiss();
		}
	}

	private RSSFeed getCategory() {
		RSSFeed rssFeed = null;

		try {
			serviceConnection = new ServiceConnection("/category.php");
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
