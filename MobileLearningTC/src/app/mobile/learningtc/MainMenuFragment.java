package app.mobile.learningtc;

import app.mobile.account.UserProfileFragment;
import app.mobile.blog.BlogFragment;
import app.mobile.calendar.CalendarFragment;
import app.mobile.course.CourseFragment;
import app.mobile.course.SemesterFragment;
import app.mobile.messaging.MessagingFragment;
import app.mobile.privatefile.PrivateFileFragment;
import app.util.gui.IconListAdapter;
import app.util.gui.IconListItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MainMenuFragment extends ListFragment {

	private String[] menuItems = { 
			"Home", 
			"Course", 
			"Semester", 
			"Profile", 
			"Calendar", 
			"Blogs", 
			"Forum Posts", 
			"Private File", 
			"Messaging"
	};

	private int[] menuIcons = { 
			android.R.drawable.ic_menu_gallery,
			android.R.drawable.ic_menu_today,
			android.R.drawable.ic_menu_manage,
			android.R.drawable.ic_menu_info_details,
			android.R.drawable.ic_menu_my_calendar,
			android.R.drawable.ic_menu_agenda,
			android.R.drawable.ic_menu_month,
			android.R.drawable.ic_menu_add,
			android.R.drawable.ic_menu_send };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		IconListAdapter adapter = new IconListAdapter(getActivity());

		for(int i = 0; i < menuItems.length; i++) {
			adapter.add(new IconListItem(menuItems[i], menuIcons[i]));
		}
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Fragment newContent = null;

		switch (position) {
		case 0:
			newContent = new HomeFragment();
			break;
		case 1:
			newContent = new CourseFragment();
			break;
		case 2:
			newContent = new SemesterFragment();
			break;
		case 3:
			newContent = new UserProfileFragment();
			break;
		case 4:
			newContent = new CalendarFragment();
			break;
		case 5:
			newContent = new BlogFragment();
			break;
		case 6:
			//newContent = new
			break;
		case 7:
			newContent = new PrivateFileFragment();
			break;
		case 8:
			newContent = new MessagingFragment();
			break;
		}

		if (newContent != null)
			switchFragment(newContent);
	}	

	// Meet of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof MainActivity) {
			MainActivity ma = (MainActivity) getActivity();
			ma.switchContent(fragment);
		}
	}
}
