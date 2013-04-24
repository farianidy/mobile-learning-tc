/* Un-Use */

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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockListFragment;

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

public class ZCourseSemesterFragment extends SherlockListFragment {

	private String[] menuItems = {
			"Course",
			"Semester"
	};

	String userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;

	private ProgressDialog progressDialog;
	private TextView feedTitle, feedDescription;

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
						// Get content course from web service
						getCourseTask courseTask = new getCourseTask();
						courseTask.execute();
						break;
					case 2:
						// Get content category from web service
						getCategoryTask categoryTask = new getCategoryTask();
						categoryTask.execute();
						break;
				}
				return true;
			}
		};
		getSherlockActivity().getSupportActionBar().setListNavigationCallbacks(spinnerAdapter, listener);
		
		// Progress dialog
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading information...");
		progressDialog.setIndeterminate(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list_info, container, false);
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

	private class getCourseTask extends AsyncTask<Void, Void, RSSFeed> {
		@Override
		protected RSSFeed doInBackground(Void... params) {
			// Get session info
			session = new SessionManager(getActivity());
			HashMap<String, String> user = session.getUserDetails();
			userid = user.get(SessionManager.KEY_USERID);
			
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
				feedTitle = (TextView) getView().findViewById(R.id.feedTitle);
				feedDescription = (TextView) getView().findViewById(R.id.feedDescription);

				feedTitle.setText(rssFeed.getTitle());
				feedDescription.setText(rssFeed.getDescription());

				ArrayAdapter<RSSItem> rssList = new ArrayAdapter<RSSItem>(getActivity(), 
						android.R.layout.simple_list_item_1, rssFeed.getList());
				setListAdapter(rssList);
			}

			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
		}
	}
	
	private class getCategoryTask extends AsyncTask<Void, Void, RSSFeed> {
		@Override
		protected RSSFeed doInBackground(Void... params) {
			return getCategory();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(RSSFeed result) {
			if (result != null) {
				feedTitle = (TextView) getView().findViewById(R.id.feedTitle);
				feedDescription = (TextView) getView().findViewById(R.id.feedDescription);

				feedTitle.setText(result.getTitle());
				feedDescription.setText(result.getDescription());

				ArrayAdapter<RSSItem> rssList = new ArrayAdapter<RSSItem>(getActivity(), 
						android.R.layout.simple_list_item_1, result.getList());
				setListAdapter(rssList);
			}

			progressDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			progressDialog.dismiss();
		}
	}

	private RSSFeed getCourse() {
		RSSFeed rssFeed = null;
		String parameter = "userid=" + userid;

		try {
			serviceConnection = new ServiceConnection("/courseByUserid.php?" + parameter);
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
