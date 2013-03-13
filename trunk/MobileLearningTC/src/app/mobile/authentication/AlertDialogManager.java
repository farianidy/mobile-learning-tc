package app.mobile.authentication;

import app.mobile.learningtc.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogManager {

	public void showAlertDialog(Context context, String title, String message, Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		alertDialog.setTitle(title);
		alertDialog.setMessage(message);

		if(status != null)
			alertDialog.setIcon((status) ? R.drawable.icon_login_success : R.drawable.icon_login_fail);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int which) {
			}
		});

		alertDialog.show();
	}
}