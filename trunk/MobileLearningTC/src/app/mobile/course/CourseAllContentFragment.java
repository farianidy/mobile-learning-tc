package app.mobile.course;

import java.util.HashMap;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.util.connection.ServiceConnection;
import app.util.xml.RSSFeed;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.MenuItem;

public class CourseAllContentFragment extends SherlockListFragment {

	String courseid, courseFullname, userid, serviceUrl;
	SessionManager session;
	ServiceConnection serviceConnection;
	RSSFeed rssFeed = null;

	private ProgressDialog progressCourseAll;
//	private TextView feedCourseAllTitle, feedCourseAllDescription;

	public CourseAllContentFragment newInstance(String courseid) {
		CourseAllContentFragment fragment = new CourseAllContentFragment();

		fragment.courseid = courseid;

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get session info
		session = new SessionManager(getActivity());
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);

		// Progress dialog
		progressCourseAll = new ProgressDialog(getActivity());
		progressCourseAll.setMessage("Loading information...");
		progressCourseAll.setIndeterminate(true);

//		courseAllTask = new getCourseWeeklyTask();
//		courseAllTask.execute();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.list, container, false);
		return v;
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
}
