package app.mobile.course;

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

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.tool.config.ServiceConnection;
import app.tool.xml.RSSFeed;
import app.tool.xml.RSSHandler;
import app.tool.xml.RSSItem;

import android.os.Bundle;
import android.os.StrictMode;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity extends ListActivity {

	String userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	RSSFeed rssFeed = null;

	private TextView feedCourseTitle, feedCourseDescription, feedCoursePubdate, feedCourseLink;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course);

		session = new SessionManager(this);

		Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), 
				Toast.LENGTH_LONG).show();

		session.checkLogin();

		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		String parameter = "userid=" + userid;

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

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

		if (rssFeed != null) {
			feedCourseTitle = (TextView) findViewById(R.id.feedCourseTitle);
			feedCourseDescription = (TextView) findViewById(R.id.feedCourseDescription);
			feedCoursePubdate = (TextView) findViewById(R.id.feedCoursePubdate);
			feedCourseLink = (TextView) findViewById(R.id.feedCourseLink);

			feedCourseTitle.setText(rssFeed.getTitle());
			feedCourseDescription.setText(rssFeed.getDescription());
			feedCoursePubdate.setText(rssFeed.getPubdate());
			feedCourseLink.setText(rssFeed.getLink());

			ArrayAdapter<RSSItem> rssList = new ArrayAdapter<RSSItem>(this, 
					android.R.layout.simple_list_item_1, rssFeed.getList());
			setListAdapter(rssList);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.course, menu);
		return true;
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, CourseTopicActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putString("courseTitle", rssFeed.getItem(position).getTitle());
//		bundle.putString("keyUname", rssFeed.getItem(position).getDescription());
//		bundle.putString("keyIdc", rssFeed.getItem(position).getLink());
//		bundle.putString("keyUserId", rssFeed.getItem(position).getPubdate());
//		intent.putExtras(bundle);
		startActivity(intent);
	}
}
