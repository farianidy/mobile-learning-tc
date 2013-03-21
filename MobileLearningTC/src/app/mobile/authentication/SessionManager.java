package app.mobile.authentication;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import app.mobile.learningtc.IntroActivity;

public class SessionManager {
	
	SharedPreferences sharedPref;
	Editor editor;
	Context context;
	
	int PRIVATE_MODE = 0;
	
	private static final String SHARED_PREF_NAME = "MobileLearningTCSharedPreference";
	private static final String IS_LOGIN = "IsLoggedIn";
	
	public static final String KEY_USERID = "userid";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_FULLNAME = "fullname";
	
	public SessionManager(Context _context){
		this.context = _context;
		sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, PRIVATE_MODE);
		editor = sharedPref.edit();
		editor.commit();
	}
	
	public void createLoginSession(String userid, String username, String fullname) {
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_USERID, userid);
		editor.putString(KEY_USERNAME, username);
		editor.putString(KEY_FULLNAME, fullname);
		editor.commit();
	}	
	
	public void checkLogin() {
		if (!this.isLoggedIn()) {
			Intent i = new Intent(context, IntroActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}
	
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_USERID, sharedPref.getString(KEY_USERID, null));
		user.put(KEY_USERNAME, sharedPref.getString(KEY_USERNAME, null));
		user.put(KEY_FULLNAME, sharedPref.getString(KEY_FULLNAME, null));
		
		return user;
	}
	
	public void logoutUser() {
		editor.remove(KEY_USERID);
		editor.remove(KEY_USERNAME);
		editor.remove(KEY_FULLNAME);
		editor.clear();
		editor.commit();
		
		Intent i = new Intent(context, IntroActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
	
	public boolean isLoggedIn() {
		return sharedPref.getBoolean(IS_LOGIN, false);
	}
}
