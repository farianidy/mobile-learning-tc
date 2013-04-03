package app.mobile.learningtc;

import app.mobile.account.UserProfileFragment;
import app.mobile.blog.BlogFragment;
import app.mobile.calendar.CalendarFragment;
import app.mobile.course.CourseFragment;
import app.mobile.messaging.MessagingFragment;
import app.mobile.privatefile.PrivateFileFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainMenuFragment extends ListFragment {

	private String[] menuItems = { 
			"Home", 
			"Course", 
			"Calendar", 
			"Profile",
			"Blog", 
			"Private File", 
			"Messaging", 
			"Settings"
	};

	private int[] menuIcons = { 
			android.R.drawable.ic_menu_gallery,
			android.R.drawable.ic_menu_today,
			android.R.drawable.ic_menu_my_calendar,
			android.R.drawable.ic_menu_info_details,
			android.R.drawable.ic_menu_agenda,
			android.R.drawable.ic_menu_add,
			android.R.drawable.ic_menu_send,
			android.R.drawable.ic_menu_preferences };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SampleAdapter adapter = new SampleAdapter(getActivity());

		for(int i = 0; i < menuItems.length; i++) {
			adapter.add(new SampleItem(menuItems[i], menuIcons[i]));
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
			newContent = new CalendarFragment();
			break;
		case 3:
			newContent = new UserProfileFragment();
			break;
		case 4:
			newContent = new BlogFragment();
			break;
		case 5:
			newContent = new PrivateFileFragment();
			break;
		case 6:
			newContent = new MessagingFragment();
			break;
		}

		if (newContent != null)
			switchFragment(newContent);
	}

	private class SampleItem {
		public String tag;
		public int iconRes;

		public SampleItem(String tag, int iconRes) {
			this.tag = tag; 
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {
		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);

			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);

			return convertView;
		}
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
