package app.mobile.course;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

public class ZCourseWeeklyFragment extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		FragmentManager fm = getSupportFragmentManager();

		// Create the list fragment and add it as our sole content.
		if (fm.findFragmentById(android.R.id.content) == null) {
			CourseWeeklyLoaderListFragment list = new CourseWeeklyLoaderListFragment();
			fm.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	public static class CourseWeeklyLoaderListFragment extends SherlockListFragment {

		@Override public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			setEmptyText("No week is arranged.");

			// We have a menu item to show in action bar.
			setHasOptionsMenu(true);
			
			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader.  Either re-connect with an existing one,
			// or start a new one.
			//getLoaderManager().initLoader(0, null, this);
		}

		@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//			MenuItem item = menu.add("Search");
//			item.setIcon(android.R.drawable.ic_menu_search);
//			item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//			SherlockFragmentActivity activity = (SherlockFragmentActivity)getActivity();
//			View searchView = SearchViewCompat.newSearchView(activity.getSupportActionBar().getThemedContext());
//			if (searchView != null) {
//				SearchViewCompat.setOnQueryTextListener(searchView,
//						new OnQueryTextListenerCompat() {
//					@Override
//					public boolean onQueryTextChange(String newText) {
//						// Called when the action bar search text has changed.  Update
//						// the search filter, and restart the loader to do a new query
//						// with this filter.
//						currFilter = !TextUtils.isEmpty(newText) ? newText : null;
//						getLoaderManager().restartLoader(0, null, CourseWeeklyLoaderListFragment.this);
//						return true;
//					}
//				});
//				item.setActionView(searchView);
//			}
		}

		@Override public void onListItemClick(ListView l, View v, int position, long id) {
			// Insert desired behavior here.
			//Log.i("FragmentComplexList", "Item clicked: " + id);
		}
		
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//			adapter.swapCursor(data);

			if (isResumed())
				setListShown(true);
			else
				setListShownNoAnimation(true);
		}

		public void onLoaderReset(Loader<Cursor> loader) {
//			adapter.swapCursor(null);
		}
	}
}
