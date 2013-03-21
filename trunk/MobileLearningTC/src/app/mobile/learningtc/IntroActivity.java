package app.mobile.learningtc;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import app.mobile.authentication.LoginActivity;

public class IntroActivity extends Activity {

	private Button bRegister, bLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_intro);

		ViewPager viewPager = (ViewPager) findViewById(R.id.vpIntro);
		ImagePageAdapter adapter = new ImagePageAdapter();
		viewPager.setAdapter(adapter);

		bRegister = (Button) findViewById(R.id.bRegister);
		bLogin = (Button) findViewById(R.id.bLogin);

		bRegister.setMovementMethod(LinkMovementMethod.getInstance());

		bLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(IntroActivity.this, LoginActivity.class);
				startActivity(i);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_no_session, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_about:
			showDialog(0);
			break;
		case R.id.action_credits:
			showDialog(1);
			break;
		}

		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		final Dialog dialog = new Dialog(this);

		switch (id) {
		case 0:
			dialog.setContentView(R.layout.view_about);
			dialog.setTitle("About");
			break;
		case 1:
			dialog.setContentView(R.layout.view_credits);
			dialog.setTitle("Credits");
			break;
		}

		return dialog;
	}

//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			moveTaskToBack(true);
//			return true;
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}

	private class ImagePageAdapter extends PagerAdapter {
		private int[] images = new int[] {
				R.drawable.tes_swipe_1,
				R.drawable.tes_swipe_2,
				R.drawable.tes_swipe_3
		};

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((ImageView) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Context context = IntroActivity.this;
			ImageView imageView = new ImageView(context);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setImageResource(images[position]);
			((ViewPager) container).addView(imageView, 0);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((ImageView) object);
		}
	}
}
