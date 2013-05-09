package app.mobile.resource;

import app.mobile.learningtc.R;
import app.util.connection.DownloadConnection;
import app.util.connection.ServiceConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockActivity;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResourceDetailActivity extends SherlockActivity {

	String cmId, resId, resName, serviceUrl, downloadUrl, fileId, contextId, 
		component, fileName, fileArea;
	ServiceConnection serviceConnection;
	DownloadConnection downloadConnection;

	private ProgressDialog pdService, pdDownload;
	private TextView tvDesc, tvType, tvDate, tvComponent, tvAuthor, tvLicense;
	private Button bDownload;

	static final int progress_bar_type = 0; 

	public static Intent newInstance(Activity activity, String cmId, String resId, String resName) {
		Intent intent = new Intent(activity, ResourceDetailActivity.class);
		intent.putExtra("cmId", cmId);
		intent.putExtra("resId", resId);
		intent.putExtra("resName", resName);
		return intent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getIntent().getExtras() != null) {
			cmId = getIntent().getExtras().getString("cmId");
			resId = getIntent().getExtras().getString("resId");
			resName = getIntent().getExtras().getString("resName");
		}

		setContentView(R.layout.activity_resource_detail);
		setTitle(resName);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		pdService = new ProgressDialog(this);
		pdService.setMessage("Loading information...");
		pdService.setIndeterminate(true);

		new getResourceDetailTask().execute();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pdDownload = new ProgressDialog(this);
			pdDownload.setMessage("Downloading file...");
			pdDownload.setIndeterminate(false);
			pdDownload.setMax(100);
			pdDownload.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pdDownload.setCancelable(true);
			pdDownload.show();
			return pdDownload;
		default:
			return null;
		}
	}

	private class getResourceDetailTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			return getResourceDetailJSON();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pdService.show();
		}

		@Override
		protected void onPostExecute(String content) {
			if (!content.equals(null)) {
				try {
					JSONArray jsonArray = new JSONArray(content);
					String desc = "", type = "", date = "", author = "", license = "";

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObj = jsonArray.getJSONObject(i);

						String _desc = jsonObj.getString("intro");
						String _type = jsonObj.getString("mimetype");
						String _date = jsonObj.getString("date");
						String _component = jsonObj.getString("component");
						String _author = jsonObj.getString("author");
						String _license = jsonObj.getString("license");

						fileId = jsonObj.getString("fileid");
						contextId = jsonObj.getString("contextid");
						fileName = jsonObj.getString("filename");
						fileArea = jsonObj.getString("filearea");

						desc += _desc;
						type += _type;
						date += _date;
						component = _component;
						author += _author;
						license += _license;
					}

					tvDesc = (TextView) findViewById(R.id.tvDescContent);
					tvType = (TextView) findViewById(R.id.tvTypeContent);
					tvDate = (TextView) findViewById(R.id.tvDateContent);
					tvComponent = (TextView) findViewById(R.id.tvComponentContent);
					tvAuthor = (TextView) findViewById(R.id.tvAuthorContent);
					tvLicense = (TextView) findViewById(R.id.tvLicenseContent);

					bDownload = (Button) findViewById(R.id.bDownload);
					bDownload.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							new downloadResourceTask().execute();
						}
					});

					tvDesc.setText(Html.fromHtml(desc));
					tvType.setText(type);
					tvDate.setText(date);
					tvComponent.setText(component);
					tvAuthor.setText(author);
					tvLicense.setText(license);
				}
				catch (JSONException e) {
					e.printStackTrace();
				}
			}

			pdService.dismiss();
		}

		@Override
		protected void onCancelled() {
			pdService.dismiss();
		}
	}

	private String getResourceDetailJSON() {
		String parameter = "cmid=" + cmId;
		String content = null, line = null;

		serviceConnection = new ServiceConnection("/resourceDetail.php?" + parameter);
		serviceUrl = serviceConnection.getUrlServiceServer();
		Log.d("Server URL", serviceUrl);

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(serviceUrl);

		// Parameter
		ArrayList<NameValuePair> param = new ArrayList<NameValuePair>(); {
			try {
				// Add parameter
				httpPost.setEntity(new UrlEncodedFormEntity(param));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();

				// Read content
				InputStream is = httpEntity.getContent();
				BufferedReader read = new BufferedReader(new InputStreamReader(is));

				content = "";
				line = "";

				while ((line = read.readLine()) != null) {
					content += line;
				}

				Log.d("Content", content);
			}
			catch (ClientProtocolException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		return content;
	}

	private class downloadResourceTask extends AsyncTask<Void, String, String> {
		@Override
		protected String doInBackground(Void... params) {
			int count;
			try {
				String encodeFileName = Uri.encode(fileName);
				downloadConnection = new DownloadConnection("/moodle/pluginfile.php/" + 
						contextId + "/" + component + "/" + fileArea + "/1/" + encodeFileName);
				downloadUrl = downloadConnection.getUrlDownloadServer();
//				downloadUrl = "http://192.168.1.4/mobile-learning-tc/" + encodeFileName;
//				downloadUrl = "http://192.168.1.4/moodle/pluginfile.php/55/mod_resource/content/1/" + encodeFileName;
				Log.d("Download URL", downloadUrl);
				
				String downloadPath = Environment.getExternalStorageDirectory().getPath() + "/MLearning";
				Log.d("Path", downloadPath);
				File dir = new File(downloadPath);
				dir.mkdirs();

				URL url = new URL(downloadUrl);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.setDoOutput(true);
				urlConnection.connect();
				
				// Download file
				InputStream is = urlConnection.getInputStream();

				// Output stream
				File file = new File(dir, fileName);
				FileOutputStream fos = new FileOutputStream(file);		
				
				int length = urlConnection.getContentLength();
				byte data[] = new byte[1024];
				long total = 0;
				
				while ((count = is.read(data)) > 0) {
					total += count;
					publishProgress("" + (int)((total * 100) / length));
					fos.write(data, 0, count);
				}

				// Flushing output
				fos.flush();

				// Closing streams
				fos.close();
				is.close();
			}
			catch (MalformedURLException e) {
				e.printStackTrace();
			}
			catch (ProtocolException e) {
				e.printStackTrace();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissDialog(progress_bar_type);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			pdDownload.setProgress(Integer.parseInt(progress[0]));
		}
	}
}
