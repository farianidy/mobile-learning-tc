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
	
	int PRIVATE_MODE = 0;
	
	private static final String PREF_NAME = "MobileLearningTCPref";
	private static final String IS_LOGIN = "IsLoggedIn";
	
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
	
	public void checkLogin() {
		if (!this.isLoggedIn()) {
			Intent i = new Intent(_context, LoginActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			_context.startActivity(i);
		}
	}
	
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		user.put(KEY_USERID, pref.getString(KEY_USERID, null));
		user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));
		
		return user;
	}
	
	public void logoutUser() {
		editor.clear();
		editor.commit();
		
		Intent i = new Intent(_context, LoginActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(i);
	}
	
	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGIN, false);
	}
}
