package app.mobile.authentication;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
	
	SharedPreferences pref;
	Editor editor;
	Context _context;
	
	int PRIVATE_MODE = 0;		// Shared preference mode
	
	private static final String PREF_NAME = "MobileLearningTCPref";	// Shared preference file name
	private static final String IS_LOGIN = "IsLoggedIn";			// All Shared Preferences Keys
	
	// Make variable public to access from outside
	public static final String KEY_USERID = "userid";
	public static final String KEY_USERNAME = "username";
	
	public SessionManager(Context context){
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
		editor.commit();
	}
	
	public void createLoginSession(String userid, String username) {
		editor.putBoolean(IS_LOGIN, true);
		editor.putString(KEY_USERID, userid);
		editor.putString(KEY_USERNAME, username);
		editor.commit();
	}	
	
	/**
	 * Check login method will check user login status
	 * If false it will redirect user to login page
	 * Else won't do anything
	 * */
	public void checkLogin() {
		if (!this.isLoggedIn()) {
			// user is not logged in redirect him to Login Activity
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);		// Closing all the Activities
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);		// Add new Flag to start new Activity
			_context.startActivity(i);						// Starting Login Activity
		}
		
	}
	
	// Get stored session data
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_USERID, pref.getString(KEY_USERID, null));
		user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
		
		return user;
	}
	
	// Clear session details
	public void logoutUser() {
		// Clearing all data from Shared Preferences
		editor.clear();
		editor.commit();
		
		// After logout redirect user to Login Activity
		Intent i = new Intent(_context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	// Closing all the Activities
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);	// Add new Flag to start new Activity
		_context.startActivity(i);					// Starting Login Activity
	}
	
	// Quick check for login
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
