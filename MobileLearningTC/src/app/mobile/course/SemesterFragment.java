package app.mobile.course;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.MainActivity;
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
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;

public class SemesterFragment extends SherlockListFragment {
	
	String userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	RSSFeed rssFeed = null;

	private ProgressDialog progressDialog;
	private TextView tvFeedTitle, tvFeedDescription;

	getCategoryTask serviceTask = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set view
		getSherlockActivity().getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		getSherlockActivity().getSupportActionBar().setTitle(R.string.title_fragment_semester);
		
		// Get session info
		session = new SessionManager(getActivity());
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// Progress dialog
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage("Loading information...");
		progressDialog.setIndeterminate(true);

		// Get content from web service
		serviceTask = new getCategoryTask();
		serviceTask.execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list_info, container, false);
		return v;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (getActivity() == null)
			return;
		
		MainActivity activity = (MainActivity) getActivity();
		activity.onSemesterPressed(rssFeed.getItem(position).getLink(), rssFeed.getItem(position).getTitle());
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
				tvFeedTitle = (TextView) getView().findViewById(R.id.tvFeedTitle);
				tvFeedDescription = (TextView) getView().findViewById(R.id.tvFeedDescription);

				tvFeedTitle.setText(result.getTitle());
				tvFeedDescription.setText(result.getDescription());

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

	private RSSFeed getCategory() {
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
