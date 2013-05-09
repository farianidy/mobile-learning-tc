package app.util.upload;

import app.mobile.authentication.SessionManager;
import app.mobile.learningtc.R;
import app.util.connection.ServiceConnection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class UploadActivity extends ListActivity {

	String serviceUrl, root;
	SessionManager session;
	ServiceConnection serviceConnection;
	List<String> item = null;
	List<String> path = null;

	private Button bUpload;
	private EditText etPath;
	private TextView tvInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		bUpload = (Button) findViewById(R.id.bUpload);
		etPath = (EditText) findViewById(R.id.etPath);
		tvInfo = (TextView) findViewById(R.id.tvDesc);
		
		root = Environment.getExternalStorageDirectory().getPath();
		getDirectory(root);

		bUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					serviceConnection = new ServiceConnection("");
					serviceUrl = serviceConnection.getUrlServiceServer();
					
					Uploader uploader = new Uploader();
					uploader.setUrlAndFile(serviceUrl, etPath.getText().toString(), tvInfo);
					uploader.execute();
				}
				catch (Exception e) {
					etPath.setText(e.toString());
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.upload, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = new File(path.get(position));

		if (file.isDirectory()) {
			if(file.canRead())
				getDirectory(path.get(position));
			else {
				new AlertDialog.Builder(this)
					.setTitle("[" + file.getName() + "] folder can't be read!")
					.setPositiveButton("OK", 
						new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
					}
				}).show();
			}
		}
		else
			etPath.setText(file.getPath());
	}

	private void getDirectory(String dirPath) {
		etPath.setText(dirPath);

		item = new ArrayList<String>();
		path = new ArrayList<String>();

		File f = new File(dirPath);
		File[] files = f.listFiles();

		if (!dirPath.equals(root)) {
			item.add(root);
			path.add(root);

			item.add("../");
			path.add(f.getParent());
		}

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			path.add(file.getPath());
			
			if(file.isDirectory())
				item.add(file.getName() + "/");
			else
				item.add(file.getName());

		}

		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
		setListAdapter(fileList);
	}
}
