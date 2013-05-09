package app.mobile.course;

import app.mobile.account.ParticipantCourseFragment;
import app.mobile.activity.assignment.AssignmentCourseFragment;
import app.mobile.activity.assignment.AssignmentDetailActivity;
import app.mobile.activity.forum.ForumCourseFragment;
import app.mobile.activity.quiz.QuizCourseFragment;
import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.mobile.resource.ResourceCourseFragment;
import app.mobile.resource.ResourceDetailActivity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitlePageIndicator.IndicatorStyle;

public class CourseContentActivity extends SherlockFragmentActivity {

	CourseContentFragmentAdapter tabsAdapter;
	PageIndicator pageIndicator;
	
	static String courseid;
	String courseFullname, userid;
	SessionManager session;
	
	private ViewPager viewPager;
	
	public static Intent newInstance(Activity activity, String courseId, String courseFullname) {
		Intent intent = new Intent(activity, CourseContentActivity.class);
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
        
        setContentView(R.layout.simple_titles);
        setTitle(courseFullname);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		// Get session info
		session = new SessionManager(this);
		HashMap<String, String> user = session.getUserDetails();
		userid = user.get(SessionManager.KEY_USERID);
		
		tabsAdapter = new CourseContentFragmentAdapter(getSupportFragmentManager());

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(tabsAdapter);
        
        TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        indicator.setFooterIndicatorStyle(IndicatorStyle.Triangle);
        indicator.setCurrentItem(0);
        indicator.setFooterColor(Color.parseColor("#FF7400"));
        indicator.setTextColor(0xFF000000);
        indicator.setSelectedColor(0xFF000000);
        indicator.setSelectedBold(true);
        
        pageIndicator = indicator;
    }
    
    private static class CourseContentFragmentAdapter extends FragmentPagerAdapter {
    	static String[] CONTENT = new String[] {
    		"Weekly", "Assignments", "Resources", "News Forum", "Quiz", "Participants" };
    	int count = CONTENT.length;
    	
    	public CourseContentFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment newContent = new Fragment();
			
			switch(position) {
			case 0:
				newContent = new CourseWeeklyFragment().newInstance(courseid);
				break;
			case 1:
				newContent = new AssignmentCourseFragment().newInstance(courseid);
				break;
			case 2:
				newContent = new ResourceCourseFragment().newInstance(courseid);
				break;
			case 3:
				newContent = new ForumCourseFragment().newInstance(courseid);
				break;
			case 4:
				newContent = new QuizCourseFragment().newInstance(courseid);
				break;
			case 5:
				newContent = new ParticipantCourseFragment().newInstance(courseid); 
				break;
			}
			
			return newContent;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return CourseContentFragmentAdapter.CONTENT[position % CONTENT.length];
		}
    }
    
    public void onAssignmentPressed(String assignmentId, String assignmentName) {
		Intent intent =  AssignmentDetailActivity.newInstance(this, assignmentId, assignmentName);
		startActivity(intent);
	}
    
    public void onResourcePressed(String cmId, String resId, String resName) {
		Intent intent =  ResourceDetailActivity.newInstance(this, cmId, resId, resName);
		startActivity(intent);
	}
}
