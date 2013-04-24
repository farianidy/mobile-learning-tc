package app.mobile.course;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class ZCourseContentActivity extends SherlockFragmentActivity implements ActionBar.OnNavigationListener {

	TabsAdapter tabsAdapter;
	
	String[] locations;
	String courseid, courseFullname, userid;
	SessionManager session;
	
	private TabHost tabHost;
	private ViewPager viewPager;
	
	public static Intent newInstance(Activity activity, String courseId, String courseFullname) {
		Intent intent = new Intent(activity, ZCourseContentActivity.class);
		intent.putExtra("courseId", courseId);
		intent.putExtra("courseFullname", courseFullname);
		return intent;
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null) {
			courseid = getIntent().getExtras().getString("courseId");
			courseFullname = getIntent().getExtras().getString("courseFullname");
		}
        
        setContentView(R.layout.fragment_tabs_pager);
        setTitle(courseFullname);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get session info
		session = new SessionManager(this);
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
        
		// Set tab
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        tabHost.setup();

        viewPager = (ViewPager)findViewById(R.id.pager);

        tabsAdapter = new TabsAdapter(this, tabHost, viewPager);
        
//        tabsAdapter.addTab(tabHost.newTabSpec("all").setIndicator("All"),
//                CourseAllContentFragment.AllContentLoaderListFragment.class, null);
//        tabsAdapter.addTab(tabHost.newTabSpec("weekly").setIndicator("Weekly"),
//        		ZCourseWeeklyFragment.CourseWeeklyLoaderListFragment.class, null);
//        tabsAdapter.addTab(tabHost.newTabSpec("assignments").setIndicator("Assignments"),
//                AssignmentCourseFragment.AssignmentLoaderListFragment.class, null);
//        tabsAdapter.addTab(tabHost.newTabSpec("resources").setIndicator("Resources"),
//                ResourceCourseFragment.ResourceLoaderListFragment.class, null);
//        tabsAdapter.addTab(tabHost.newTabSpec("newsForum").setIndicator("News Forum"),
//                ForumCourseFragment.NewsForumLoaderListFragment.class, null);
//        tabsAdapter.addTab(tabHost.newTabSpec("participants").setIndicator("Participants"),
//        		ParticipantCourseFragment.ParticipantsLoaderListFragment.class, null);

        if (savedInstanceState != null) {
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tab", tabHost.getCurrentTabTag());
    }

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	public static class TabsAdapter extends FragmentPagerAdapter implements 
		TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
		
		private final Context mContext;
		private final TabHost mTabHost;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			@SuppressWarnings("unused")
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		static class DummyTabFactory implements TabHost.TabContentFactory {
			private final Context mContext;

			public DummyTabFactory(Context context) {
				mContext = context;
			}

			@Override
			public View createTabContent(String tag) {
				View v = new View(mContext);
				v.setMinimumWidth(0);
				v.setMinimumHeight(0);
				return v;
			}
		}

		public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mTabHost = tabHost;
			mViewPager = pager;
			mTabHost.setOnTabChangedListener(this);
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args) {
			tabSpec.setContent(new DummyTabFactory(mContext));
			String tag = tabSpec.getTag();

			TabInfo info = new TabInfo(tag, clss, args);
			mTabs.add(info);
			mTabHost.addTab(tabSpec);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(), info.args);
		}

		@Override
		public void onTabChanged(String tabId) {
			int position = mTabHost.getCurrentTab();
			mViewPager.setCurrentItem(position);
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			// Unfortunately when TabHost changes the current tab, it kindly
			// also takes care of putting focus on it when not in touch mode.
			// The jerk.
			// This hack tries to prevent this from pulling focus out of our
			// ViewPager.
			TabWidget widget = mTabHost.getTabWidget();
			int oldFocusability = widget.getDescendantFocusability();
			widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
			mTabHost.setCurrentTab(position);
			widget.setDescendantFocusability(oldFocusability);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}
}
